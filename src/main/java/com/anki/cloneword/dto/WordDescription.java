package com.anki.cloneword.dto;

import java.util.List;
import java.util.Map;

public class WordDescription {

    private String word;

    private List<EntryDescription> entries;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public List<EntryDescription> getEntries() {
        return entries;
    }

    public void setEntries(List<EntryDescription> entries) {
        this.entries = entries;
    }

    public static class EntryDescription {
        private String word;
        private String type;
        private String typeCode;
        private String pronounceUK;
        private String pronounceUS;
        private String audioUK;
        private String audioUS;
        private Map<String, List<Mean>> means;

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeCode() {
            return typeCode;
        }

        public void setTypeCode(String typeCode) {
            this.typeCode = typeCode;
        }

        public String getPronounceUK() {
            return pronounceUK;
        }

        public void setPronounceUK(String pronounceUK) {
            this.pronounceUK = pronounceUK;
        }

        public String getPronounceUS() {
            return pronounceUS;
        }

        public void setPronounceUS(String pronounceUS) {
            this.pronounceUS = pronounceUS;
        }

        public String getAudioUK() {
            return audioUK;
        }

        public void setAudioUK(String audioUK) {
            this.audioUK = audioUK;
        }

        public String getAudioUS() {
            return audioUS;
        }

        public void setAudioUS(String audioUS) {
            this.audioUS = audioUS;
        }

        public Map<String, List<Mean>> getMeans() {
            return means;
        }

        public void setMeans(Map<String, List<Mean>> means) {
            this.means = means;
        }
    }

    public static class Mean {
        private String type;
        private String code;
        private String more;
        private String mean;
        private String trans;
        private List<String> examples;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMore() {
            return more;
        }

        public void setMore(String more) {
            this.more = more;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }

        public String getMean() {
            return mean;
        }

        public void setMean(String mean) {
            this.mean = mean;
        }

        public List<String> getExamples() {
            return examples;
        }

        public void setExamples(List<String> examples) {
            this.examples = examples;
        }
    }
}
