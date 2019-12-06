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
public class CreateClosedQuestionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

	private static final Logger LOG = LogManager.getLogger(Handler.class);

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		try {

			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
			ClosedQuestion question= new ClosedQuestion();
			/*
			
			Przy iterowania po mapie i tak bede musial wszystkie pola sprawdzac i przypisywac do odpowiednich wartosci
			rozwiazanie do przemyslenia na nastepna iteracje
			
			*/
			try {
			question.setId(body.get("id").asText());
			} 
			catch (Exception ex)
			{
			question.setId(null);
			}
			try {
			question.setName(body.get("name").asText());
			} 
			catch (Exception ex)
			{
			question.setName(null);}
			try {
			question.setQuestion(body.get("question").asText());
			} 
			catch (Exception ex)
			{
			question.setQuestion(null);}
			try {
			question.setForm_membership(body.get("form").asText());
			} 
			catch (Exception ex)
			{
			question.setForm_membership(null);}
			try {
			question.setCorrect_answer(body.get("correct").asText());
			} 
			catch (Exception ex)
			{
			question.setCorrect_answer(null);}
			try {
			question.setWrong_answer1(body.get("wrong1").asText());
			} 
			catch (Exception ex)
			{
			question.setWrong_answer1(null);}
			try {
			question.setWrong_answer2(body.get("wrong2").asText());
			} 
			catch (Exception ex)
			{
			question.setWrong_answer2(null);}
			try {
			question.setWrong_answer1(body.get("wrong2").asText());}
			catch (Exception ex)
			{
			question.setWrong_answer3(null);}
			/*
			Task task= new Task();
			task.setId("test1");
			task.setName("test");
			task.setQuestion("test?");
			task.setForm_membership("form2");*/
			QuestionDBTable dbquest=new QuestionDBTable();
			
			dbquest.save(question);
			return ApiGatewayResponse.builder()
      				.setStatusCode(200)
      				.setObjectBody(question)
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
