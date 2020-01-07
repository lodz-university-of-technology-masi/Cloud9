package com.serverless.db.converter;


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.serverless.db.model.SingleAnswer;

import java.util.List;

public class SingleAnswerListTypeConverter implements DynamoDBTypeConverter<String, List<SingleAnswer>> {

    @Override
    public String convert(List<SingleAnswer> answers) {
        return new Gson().toJson(answers);
    }

    @Override
    public List<SingleAnswer> unconvert(String s) {;
        return new Gson().fromJson(s, new TypeToken<List<SingleAnswer>>(){}.getType());
    }
}