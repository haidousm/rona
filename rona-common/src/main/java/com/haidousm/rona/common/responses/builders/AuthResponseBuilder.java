package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.TokenResponse;

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

    public TokenResponse build() {
        return new TokenResponse(token, expiryTimestamp);
    }

    public TokenResponse build(GenericResponse genericResponse) {
        TokenResponse tokenResponse = new Gson().fromJson(genericResponse.getResponse(), TokenResponse.class);
        tokenResponse.setStatus(genericResponse.getStatus());
        return tokenResponse;
    }

    public static AuthResponseBuilder builder() {
        return new AuthResponseBuilder();
    }
}
