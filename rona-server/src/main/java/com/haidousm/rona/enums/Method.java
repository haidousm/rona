package com.haidousm.rona.enums;

public enum Method {
    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    GET_USER("GET_USER"),
    UNDEFINED("UNDEFINED");

    private String method;

    Method(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public static Method of(String method) {
        for (Method m : Method.values()) {
            if (m.getMethod().equals(method)) {
                return m;
            }
        }
        return UNDEFINED;
    }
}
