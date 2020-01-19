package com.serverless.handlers.solveform;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.db.FormDBTable;
import com.serverless.db.FormSolutionsDBTable;
import com.serverless.db.QuestionDBTable;
import com.serverless.db.model.*;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.GetFormSolutionsPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import com.serverless.services.CognitoServices;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetFormSolutionsHandler  implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
    private static final Logger LOG = LogManager.getLogger(GetFormSolutionsHandler.class);
    private List<Question> getQuestionForForm(FormSolutions formSolution) throws IOException {
        List<Question> results = new ArrayList<Question>();
        QuestionDBTable questionDBTable = new QuestionDBTable();
        for(SingleAnswer answer: formSolution.getAnswers())
            results.add(questionDBTable.get(answer.getQuestionId()));
        return results;
    }
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
        LOG.info("Call GetFormSolutionsHandler::handleRequest(" + input + ", " + context + ")");
        try {
            Map<String, String> pathParameters = (Map<String, String>) input.get("pathParameters");
            String formSolutionsId = pathParameters.get("id");
            if(formSolutionsId == null){
                LOG.error("not found form solutions");
                return ApiRespnsesHandlerPojo.sendResponse("not found form solutions", 404);
            }
            FormSolutions formSolution =  new FormSolutionsDBTable().get(formSolutionsId);
            User user = new CognitoServices().getUser(formSolution.getUserId());
            List<Question> questions = this.getQuestionForForm(formSolution);
            Form form = new FormDBTable().get(formSolution.getFormId());
            //String formId, List<SingleAnswer> answers, List<CheckAnswer> check, Boolean fitInTime, User user, List<Question> questions
            GetFormSolutionsPojo getFormSolutionsPojo = new GetFormSolutionsPojo(
                    form.getRecruiterId(),
                    formSolution.getAnswers(),
                    formSolution.getCheck(),
                    formSolution.getFitInTime(),
                    user,
                    questions
            );

            return ApiRespnsesHandlerPojo.sendResponse(getFormSolutionsPojo, 200);
        }
        catch (Exception e) {
            LOG.error("GetFormSolutionsHandler Exception ->" + e.toString());
            return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
        }
    }
}
