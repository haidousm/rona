package com.haidousm.rona.common.enums;

public enum Method {
    /*  Auth Stuff */
    LOGIN("LOGIN"),
    REGISTER("REGISTER"),
    /* Users Stuff */
    GET_USER("GET_CURRENT_USER"),
    FIND_USER_BY_USERNAME("FIND_USER_BY_USERNAME"),
    GET_ALL_USERS("GET_ALL_USERS"),
    /* Trusted Users Stuff */
    ADD_TRUSTED_USER("ADD_TRUSTED_USER"),
    REMOVE_TRUSTED_USER("REMOVE_TRUSTED_USER"),
    GET_TRUSTED_USERS("GET_TRUSTED_USERS"),
    GET_TRUSTED_BY_USERS("GET_TRUSTED_BY_USERS"),
    /*  Health Status Stuff */
    GET_HEALTH_STATUS("GET_USER_STATUS"),
    UPDATE_HEALTH_STATUS("UPDATE_USER_STATUS"),
    /*  Stats Stuff */
    GET_STATS("GET_STATS"),
    UNDEFINED("UNDEFINED");

    private final String method;

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
