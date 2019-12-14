package com.serverless.dal;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.fasterxml.jackson.databind.JsonNode;


public class Question {

	
	protected String id;
    protected String type;
    protected String question;
    protected String form_membership;
    protected Integer answer;
    protected List<String> answer_list;
    protected Integer language;
    protected String recruiter_id;

    private Logger logger = LogManager.getLogger(this.getClass());
    
    public Question() {};
    
    public Question(JsonNode body) {
    logger.info("Call Question constructor(JsonNode)");
    if (body.get("type").asText().equals("0")) {
    	this.id = body.get("id").asText();
    	this.type = body.get("type").asText();
    	this.question = body.get("name").asText();
    	this.answer = body.get("answer").asInt();
    	this.recruiter_id = body.get("recruiter_id").asText();
    	this.language = body.get("language").asInt();
    	this.form_membership = body.get("form_id").asText();
    }
    	
    if (body.get("type").asText().equals("1")) {
    	this.id = body.get("id").asText();
    	this.type = body.get("type").asText();
    	this.question = body.get("name").asText();
    	this.answer = body.get("answer").asInt();
    	this.recruiter_id = body.get("recruiter_id").asText();
    	this.language = body.get("language").asInt();
    	this.form_membership = body.get("form_id").asText();
    	String answers = body.get("answer_list").toString(); //do zmiany
    	logger.info("answers in string"+answers);
    	this.answer_list =QuestionDBTable.singleChars(answers);
    	logger.info("answers in answer_list"+answer_list);  	
    }
    	
    if (body.get("type").asText().equals("2")) {
    	this.id = body.get("id").asText();
    	this.type = body.get("type").asText();
    	this.question = body.get("name").asText();
    	this.recruiter_id = body.get("recruiter_id").asText();
    	this.language = body.get("language").asInt();
    	this.form_membership = body.get("form_id").asText();

    }
    	
    };

   
    public void update(JsonNode body) {
		 logger.info("Call Question::update(" + body + ")");
		 JsonNode newId = body.get("id");
		 JsonNode newType = body.get("type");
		 JsonNode newQuestion = body.get("name");
		 JsonNode newAnswer = body.get("answer");
		 JsonNode newRecrutierId = body.get("recruiter_id");
		 JsonNode newForm_membership = body.get("form_membership");
		 JsonNode newLanguage = body.get("language");
		 JsonNode newAnswer_list = body.get("answer_list");

		 
		 
		 
		 if(newId != null) {
			 this.setId(newId.asText());
		 }
		 else {
			 logger.warn("Id is null");
		 }
		 
		 if(newType != null) {
			 this.setType(newType.asText());
		 }
		 else {
			 logger.warn("newType is null");
		 }
		 
		 if(newQuestion != null) {
			 this.setQuestion((newQuestion.asText()));
		 }
		 else {
			 logger.warn("Quesrtion is null");
		 }
		 if(newRecrutierId != null) {
			 this.setRecruiter_id((newRecrutierId.asText()));
		 }
		 else {
			 logger.warn("recruiterid is null");
		 }
		 if(newForm_membership != null) {
			 this.setForm_membership((newForm_membership.asText()));
		 }
		 else {
			 logger.warn("membership is null");
		 }
		 if(newLanguage != null) {
			 this.setLanguage((newLanguage.asInt()));
		 }
		 else {
			 logger.warn("Language is null");
		 }
		 if(newAnswer != null) {
			 this.setAnswer((newAnswer.asInt()));
		 }
		 else {
			 logger.warn("Answer is null");
		 }
		 if(newAnswer_list != null) {
			 this.setAnswer((newAnswer_list.asInt()));
		 }
		 else {
			 logger.warn("Answer is null");
		 }
	 }
    
    @DynamoDBHashKey(attributeName = "id")
	public String getId() {
    	logger.info("Call Question::getId() -> " + this.id);
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	@DynamoDBAttribute(attributeName = "question")
	public String getQuestion() {
		logger.info("Call Question::getQuestion() -> " + this.question);
		return question;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	
	
    
	@DynamoDBAttribute(attributeName = "form_membership")
	public String getForm_membership() {
		logger.info("Call Question::formId() -> " + this.form_membership);
		return form_membership;
	}
	public void setForm_membership(String form_membership) {
		this.form_membership = form_membership;
	}
    
	@DynamoDBAttribute(attributeName = "type")
	public String getType() {
		logger.info("Call Question::getType() -> " + this.type);
		return type;
	}





	public void setType(String type) {
		this.type = type;
	}




	@DynamoDBAttribute(attributeName = "answer")
	public Integer getAnswer() {
		logger.info("Call Question::getAnswer() -> " + this.answer);
		return answer;
	}





	public void setAnswer(Integer answer) {
		this.answer = answer;
	}




	@DynamoDBAttribute(attributeName = "answer_list")
	public List<String> getAnswer_list() {
		logger.info("Call Question::getAnswerList() -> ");
		return answer_list;
	}





	public void setAnswer_list(List<String> answer_list) {
		this.answer_list = answer_list;
	}




	@DynamoDBAttribute(attributeName = "language")
	public Integer getLanguage() {
		logger.info("Call Question::getLanguage() -> " + this.language);
		return language;
	}





	public void setLanguage(Integer language) {
		this.language = language;
	}




	@DynamoDBAttribute(attributeName = "recruiter_id")
	public String getRecruiter_id() {
		logger.info("Call Question::getRecruiterId() -> " + this.recruiter_id);
		return recruiter_id;
	}





	public void setRecruiter_id(String recruiter_id) {
		this.recruiter_id = recruiter_id;
	}
	
    
}