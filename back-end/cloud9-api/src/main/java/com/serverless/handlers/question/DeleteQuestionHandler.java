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

import java.util.Map;

public class DeleteQuestionHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(DeleteQuestionHandler.class);
    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        try{
            Map<String,String> pathParameters =  (Map<String,String>)input.get("pathParameters");
            String formId = pathParameters.get("id");
            String questionId = pathParameters.get("question");
            Form form = new FormDBTable().get(formId);
            if(form == null){
                LOG.warn("Form not found" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("form not found", 404);
            }

            Question question = new QuestionDBTable().get(questionId);
            if(question == null){
                LOG.warn("Question not found" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("question not found", 404);
            }

            if(question.getFormId().compareTo(form.getId()) != 0){
                LOG.warn("Unauthorized form for question ->" + formId);
                return ApiRespnsesHandlerPojo.sendResponse("unauthorized operation", 401);
            }

            if(new QuestionDBTable().delete(questionId)){
                LOG.warn("Delete " + questionId);
                return ApiRespnsesHandlerPojo.sendResponse("success", 200);
            }
            else{
                LOG.warn("Failed  " + questionId);
                return ApiRespnsesHandlerPojo.sendResponse("failed", 404);
            }
        }
        catch (Exception e){
            LOG.error("DeleteQuestionHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }

    }
}
