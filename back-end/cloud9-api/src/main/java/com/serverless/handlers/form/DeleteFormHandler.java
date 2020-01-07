package com.serverless.handlers.form;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class DeleteFormHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(DeleteFormHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call DeleteFormHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String formId = pathParameters.get("id");
            if(new FormDBTable().delete(formId)){
                LOG.info("Delete" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("success", 200);
            }
            else{
                LOG.info("Failed  " + formId);
                return ApiRespnsesHandlerPojo.sendResponse("failed", 404);
            }

        }
        catch (Exception e){
            LOG.error("DeleteFormHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }

}
