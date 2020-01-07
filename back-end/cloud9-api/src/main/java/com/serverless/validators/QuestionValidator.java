package com.serverless.validators;

import com.amazonaws.services.dynamodbv2.xspec.S;
import com.fasterxml.jackson.databind.JsonNode;
import com.serverless.handlers.pojo.QuestionPojo;
import com.serverless.handlers.pojo.ResponseListErrorMessagePojo;

import java.text.ParseException;
import java.util.List;

public final class QuestionValidator {
    public static ResponseListErrorMessagePojo validatorQuestionTypeO(QuestionPojo question) {
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        if(question == null || question.getQuestion().length() == 0)
            errors.add("Musisz podać nazwę pytania.");
        return errors;
    }

    public static ResponseListErrorMessagePojo validatorQuestionTypeW(QuestionPojo question) {
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        if(question == null || question.getQuestion().length() == 0)
            errors.add("Musisz podać nazwę pytania.");

        if(question.getAnswer() == null || question.getAnswer().length() == 0)
            errors.add("Musisz podać prawidłową odpowiedz na pytanie.");

        if(question.getAnswers() == null)
            errors.add("Musisz podać odpowiedzi na pytanie.");

        try{
            if(Integer.parseInt(question.getAnswer()) > question.getAnswers().size())
                errors.add("Odpowiedz nie moze być większa, niż lista pytań");
        }
        catch(NumberFormatException  e){
            errors.add("Odpowiedz w typ typie pytań musi być liczbą.");
        }
        return errors;
    }

    public static ResponseListErrorMessagePojo validatorQuestionTypeL(QuestionPojo question) {
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        if(question.getQuestion() == null || question.getQuestion().length() == 0)
            errors.add("Musisz podać nazwę pytania.");
        return errors;
    }
}