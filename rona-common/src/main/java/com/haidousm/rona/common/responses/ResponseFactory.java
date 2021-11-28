package com.haidousm.rona.common.responses;

import com.haidousm.rona.common.responses.builders.GenericResponseBuilder;
import com.haidousm.rona.common.responses.builders.AuthResponseBuilder;

public class ResponseFactory {
    public static Response createResponse(String responseString) {
        GenericResponse genericResponse = GenericResponseBuilder.builder().build(responseString);
        switch (genericResponse.getMethod()) {
            case LOGIN:
            case REGISTER:
                return AuthResponseBuilder.builder().build(genericResponse);
            default:
                return genericResponse;
        }
    }
}
