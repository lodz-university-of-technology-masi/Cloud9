package com.serverless.handlers.form;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.handlers.pojo.FormInputPojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.db.model.Form;
import com.serverless.db.FormDBTable;
import com.serverless.ApiGatewayResponse;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.handlers.pojo.ResponseListErrorMessagePojo;
import com.serverless.validators.FormValidator;
public class CreateFormHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(CreateFormHandler.class);
    public Form saveForm(FormInputPojo formInputPojo) throws IOException {
        Form form = new Form();
        form.setName(formInputPojo.getName());
        form.setDescription(formInputPojo.getDescription());
        form.setTime(formInputPojo.getTime());
        form.setRecruiterId(formInputPojo.getRecruiter());
        form.setCreationDate(LocalDate.now().toString());
        form.setLang(formInputPojo.getLang());
        new FormDBTable().save(form);
        return form;
    }
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call CreateFormHandler::handleRequest(" + input + ", " + context + ")");
        try {
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            FormInputPojo formInputPojo = new ObjectMapper().convertValue(body, FormInputPojo.class);

            ResponseListErrorMessagePojo errors = FormValidator.validator(formInputPojo);
            if(errors.getErrors().size() > 0)
                return ApiRespnsesHandlerPojo.sendResponse(errors, 500);

            Form form = this.saveForm(formInputPojo);
            LOG.error("Create Form ->" + form.toString());
            return ApiRespnsesHandlerPojo.sendResponse(form, 200);
        }
        catch (Exception e) {
            LOG.error("CreateFormHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}