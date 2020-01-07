package com.serverless.handlers.pojo;
import java.util.Collections;
import com.serverless.ApiGatewayResponse;

public final class ApiRespnsesHandlerPojo {
    public static ApiGatewayResponse sendResponse(Object objectBody, Integer statusCode){
        return ApiGatewayResponse.builder()
                .setStatusCode(statusCode)
                .setObjectBody(objectBody)
                .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
                .build();
    }
}
