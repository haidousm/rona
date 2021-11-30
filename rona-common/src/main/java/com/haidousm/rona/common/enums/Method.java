package com.haidousm.rona.common.enums;

public enum Method {
    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    GET_USER("GET_CURRENT_USER"),
    GET_TRUSTED_USERS("GET_TRUSTED_USERS"),
    GET_TRUSTED_BY_USERS("GET_TRUSTED_BY_USERS"),
    FIND_USER_BY_USERNAME("FIND_USER_BY_USERNAME"),
    GET_ALL_USERS("GET_ALL_USERS"),
    GET_HEALTH_STATUS("GET_USER_STATUS"),
    UPDATE_HEALTH_STATUS("UPDATE_USER_STATUS"),
    GET_STATS("GET_STATS"),
    ADD_TRUSTED_USER("ADD_TRUSTED_USER"),
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
