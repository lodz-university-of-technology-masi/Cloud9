package com.serverless.handlers.pojo;

public class TranslatePojo {
    private String targetLanguage;
    private String sourceLanguage;
    private String recruiterId;

    public String getTargetLanguage() {
        return targetLanguage;
    }

    public void setTargetLanguage(String targetLanguage) {
        this.targetLanguage = targetLanguage;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    public void setSourceLanguage(String sourceLanguage) {
        this.sourceLanguage = sourceLanguage;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    @Override
    public String toString() {
        return "TranslatePojo{" +
                "targetLanguage='" + targetLanguage + '\'' +
                ", sourceLanguage='" + sourceLanguage + '\'' +
                ", recruiterId='" + recruiterId + '\'' +
                '}';
    }
}
