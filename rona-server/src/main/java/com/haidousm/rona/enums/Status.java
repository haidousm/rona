package com.haidousm.rona.enums;

public enum Status {
    SUCCESS(200),
    INCORRECT_CREDENTIALS(401),
    USER_NOT_FOUND(404),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    INTERNAL_SERVER_ERROR(500);

    private final int code;

    Status(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
