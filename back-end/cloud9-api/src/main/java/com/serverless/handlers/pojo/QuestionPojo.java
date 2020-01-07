package com.serverless.handlers.pojo;


import java.util.List;

public class QuestionPojo {
        private String lang;
        private String type;
        private String question;
        private String answer;
        private List<String> answers;

        public String getLang() {
                return lang;
        }

        public void setLang(String lang) {
                this.lang = lang;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getQuestion() {
                return question;
        }

        public void setQuestion(String question) {
                this.question = question;
        }

        public String getAnswer() {
                return answer;
        }

        public void setAnswer(String answer) {
                this.answer = answer;
        }

        public List<String> getAnswers() {
                return answers;
        }

        public void setAnswers(List<String> answers) {
                this.answers = answers;
        }

        @Override
        public String toString() {
                return "QuestionPojo{" +
                        "lang='" + lang + '\'' +
                        ", type='" + type + '\'' +
                        ", question='" + question + '\'' +
                        ", answer='" + answer + '\'' +
                        ", answers=" + answers +
                        '}';
        }
}