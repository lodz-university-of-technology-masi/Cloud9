package com.serverless.handlers.question;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
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

public class ListQuestionsWithoutLangForForm implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(ListQuestionsWithoutLangForForm.class);
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call ListQuestionsWithoutLangForForm::handleRequest(" + input + ", " + context + ")");
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
                    .collect(Collectors.toList());
            return ApiRespnsesHandlerPojo.sendResponse(questions, 200);
        }
        catch (Exception e){
            LOG.error("ListQuestionsWithoutLangForForm Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }

    }
}