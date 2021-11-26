package com.haidousm.rona.common.requests;

import com.haidousm.rona.common.enums.Method;

public class Request {
    private final Method method;
    private final String body;

    public Request(String untokenizedRequest) {
        if (!untokenizedRequest.contains(";;")) {
            this.method = Method.of(untokenizedRequest);
            this.body = "{}";
        } else {
            String[] tokens = untokenizedRequest.split(";;", 2);
            if (tokens.length != 2) {
                this.method = Method.UNDEFINED;
                this.body = "";
            } else {
                this.method = Method.of(tokens[0]);
                this.body = tokens[1];
            }
        }

    }

    public Request(Method method, String body) {
        this.method = method;
        this.body = body;
    }

    public Method getMethod() {
        return method;
    }

    public String getBody() {
        return body;
    }

    public String toString() {
        return method.toString() + ";;" + body;
    }
}
