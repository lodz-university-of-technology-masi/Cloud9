package com.serverless.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedQueryList;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.serverless.db.model.Form;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class FormDBTable {
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

    public boolean ifTableExists() {
        boolean result = this.client.describeTable(FORMS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
        logger.info("Call FormDBTable::ifTableExists() -> " + result);
        return result;
    }

    public Form get(String id) throws IOException {
        Form form = null;
        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Form> queryExp = new DynamoDBQueryExpression<Form>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(av);

        PaginatedQueryList<Form> result = this.mapper.query(Form.class, queryExp);
        if (result.size() > 0) {
            form = result.get(0);
            logger.info("Form - get(): form - " + form.toString());
        }
        return form;
    }

    public List<Form> listForms() throws IOException {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<Form> results = this.mapper.scan(Form.class, scanExp);
        logger.info("Call FormDBTable::list() -> " + results);
        return results;
    }

    public void save(Form form) throws IOException {
        logger.info("Products - save(): " + form.toString());
        this.mapper.save(form);
    }

    public boolean delete(String id) throws IOException {
        Form form = this.get(id);
        if (form != null) {
            logger.info("Call FormDBTable::delete(" + id + ")-> OK");
            this.mapper.delete(form);
            return true;
        }
        logger.warn("Call FormDBTable::delete(" + id + ")-> does not exist.");
        return false;
    }

    public void update(Form form) throws IOException {
        logger.info("Call FormDBTable::update(" + form.toString() + ")");
        this.mapper.save(form);
    }
}