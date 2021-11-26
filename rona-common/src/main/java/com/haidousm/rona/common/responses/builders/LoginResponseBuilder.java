package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.LoginResponse;

public class LoginResponseBuilder {
    private String token;
    private long expiryTimestamp;

    public LoginResponseBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public LoginResponseBuilder setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
        return this;
    }

    public LoginResponse build() {
        return new LoginResponse(token, expiryTimestamp);
    }

    public LoginResponse build(GenericResponse genericResponse) {
        LoginResponse loginResponse = new Gson().fromJson(genericResponse.getBody(), LoginResponse.class);
        loginResponse.setStatus(genericResponse.getStatus());
        return loginResponse;
    }

    public static LoginResponseBuilder builder() {
        return new LoginResponseBuilder();
    }
}
