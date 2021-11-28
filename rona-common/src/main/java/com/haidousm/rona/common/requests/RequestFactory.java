package com.haidousm.rona.common.requests;

import com.google.gson.Gson;
import com.haidousm.rona.common.requests.builders.LoginRequestBuilder;
import com.haidousm.rona.common.requests.builders.RegisterRequestBuilder;
import com.haidousm.rona.common.requests.builders.AuthorizedRequestBuilder;
import com.haidousm.rona.common.requests.builders.UserDetailsRequestBuilder;
import com.haidousm.rona.common.enums.Method;

public class RequestFactory {
    public static Request createRequest(String requestString) {
        String[] tokens = requestString.split(";;");
        if (tokens.length == 0) {
            throw new IllegalArgumentException("Request string is empty");
        }
        Method requestMethod = Method.of(tokens[0]);
        if (tokens.length == 1) {
            return handleNoBodyRequest(requestMethod);
        }
        if (tokens.length == 2) {
            return handleBodyRequest(requestMethod, tokens[1]);
        }
        throw new IllegalArgumentException("Invalid request string: " + requestString);
    }

    public static String createRequestString(Request request) {
        return request.getMethod().getMethod() +
                ";;" +
                new Gson().toJson(request);
    }

    private static Request handleNoBodyRequest(Method requestMethod) {
        switch (requestMethod) {
            case GET_ALL_USERS:
                return LoginRequestBuilder.builder().build();
            default:
                throw new IllegalArgumentException("Invalid request type: " + requestMethod);
        }
    }

    private static Request handleBodyRequest(Method requestMethod, String requestBody) {
        switch (requestMethod) {
            case LOGIN:
                return LoginRequestBuilder.builder().build(requestBody);
            case REGISTER:
                return RegisterRequestBuilder.builder().build(requestBody);
            case GET_USER_BY:
                return UserDetailsRequestBuilder.builder().build(requestBody);
            case GET_USER:
            case GET_HEALTH_STATUS:
                return AuthorizedRequestBuilder.builder().setMethod(requestMethod).build(requestBody);
            default:
                throw new IllegalArgumentException("Invalid request type: " + requestMethod);
        }
    }

}
