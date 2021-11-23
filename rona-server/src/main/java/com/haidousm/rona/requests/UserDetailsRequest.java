package com.haidousm.rona.requests;

import com.sun.istack.Nullable;

public class UserDetailsRequest {

    @Nullable
    private int ID;
    @Nullable
    private String email;
    @Nullable
    private String username;

    public Integer getID() {
        return ID;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }
}
