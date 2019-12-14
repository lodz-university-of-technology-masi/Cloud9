package QuestionHandlers;

import java.util.Collections;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Handler;
import com.serverless.Response;
import com.serverless.dal.QuestionDBTable;


public class DeleteQuestionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Call DeleteQuestionHandler::handleRequest(" + input + ", " + context + ")");
		try {
	        // get pathParameters
			@SuppressWarnings("unchecked")
			Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
	        String questionId = pathParameters.get("id");
	        Boolean results = new QuestionDBTable().delete(questionId);
	        if (results) {
	            return ApiGatewayResponse.builder()
	        				.setStatusCode(204)
	        				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	        				.build();
	        } else {
	            return ApiGatewayResponse.builder()
	        				.setStatusCode(404)
	        				.setObjectBody("Question with id: '" + questionId + "' not found.")
	        				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	        				.build();
	        }
	        
	    } catch (Exception ex) {
	    	LOG.error("Error in deleting Question: " + ex);
	        // send the error response back
	  			Response responseBody = new Response("Error in deleting Questions: ", input);
	  			return ApiGatewayResponse.builder()
	  					.setStatusCode(500)
	  					.setObjectBody(responseBody)
	  					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	  					.build();
	    }
	}
}
