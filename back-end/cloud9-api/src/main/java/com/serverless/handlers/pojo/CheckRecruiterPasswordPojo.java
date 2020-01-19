package com.serverless.handlers.pojo;

public class CheckRecruiterPasswordPojo {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CheckRecruiterPasswordPojo{" +
                "name='" + name + '\'' +
                '}';
    }
}
