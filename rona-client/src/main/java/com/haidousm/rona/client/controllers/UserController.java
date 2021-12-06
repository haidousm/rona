package com.haidousm.rona.client.controllers;

import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.LoginRequest;
import com.haidousm.rona.common.requests.RegisterRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.utils.MiscUtils;

import java.nio.file.Path;

public class UserController {

    public static Request prepareRegisterUser(String firstname, String lastname, String email, String username, String password, boolean isVaccinated, Path vaccineCertificateFilePath, Path imageFilePath) {

        // TODO: validate input
        String vaccineCertificateFile = "";
        if (vaccineCertificateFilePath.toFile().exists()) {
            vaccineCertificateFile = MiscUtils.encodeFileToBase64(vaccineCertificateFilePath);
        }

        String imageFile = "";
        if (imageFilePath.toFile().exists()) {
            imageFile = MiscUtils.encodeFileToBase64(imageFilePath);
        }
        return new RegisterRequest(firstname, lastname, email, username, password, isVaccinated, vaccineCertificateFile, imageFile);
    }

    public static Request prepareLogin(String username, String password) {
        // TODO: validate input
        // TODO: encrypt password?

        return new LoginRequest(username, password);
    }

    public static Request prepareGetCurrentUser(String token) {
        return new AuthorizedRequest(Method.GET_USER, token);
    }
}
