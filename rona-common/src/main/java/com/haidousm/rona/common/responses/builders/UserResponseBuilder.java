package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.responses.GenericResponse;

import java.lang.reflect.Type;
import java.util.List;

public class UserResponseBuilder {

    public User build(GenericResponse response) {
        if (response.getResponse() == null) {
            return null;
        }
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(response.getResponse(), User.class);
    }

    public List<User> buildList(GenericResponse response) {
        Type listOfUsers = new TypeToken<List<User>>() {
        }.getType();
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(response.getResponse(), listOfUsers);
    }

    public static UserResponseBuilder builder() {
        return new UserResponseBuilder();
    }
}
