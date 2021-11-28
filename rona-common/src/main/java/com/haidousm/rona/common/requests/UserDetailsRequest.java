package com.haidousm.rona.common.requests;

import com.haidousm.rona.common.enums.Method;
import com.sun.istack.Nullable;

public class UserDetailsRequest extends GenericRequest {

    @Nullable
    private int ID;
    @Nullable
    private String email;
    @Nullable
    private String username;

    public UserDetailsRequest(int ID, String email, String username) {
        this.ID = ID;
        this.email = email;
        this.username = username;
    }

    public Integer getID() {
        return ID;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public Method getMethod() {
        return Method.GET_USER;
    }
}
