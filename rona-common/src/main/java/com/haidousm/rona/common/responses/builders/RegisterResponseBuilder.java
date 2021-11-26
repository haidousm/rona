package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.RegisterResponse;

public class RegisterResponseBuilder {
    private int userID;

    public RegisterResponseBuilder setUserID(int userID) {
        this.userID = userID;
        return this;
    }

    public RegisterResponse build() {
        return new RegisterResponse(userID);
    }

    public RegisterResponse build(GenericResponse response) {
        RegisterResponse registerResponse = new Gson().fromJson(response.getBody(), RegisterResponse.class);
        registerResponse.setStatus(response.getStatus());
        return registerResponse;
    }

    public static RegisterResponseBuilder builder() {
        return new RegisterResponseBuilder();
    }

}
