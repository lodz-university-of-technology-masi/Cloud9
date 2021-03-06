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
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ListQuestionsForForm implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(ListQuestionsForForm.class);
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call ListQuestionsForForm::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String,String> pathParameters = (Map<String,String>)input.get("pathParameters");
            String formId = pathParameters.get("id");
            Form form = new FormDBTable().get(formId);
            if(form == null){
                LOG.warn("Not found form ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("not found", 404);
            }

            List<Question> questions = new QuestionDBTable()
                    .listQuestions()
                    .stream()
                    .filter(q -> q.getFormId().compareTo(formId) == 0)
                    .filter(q -> q.getLanguage().compareTo(form.getLang() == null ? "pl": form.getLang()) == 0)
                    .collect(Collectors.toList());
            return ApiRespnsesHandlerPojo.sendResponse(questions, 200);
        }
        catch (Exception e){
            LOG.error("ListQuestionsForForm Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }

    }
}
