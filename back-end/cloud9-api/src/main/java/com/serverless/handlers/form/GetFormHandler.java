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

import java.util.List;
import java.util.Map;

public class GetFormHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(ListFormsHandler.class);
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call GetFormHandler::handleRequest(" + input + ", " + context + ")");
        try{
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String formId = pathParameters.get("id");
            Form form = new FormDBTable().get(formId);
            if(form == null){
                LOG.warn("Form not found" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }
            LOG.info("Get form ->" + form.toString());
            FormPojo formPojo;
            if(form.getUsers() != null){
                List<User> users = new CognitoServices().listUser(form.getUsers());
                formPojo = new FormPojo(form.getId(), form.getName(), form.getDescription(), form.getCreationDate(), form.getRecruiterId(), form.getTime(), form.getLang(), users);
            }
            else
                formPojo = new FormPojo(form.getId(), form.getName(), form.getDescription(), form.getCreationDate(), form.getRecruiterId(), form.getTime(), form.getLang(), null);
            return ApiRespnsesHandlerPojo.sendResponse(formPojo, 200);
        }
        catch (Exception e){
            LOG.error("ListFormsHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }



//        try {
//            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
//            String formId = pathParameters.get("id");
//            Form form = new FormDBTable().get(formId);
//            if(form == null){
//                LOG.warn("Form not found" + formId);
//                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
//            }
//            LOG.info("Get form ->" + form.toString());
//            List<User> users = new CognitoServices().listUser(form.getUsers());
////            FormPojo formPojo;
////            if(users != null)
////                formPojo = new FormPojo(form.getId(), form.getName(), form.getDescription(), form.getCreationDate(), form.getRecruiterId(), form.getTime(), form.getLang(), users);
////            else
////                formPojo = new FormPojo(form.getId(), form.getName(), form.getDescription(), form.getCreationDate(), form.getRecruiterId(), form.getTime(), form.getLang(), null);
//            return ApiRespnsesHandlerPojo.sendResponse(form, 200);
//
//        }
//        catch (Exception e){
//            LOG.error("ListFormsHandler Exception ->" + e.toString());
//            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
//        }
    }
}