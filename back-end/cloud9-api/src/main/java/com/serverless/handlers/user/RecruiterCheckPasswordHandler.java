package com.serverless.handlers.user;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;


public class RecruiterCheckPasswordHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private static final Logger LOG = LogManager.getLogger(RecruiterCheckPasswordHandler.class);
	private static final String RECRUITER_PASSWORD = System.getenv("RECRUITER_PASSWORD");

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Call RecruiterCheckPasswordHandler::handleRequest(" + input + ", " + context + ")");
		try {
			String checkPassword;
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
			if(body.get("name").asText().compareTo(RECRUITER_PASSWORD) == 0)
				checkPassword = "{\"status\": \"true\"}";
			else
				checkPassword = "{\"status\": \"false\"}";
			LOG.info("Check Password:" + checkPassword);
			Response responseBody = new Response(checkPassword, input);
			return ApiGatewayResponse.builder()
					.setStatusCode(200)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
					.build();

		} catch (IOException e) {
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody("Error in check recruiter password")
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
					.build();
		}
	}
}