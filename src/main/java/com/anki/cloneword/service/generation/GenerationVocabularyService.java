package com.anki.cloneword.service.generation;

import com.anki.cloneword.dto.WordDescription;

public abstract class GenerationVocabularyService {

    public abstract String generate(WordDescription wordDescription);
}
