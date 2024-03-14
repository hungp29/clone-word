package com.anki.cloneword.service.extraction;

import com.anki.cloneword.dto.DictionaryProperty;
import com.anki.cloneword.dto.WordDescription;
import com.anki.cloneword.util.WordBuilder;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.InitializingBean;

import java.io.IOException;

public abstract class WordExtractionService implements InitializingBean {

    private WordBuilder builder;

    public abstract WordDescription extract(String word) throws IOException;

    protected void build(DictionaryProperty dictionaryProperty) {
        builder = new WordBuilder().setUrl(dictionaryProperty.getUrlSearch());
    }

    protected Document getDocument(String word) throws IOException {
        return builder.setWord(word).get();
    }

    protected void log(String value) {
        System.out.println(value);
    }
}
