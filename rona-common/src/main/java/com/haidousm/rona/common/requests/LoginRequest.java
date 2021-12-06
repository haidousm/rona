package com.haidousm.rona.common.requests;


import com.google.gson.annotations.Expose;
import com.haidousm.rona.common.enums.Method;

public class LoginRequest extends GenericRequest {

    @Expose
    private final String username;
    @Expose
    private final String password;

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

    @Override
    public Method getMethod() {
        return Method.LOGIN;
    }

}
