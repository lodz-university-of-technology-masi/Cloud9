package com.serverless.handlers.pojo;

import com.serverless.db.model.User;

import java.util.List;

public class FormPojo {
    private String id;
    private String name;
    private String description;
    private String creationDate;
    private String recruiterId;
    private Integer time;
    private String lang;
    private List<User> users;

    public FormPojo(String id, String name, String description, String creationDate, String recruiterId, Integer time,String lang, List<User> users) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.creationDate = creationDate;
        this.recruiterId = recruiterId;
        this.time = time;
        this.lang = lang;
        this.users = users;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
