package com.serverless;

import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.dal.SolvedForm;
import com.serverless.dal.SolvedFormDBTable;

public class GetSolvedFormHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Call GetSolvedFormHandler::handleRequest(" + input + ", " + context + ")");
		try {
	        // get pathParameters
			@SuppressWarnings("unchecked")
			Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
	        String id = pathParameters.get("id");
	        SolvedForm solvedForm = new SolvedFormDBTable().get(id);

	        // send the response back
	        if (solvedForm != null) {
	          return ApiGatewayResponse.builder()
	      				.setStatusCode(200)
	      				.setObjectBody(solvedForm)
	      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	      				.build();
	        } else {
	          return ApiGatewayResponse.builder()
	      				.setStatusCode(404)
	              .setObjectBody("Solved with id: '" + id + "' not found.")
	      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	      				.build();
	        }
	    } catch (Exception ex) {
	    	LOG.error("Error in getting solved form: " + ex);
	        // send the error response back
	  			Response responseBody = new Response("Error in retrieving forms: ", input);
	  			return ApiGatewayResponse.builder()
	  					.setStatusCode(500)
	  					.setObjectBody(responseBody)
	  					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	  					.build();
	    }
	}
}
