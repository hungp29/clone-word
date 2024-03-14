package com.anki.cloneword;

import com.anki.cloneword.dto.DictionaryProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DictionaryConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "dictionary.english")
    public DictionaryProperty englishProperties() {
        return new DictionaryProperty();
    }

    @Bean
    @ConfigurationProperties(prefix = "dictionary.english.vietnamese")
    public DictionaryProperty englishVietnameseProperties() {
        return new DictionaryProperty();
    }
}
