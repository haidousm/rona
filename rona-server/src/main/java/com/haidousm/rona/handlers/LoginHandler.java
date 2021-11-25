package com.haidousm.rona.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.enums.Status;
import com.haidousm.rona.requests.LoginRequest;
import com.haidousm.rona.requests.Request;
import com.haidousm.rona.response.Response;

public class LoginHandler {

    public static Response handleLogin(Request request) {
        Response response = new Response();
        try {
            LoginRequest loginRequest = new Gson().fromJson(request.getBody(), LoginRequest.class);
            response = login(loginRequest);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }

    private static Response login(LoginRequest loginRequest) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        if (loginRequest.getUsername().equals("admin") && loginRequest.getPassword().equals("admin")) {
            response.setBody("{\"token\":\"admin\"}");
        } else {
            response.setStatus(Status.UNAUTHORIZED);
        }
        return response;
    }
}
