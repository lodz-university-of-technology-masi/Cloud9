package com.serverless.db.model;

public class CheckAnswer {
    private String id;
    private Boolean check;

    public CheckAnswer(String id, Boolean check) {
        this.id = id;
        this.check = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
