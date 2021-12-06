package com.haidousm.rona.common.responses;

import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.enums.Status;

public class TokenResponse implements Response {
    private Status status;
    @Expose
    private String token;
    @Expose
    private long expiryTimestamp;

    public TokenResponse() {

    }

    public TokenResponse(String token, long expiryTimestamp) {
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

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Method getMethod() {
        return Method.LOGIN;
    }
}
