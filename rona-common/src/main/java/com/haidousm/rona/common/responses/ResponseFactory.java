package com.haidousm.rona.common.responses;

import com.haidousm.rona.common.responses.builders.GenericResponseBuilder;
import com.haidousm.rona.common.responses.builders.LoginResponseBuilder;
import com.haidousm.rona.common.responses.builders.RegisterResponseBuilder;

public class ResponseFactory {
    public static Response createResponse(String responseString) {
        GenericResponse genericResponse = GenericResponseBuilder.builder().build(responseString);
        switch (genericResponse.getMethod()) {
            case LOGIN:
                return LoginResponseBuilder.builder().build(genericResponse);
            case REGISTER:
                return RegisterResponseBuilder.builder().build(genericResponse);
            default:
                return genericResponse;
        }
    }
}
