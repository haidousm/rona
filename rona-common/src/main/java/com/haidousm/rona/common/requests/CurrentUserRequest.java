package com.haidousm.rona.common.requests;

import com.haidousm.rona.common.enums.Method;

public class CurrentUserRequest implements Request {
    private String token;

    public CurrentUserRequest(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Method getMethod() {
        return Method.GET_CURRENT_USER;
    }

}
