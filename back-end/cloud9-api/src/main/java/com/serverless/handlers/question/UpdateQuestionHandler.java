package com.serverless.handlers.question;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.QuestionDBTable;
import com.serverless.db.model.Form;
import com.serverless.db.model.Question;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.QuestionPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.handlers.pojo.ResponseListErrorMessagePojo;
import com.serverless.validators.QuestionValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public class UpdateQuestionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(UpdateQuestionHandler.class);

    private ResponseListErrorMessagePojo validate(QuestionPojo question){
        ResponseListErrorMessagePojo errors = new ResponseListErrorMessagePojo();
            switch (question.getType()) {
                case "O":
                    for(String e: QuestionValidator.validatorQuestionTypeO(question).getErrors())
                        errors.add(e +"" + question.toString());
                    break;
                case "L":
                    for(String e: QuestionValidator.validatorQuestionTypeL(question).getErrors())
                        errors.add(e +"" + question.toString());
                    break;
                case "W":
                    for(String e: QuestionValidator.validatorQuestionTypeW(question).getErrors())
                        errors.add(e +"" + question.toString());
                    break;
                default:
                    errors.add("Nieznany typ pytania: " +question.toString());
            }

        return errors;
    }
    private void updateQuestnion(QuestionPojo questionPojo, Question question) throws IOException {
        QuestionDBTable questionDBTable = new QuestionDBTable();
        if(questionPojo.getQuestion() != null)
            question.setQuestion(questionPojo.getQuestion());
        if(questionPojo.getLang() != null)
            question.setLanguage(questionPojo.getLang());
            switch (question.getType()) {
                case "O":
                case "L":
                    questionDBTable.update(question);
                    break;
                case "W":
                    if(questionPojo.getAnswer() != null)
                        question.setAnswer(questionPojo.getAnswer());
                    if(questionPojo.getAnswers() != null)
                        question.setAnswerList(questionPojo.getAnswers());
                    questionDBTable.update(question);
                    break;
            }
        }
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call UpdateQuestionHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            String formId = pathParameters.get("id");
            String questionId = pathParameters.get("question");
            Form form = new FormDBTable().get(formId);
            if (form == null) {
                LOG.warn("Form not found" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("form not found", 404);
            }

            Question question = new QuestionDBTable().get(questionId);
            if (question == null) {
                LOG.warn("Question not found" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("question not found", 404);
            }

            if (question.getFormId().compareTo(form.getId()) != 0) {
                LOG.warn("Unauthorized form for question ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("unauthorized form operation", 401);
            }

            JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
            if (form.getRecruiterId().compareTo(body.get("recruiter").asText()) != 0) {
                LOG.warn("Unauthorized form for question ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("unauthorized user operation", 401);
            }

            QuestionPojo questionPojo = new ObjectMapper().convertValue(body.get("question"), QuestionPojo.class);

            ResponseListErrorMessagePojo errors = this.validate(questionPojo);
            if(errors.getErrors().size() > 0)
                return ApiRespnsesHandlerPojo.sendResponse(errors, 500);

            this.updateQuestnion(questionPojo, question);
            return ApiRespnsesHandlerPojo.sendResponse(questionPojo, 200);


        } catch (Exception e) {
            LOG.error("ListQuestionsForForm Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
