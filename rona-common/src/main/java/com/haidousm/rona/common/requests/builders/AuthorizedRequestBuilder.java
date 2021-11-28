package com.haidousm.rona.common.requests.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.requests.AuthorizedRequest;

public class AuthorizedRequestBuilder {
    private Method method;
    private String token;
    private String body;

    public AuthorizedRequestBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public AuthorizedRequestBuilder setMethod(Method method) {
        this.method = method;
        return this;
    }

    public AuthorizedRequestBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public AuthorizedRequest build() {
        return new AuthorizedRequest(method, token, body);
    }

    public AuthorizedRequest build(String json) {
        return new Gson().fromJson(json, AuthorizedRequest.class);
    }

    public static AuthorizedRequestBuilder builder() {
        return new AuthorizedRequestBuilder();
    }
}
