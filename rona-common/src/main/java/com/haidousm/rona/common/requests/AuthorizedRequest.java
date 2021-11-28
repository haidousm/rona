package com.haidousm.rona.common.requests;

import com.haidousm.rona.common.enums.Method;

public class AuthorizedRequest extends GenericRequest {
    private Method method;
    private String token;

    public AuthorizedRequest(Method method, String token) {
        this.method = method;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public Method getMethod() {
        return method;
    }

}
