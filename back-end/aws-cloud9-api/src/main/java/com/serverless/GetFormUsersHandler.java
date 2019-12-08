package com.serverless;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dal.Form;
import com.serverless.dal.FormDBTable;


public class GetFormUsersHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse>{
	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			LOG.info("Call GetFormUsersHanlder::handleRequest(" + input + ", " + context + ")");
			@SuppressWarnings("unchecked")
			Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
	        String formId = pathParameters.get("id");
			Form form = new FormDBTable().get(formId);
			if(form!=null) {

			return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(form.getUsers())
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();
			}
			else {
				LOG.error("Form with id: '" + formId + "' not found.");
				return ApiGatewayResponse.builder()
        				.setStatusCode(404)
        				.setObjectBody("Form with id: '" + formId + "' not found.")
        				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
        				.build();
			}
			
		} catch (IOException e) {
			LOG.error("Error in getting users from form: " + e);
			Response responseBody = new Response("lipa", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(responseBody)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
					.build();
		}
	}
}

