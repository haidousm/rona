package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.server.entity.User;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.server.response.Response;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.RegisterUserRequest;
import com.haidousm.rona.server.utils.HibernateUtil;
import com.haidousm.rona.server.utils.MiscUtils;
import org.hibernate.Transaction;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RegisterHandler {
    public static Response handleRegister(Request request) {
        Response response = new Response();
        try {
            RegisterUserRequest registerUserRequest = new Gson().fromJson(request.getBody(), RegisterUserRequest.class);
            response = register(registerUserRequest);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }

    private static Response register(RegisterUserRequest registerUserRequest) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);

        if (registerUserRequest.getImageFile().isEmpty()) {
            response.setStatus(Status.BAD_REQUEST);
            return response;
        }

        if (registerUserRequest.isVaccinated() && registerUserRequest.getVaccineCertificateFile().isEmpty()) {
            response.setStatus(Status.BAD_REQUEST);
            return response;
        }

        Path imageFilePath = Paths.get("user-data", "user-images", registerUserRequest.getUsername() + ".jpg");
        Path vaccineCertificateFilePath = Path.of("");
        try {
            MiscUtils.decodeBase64ToFile(registerUserRequest.getImageFile(), imageFilePath);
            if (registerUserRequest.isVaccinated()) {
                vaccineCertificateFilePath = Paths.get("user-data", "vaccine-certificates", registerUserRequest.getUsername() + ".pdf");
                MiscUtils.decodeBase64ToFile(registerUserRequest.getVaccineCertificateFile(), vaccineCertificateFilePath);
            }

            User newUser = new User(registerUserRequest.getFirstname(), registerUserRequest.getLastname(), registerUserRequest.getEmail(), registerUserRequest.getUsername(), registerUserRequest.getPassword(), registerUserRequest.isVaccinated(), vaccineCertificateFilePath.toString(), imageFilePath.toString());

            Transaction tx = HibernateUtil.beginTransaction();
            HibernateUtil.getSession().save(newUser);
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(Status.BAD_REQUEST);
        }

        return response;
    }
}
