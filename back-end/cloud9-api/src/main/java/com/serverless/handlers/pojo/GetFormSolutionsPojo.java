package com.serverless.handlers.pojo;

import com.serverless.db.model.CheckAnswer;
import com.serverless.db.model.Question;
import com.serverless.db.model.SingleAnswer;
import com.serverless.db.model.User;

import java.util.List;

public class GetFormSolutionsPojo {
    private String recruiterId;
    private List<SingleAnswer> answers;
    private List<CheckAnswer> check;
    private Boolean fitInTime;
    private User user;
    private List<Question> questions;

    public GetFormSolutionsPojo() {
    }

    public GetFormSolutionsPojo(String recruiterId, List<SingleAnswer> answers, List<CheckAnswer> check, Boolean fitInTime, User user, List<Question> questions) {
        this.recruiterId = recruiterId;
        this.answers = answers;
        this.check = check;
        this.fitInTime = fitInTime;
        this.user = user;
        this.questions = questions;
    }

    public String getRecruiterId() {
        return recruiterId;
    }

    public void setRecruiterId(String recruiterId) {
        this.recruiterId = recruiterId;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    @Override
    public String toString() {
        return "GetFormSolutionsPojo{" +
                "formId='" + recruiterId + '\'' +
                ", answers=" + answers +
                ", check=" + check +
                ", fitInTime=" + fitInTime +
                ", user=" + user +
                ", questions=" + questions +
                '}';
    }
}
