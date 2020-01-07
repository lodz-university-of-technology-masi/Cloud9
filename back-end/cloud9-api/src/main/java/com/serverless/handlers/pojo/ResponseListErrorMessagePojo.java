package com.serverless.handlers.pojo;

import java.util.ArrayList;
import java.util.List;

public class ResponseListErrorMessagePojo {
    List<String> errors;

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public ResponseListErrorMessagePojo() {
        this.errors = new ArrayList<String>();
    }

    public List<String> getErrors() {
        return errors;
    }

    public void add(String error){
        this.errors.add(error);
    }

}
