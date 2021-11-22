package com.haidousm.LAURona.requests;

import com.haidousm.LAURona.enums.Method;

public class Request {
    private final Method method;
    private final String body;


    public Request(String untokenizedRequest) {
        String[] tokens = untokenizedRequest.split(";;", 2);
        if (tokens.length != 2) {
            this.method = Method.UNDEFINED;
            this.body = "";
        } else {
            this.method = Method.of(tokens[0]);
            this.body = tokens[1];
        }

    }

    public Method getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }
}
