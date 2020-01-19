package com.serverless.handlers.form;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.model.Form;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.FormInputPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.handlers.pojo.ResponseListErrorMessagePojo;
import com.serverless.validators.FormValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Map;

public class UpdateFormHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(UpdateFormHandler.class);
    private Form update(FormInputPojo formInputPojo, Form form) throws IOException {
        FormDBTable table = new FormDBTable();
        if(formInputPojo.getName() != null)
            form.setName(formInputPojo.getName());

        if(formInputPojo.getDescription() != null)
            form.setDescription(formInputPojo.getDescription());

        if(formInputPojo.getTime() != null)
            form.setTime(formInputPojo.getTime());

        if(formInputPojo.getLang() != null)
            form.setLang(formInputPojo.getLang());

        if(formInputPojo.getUsers() != null)
            form.setUsers(formInputPojo.getUsers());
        table.update(form);

        LOG.info("Update form after ->" + form.toString());
        return form;
    }
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call UpdateFormHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String formId = pathParameters.get("id");

            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            FormInputPojo formInputPojo = new ObjectMapper().convertValue(body, FormInputPojo.class);

            FormDBTable table = new FormDBTable();
            Form form = table.get(formId);
            if(form == null) {
                LOG.warn("Update not found form ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }
            if(form.getRecruiterId().compareTo(formInputPojo.getRecruiter()) != 0) {
                LOG.warn("Unauthorized recruiter for form ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("unauthorized user", 401);
            }

            ResponseListErrorMessagePojo errors = FormValidator.validatorUpdate(formInputPojo);
            if(errors.getErrors().size() > 0)
                return ApiRespnsesHandlerPojo.sendResponse(errors, 500);

            form = this.update(formInputPojo, form);
            return ApiRespnsesHandlerPojo.sendResponse(form, 200);
        }
        catch (Exception e){
            LOG.error("UpdateFormHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
