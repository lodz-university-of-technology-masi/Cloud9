package com.serverless.dal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.JsonNode;


@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class QuestionDBTable {
private Logger logger = LogManager.getLogger(this.getClass());
private static final String QUESTIONS_TABLE_NAME = System.getenv("QUESTIONS_TABLE_NAME");
    
	private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;



   

	public QuestionDBTable() {
    		logger.info("QuestionDBTable constructor");
	        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
	            .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(QUESTIONS_TABLE_NAME))
	            .build();
	        // get the db adapter
	        this.db_adapter = DynamoDBAdapter.getInstance();
	        this.client = this.db_adapter.getDbClient();
	        // create the mapper with config
	        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
	}
		   
	   
	   public List<Question> list() throws IOException {
	    	DynamoDBScanExpression scanExp = new DynamoDBScanExpression();

	        List<Question> results = this.mapper.scan(Question.class, scanExp);
	        logger.info("Call QuestionDBTable::list() -> " + results);
	        return results;
	    }

	

	
	    
	

	
	// methods
    public Boolean ifTableExists() {
        boolean result = this.client.describeTable(QUESTIONS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
        logger.info("Call FormDBTable::ifTableExists() -> " + result);
        return result;
    }
    
    
 
    public Question get(String id) throws IOException {
    	logger.info("Call QuestionDBTable::get(" + id + ")");
    	
    	Question question = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Question> queryExp = new DynamoDBQueryExpression<Question>()
            .withKeyConditionExpression("id = :v1")
            .withExpressionAttributeValues(av);

        PaginatedQueryList<Question> result = this.mapper.query(Question.class, queryExp);
        if(result.size() > 0) {
        	question = result.get(0);
        }
        return question;
    }
    
  
    
    public  List<Question> getFormQuestions(String formId) throws IOException {
    	QuestionDBTable questionTable=new QuestionDBTable();
    	List<Question> questions = questionTable.list();
    	List<Question> res = new ArrayList<Question>();
    	for (Question question : questions)
        {
        	if (question.getForm_membership()!=null) {
            	if(question.getForm_membership().equals(formId))
            	{
            		res.add(question);
            	}
            	}
        	
        }

        return res;
    }
    
    
    public  List<Question> getRecruiterQuestions(String recruiterId) throws IOException {
    	QuestionDBTable questionTable=new QuestionDBTable();
    	List<Question> questions = questionTable.list();
    	List<Question> res = new ArrayList<Question>();
    	for (Question question : questions)
        {
        	if (question.getRecruiter_id()!=null) {
            	if(question.getRecruiter_id().equals(recruiterId))
            	{
            		res.add(question);
            	}
            	}
        	
        }

        return res;
    }
    
    
    
    public Question update(JsonNode newBody) throws IOException {
    	logger.info("Call QuestionDBTable::update(" + newBody + ")");
    	String id = newBody.get("id").asText();
    	Question question = this.get(id);
    	if(question == null) {
    		logger.error("Question with id " + id + " not found");
    		return null;
    	}
    	question.update(newBody);
    	this.mapper.save(question);
    	return question;
    }

    

    
    
    
    public void save(Question newQuestion) throws IOException {
    	try {
    	logger.info("Call QuestionDBTable::save(" + newQuestion + ")");
        this.mapper.save(newQuestion);
    	}
    	catch (Exception ex)
    	{ }
        
    }
    


    
 
    
    public Boolean delete(String id) throws IOException {
        Question question = null;

        // get product if exists
        question = this.get(id);
        if (question != null) {
          logger.info("Call QuestionDBTable::delete(" + id + ")-> OK");
          this.mapper.delete(question);
        } else {
          logger.warn("Call QuestionDBTable::delete(" + id + ")-> does not exist.");
          return false;
        }
        return true;
    }
    
    
    public static List<String> singleChars(String s) {
        String[] answersInTable = s.split("\"");
        List <String> answer_list= new ArrayList<String>();	
    	for (int i =0; i< answersInTable.length; i++)
    	{
    		if(answersInTable[i].equals("[")== false && answersInTable[i].equals("]")== false && answersInTable[i].equals(",")== false) {
    		answer_list.add(answersInTable[i]);   }		
    	}
    	
    	return answer_list;
    }
    
}