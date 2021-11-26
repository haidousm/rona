package com.haidousm.rona.common.responses;

import com.google.gson.GsonBuilder;

public class RegisteredUserResponse {
    private int userID;

    public RegisteredUserResponse(int userID) {
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
