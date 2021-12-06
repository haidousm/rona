package com.haidousm.rona.common.responses;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.utils.MiscUtils;

public class GenericResponse implements Response {

    @Expose
    private Status status;
    @Expose
    private String response = "";

    public GenericResponse() {

    }

    public GenericResponse(Status status, String response) {
        this.status = status;
        this.response = response;
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

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}



