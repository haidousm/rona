package com.haidousm.rona.common.enums;

public enum Method {
    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    GET_USER("GET_CURRENT_USER"),
    GET_USER_BY("GET_USER_BY"),
    GET_ALL_USERS("GET_ALL_USERS"),
    GET_HEALTH_STATUS("GET_USER_STATUS"),
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
