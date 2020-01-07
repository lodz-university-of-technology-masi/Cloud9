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
import java.util.stream.Collectors;

public class ListFormForRecruiterHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(ListFormForRecruiterHandler.class);
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call ListFormForRecruiterHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String recruiterId = pathParameters.get("id");

            List<Form> forms = new FormDBTable()
                                    .listForms()
                                    .stream()
                                    .filter(u -> u.getRecruiterId().compareTo(recruiterId) == 0)
                                    .collect(Collectors.toList());

            List<FormPojo> formsPojo = new ArrayList<>();

            for(Form form: forms) {
                if(form.getUsers() != null) {
                    List<User> users = new CognitoServices().listUser(form.getUsers());
                    formsPojo.add(new FormPojo(form.getId(), form.getName(), form.getDescription(), form.getCreationDate(), form.getRecruiterId(), form.getTime(),form.getLang(), users));
                }
                else
                    formsPojo.add(new FormPojo(form.getId(), form.getName(), form.getDescription(), form.getCreationDate(), form.getRecruiterId(), form.getTime(),form.getLang(), null));

            }
            return ApiRespnsesHandlerPojo.sendResponse(formsPojo, 200);
        }
        catch (Exception e){
            LOG.error("ListFormForRecruiterHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
