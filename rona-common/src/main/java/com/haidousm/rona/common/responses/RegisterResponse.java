package com.haidousm.rona.common.responses;

import com.google.gson.GsonBuilder;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.enums.Status;

public class RegisterResponse implements Response {
    private Status status;
    private final int userID;

    public RegisterResponse(int userID) {
        this.userID = userID;
    }

    public int getUserID() {
        return userID;
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
        return Method.REGISTER;
    }
}
