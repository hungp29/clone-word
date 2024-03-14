package com.anki.cloneword.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class WordBuilder {
    private String url;
    private String word;
    private Document document;

    public WordBuilder setUrl(String url) {
        this.url = url;
        return this;
    }

    public WordBuilder setWord(String word) {
        this.word = word;
        return this;
    }

    public Document get() throws IOException {
        if (!StringUtils.isEmpty(url) && !StringUtils.isEmpty(word)) {
            document = Jsoup.connect(url + (url.endsWith("/") ? word : "/" + word)).get();
        }
        return document;
    }
}
