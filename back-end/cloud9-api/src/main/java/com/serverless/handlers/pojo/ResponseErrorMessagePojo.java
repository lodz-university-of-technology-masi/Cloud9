package com.serverless.handlers.pojo;

public class ResponseErrorMessagePojo {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseErrorMessagePojo(String message) {
        this.message = message;
    }
}
