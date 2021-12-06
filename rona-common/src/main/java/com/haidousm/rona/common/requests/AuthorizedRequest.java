package com.haidousm.rona.common.requests;

import com.google.gson.annotations.Expose;
import com.haidousm.rona.common.enums.Method;

public class AuthorizedRequest extends GenericRequest {
    @Expose
    private String token;
    public AuthorizedRequest(Method method, String token) {
        this.token = token;
        this.setMethod(method);
    }

    public AuthorizedRequest(Method method, String token, String body) {
        this.setMethod(method);
        this.token = token;
        this.setBody(body);
    }

    public String getToken() {
        return token;
    }

}
