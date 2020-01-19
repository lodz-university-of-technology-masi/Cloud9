package com.serverless.handlers.solveform;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.FormSolutionsDBTable;
import com.serverless.db.QuestionDBTable;
import com.serverless.db.model.*;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.FormSolutionsForRecruiterPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.services.CognitoServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListFormSolutionsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(ListFormSolutionsHandler.class);
    List<FormSolutions> getFormSolutions(String formId) throws IOException {
        return new FormSolutionsDBTable()
                .listFormSolutions()
                .stream()
                .filter(f -> f.getFormId().compareTo(formId) == 0)
                .collect(Collectors.toList());
    }
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call ListFormSolutionsHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            String formId = pathParameters.get("id");
            if(formId == null){
                LOG.error("not found form");
                return ApiRespnsesHandlerPojo.sendResponse("error in path", 500);
            }
            Form form = new FormDBTable().get(formId);
            if(form == null){
                LOG.error("not found");
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }
            List<FormSolutions> formSolutions = this.getFormSolutions(formId);
            List<FormSolutionsForRecruiterPojo> formSolutionPojo = new ArrayList<FormSolutionsForRecruiterPojo>();
            //String id, User user, List<SingleAnswer> answers, List<CheckAnswer> check, Boolean fitInTime
            for(FormSolutions v: formSolutions){
                formSolutionPojo.add(
                        new FormSolutionsForRecruiterPojo(
                                v.getId(),
                                new CognitoServices().getUser(v.getUserId()),
                                v.getAnswers(),
                                v.getCheck(),
                                v.getFitInTime()
                        )
                );
            }


            return ApiRespnsesHandlerPojo.sendResponse(formSolutionPojo, 200);
        }
        catch (Exception e) {
            LOG.error("ListFormSolutionsHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
