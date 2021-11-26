package com.haidousm.rona.common.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.requests.UserDetailsRequest;
import com.sun.istack.Nullable;

public class UserDetailsRequestBuilder {
    @Nullable
    private int ID;
    @Nullable
    private String email;
    @Nullable
    private String username;

    public UserDetailsRequestBuilder ID(int ID) {
        this.ID = ID;
        return this;
    }

    public UserDetailsRequestBuilder email(String email) {
        this.email = email;
        return this;
    }

    public UserDetailsRequestBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserDetailsRequest build() {
        return new UserDetailsRequest(ID, email, username);
    }

    public UserDetailsRequest build(String body) {
        return new Gson().fromJson(body, UserDetailsRequest.class);
    }

    public static UserDetailsRequestBuilder builder() {
        return new UserDetailsRequestBuilder();
    }


}
