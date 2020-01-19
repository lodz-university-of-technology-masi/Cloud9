package com.serverless.handlers.solveform;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.FormSolutionsDBTable;
import com.serverless.db.QuestionDBTable;
import com.serverless.db.model.CheckAnswer;
import com.serverless.db.model.Form;
import com.serverless.db.model.FormSolutions;
import com.serverless.handlers.pojo.*;
import com.serverless.validators.CheckAnswerValidator;
import com.serverless.validators.FormSolutionsValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AddCheckAnswersHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(AddCheckAnswersHandler.class);

    private ResponseListErrorMessagePojo validate(List<CheckAnswersFormPojo> answers) throws IOException {
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
        for(CheckAnswersFormPojo answer: answers){
            for(String e: CheckAnswerValidator.validator(answer).getErrors())
                errors.add(e);
            if(new QuestionDBTable().get(answer.getId()) == null)
                errors.add("Pytanie nie istnieje " + answer.toString());
        }

        return errors;
    }
    private List<CheckAnswer> generateCheckAnswerList(List<CheckAnswersFormPojo> answers) {
        List<CheckAnswer> checkAnswers = new ArrayList<CheckAnswer>();
        for(CheckAnswersFormPojo answer: answers){
            checkAnswers.add(new CheckAnswer(answer.getId(), answer.getCheck()));
        }
        return checkAnswers;
    }

    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call AddCheckAnswersHandler::handleRequest(" + input + ", " + context + ")");
        try{
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            String formSolutionsId = pathParameters.get("id");
            String userId = pathParameters.get("user");

            FormSolutions formSolutions = new FormSolutionsDBTable().get(formSolutionsId);
            if(formSolutions == null){
                LOG.warn("Not found form solutions ->" + formSolutionsId);
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }
            if(formSolutions.getUserId().compareTo(userId) != 0)
            {
                LOG.warn("Unauthorized user for form ->" + formSolutionsId + "userId" + userId);
                return ApiRespnsesHandlerPojo.sendResponse("unauthorized user", 401);
            }
            List<CheckAnswersFormPojo> checkAnswers = Arrays.asList(
                    new ObjectMapper().convertValue(
                            body.get("checks"),
                            CheckAnswersFormPojo[].class
                    )
            );
            ResponseListErrorMessagePojo errors = this.validate(checkAnswers);
            if(errors.getErrors().size() > 0)
                return ApiRespnsesHandlerPojo.sendResponse(errors, 500);

            formSolutions.setCheck(this.generateCheckAnswerList(checkAnswers));
            new FormSolutionsDBTable().update(formSolutions);
            return ApiRespnsesHandlerPojo.sendResponse(formSolutions, 200);
        }
        catch (Exception e) {
            LOG.error("AddCheckAnswersHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
