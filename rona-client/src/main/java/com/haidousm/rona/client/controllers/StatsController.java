package com.haidousm.rona.client.controllers;

import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.builders.AuthorizedRequestBuilder;

public class StatsController {
    public static Request prepareGetStatsRequest(String token) {
        return AuthorizedRequestBuilder.builder().setToken(token).setMethod(Method.GET_STATS).build();
    }
}
