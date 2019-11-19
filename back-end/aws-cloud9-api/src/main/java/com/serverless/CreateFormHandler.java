package com.serverless;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.dal.Form;
import java.util.Collections;
import java.util.Map;
import java.util.List;
public class CreateFormHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
			Form form = new Form();
			form.setName(body.get("name").asText());
			form.setDescription(body.get("description").asText());
			form.save();
			return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(form)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();
			
		} catch (IOException e) {
			Response responseBody = new Response("lipa", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
					.build();
		}
		
		
		
		
		
	}
}
