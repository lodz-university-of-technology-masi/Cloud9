package com.serverless.QuestionHandlers;

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
import com.serverless.ApiGatewayResponse;
import com.serverless.Handler;
import com.serverless.Response;
import com.serverless.dal.ClosedQuestion;
import com.serverless.dal.Form;
import com.serverless.dal.FormDBTable;
import com.serverless.dal.Question;
import com.serverless.dal.QuestionDBTable;

import java.util.Collections;
import java.util.Map;
import java.util.List;
public class SetCorrectAnswerHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
			String questionid=(body.get("idquestion").asText());
			String answer=(body.get("answer").asText());
			
			QuestionDBTable question = new QuestionDBTable(); //form i table
			ClosedQuestion test =question.setCorrectAnswerr(questionid, answer);
			question.save(test);
		
		

			
		
			return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(test)
      				.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
      				.build();
			
		} catch (IOException e) {
			Response responseBody = new Response("lipa", input);
			return ApiGatewayResponse.builder()
					.setStatusCode(500)
					.setObjectBody(e)
					.setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & serverless"))
					.build();
		}
		
		
		
		
		
	}
}
