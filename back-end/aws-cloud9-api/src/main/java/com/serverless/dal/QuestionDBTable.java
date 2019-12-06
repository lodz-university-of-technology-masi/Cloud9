package com.serverless.dal;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class QuestionDBTable {

private static final String QUESTIONS_TABLE_NAME = System.getenv("QUESTIONS_TABLE_NAME");
    
	private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;



    

    
    
   

	public QuestionDBTable() {
	        // build the mapper config
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
	        return results;
	    }
	   
	   
	   public List<ClosedQuestion> listclosed() throws IOException {
	    	DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
	        List<ClosedQuestion> results = this.mapper.scan(ClosedQuestion.class, scanExp);
	        return results;
	    }

	
	    
	

	
	// methods
    public Boolean ifTableExists() {
        return this.client.describeTable(QUESTIONS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }
    
 
    public ClosedQuestion get(String id) throws IOException {
    	ClosedQuestion question = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<ClosedQuestion> queryExp = new DynamoDBQueryExpression<ClosedQuestion>()
            .withKeyConditionExpression("id = :v1")
            .withExpressionAttributeValues(av);

        PaginatedQueryList<ClosedQuestion> result = this.mapper.query(ClosedQuestion.class, queryExp);
        if(result.size() > 0) {
        	question = result.get(0);
        }
        return question;
    }
    
  
    
    public  List<ClosedQuestion> getFormQuestions(String formid) throws IOException {
    	DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<ClosedQuestion> results = this.mapper.scan(ClosedQuestion.class, scanExp);
        List<ClosedQuestion> res = new ArrayList<ClosedQuestion>();
        for (int i=0; i< results.size(); i++)
        {
        	if (results.get(i).getForm_membership()!=null) {
        	if(results.get(i).getForm_membership().equals(formid))
        	{
        		res.add(results.get(i));
        	}
        	}
        }
        
        
        return res;
    }
    
    public  Boolean deleteQuestionFromForm(String questionid) throws IOException {
    	Question question = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(questionid));

        DynamoDBQueryExpression<Question> queryExp = new DynamoDBQueryExpression<Question>()
            .withKeyConditionExpression("id = :v1")
            .withExpressionAttributeValues(av);

        PaginatedQueryList<Question> result = this.mapper.query(Question.class, queryExp);
        if (result.size() > 0) {
        	question = result.get(0);

        }
        
        question.setForm_membership(null);
        this.mapper.save(question);
        
        return true;
    }
    
    public  Boolean createMembership(String questionid, String formid) throws IOException {
    	Question question = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(questionid));

        DynamoDBQueryExpression<Question> queryExp = new DynamoDBQueryExpression<Question>()
            .withKeyConditionExpression("id = :v1")
            .withExpressionAttributeValues(av);

        PaginatedQueryList<Question> result = this.mapper.query(Question.class, queryExp);
        if (result.size() > 0) {
        	question = result.get(0);

        }
        
        question.setForm_membership(formid);
        this.mapper.save(question);
        
        return true;
    }
    
    
    public  ClosedQuestion setCorrectAnswerr(String id, String answer) throws IOException {
    	ClosedQuestion closed = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<ClosedQuestion> queryExp = new DynamoDBQueryExpression<ClosedQuestion>()
            .withKeyConditionExpression("id = :v1")
            .withExpressionAttributeValues(av);

        PaginatedQueryList<ClosedQuestion> result = this.mapper.query(ClosedQuestion.class, queryExp);
        if(result.size() > 0) {
        	closed = result.get(0);
        }
        closed.setCorrect_answer(answer);
       return closed;
    }
    
    
    
    
    
    public void save(Question newquestion) throws IOException {
        this.mapper.save(newquestion);
    }
    
    public void save(ClosedQuestion newquestion) throws IOException {
        this.mapper.save(newquestion);
    }
    

    
 
    
    public Boolean delete(String id) throws IOException {
    	Question question = null;

        // get product if exists
    	question = get(id);
        if (question != null) {
          this.mapper.delete(question);
        } 
        
        return true;
    }
    
    
}