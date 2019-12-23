package com.serverless;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dal.SolvedForm;
import com.serverless.dal.SolvedFormDBTable;

public class ListSolvedFormsHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Call ListFormHandler::handleRequest(" + input + ", " + context + ")");
		try {
	        // get all forms
	        List<SolvedForm> solvedForms = new SolvedFormDBTable().list();
	        
	        // send the response back
	        return ApiGatewayResponse.builder()
	    				.setStatusCode(200)
	    				.setObjectBody(solvedForms)
	    				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	    				.build();
	    } catch (Exception ex) {
	    	LOG.error("Error in listing forms: " + ex);

	        // send the error response back
	  			Response responseBody = new Response("Error in listing forms: ", input);
	  			return ApiGatewayResponse.builder()
	  					.setStatusCode(500)
	  					.setObjectBody(responseBody)
	  					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	  					.build();
	    }
	}
}
