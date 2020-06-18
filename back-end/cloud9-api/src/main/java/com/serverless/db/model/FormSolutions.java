package com.serverless.db.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.serverless.db.converter.CheckAnswerListTypeConventer;
import com.serverless.db.converter.SingleAnswerListTypeConverter;

import java.util.List;

public class FormSolutions {
    private String id;
    private String userId;
    private String formId;
    private List<SingleAnswer> answers;
    private List<CheckAnswer> check;
    private Boolean fitInTime;

    public FormSolutions() {}

    public FormSolutions(String userId, String formId, List<SingleAnswer> answers) {
        this.userId = userId;
        this.formId = formId;
        this.answers = answers;
    }
    public FormSolutions(String userId, String formId, List<SingleAnswer> answers, Boolean fitInTime) {
        this.userId = userId;
        this.formId = formId;
        this.answers = answers;
        this.answers = answers;
        this.fitInTime = fitInTime;
    }

    @DynamoDBHashKey(attributeName = "id")
    @DynamoDBAutoGeneratedKey
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    @DynamoDBAttribute(attributeName = "user")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    @DynamoDBAttribute(attributeName = "form")
    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }
    @DynamoDBTypeConverted(converter = SingleAnswerListTypeConverter.class)
    @DynamoDBAttribute(attributeName = "answers")
    public List<SingleAnswer> getAnswers() {
        return answers;
    }
    @DynamoDBAttribute(attributeName = "answers")
    public void setAnswers(List<SingleAnswer> answers) {
        this.answers = answers;
    }

    @DynamoDBTypeConverted(converter = CheckAnswerListTypeConventer.class)
    @DynamoDBAttribute(attributeName = "check")
    public List<CheckAnswer> getCheck() {
        return check;
    }
    @DynamoDBAttribute(attributeName = "check")
    public void setCheck(List<CheckAnswer> check) {
        this.check = check;
    }

    @DynamoDBAttribute(attributeName = "fitInTime")
    public Boolean getFitInTime() {
        return fitInTime;
    }

    public void setFitInTime(Boolean fitInTime) {
        this.fitInTime = fitInTime;
    }
}