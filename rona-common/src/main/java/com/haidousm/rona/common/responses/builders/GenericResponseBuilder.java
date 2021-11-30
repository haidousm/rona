package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.responses.GenericResponse;

public class GenericResponseBuilder {
    private Status status;
    private String body;

    public GenericResponseBuilder setStatus(Status status) {
        this.status = status;
        return this;
    }

    public GenericResponseBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public GenericResponse build() {
        return new GenericResponse(status, body);
    }

    public GenericResponse build(String json) {
        return new Gson().fromJson(json, GenericResponse.class);
    }


    public static GenericResponseBuilder builder() {
        return new GenericResponseBuilder();
    }
}
