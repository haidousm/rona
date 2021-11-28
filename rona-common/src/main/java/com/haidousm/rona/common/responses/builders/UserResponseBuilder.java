package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.responses.GenericResponse;

public class UserResponseBuilder {

    public User build(GenericResponse response) {
        return new Gson().fromJson(response.getResponse(), User.class);
    }

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }
}
