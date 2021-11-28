package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.responses.GenericResponse;

public class HealthStatusResponseBuilder {

    public HealthStatus build(GenericResponse response) {
        return new Gson().fromJson(response.getResponse(), HealthStatus.class);
    }
    public static HealthStatusResponseBuilder builder() {
        return new HealthStatusResponseBuilder();
    }
}
