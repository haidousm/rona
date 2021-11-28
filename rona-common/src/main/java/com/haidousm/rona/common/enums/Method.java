package com.haidousm.rona.common.enums;

public enum Method {
    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    GET_CURRENT_USER("GET_CURRENT_USER"),
    GET_USER("GET_USER"),
    GET_ALL_USERS("GET_ALL_USERS"),
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
