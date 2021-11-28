package com.haidousm.rona.common.requests.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.requests.AuthorizedRequest;

public class AuthorizedRequestBuilder {
    private String token;
    private Method method;

    public AuthorizedRequestBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public AuthorizedRequestBuilder setMethod(Method method) {
        this.method = method;
        return this;
    }

    public AuthorizedRequest build() {
        return new AuthorizedRequest(method, token);
    }

    public AuthorizedRequest build(String body) {
        return new Gson().fromJson(body, AuthorizedRequest.class);
    }

    public static AuthorizedRequestBuilder builder() {
        return new AuthorizedRequestBuilder();
    }
}
