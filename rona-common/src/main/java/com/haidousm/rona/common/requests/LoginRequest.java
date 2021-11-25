package com.haidousm.rona.common.requests;


import com.google.gson.Gson;

public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return new Gson().toJson(this);
    }

}
