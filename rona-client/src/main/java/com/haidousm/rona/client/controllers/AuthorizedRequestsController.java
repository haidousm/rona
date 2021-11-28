package com.haidousm.rona.client.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.builders.AuthorizedRequestBuilder;

public class AuthorizedRequestsController {
    public static Request prepareGetHealthStatusRequest(String token) {
        return AuthorizedRequestBuilder.builder().setToken(token).setMethod(Method.GET_HEALTH_STATUS).build();
    }

    public static Request prepareUpdateHealthStatusRequest(String token, Health healthStatus) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", healthStatus.name());
        return AuthorizedRequestBuilder.builder().setToken(token).setMethod(Method.UPDATE_HEALTH_STATUS).setBody(new Gson().toJson(jsonObject)).build();
    }

    public static Request prepareGetStatsRequest(String token) {
        return AuthorizedRequestBuilder.builder().setToken(token).setMethod(Method.GET_STATS).build();
    }

    public static Request prepareGetTrustedUsersRequest(String token) {
        return AuthorizedRequestBuilder.builder().setToken(token).setMethod(Method.GET_TRUSTED_USERS).build();
    }
}
