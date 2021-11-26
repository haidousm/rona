package com.haidousm.rona.client.controllers;

import com.haidousm.rona.common.requests.builders.LoginRequestBuilder;
import com.haidousm.rona.common.requests.builders.RegisterRequestBuilder;
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
        return RegisterRequestBuilder.builder().setFirstname(firstname).setLastname(lastname).setEmail(email).setUsername(username).setPassword(password).setVaccinated(isVaccinated).setVaccineCertificateFile(vaccineCertificateFile).setImageFile(imageFile).build();
    }

    public static Request prepareLogin(String username, String password) {
        // TODO: validate input
        // TODO: encrypt password?

        return LoginRequestBuilder.builder().setUsername(username).setPassword(password).build();
    }

}
