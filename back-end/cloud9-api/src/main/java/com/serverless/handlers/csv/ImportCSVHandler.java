package com.serverless.handlers.csv;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.model.Bucket;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.model.Form;
import com.serverless.handlers.form.ListFormsHandler;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.FormInputPojo;
import com.serverless.handlers.pojo.ImportCSVHandlerPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.services.BucketService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

public class ImportCSVHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(ImportCSVHandler.class);
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call ImportCSVHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String formId = pathParameters.get("id");
            Form form = new FormDBTable().get(formId);
            if(form == null){
                LOG.warn("Form not found" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("not found form", 404);
            }

            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            ImportCSVHandlerPojo importCSVHandlerPojo = new ObjectMapper().convertValue(body, ImportCSVHandlerPojo.class);
            if(importCSVHandlerPojo.getNameFile() == null)
                return ApiRespnsesHandlerPojo.sendResponse("not found file name", 500);

            BucketService bucketService = new BucketService();
            List<String> strings = bucketService.uploadFile(importCSVHandlerPojo.getNameFile());
            return ApiRespnsesHandlerPojo.sendResponse(strings, 200);
        }
        catch (Exception e){
            LOG.error("ImportCSVHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}