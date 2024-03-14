package com.anki.cloneword.service;

import com.anki.cloneword.constant.LanguageType;
import com.anki.cloneword.dto.WordDescription;
import com.anki.cloneword.service.extraction.WordExtractionService;
import com.anki.cloneword.service.factory.DictionaryFactory;
import com.anki.cloneword.util.CollectionUtils;
import com.anki.cloneword.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ExtractionService {

    @Autowired
    private DictionaryFactory dictionaryFactory;

    public Map<LanguageType, WordDescription> extract(String word, LanguageType ...languages) {
        Map<LanguageType, WordDescription> wordDescriptionMap = new LinkedHashMap<>();

        if (!StringUtils.isEmpty(word) && !CollectionUtils.isEmpty(languages)) {
            Arrays.asList(languages).forEach(language -> {
                WordExtractionService wordExtractionService = dictionaryFactory.getExtraction(language);
                try {
                    WordDescription wordDescription = wordExtractionService.extract(word);
                    if (wordDescription != null) {
                        wordDescriptionMap.put(language, wordDescription);
                    }
                } catch (IOException ignored) {
                }
            });

            syncPronounce(wordDescriptionMap);
        }
        return wordDescriptionMap;
    }

    private void syncPronounce(Map<LanguageType, WordDescription> map) {
        if (!CollectionUtils.isEmpty(map)) {
            WordDescription engWord = map.get(LanguageType.EN);

            if (engWord != null && !CollectionUtils.isEmpty(engWord.getEntries())) {
                // get US audio
                Map<String, WordDescription.EntryDescription> mapTypeEntry = engWord.getEntries().stream().collect(Collectors.toMap(WordDescription.EntryDescription::getType, Function.identity()));

                map.entrySet().forEach(entry -> {
                    if (!entry.getKey().equals(LanguageType.EN) && !CollectionUtils.isEmpty(entry.getValue().getEntries())) {
                        entry.getValue().getEntries().forEach(wordEntry -> {
                            WordDescription.EntryDescription engEntry = mapTypeEntry.get(wordEntry.getType());

                            if (engEntry != null) {
                                wordEntry.setPronounceUS(engEntry.getPronounceUS());
                                wordEntry.setPronounceUK(engEntry.getPronounceUK());
                                wordEntry.setAudioUS(engEntry.getAudioUS());
                                wordEntry.setAudioUK(engEntry.getAudioUK());
                            }
                        });
                    }
                });
            }
        }
    }
}
