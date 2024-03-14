package com.anki.cloneword.service.factory;

import com.anki.cloneword.constant.LanguageType;
import com.anki.cloneword.service.extraction.WordExtractionService;
import com.anki.cloneword.service.generation.GenerationVocabularyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.BeanFactoryAnnotationUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class DictionaryFactory {

    @Autowired
    private ApplicationContext context;

    public WordExtractionService getExtraction(LanguageType language) {
        WordExtractionService wordExtractionService = null;

        if (LanguageType.EN.equals(language)) {
            wordExtractionService = BeanFactoryAnnotationUtils.qualifiedBeanOfType(context.getAutowireCapableBeanFactory(), WordExtractionService.class, "engExtraction");
        } else if (LanguageType.VI.equals(language)) {
            wordExtractionService = BeanFactoryAnnotationUtils.qualifiedBeanOfType(context.getAutowireCapableBeanFactory(), WordExtractionService.class, "engVieExtraction");
        }
        return wordExtractionService;
    }

    public GenerationVocabularyService getGeneration(LanguageType language) {
        GenerationVocabularyService generationVocabularyService = null;

        if (LanguageType.EN.equals(language)) {
            generationVocabularyService = BeanFactoryAnnotationUtils.qualifiedBeanOfType(context.getAutowireCapableBeanFactory(), GenerationVocabularyService.class, "engGeneration");
        } else if (LanguageType.VI.equals(language)) {
            generationVocabularyService = BeanFactoryAnnotationUtils.qualifiedBeanOfType(context.getAutowireCapableBeanFactory(), GenerationVocabularyService.class, "engVieGeneration");
        }
        return generationVocabularyService;
    }
}
