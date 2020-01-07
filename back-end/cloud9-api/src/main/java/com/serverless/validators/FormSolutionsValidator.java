package com.serverless.validators;


import com.serverless.handlers.pojo.ResponseListErrorMessagePojo;
import com.serverless.handlers.pojo.SingleAnswerFormPojo;

public final class FormSolutionsValidator {
    public static ResponseListErrorMessagePojo validator(SingleAnswerFormPojo answer) {
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        if (answer.getId() == null)
            errors.add("Musisz podać id pytania: " + answer.toString());
        if (answer.getAnswer() == null)
            errors.add("Musisz podać odpowiedz na pytanie: " + answer.toString());
        return errors;
    }
}
