package com.haidousm.rona.common.responses;

import com.google.gson.GsonBuilder;

public class LoginResponse {
    private final String token;
    private final long expiryTimestamp;

    public LoginResponse(String token, long expiryTimestamp) {
        this.token = token;
        this.expiryTimestamp = expiryTimestamp;
    }

    public String getToken() {
        return token;
    }

    public long getExpiryTimestamp() {
        return expiryTimestamp;
    }

    @Override
    public String toString() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(this);
    }
}
