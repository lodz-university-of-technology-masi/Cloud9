package com.serverless.handlers.question;

import com.amazonaws.services.kms.model.EnableKeyRotationRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.QuestionDBTable;
import com.serverless.db.model.Form;
import com.serverless.db.model.Question;
import com.serverless.handlers.form.ListFormsHandler;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.handlers.pojo.ResponseListErrorMessagePojo;
import com.serverless.validators.QuestionValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.serverless.handlers.pojo.QuestionPojo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AddQuestionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(AddQuestionHandler.class);

    private ResponseListErrorMessagePojo validate(List<QuestionPojo> qustions){
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        for(QuestionPojo question: qustions){
            switch (question.getType()) {
                    case "O":
                        for(String e: QuestionValidator.validatorQuestionTypeO(question).getErrors())
                            errors.add(e +"" + question.toString());
                    break;
                case "L":
                        for(String e: QuestionValidator.validatorQuestionTypeL(question).getErrors())
                            errors.add(e +"" + question.toString());
                    break;
                case "W":
                        for(String e: QuestionValidator.validatorQuestionTypeW(question).getErrors())
                            errors.add(e +"" + question.toString());
                    break;
                default:
                    errors.add("Nieznany typ pytania: " +question.toString());
            }
        }
        return errors;
    }
    private void saveQuestnions(List<QuestionPojo> qustions, String formId) throws IOException {
        QuestionDBTable questionDBTable = new QuestionDBTable();
        for(QuestionPojo question: qustions){
            switch (question.getType()) {
                case "O":
                case "L":
                    questionDBTable.save(new Question(formId, question.getType(), question.getLang(), question.getQuestion()));
                    break;
                case "W":
                    questionDBTable.save(new Question(formId, question.getType(), question.getLang(), question.getQuestion(), question.getAnswer(), question.getAnswers()));
                    break;
            }
        }
    }
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call AddQuestionHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            String formId = pathParameters.get("id");
            Form form = new FormDBTable().get(formId);
            if(form == null){
                LOG.warn("Not found form ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }

            if(form.getRecruiterId().compareTo(body.get("recruiter").asText()) != 0)
            {
                LOG.warn("Unauthorized recruiter for form ->" + formId + "recruiterId" + body.get("recruiter").asText());
                return ApiRespnsesHandlerPojo.sendResponse("unauthorized user", 401);
            }

            List<QuestionPojo> questions = Arrays.asList(
                    new ObjectMapper().convertValue(
                            body.get("questions"),
                            QuestionPojo[].class
                    )
            );

            ResponseListErrorMessagePojo errors = this.validate(questions);
            if(errors.getErrors().size() > 0)
                return ApiRespnsesHandlerPojo.sendResponse(errors, 500);

            this.saveQuestnions(questions, formId);
            return ApiRespnsesHandlerPojo.sendResponse(questions, 200);
        }
        catch (Exception e){
            LOG.error("AddQuestionHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }

    }
}