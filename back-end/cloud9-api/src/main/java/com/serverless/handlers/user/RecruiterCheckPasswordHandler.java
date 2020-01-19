package com.serverless.handlers.user;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.serverless.handlers.pojo.ApiRespnsesHandlerPojo;
import com.serverless.handlers.pojo.CheckRecruiterPasswordPojo;
import com.serverless.handlers.pojo.FormInputPojo;
import com.serverless.handlers.pojo.ResponseErrorMessagePojo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.serverless.ApiGatewayResponse;
import com.serverless.Response;


public class RecruiterCheckPasswordHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {
	private static final Logger LOG = LogManager.getLogger(RecruiterCheckPasswordHandler.class);
	private static final String RECRUITER_PASSWORD = System.getenv("RECRUITER_PASSWORD");

	@Override
	public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {
		LOG.info("Call RecruiterCheckPasswordHandler::handleRequest(" + input + ", " + context + ")");
		try {
			JsonNode body = new ObjectMapper().readTree((String) input.get("body"));
			CheckRecruiterPasswordPojo checkRecruiterPasswordPojo = new ObjectMapper().convertValue(body, CheckRecruiterPasswordPojo.class);
			if(checkRecruiterPasswordPojo.getName() == null)
				return ApiRespnsesHandlerPojo.sendResponse("error in body", 500);

			if(checkRecruiterPasswordPojo.getName().compareTo(RECRUITER_PASSWORD) == 0)
				return ApiRespnsesHandlerPojo.sendResponse("ok", 200);
			else
				return ApiRespnsesHandlerPojo.sendResponse("bad password", 200);

		} catch (IOException e) {
			LOG.error("RecruiterCheckPasswordHandler Exception ->" + e.toString());
			return ApiRespnsesHandlerPojo.sendResponse(new ResponseErrorMessagePojo(e.toString()), 500);
		}
	}
}