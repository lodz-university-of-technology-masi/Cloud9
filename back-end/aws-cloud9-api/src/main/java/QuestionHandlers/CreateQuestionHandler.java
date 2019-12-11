package QuestionHandlers;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Handler;
import com.serverless.Response;
import com.serverless.dal.Question;
import com.serverless.dal.QuestionDBTable;


public class CreateQuestionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Call CreateQuestionHandler::handleRequest(" + input + ", " + context + ")");
		try {
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));			
			QuestionDBTable dbquest=new QuestionDBTable();
			Question question = new Question(body);

			
			dbquest.save(question);
			return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(question)
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