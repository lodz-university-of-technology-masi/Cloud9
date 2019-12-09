package com.serverless;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.dal.Form;
import com.serverless.dal.FormDBTable;


public class CreateFormHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Call CreateFormHandler::handleRequest(" + input + ", " + context + ")");
		try {
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
			FormDBTable table = new FormDBTable();
			Form form = new Form(body);
			table.save(form);
			return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(form)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();
			
		} catch (IOException e) {
			LOG.error("Error in creating form: " + e);
			Response responseBody = new Response("lipa", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
					.build();
		}
		
		
		
		
		
	}
}
