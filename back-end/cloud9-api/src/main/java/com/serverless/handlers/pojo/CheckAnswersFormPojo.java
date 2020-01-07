package com.serverless.handlers.pojo;

public class CheckAnswersFormPojo {
    private String id;
    private Boolean check;

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

    @Override
    public String toString() {
        return "CheckAnswersFormPojo{" +
                "id='" + id + '\'' +
                ", check=" + check +
                '}';
    }
}
