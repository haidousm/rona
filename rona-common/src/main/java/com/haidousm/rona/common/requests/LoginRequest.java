package com.haidousm.rona.common.requests;


import com.google.gson.Gson;
import com.haidousm.rona.common.enums.Method;

public class LoginRequest implements Request {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String toString() {
        return new Gson().toJson(this);
    }

    public Method getMethod() {
        return Method.LOGIN;
    }

}
