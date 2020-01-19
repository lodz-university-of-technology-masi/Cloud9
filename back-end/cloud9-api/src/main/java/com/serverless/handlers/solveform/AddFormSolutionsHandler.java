package com.serverless.handlers.solveform;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.FormSolutionsDBTable;
import com.serverless.db.QuestionDBTable;
import com.serverless.db.model.Form;
import com.serverless.db.model.FormSolutions;
import com.serverless.db.model.SingleAnswer;
import com.serverless.handlers.pojo.*;
import com.serverless.validators.FormSolutionsValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AddFormSolutionsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(AddFormSolutionsHandler.class);

    private ResponseListErrorMessagePojo validate(List<SingleAnswerFormPojo> answers) throws IOException {
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        for(SingleAnswerFormPojo answer: answers){
            for(String e: FormSolutionsValidator.validator(answer).getErrors())
                errors.add(e);
            if(new QuestionDBTable().get(answer.getId()) == null)
                errors.add("Pytanie nie istnieje " + answer.toString());
        }
        return errors;
    }

    private List<SingleAnswer> generateSingleAnswerList(List<SingleAnswerFormPojo> answers) {
        List<SingleAnswer> singleAnswers = new ArrayList<SingleAnswer>();
        for(SingleAnswerFormPojo answer: answers){
            singleAnswers.add(new SingleAnswer( answer.getId(), answer.getAnswer()));
        }
        return singleAnswers;
    }

    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call AddSoleFormnHandler::handleRequest(" + input + ", " + context + ")");
        try{
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");

            String formId = pathParameters.get("id");
            String userId = pathParameters.get("user");

            Form form = new FormDBTable().get(formId);
            if(form == null) {
                LOG.warn("Not form ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }

            if(!form.getUsers().contains(userId)) {
                LOG.warn("Unauthorized user for form solutions->" + formId + "userId" + userId);
                return ApiRespnsesHandlerPojo.sendResponse("unauthorized user", 401);
            }

            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            Boolean fitInTime = body.get("fitInTime").asBoolean();
            if(fitInTime == null){
                LOG.warn("You must add fitInTime");
                return ApiRespnsesHandlerPojo.sendResponse("You must add fitInTime", 500);
            }

            List<SingleAnswerFormPojo> answers = Arrays.asList(
                    new ObjectMapper().convertValue(
                            body.get("answers"),
                            SingleAnswerFormPojo[].class
                    )
            );

            ResponseListErrorMessagePojo errors = this.validate(answers);
            if(errors.getErrors().size() > 0)
                return ApiRespnsesHandlerPojo.sendResponse(errors, 500);

            FormSolutions formSolutions = new FormSolutions(userId, formId, this.generateSingleAnswerList(answers), fitInTime);
            new FormSolutionsDBTable().save(formSolutions);

            return ApiRespnsesHandlerPojo.sendResponse(formSolutions, 200);
        } catch (Exception e) {
            LOG.error("AddFormSolutionsHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
