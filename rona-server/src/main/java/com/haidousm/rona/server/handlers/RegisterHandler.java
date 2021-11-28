package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.common.responses.RegisterResponse;
import com.haidousm.rona.common.responses.builders.RegisterResponseBuilder;
import com.haidousm.rona.server.entity.User;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.RegisterRequest;
import com.haidousm.rona.server.utils.HibernateUtil;
import com.haidousm.rona.common.utils.MiscUtils;
import org.hibernate.Transaction;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RegisterHandler {
    public static GenericResponse handleRegister(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            RegisterResponse registerResponse = register((RegisterRequest) request);
            genericResponse.setStatus(registerResponse.getStatus());
            genericResponse.setResponse(new Gson().toJson(registerResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static RegisterResponse register(RegisterRequest registerRequest) {
        RegisterResponse registerResponse = RegisterResponseBuilder.builder().build();

        if (registerRequest.getImageFile().isEmpty()) {
            registerResponse.setStatus(Status.BAD_REQUEST);
            return registerResponse;
        }

        if (registerRequest.isVaccinated() && registerRequest.getVaccineCertificateFile().isEmpty()) {
            registerResponse.setStatus(Status.BAD_REQUEST);
            return registerResponse;
        }

        Path imageFilePath = Paths.get("user-data", "user-images", registerRequest.getUsername() + ".jpg");
        Path vaccineCertificateFilePath = Path.of("");
        try {
            MiscUtils.decodeBase64ToFile(registerRequest.getImageFile(), imageFilePath);
            if (registerRequest.isVaccinated()) {
                vaccineCertificateFilePath = Paths.get("user-data", "vaccine-certificates", registerRequest.getUsername() + ".pdf");
                MiscUtils.decodeBase64ToFile(registerRequest.getVaccineCertificateFile(), vaccineCertificateFilePath);
            }

            User newUser = new User(registerRequest.getFirstname(), registerRequest.getLastname(), registerRequest.getEmail(), registerRequest.getUsername(), registerRequest.getPassword(), registerRequest.isVaccinated(), vaccineCertificateFilePath.toString(), imageFilePath.toString());

            Transaction tx = HibernateUtil.beginTransaction();
            HibernateUtil.getSession().save(newUser);
            tx.commit();

            registerResponse = RegisterResponseBuilder.builder().setUserID(newUser.getId()).build();
            registerResponse.setStatus(Status.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            registerResponse.setStatus(Status.BAD_REQUEST);
        }

        return registerResponse;
    }
}
