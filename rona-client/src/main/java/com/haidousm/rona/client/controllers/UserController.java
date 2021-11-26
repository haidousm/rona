package com.haidousm.rona.client.controllers;

import com.google.gson.Gson;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.requests.LoginRequest;
import com.haidousm.rona.common.requests.RegisterUserRequest;
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
        RegisterUserRequest registerUserRequest = new RegisterUserRequest(firstname, lastname, email, username, password, isVaccinated, vaccineCertificateFile, imageFile);
        return new Request(Method.REGISTER, new Gson().toJson(registerUserRequest));
    }

    public static Request prepareLogin(String username, String password) {
        // TODO: validate input
        // TODO: encrypt password?

        return new Request(Method.LOGIN, new Gson().toJson(new LoginRequest(username, password)));
    }

}
