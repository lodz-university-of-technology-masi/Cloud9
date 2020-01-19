package com.serverless.handlers.translate;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.FormInputPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.handlers.pojo.TranslatePojo;
import com.serverless.handlers.solveform.GetSummaryFormHandler;
import com.serverless.settings.YandexSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
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
    private List<Question> getQuestions(String formId, String sourceLanguage) throws IOException {
        return new QuestionDBTable()
                .listQuestions()
                .stream()
                .filter(q -> (q.getFormId().compareTo(formId) == 0 && q.getLanguage().compareTo(sourceLanguage) == 0))
                .collect(Collectors.toList());
    }
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try{
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String formId = pathParameters.get("id");
            Form form = new FormDBTable().get(formId);
            if(form == null){
                LOG.warn("Not found form ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }

            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            TranslatePojo translatePojo = new ObjectMapper().convertValue(body, TranslatePojo.class);
//            if(form.getRecruiterId().compareTo(translatePojo.getRecruiterId()) !=0){
//                LOG.warn("not authorized form ->" + formId);
//                return ApiRespnsesHandlerPojo.sendResponse("not authorized", 500);
//            }
            List<Question> questions = this.getQuestions(formId, translatePojo.getSourceLanguage().toUpperCase());

            if(questions.size() == 0) {
                LOG.warn("Not questions found with source language ->" + translatePojo.getSourceLanguage());
                return ApiRespnsesHandlerPojo.sendResponse("Not questions found with that source language", 404);
            }
            List<Question> translatedQuestions = new ArrayList<Question>();
            QuestionDBTable questionDBTable = new QuestionDBTable();
            String yandexKey = YandexSettings.YANDEX_KEY;
            String lang = translatePojo.getSourceLanguage() + "-" + translatePojo.getTargetLanguage();
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
                    Question translatedQuestion = new Question(formId, questions.get(i).getType(), translatePojo.getTargetLanguage().toUpperCase(), question, answer, typeWQuestion);
                    translatedQuestions.add(translatedQuestion);
                    questionDBTable.save(translatedQuestion);
                }
                else {
                    Question translatedQuestion = new Question(formId, questions.get(i).getType(), translatePojo.getTargetLanguage().toUpperCase(), text.get(i));
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