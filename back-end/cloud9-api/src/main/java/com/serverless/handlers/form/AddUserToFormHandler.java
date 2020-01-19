package com.serverless.handlers.form;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.model.Form;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AddUserToFormHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(AddUserToFormHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call AddUserToFormHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String formId = pathParameters.get("id");
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            List<String> users = new ObjectMapper().convertValue(body.get("users"), ArrayList.class);
            Form form = new FormDBTable().get(formId);
            if(form == null)
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);

            if(form.getUsers() == null)
                form.setUsers(users);
            else {
                List<String> usersForm = form.getUsers();
                for (String user : users)
                    if(!form.getUsers().contains(user))
                        usersForm.add(user);
                form.setUsers(usersForm);
            }
            new FormDBTable().save(form);
            return ApiRespnsesHandlerPojo.sendResponse(form, 200);
        }
        catch(Exception e){
            LOG.error("AddUserToFormHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
