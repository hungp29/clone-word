package com.anki.cloneword.service;

import com.anki.cloneword.constant.CambridgeClassName;
import com.anki.cloneword.dto.WordDescription;
import com.anki.cloneword.util.StringUtils;
import com.anki.cloneword.util.WordBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WordExtractionServiceTemp {
    public static String SOURCE_URL = "https://dictionary.cambridge.org/vi/dictionary/english";
    public static String CAMBRIDGE_URL = "https://dictionary.cambridge.org";

    private String sourceURL;
    private WordBuilder builder;

    public WordExtractionServiceTemp() {
        this.sourceURL = SOURCE_URL;
        build();
    }

    public WordExtractionServiceTemp(String sourceURL) {
        this.sourceURL = sourceURL;
        build();
    }

    private void build() {
        builder = new WordBuilder().setUrl(sourceURL);
    }

    public WordDescription extract(String word) throws IOException {
        WordDescription wordDescription = new WordDescription();
        Document document = getDocument(word);

        // Get dictionary root
        Element dictionary = getDictionaryRoot(document);
        Elements entries = getEntryBody(dictionary);
        if (!entries.isEmpty()) {
            List<WordDescription.EntryDescription> wordEntries = new ArrayList<>();
            wordDescription.setEntries(wordEntries);
//            log("Have " + entries.size() + " entry");
            entries.forEach(entry -> {
                WordDescription.EntryDescription wordEntry = new WordDescription.EntryDescription();
                wordEntries.add(wordEntry);

                String wordText = entry.select(CambridgeClassName.CLASS_WORD).text();
                String wordType = entry.select(CambridgeClassName.CLASS_WORD_TYPE).text();
                String wordTypeCode = getWordTypeCode(entry);
                wordEntry.setWord(wordText);
                wordEntry.setType(wordType);
                wordEntry.setTypeCode(wordTypeCode);
//                log("Processing: " + wordText + " " + wordType + " " + wordTypeCode);

                String pronounceUK = String.format("/%s/", entry.select(CambridgeClassName.CLASS_PRONOUNCE_UK).text());
                String pronounceUS = String.format("/%s/", entry.select(CambridgeClassName.CLASS_PRONOUNCE_US).text());
                String audioUK = CAMBRIDGE_URL + entry.select(CambridgeClassName.CLASS_PRONOUNCE_AUDIO_UK).attr("src");
                String audioUS = CAMBRIDGE_URL + entry.select(CambridgeClassName.CLASS_PRONOUNCE_AUDIO_US).attr("src");
                wordEntry.setPronounceUK(pronounceUK);
                wordEntry.setPronounceUS(pronounceUS);
                wordEntry.setAudioUK(audioUK);
                wordEntry.setAudioUS(audioUS);
//                log("Pronounce UK: " + pronounceUK + " " + audioUK);
//                log("Pronounce US: " + pronounceUS + " " + audioUS);

                // Mean
//                log("Get means of word");
                Elements means = entry.select(CambridgeClassName.CLASS_WRAPPER_MEAN);
                if (!means.isEmpty()) {
                    Map<String, List<WordDescription.Mean>> wordMeanMap = new HashMap<>();
                    wordEntry.setMeans(wordMeanMap);

                    means.forEach(mean -> {
                        String meanType = mean.select(CambridgeClassName.CLASS_MEAN_TYPE).text();
                        String meanTypeCode = mean.select(CambridgeClassName.CLASS_MEAN_TYPE_CODE).text();
                        String meanMore = mean.select(CambridgeClassName.CLASS_MEAN_MORE).text();

                        String key = StringUtils.trim(meanType + " " + meanTypeCode + " " + meanMore);
                        List<WordDescription.Mean> wordMeans = wordMeanMap.computeIfAbsent(key, k -> new ArrayList<>());
//                        log("Mean: " + key);

                        mean.select(CambridgeClassName.CLASS_MEAN_BLOCK).forEach(block -> {
//                            log("Block Class: " + block.parent().attr("class"));
                            if(!block.parent().hasClass("phrase-body")) {
                                String meanDef = block.select(CambridgeClassName.CLASS_MEAN_BLOCK_DEFINE).text();
//                                log("Define: " + meanDef);

//                                log("Example: ");
                                List<String> examples = block.select(CambridgeClassName.CLASS_MEAN_BLOCK_EXAMPLE)
                                        .stream().map(Element::text).collect(Collectors.toList());
                                examples.forEach(System.out::println);

                                WordDescription.Mean wordMean = new WordDescription.Mean();
                                wordMean.setType(meanType);
                                wordMean.setCode(meanTypeCode);
                                wordMean.setMore(meanMore);
                                wordMean.setMean(meanDef);
                                wordMean.setExamples(examples);
                                wordMeans.add(wordMean);
                            } else {
//                                log("### ignore");
                            }
                        });

                    });
                }
            });
        }

        return wordDescription;
    }

    public String getWordTypeCode(Element entry) {
        return entry.select(CambridgeClassName.CLASS_WORD_TYPE_CODE).stream()
                .map(Element::text)
                .collect(Collectors.joining(" or "));
    }

    private Elements getEntryBody(Element dictionary) {
        return dictionary.select(CambridgeClassName.CLASS_ENTRY_BODY);
    }

    private Element getDictionaryRoot(Document document) {
        return document.select(CambridgeClassName.CLASS_WRAPPER).first();
    }

    private Document getDocument(String word) throws IOException {
        return builder.setWord(word).get();
    }
}
