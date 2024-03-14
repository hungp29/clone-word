package com.anki.cloneword.service;

import com.anki.cloneword.constant.LanguageType;
import com.anki.cloneword.dto.WordDescription;
import com.anki.cloneword.service.factory.DictionaryFactory;
import com.anki.cloneword.service.generation.GenerationVocabularyService;
import com.anki.cloneword.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GenerationService {

    @Autowired
    private DictionaryFactory dictionaryFactory;

    public String generate(Map<LanguageType, WordDescription> wordDescriptionMap) {
        StringBuilder result = new StringBuilder();

        if (!CollectionUtils.isEmpty(wordDescriptionMap)) {
            wordDescriptionMap.entrySet().forEach(entry -> {
                GenerationVocabularyService generation = dictionaryFactory.getGeneration(entry.getKey());

                if (generation != null) {
                    result.append(generation.generate(entry.getValue())).append("\n\n");
                }
            });
        }

        return result.toString();
    }
}
