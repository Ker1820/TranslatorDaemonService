package com.codedinn.translator;

import java.util.List;

public class Data {
    public static class TranslatorBody {
        private String text;
        private String model_id;

        public TranslatorBody() {

        }

        public TranslatorBody(String text, String model_id) {
            this.text = text;
            this.model_id = model_id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getModel_id() {
            return model_id;
        }

        public void setModel_id(String model_id) {
            this.model_id = model_id;
        }

//        @Override
//        public String toString() {
//            return "{\"text\":\"" + text + "\",\"model_id\":\"" + model_id + "\"}";
//        }
    }

    public static class TranslatorResponse {
        private Double word_count;
        private List<Translate> translations;
        private Double character_count;

        public Double getWord_count() {
            return word_count;
        }

        public void setWord_count(Double word_count) {
            this.word_count = word_count;
        }

        public List<Translate> getTranslations() {
            return translations;
        }

        public void setTranslations(List<Translate> translations) {
            this.translations = translations;
        }

        public Double getCharacter_count() {
            return character_count;
        }

        public void setCharacter_count(Double character_count) {
            this.character_count = character_count;
        }
    }

    public static class Translate {
        private String translation;

        public String getTranslation() {
            return translation;
        }

        public void setTranslation(String translation) {
            this.translation = translation;
        }
    }
}
