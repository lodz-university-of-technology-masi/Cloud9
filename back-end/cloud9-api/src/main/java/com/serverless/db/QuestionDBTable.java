package com.serverless.db;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.serverless.db.model.Form;
import com.serverless.db.model.Question;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@DynamoDBTable(tableName = "PLACEHOLDER_PRODUCTS_TABLE_NAME")
public class QuestionDBTable {
    private static final String FORMS_TABLE_NAME = System.getenv("QUESTIONS_TABLE_NAME");
    private static DynamoDBAdapter db_adapter = DynamoDBAdapter.getInstance();
    private final AmazonDynamoDB client;
    private final DynamoDBMapper mapper;
    private Logger logger = LogManager.getLogger(this.getClass());

    public QuestionDBTable() {
        logger.info("QuestionDBTable constructor");
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
        logger.info("Call QuestionDBTable::ifTableExists() -> " + result);
        return result;
    }

    public Question get(String id) throws IOException {
        Question question = null;
        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Question> queryExp = new DynamoDBQueryExpression<Question>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(av);

        PaginatedQueryList<Question> result = this.mapper.query(Question.class, queryExp);
        if (result.size() > 0) {
            question = result.get(0);
            logger.info("Question - get(): Question - " + question.toString());
        }
        return question;
    }

    public List<Question> listQuestions() throws IOException {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<Question> results = this.mapper.scan(Question.class, scanExp);
        logger.info("Call QuestionDBTable::list() -> " + results);
        return results;
    }

    public void save(Question question) throws IOException {
        logger.info("Question - save(): " + question.toString());
        this.mapper.save(question);
    }

    public boolean delete(String id) throws IOException {
        Question question = this.get(id);
        if (question != null) {
            logger.info("Call QuestionDBTable::delete(" + id + ")-> OK");
            this.mapper.delete(question);
            return true;
        }
        logger.warn("Call QuestionDBTable::delete(" + id + ")-> does not exist.");
        return false;
    }

    public void update(Question question) throws IOException {
        logger.info("Call QuestionDBTable::update(" + question.toString() + ")");
        this.mapper.save(question);
    }
}
