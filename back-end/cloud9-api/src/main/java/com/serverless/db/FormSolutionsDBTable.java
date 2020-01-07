package com.serverless.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.db.model.FormSolutions;
import com.serverless.db.model.SingleAnswer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class FormSolutionsDBTable {
    private static final String FORM_SOLUTIONS_TABLE_NAME = System.getenv("FORM_SOLUTIONS_TABLE_NAME");
    private static DynamoDBAdapter db_adapter = DynamoDBAdapter.getInstance();
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;
    private Logger logger = LogManager.getLogger(this.getClass());

    public FormSolutionsDBTable() {
        logger.info("FormSolutionsDBTable constructor");
        // build the mapper config
        DynamoDBMapperConfig mapperConfig = DynamoDBMapperConfig.builder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(FORM_SOLUTIONS_TABLE_NAME))
                .build();
        this.client = db_adapter.getDbClient();
        // create the mapper with config
        this.mapper = db_adapter.createDbMapper(mapperConfig);
    }

    public boolean ifTableExists() {
        boolean result = this.client.describeTable(FORM_SOLUTIONS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
        logger.info("Call FormDBTable::ifTableExists() -> " + result);
        return result;
    }

    public FormSolutions get(String id) throws IOException {
        FormSolutions formSolutions = null;
        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<FormSolutions> queryExp = new DynamoDBQueryExpression<FormSolutions>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(av);

        PaginatedQueryList<FormSolutions> result = this.mapper.query(FormSolutions.class, queryExp);
        if (result.size() > 0) {
            formSolutions = result.get(0);
            logger.info("FormSolutions - get(): formSolutions - " + formSolutions.toString());
        }
        return formSolutions;
    }

    public List<FormSolutions> listFormSolutions() throws IOException {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<FormSolutions> results = this.mapper.scan(FormSolutions.class, scanExp);
        logger.info("Call FormSolutionsDBTable::list() -> " + results);
        return results;
    }

    public void save(FormSolutions formSolutions) throws IOException {
        logger.info("FormSolutions - save(): " + formSolutions.toString());
        this.mapper.save(formSolutions);
    }

    public boolean delete(String id) throws IOException {
        FormSolutions formSolutions = this.get(id);
        if (formSolutions != null) {
            logger.info("Call FormSolutions::delete(" + id + ")-> OK");
            this.mapper.delete(formSolutions);
            return true;
        }
        logger.warn("Call FormSolutions::delete(" + id + ")-> does not exist.");
        return false;
    }

    public void update(FormSolutions formSolutions) throws IOException {
        logger.info("Call FormSolutions::update(" + formSolutions.toString() + ")");
        this.mapper.save(formSolutions);
    }
}