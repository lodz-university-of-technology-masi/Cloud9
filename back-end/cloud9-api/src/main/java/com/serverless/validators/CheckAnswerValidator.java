package com.serverless.validators;

import com.serverless.handlers.pojo.CheckAnswersFormPojo;
import com.serverless.handlers.pojo.ResponseListErrorMessagePojo;

public final class CheckAnswerValidator {
    public static ResponseListErrorMessagePojo validator(CheckAnswersFormPojo check) {
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        if (check.getId() == null)
            errors.add("Musisz podać id pytania: " + check.toString());
        if (check.getCheck() == null)
            errors.add("Musisz podać odpowiedz na pytanie: " + check.toString());
        return errors;
    }
}
