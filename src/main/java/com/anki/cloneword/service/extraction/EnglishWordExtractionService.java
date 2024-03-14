package com.anki.cloneword.service.extraction;

import com.anki.cloneword.dto.DictionaryProperty;
import com.anki.cloneword.dto.WordDescription;
import com.anki.cloneword.util.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(value = "engExtraction")
public class EnglishWordExtractionService extends WordExtractionService  {

    @Autowired
    @Qualifier("englishProperties")
    private DictionaryProperty dictionaryProperty;

    @Override
    public WordDescription extract(String word) throws IOException {
        WordDescription wordDescription = new WordDescription();
        wordDescription.setWord(word);

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

                String wordText = entry.select(dictionaryProperty.getClassWord()).text();
                String wordType = entry.select(dictionaryProperty.getClassWordType()).text();
                String wordTypeCode = getWordTypeCode(entry);
                wordEntry.setWord(wordText);
                wordEntry.setType(wordType);
                wordEntry.setTypeCode(wordTypeCode);
//                log("Processing: " + wordText + " " + wordType + " " + wordTypeCode);

                String pronounceUK = String.format("/%s/", entry.select(dictionaryProperty.getClassPronounceUK()).text());
                String pronounceUS = String.format("/%s/", entry.select(dictionaryProperty.getClassPronounceUS()).text());
                String audioUK = dictionaryProperty.getUri() + entry.select(dictionaryProperty.getClassPronounceAudioUK()).attr("src");
                String audioUS = dictionaryProperty.getUri() + entry.select(dictionaryProperty.getClassPronounceAudioUS()).attr("src");
                wordEntry.setPronounceUK(pronounceUK);
                wordEntry.setPronounceUS(pronounceUS);
                wordEntry.setAudioUK(audioUK);
                wordEntry.setAudioUS(audioUS);
//                log("Pronounce UK: " + pronounceUK + " " + audioUK);
//                log("Pronounce US: " + pronounceUS + " " + audioUS);

                // Mean
//                log("Get means of word");
                Elements means = entry.select(dictionaryProperty.getClassWrapperMean());
                if (!means.isEmpty()) {
                    Map<String, List<WordDescription.Mean>> wordMeanMap = new HashMap<>();
                    wordEntry.setMeans(wordMeanMap);

                    means.forEach(mean -> {
                        String meanType = mean.select(dictionaryProperty.getClassMeanType()).text();
                        String meanTypeCode = mean.select(dictionaryProperty.getClassMeanTypeCode()).text();
                        String meanMore = mean.select(dictionaryProperty.getClassMeanMore()).text();

                        String key = StringUtils.trim(meanType + " " + meanTypeCode + " " + meanMore);
                        List<WordDescription.Mean> wordMeans = wordMeanMap.computeIfAbsent(key, k -> new ArrayList<>());
//                        log("Mean: " + key);

                        mean.select(dictionaryProperty.getClassMeanBlock()).forEach(block -> {
//                            log("Block Class: " + block.parent().attr("class"));
                            if(!block.parent().hasClass("phrase-body")) {
                                String meanDef = block.select(dictionaryProperty.getClassMeanBlockDefine()).text();
//                                log("Define: " + meanDef);

//                                log("Example: ");
                                List<String> examples = block.select(dictionaryProperty.getClassMeanBlockExample())
                                        .stream().map(Element::text).collect(Collectors.toList());

                                WordDescription.Mean wordMean = new WordDescription.Mean();
                                wordMean.setType(meanType);
                                wordMean.setCode(meanTypeCode);
                                wordMean.setMore(meanMore);
                                wordMean.setMean(meanDef);
                                wordMean.setExamples(examples);
                                wordMeans.add(wordMean);
                            }
                        });
                    });
                }
            });
        }

        return wordDescription;
    }

    private String getWordTypeCode(Element entry) {
        return entry.select(dictionaryProperty.getClassWordTypeCode()).stream()
                .map(Element::text)
                .collect(Collectors.joining(" or "));
    }

    private Elements getEntryBody(Element dictionary) {
        return dictionary.select(dictionaryProperty.getClassEntryBody());
    }

    private Element getDictionaryRoot(Document document) {
        return document.select(dictionaryProperty.getClassWrapper()).first();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        build(dictionaryProperty);
    }
}
