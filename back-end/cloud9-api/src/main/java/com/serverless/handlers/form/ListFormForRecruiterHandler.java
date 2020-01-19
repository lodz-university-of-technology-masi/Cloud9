package com.serverless.handlers.form;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.model.Form;
import com.serverless.db.model.User;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.FormPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.services.CognitoServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListFormForRecruiterHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(ListFormForRecruiterHandler.class);
    private List<Form> getAllFormForRecruiter(String recruiterId) throws IOException {
        return new FormDBTable()
                .listForms()
                .stream()
                .filter(f -> f.getRecruiterId().compareTo(recruiterId) == 0)
                .collect(Collectors.toList());
    }

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call ListFormForRecruiterHandler::handleRequest(" + input + ", " + context + ")");
        try{
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String recruiterId = pathParameters.get("id");
            if(recruiterId == null)
                return ApiRespnsesHandlerPojo.sendResponse("You have error in path", 500);
            return ApiRespnsesHandlerPojo.sendResponse(getAllFormForRecruiter(recruiterId), 200);
        }
        catch(Exception e){
            LOG.error("ListFormForRecruiterHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
