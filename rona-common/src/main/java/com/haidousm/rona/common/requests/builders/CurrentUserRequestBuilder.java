package com.haidousm.rona.common.requests.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.requests.CurrentUserRequest;

public class CurrentUserRequestBuilder {
    private String token;

    public CurrentUserRequestBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public CurrentUserRequest build() {
        return new CurrentUserRequest(token);
    }

    public CurrentUserRequest build(String body) {
        return new Gson().fromJson(body, CurrentUserRequest.class);
    }

    public static CurrentUserRequestBuilder builder() {
        return new CurrentUserRequestBuilder();
    }
}
