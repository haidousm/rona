package com.haidousm.rona.common.requests;

import com.haidousm.rona.common.enums.Method;

public class AuthorizedRequest extends GenericRequest {
    private String token;

    public AuthorizedRequest(Method method, String token, String body) {
        this.setMethod(method);
        this.token = token;
        this.setBody(body);
    }

    public String getToken() {
        return token;
    }


}
