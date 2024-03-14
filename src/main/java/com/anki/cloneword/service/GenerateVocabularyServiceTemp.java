package com.anki.cloneword.service;


import com.anki.cloneword.dto.WordDescription;
import com.anki.cloneword.util.CollectionUtils;
import com.anki.cloneword.util.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class GenerateVocabularyServiceTemp {
    private int numberOfExample = 2;
    private String DEFAULT_COLOR = "rgb(255, 0, 0)";
    private String NOUN_COLOR = "rgb(21, 194, 24)";
    private String VERB_COLOR = "rgb(194, 21, 136)";
    private final String PATTERN_WORD_DEFINE =
            "        <div style=\"font-weight: bold; margin-top: 20px;\">\n" +
                    "            <p><span style=\"color: ``color``;font-size: 20px;\">``word``&nbsp;</span><span>``wordType`` <sup>[``typeCode``]</sup></span></p>\n" +
                    "            <p>\n" +
                    "                <span style=\"padding-right: 15px;\">UK <a style=\"text-decoration: none;\" href=\"``audioUK``\">``pronounceUK``</a></span>\n" +
                    "                <span>US <a style=\"text-decoration: none;\" href=\"``audioUS``\">``pronounceUS``</a></span>\n" +
                    "            </p>\n" +
                    "        </div>\n";
    private final String PATTERN_MEAN_WRAPPER_ONE =
            "        <div style=\"border-top: 3px solid rgb(93, 47, 193);\">\n" +
                    "``meanContent``" +
                    "        </div>";

    private final String PATTERN_MEAN_WRAPPER_TWO =
            "        <div style=\"\">\n" +
                    "``meanContent``" +
                    "        </div>";
    private final String PATTERN_MEAN_TYPE =
            "            <p style=\"font-weight: bold; color: rgb(93, 47, 193);\">``word`` <i>``type``</i> <sup>``code``</sup> ``more``</p>\n";
    private final String PATTERN_MEAN =
            "            <p style=\"font-weight: bold;\">``mean``</p>\n";
    private final String PATTERN_MEAN_EXAMPLE_WRAPPER =
            "            <ul>\n" +
            "``exampleContent``" +
            "            </ul>\n" +
            "            <span style=\"color: rgb(253, 253, 61); font-weight: bold; padding-bottom: 30px;\">Images:</span> \n\n";
    private final String PATTERN_MEAN_EXAMPLE =
            "                <li>``example``</li>\n";

    public String generate(WordDescription wordDescription) {
//        log("#### GENERATE WORD");
        StringBuilder builder = new StringBuilder();

        if (!CollectionUtils.isEmpty(wordDescription.getEntries())) {
            builder.append("<div style=\"font-size: 15px; text-align: left;\">\n    <div>\n");
            for (WordDescription.EntryDescription entry : wordDescription.getEntries()) {
                // word define
//                log(PATTERN_WORD_DEFINE.replace("``word``", entry.getWord())
//                        .replace("``wordType``", entry.getType())
//                        .replace("``typeCode``", entry.getTypeCode())
//                        .replace("``audioUK``", entry.getAudioUK())
//                        .replace("``pronounceUK``", entry.getPronounceUK())
//                        .replace("``audioUS``", entry.getAudioUS())
//                        .replace("``pronounceUS``", entry.getPronounceUS()));
                String wordDef = PATTERN_WORD_DEFINE.replace("``word``", entry.getWord())
                        .replace("``wordType``", entry.getType())
                        .replace("``typeCode``", entry.getTypeCode())
                        .replace("``audioUK``", entry.getAudioUK())
                        .replace("``pronounceUK``", entry.getPronounceUK())
                        .replace("``audioUS``", entry.getAudioUS())
                        .replace("``pronounceUS``", entry.getPronounceUS());
                String color = DEFAULT_COLOR;
                if ("noun".equalsIgnoreCase(entry.getType())) {
                    color = NOUN_COLOR;
                } else if ("verb".equalsIgnoreCase(entry.getType())) {
                    color = VERB_COLOR;
                }
                wordDef = wordDef.replace("``color``", color);
                builder.append(wordDef);

                if (!CollectionUtils.isEmpty(entry.getMeans())) {
                    for (Map.Entry<String, List<WordDescription.Mean>> mapEntry : entry.getMeans().entrySet()) {
                        String key = mapEntry.getKey();
                        List<WordDescription.Mean> means = mapEntry.getValue();

                        if (!CollectionUtils.isEmpty(means)) {
                            StringBuilder meanContent = new StringBuilder();
                            String meanType = "";
                            for (WordDescription.Mean mean : means) {
                                if (StringUtils.isEmpty(meanType) && !StringUtils.isEmpty(mean.getType())) {
                                    meanType = PATTERN_MEAN_TYPE.replace("``word``", entry.getWord())
                                            .replace("``type``", mean.getType())
                                            .replace("``code``", mean.getCode())
                                            .replace("``more``", mean.getMore());
//                                    log(meanType);
                                    meanContent.append(meanType);
                                }

//                                log(PATTERN_MEAN.replace("``mean``", mean.getMean()));
                                meanContent.append(PATTERN_MEAN.replace("``mean``", mean.getMean()));

                                if (!CollectionUtils.isEmpty(mean.getExamples())) {
                                    StringBuilder examples = new StringBuilder();
                                    int count = 0;
                                    for (String example : mean.getExamples()) {
                                        if (count++ < numberOfExample) {
                                            examples.append(PATTERN_MEAN_EXAMPLE.replace("``example``", example));
                                        }
                                    }
                                    String exampleContent = PATTERN_MEAN_EXAMPLE_WRAPPER.replace("``exampleContent``", examples.toString());
//                                    log(exampleContent);
                                    meanContent.append(exampleContent);
                                }
                            }
//                            log(PATTERN_MEAN_WRAPPER.replace("``meanContent``", meanContent.toString()));
                            if (!StringUtils.isEmpty(meanType)) {
                                builder.append(PATTERN_MEAN_WRAPPER_ONE.replace("``meanContent``", meanContent.toString()));
                            } else {
                                builder.append(PATTERN_MEAN_WRAPPER_TWO.replace("``meanContent``", meanContent.toString()));
                            }
                        }
                    }
                }
            }
            builder.append("\n   </div>\n</div>");
        }

//        log(builder.toString());

//        log("#### END GENERATE WORD");
        return builder.toString().replace("\\[\\]", "");
    }
}
