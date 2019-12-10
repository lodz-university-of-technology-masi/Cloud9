package com.serverless.dal;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;

import java.util.ArrayList;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.serverless.dal.DynamoDBAdapter;
@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class FormDBTable {

    // get the table name from env. var. set in serverless.yml
    private static final String FORMS_TABLE_NAME = System.getenv("FORMS_TABLE_NAME");


    private static DynamoDBAdapter db_adapter = DynamoDBAdapter.getInstance();

    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    private Logger logger = LogManager.getLogger(this.getClass());
   

    

    public FormDBTable() {

    	logger.info("FormDBTable constructor");

        // build the mapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
            .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(FORMS_TABLE_NAME))
            .build();

        this.client = db_adapter.getDbClient();
        // create the mapper with config
        this.mapper = db_adapter.createDbMapper(mapperConfig);
    }
    
	// methods
    public Boolean ifTableExists() {

    	boolean result = this.client.describeTable(FORMS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
        logger.info("Call FormDBTable::ifTableExists() -> " + result);
    	return result;
    }

    
    public List<Form> list() throws IOException {
    	DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<Form> results = this.mapper.scan(Form.class, scanExp);
        logger.info("Call FormDBTable::list() -> " + results);
        return results;
    }
    
    

    public Form get(String id) throws IOException {
    	logger.info("Call FormDBTable::get(" + id + ")");

    	Form form = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Form> queryExp = new DynamoDBQueryExpression<Form>()
            .withKeyConditionExpression("id = :v1")
            .withExpressionAttributeValues(av);

        PaginatedQueryList<Form> result = this.mapper.query(Form.class, queryExp);
        if(result.size() > 0) {
        	form = result.get(0);
        }
        return form;
    }
    
    public List<String> getForms(String userID ) throws IOException{
	    	List<Form> allForms = list();
	    	List<String> resultForms = new ArrayList<String>();
	    	for(Form form: allForms) {
	    		if(form.findUser(userID)) {
	    			resultForms.add(form.getId());
	    		}
	    	}
	    	return resultForms;
    }

    public void save(Form newForm) throws IOException {
    	logger.info("Call FormDBTable::save(" + newForm + ")");
        this.mapper.save(newForm);
    }

    public Boolean delete(String id) throws IOException {
        Form form = null;

        // get product if exists
        form = this.get(id);
        if (form != null) {
          logger.info("Call FormDBTable::delete(" + id + ")-> OK");
          this.mapper.delete(form);
          
        } else {
          logger.warn("Call FormDBTable::delete(" + id + ")-> does not exist.");
          return false;
        }
        return true;
    }
    

    public Form update(JsonNode newBody) throws IOException {
    	logger.info("Call FormDBTable::update(" + newBody + ")");
    	String id = newBody.get("id").asText();
    	Form form = this.get(id);
    	if(form == null) {
    		logger.error("Form with id " + id + " not found");
    		return null;
    	}
    	form.update(newBody);
    	this.mapper.save(form);
    	return form;
    }
}
