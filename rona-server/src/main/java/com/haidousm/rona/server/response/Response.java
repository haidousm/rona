package com.haidousm.rona.server.response;

import com.google.gson.Gson;
import com.haidousm.rona.common.enums.Status;

public class Response {
    private Status status;
    private String body = "";

    public Response() {

    }

    public Response(Status status, String body) {
        this.status = status;
        this.body = body;
    }

    public Status getStatus() {
        return status;
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


