package com.serverless.dal;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.TableNameOverride;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAutoGeneratedKey;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.Handler;

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

    private static DynamoDBAdapter db_adapter;
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    private Logger logger = LogManager.getLogger(this.getClass());
   

    

    public FormDBTable() {
        // build the mapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
            .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(FORMS_TABLE_NAME))
            .build();
        // get the db adapter
        this.db_adapter = DynamoDBAdapter.getInstance();
        this.client = this.db_adapter.getDbClient();
        // create the mapper with config
        this.mapper = this.db_adapter.createDbMapper(mapperConfig);
    }
    
	// methods
    public Boolean ifTableExists() {
        return this.client.describeTable(FORMS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }

    
    //TODO remake to Form
    public List<Form> list() throws IOException {
    	DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<Form> results = this.mapper.scan(Form.class, scanExp);
        return results;
    }
    
    
    //TODO remake to Form
    public Form get(String id) throws IOException {
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
    
    public void save(Form newForm) throws IOException {
        this.mapper.save(newForm);
    }
    
  //TODO remake to Form
    public Boolean delete(String id) throws IOException {
        Form form = null;

        // get product if exists
        form = this.get(id);
        if (form != null) {
          //logger.info("Products - delete(): " + product.toString());
          this.mapper.delete(form);
        } else {
          //logger.info("Products - delete(): product - does not exist.");
          return false;
        }
        return true;
    }
}