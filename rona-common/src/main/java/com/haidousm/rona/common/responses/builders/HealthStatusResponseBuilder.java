package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.responses.GenericResponse;

import java.lang.reflect.Type;
import java.util.List;

public class HealthStatusResponseBuilder {

    public HealthStatus build(GenericResponse response) {
        return new Gson().fromJson(response.getResponse(), HealthStatus.class);
    }

    public static HealthStatusResponseBuilder builder() {
        return new HealthStatusResponseBuilder();
    }

    public List<HealthStatus> buildList(GenericResponse response) {
        Type listOfUsers = new TypeToken<List<HealthStatus>>() {
        }.getType();
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(response.getResponse(), listOfUsers);
    }
}
