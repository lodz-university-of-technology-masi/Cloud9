package com.serverless.handlers.solveform;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.FormSolutionsDBTable;
import com.serverless.db.model.Form;
import com.serverless.db.model.FormSolutions;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GetSummaryFormHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(GetSummaryFormHandler.class);
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call GetSummaryFormHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            String formId = pathParameters.get("id");
            String userId = pathParameters.get("user");

            Form form = new FormDBTable().get(formId);
            if(form == null){
                LOG.warn("Not found form ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }

            if(!form.getUsers().contains(userId))
            {
                LOG.warn("Unauthorized user for form ->" + formId + "userId" + userId);
                return ApiRespnsesHandlerPojo.sendResponse("unauthorized user", 401);
            }
            List<FormSolutions> formSolutions = new FormSolutionsDBTable()
                    .listFormSolutions()
                    .stream()
                    .filter(q -> q.getFormId().compareTo(formId) == 0 && q.getUserId().compareTo(userId) == 0)
                    .collect(Collectors.toList());
            if(formSolutions.size() != 0)
                return ApiRespnsesHandlerPojo.sendResponse(formSolutions.get(0).getCheck(), 200);
            else
                return ApiRespnsesHandlerPojo.sendResponse("not check", 200);
        }
        catch (Exception e) {
            LOG.error("GetSummaryFormHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
