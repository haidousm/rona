package com.haidousm.rona.common.responses;

import com.google.gson.GsonBuilder;
import com.haidousm.rona.common.enums.Method;

public class RegisterResponse {
    private int userID;

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
}
