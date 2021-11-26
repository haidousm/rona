package com.haidousm.rona.common.requests.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.requests.LoginRequest;

public class LoginRequestBuilder {
    private String username;
    private String password;

    public LoginRequestBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public LoginRequestBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public LoginRequest build() {
        return new LoginRequest(username, password);
    }

    public LoginRequest build(String body) {
        return new Gson().fromJson(body, LoginRequest.class);
    }

    public static LoginRequestBuilder builder() {
        return new LoginRequestBuilder();
    }
}

