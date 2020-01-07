package com.serverless.handlers.user;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.serverless.services.CognitoServices;

public class GetUserHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{
    private static final Logger LOG = LogManager.getLogger(ListUsersHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call GetUserHandler::handleRequest(" + input + ", " + context + ")");
        try{
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            User user = new CognitoServices().getUser(pathParameters.get("id"));
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setObjectBody(user)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        }
        catch (Exception ex){
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody("Error in get user cognito")
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                    .build();
        }
    }
}