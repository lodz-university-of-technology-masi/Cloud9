package com.serverless.handlers.translate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.handlers.solveform.GetSummaryFormHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;
import com.serverless.db.model.Form;
import com.serverless.db.model.Question;
import com.serverless.db.FormDBTable;
import com.serverless.db.QuestionDBTable;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.util.Map;

public class AddTranslateForm implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(AddTranslateForm.class);
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call AddTranslateForm::handleRequest(" + input + ", " + context + ")");
        try{
        	Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
        	String formId = pathParameters.get("id");
            Form form = new FormDBTable().get(formId);
            if(form == null){
                LOG.warn("Not found form ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }
            
            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            String targetLanguage = body.get("targetLanguage").asText();
            String sourceLanguage = body.get("sourceLanguage").asText();
            
            QuestionDBTable questionDBTable = new QuestionDBTable(); 
            List<Question> questions = questionDBTable
                    .listQuestions()
                    .stream()
                    .filter(q -> (q.getFormId().compareTo(formId) == 0 && q.getLanguage().compareTo(sourceLanguage) == 0))
                    .collect(Collectors.toList());
            
            if(questions.size() == 0) {
            	LOG.warn("Not questions found with source language ->" + sourceLanguage);
                return ApiRespnsesHandlerPojo.sendResponse("Not questions found with that source language", 404);
            }
            
            List<Question> translatedQuestions = new ArrayList<Question>();
            String yandexKey = "trnsl.1.1.20200102T123452Z.4c2ff64ffe5531e9.036a58f5df6fef3f5918c5e91066912fc8c9c9f1";
            String lang = sourceLanguage + "-" + targetLanguage;
            String url = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + yandexKey + "&lang=" + lang;
            for (Question question: questions) {
            	url = url + "&text=" + question.getQuestion();
            	
            	if(question.getType().compareTo("W") == 0) {
            		url = url + "---" + question.getAnswer();
            		for(String answer: question.getAnswerList()) {
            			url = url + "---" + answer;
            		}
            	}
            }
            CloseableHttpClient httpClient = HttpClients.createDefault();
	        HttpGet request = new HttpGet(url.replaceAll(" ", "%20"));
	        CloseableHttpResponse response = httpClient.execute(request);
	        String json = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
	        JsonNode responseBody = new ObjectMapper().readTree((String) json);
	        List<String> text = new ObjectMapper().convertValue(responseBody.get("text"), ArrayList.class);
	        for (int i = 0; i < questions.size(); i++) {
	        	
	        	if(questions.get(i).getType().compareTo("W") == 0) {
	        		List<String> typeWQuestion = new ArrayList<String>(Arrays.asList(text.get(i).split("---")));
	        		String question = typeWQuestion.remove(0);
	        		String answer = typeWQuestion.remove(0);
	        		Question translatedQuestion = new Question(formId, questions.get(i).getType(), targetLanguage, question, answer, typeWQuestion);
	        		translatedQuestions.add(translatedQuestion);
	            	questionDBTable.save(translatedQuestion);
	        	}
	        	else {
	        		Question translatedQuestion = new Question(formId, questions.get(i).getType(), targetLanguage, text.get(i));
	        		translatedQuestions.add(translatedQuestion);
	            	questionDBTable.save(translatedQuestion);
	        	}
	        	
            }
	        LOG.info(translatedQuestions);
            return ApiRespnsesHandlerPojo.sendResponse(translatedQuestions, 200);
        }
        catch (Exception e) {
            LOG.error("GetAnswersFormSolutionsHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
