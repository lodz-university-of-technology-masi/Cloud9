package com.serverless.handlers.pojo;

import com.serverless.db.model.CheckAnswer;
import com.serverless.db.model.SingleAnswer;
import com.serverless.db.model.User;

import java.util.List;

public class FormSolutionsForRecruiterPojo {
    private String id;
    private User user;
    private List<SingleAnswer> answers;
    private List<CheckAnswer> check;
    private Boolean fitInTime;

    public FormSolutionsForRecruiterPojo(String id, User user, List<SingleAnswer> answers, List<CheckAnswer> check, Boolean fitInTime) {
        this.id = id;
        this.user = user;
        this.answers = answers;
        this.check = check;
        this.fitInTime = fitInTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<SingleAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SingleAnswer> answers) {
        this.answers = answers;
    }

    public List<CheckAnswer> getCheck() {
        return check;
    }

    public void setCheck(List<CheckAnswer> check) {
        this.check = check;
    }

    public Boolean getFitInTime() {
        return fitInTime;
    }

    public void setFitInTime(Boolean fitInTime) {
        this.fitInTime = fitInTime;
    }

    @Override
    public String toString() {
        return "FormSolutionsForRecruiterPojo{" +
                "id='" + id + '\'' +
                ", user=" + user +
                ", answers=" + answers +
                ", check=" + check +
                ", fitInTime=" + fitInTime +
                '}';
    }
}
