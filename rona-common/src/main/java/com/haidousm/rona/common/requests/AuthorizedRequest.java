package com.haidousm.rona.common.requests;

import com.haidousm.rona.common.enums.Method;

public class AuthorizedRequest extends GenericRequest {
    private Method method;
    private String token;
    private String body;

    public AuthorizedRequest(Method method, String token, String body) {
        this.method = method;
        this.token = token;
        this.body = body;
    }

    public String getToken() {
        return token;
    }

    public String getBody() {
        return body;
    }

    @Override
    public Method getMethod() {
        return method;
    }

}
