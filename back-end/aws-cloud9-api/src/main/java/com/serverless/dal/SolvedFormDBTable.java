package com.serverless.dal;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class SolvedFormDBTable {
	
	 // get the table name from env. var. set in serverless.yml
    private static final String SOLVED_FORMS_TABLE_NAME = System.getenv("SOLVED_FORMS_TABLE_NAME");

    private static DynamoDBAdapter db_adapter = DynamoDBAdapter.getInstance();
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;

    private Logger logger = LogManager.getLogger(this.getClass());
    
    public SolvedFormDBTable() {
    	DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(SOLVED_FORMS_TABLE_NAME))
                .build();
    	
    	this.client = db_adapter.getDbClient();
        this.mapper = db_adapter.createDbMapper(mapperConfig);	
    }
    
    public Boolean ifTableExists() {
    	boolean result = this.client.describeTable(SOLVED_FORMS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
        logger.info("Call SolvedFormDBTable::ifTableExists() -> " + result);
    	return result;
    }
    
    public void save(SolvedForm newSolvedForm) throws IOException {
    	logger.info("Call SolvedFormDBTable::save(" + newSolvedForm + ")");
        this.mapper.save(newSolvedForm);
    }
    
    public List<SolvedForm> list() throws IOException {
    	DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<SolvedForm> results = this.mapper.scan(SolvedForm.class, scanExp);
        logger.info("Call SolvedFormDBTable::list() -> " + results);
        return results;
    }
    
    

}
