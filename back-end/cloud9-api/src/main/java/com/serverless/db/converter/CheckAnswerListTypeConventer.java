package com.serverless.db.converter;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.serverless.db.model.CheckAnswer;

import java.util.List;

public class CheckAnswerListTypeConventer implements DynamoDBTypeConverter<String, List<CheckAnswer>> {

    @Override
    public String convert(List<CheckAnswer> answers) {
        return new Gson().toJson(answers);
    }

    @Override
    public List<CheckAnswer> unconvert(String s) {;
        return new Gson().fromJson(s, new TypeToken<List<CheckAnswer>>(){}.getType());
    }
}