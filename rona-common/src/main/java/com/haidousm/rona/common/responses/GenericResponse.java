package com.haidousm.rona.common.responses;

import com.google.gson.Gson;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.enums.Status;

public class GenericResponse implements Response {
    private Status status;
    private String body = "";

    public GenericResponse() {

    }

    public GenericResponse(Status status, String body) {
        this.status = status;
        this.body = body;
    }

    public Status getStatus() {
        return status;
    }

    @Override
    public Method getMethod() {
        return Method.UNDEFINED;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String toString() {
        return new Gson().toJson(this);
    }
}



