package com.anki.cloneword.controller;

import com.anki.cloneword.constant.LanguageType;
import com.anki.cloneword.dto.WordDescription;
import com.anki.cloneword.service.ExtractionService;
import com.anki.cloneword.service.GenerateVocabularyServiceTemp;
import com.anki.cloneword.service.GenerationService;
import com.anki.cloneword.service.WordExtractionServiceTemp;
import com.anki.cloneword.util.CollectionUtils;
import com.anki.cloneword.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/vocabulary")
public class VocabularyController {

    @Autowired
    private WordExtractionServiceTemp wordExtractionServiceTemp;

    @Autowired
    private GenerateVocabularyServiceTemp generateVocabularyServiceTemp;


    @Autowired
    private ExtractionService extractionService;

    @Autowired
    private GenerationService generationService;

    @GetMapping("/{word}")
    public ResponseEntity<String> cloneWord(@PathVariable String word) throws IOException {
        StringBuilder result = new StringBuilder();
        Map<LanguageType, WordDescription> map = extractionService.extract(word, LanguageType.VI, LanguageType.EN);

        if (!CollectionUtils.isEmpty(map)) {
            result.append(HtmlUtils.htmlEscape(generationService.generate(map)));
        }
//        WordDescription wordDescription = wordExtractionServiceTemp.extract(StringUtils.trim(word));
//
//        if (wordDescription != null) {
//            result.append(HtmlUtils.htmlEscape(generateVocabularyServiceTemp.generate(wordDescription)));
//        }
        return ResponseEntity.ok(result.toString());
    }

    @GetMapping("/download/{word}")
    public ResponseEntity<InputStreamResource> download(@PathVariable String word, HttpServletRequest request) {

        StringBuilder result = new StringBuilder();

        String[] words = word.split(",");

        if (words.length > 0) {
            result.append("#separator:tab\n#html:true\n");
            for (String w : words) {
//                try {
//                    WordDescription wordDescription = wordExtractionServiceTemp.extract(StringUtils.trim(w));
//
//                    if (wordDescription != null) {
//                        result.append(StringUtils.trim(w)).append("\t");
//                        result.append(generateVocabularyServiceTemp.generate(wordDescription).replaceAll("\n", ""));
//                        result.append("\n");
//                    }
//                } catch (IOException ignored) {
//
//                }

                Map<LanguageType, WordDescription> map = extractionService.extract(w, LanguageType.VI, LanguageType.EN);

                if (!CollectionUtils.isEmpty(map)) {
                    result.append(StringUtils.trim(w)).append("\t");
                    result.append(generationService.generate(map).replaceAll("\n", ""));
                    result.append("\n");
                }
            }
        }

        byte[] data = result.toString().getBytes(StandardCharsets.UTF_8);
        HttpHeaders responseHeader = new HttpHeaders();
        // Set mimeType trả về
        responseHeader.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Thiết lập thông tin trả về
        responseHeader.set("Content-disposition", "attachment; filename=" + "English-Vocabulary.txt");
        responseHeader.setContentLength(data.length);
        InputStream inputStream = new BufferedInputStream(new ByteArrayInputStream(data));
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        return new ResponseEntity<InputStreamResource>(inputStreamResource, responseHeader, HttpStatus.OK);
    }

    @GetMapping("/test/{word}")
    public ResponseEntity<String> clone(@PathVariable String word) {
        Map<LanguageType, WordDescription> map = extractionService.extract(word, LanguageType.EN, LanguageType.VI);
        generationService.generate(map);
        return ResponseEntity.ok("");
    }
}
