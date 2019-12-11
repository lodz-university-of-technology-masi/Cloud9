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
import com.serverless.dal.Question;
import com.serverless.dal.QuestionDBTable;
import java.util.List;

public class ListQuestionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Call ListQuestionHandler::handleRequest(" + input + ", " + context + ")");
		try {
	        // get all forms
			 List<Question> questions = new QuestionDBTable().list();
	        
	        // send the response back
	        return ApiGatewayResponse.builder()
	    				.setStatusCode(200)
	    				.setObjectBody(questions)
	    				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
	    				.build();
	    } catch (Exception ex) {
	    	LOG.error("Error in listing questions: " + ex);

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
