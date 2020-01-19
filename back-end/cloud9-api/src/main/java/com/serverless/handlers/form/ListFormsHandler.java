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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ListFormsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(ListFormsHandler.class);
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call ListFormsHandler::handleRequest(" + input + ", " + context + ")");
        try {
            List<Form> forms = new FormDBTable().listForms();
            return ApiRespnsesHandlerPojo.sendResponse(forms, 200);
        }
        catch (Exception e){
            LOG.error("ListFormsHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
