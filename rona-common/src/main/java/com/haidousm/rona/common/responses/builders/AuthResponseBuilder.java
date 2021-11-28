package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.AuthResponse;

public class AuthResponseBuilder {
    private String token;
    private long expiryTimestamp;

    public AuthResponseBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public AuthResponseBuilder setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
        return this;
    }

    public AuthResponse build() {
        return new AuthResponse(token, expiryTimestamp);
    }

    public AuthResponse build(GenericResponse genericResponse) {
        AuthResponse authResponse = new Gson().fromJson(genericResponse.getResponse(), AuthResponse.class);
        authResponse.setStatus(genericResponse.getStatus());
        return authResponse;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }
}
