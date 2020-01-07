package com.serverless.validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.serverless.handlers.pojo.FormInputPojo;
import com.serverless.handlers.pojo.ResponseListErrorMessagePojo;

public final class FormValidator {
    public static ResponseListErrorMessagePojo validator(FormInputPojo form){
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        if(form.getName() == null)
            errors.add("Musisz podać nazwę testu.");

        if(form.getDescription()  == null)
            errors.add("Musisz podać opis testu.");

        if(form.getTime()  == null)
            errors.add("Musisz podać czas wykonywania testu.");

        if(form.getRecruiter() == null)
            errors.add("Musisz podać rekrutera, który tworzył test.");
        return errors;
    }
    public static ResponseListErrorMessagePojo validatorUpdate(FormInputPojo form){
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        return errors;
    }
    public static ResponseListErrorMessagePojo validatorUpdateLangForm(FormInputPojo form){
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        return errors;
    }
}
