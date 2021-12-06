package com.haidousm.rona.client.controllers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.Request;

public class AuthorizedRequestsController {
    public static Request prepareGetHealthStatusRequest(String token) {
        return new AuthorizedRequest(Method.GET_HEALTH_STATUS, token);
    }

    public static Request prepareUpdateHealthStatusRequest(String token, Health healthStatus) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", healthStatus.name());
        return new AuthorizedRequest(Method.UPDATE_HEALTH_STATUS, token, new Gson().toJson(jsonObject));
    }

    public static Request prepareGetStatsRequest(String token) {
        return new AuthorizedRequest(Method.GET_STATS, token);
    }

    public static Request prepareGetTrustedUsersRequest(String token) {
        return new AuthorizedRequest(Method.GET_TRUSTED_USERS, token);
    }

    public static Request prepareUpdateUserLocationRequest(String token, Integer[] coords) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("latitude", coords[0]);
        jsonObject.addProperty("longitude", coords[1]);
        return new AuthorizedRequest(Method.UPDATE_USER_LOCATION, token, new Gson().toJson(jsonObject));
    }
}
