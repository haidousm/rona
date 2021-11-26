package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.common.responses.RegisterResponse;
import com.haidousm.rona.server.entity.User;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.Response;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.RegisterRequest;
import com.haidousm.rona.server.utils.HibernateUtil;
import com.haidousm.rona.common.utils.MiscUtils;
import org.hibernate.Transaction;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RegisterHandler {
    public static Response handleRegister(Request request) {
        Response response = new Response();
        try {
            response = register((RegisterRequest) request);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }

    private static Response register(RegisterRequest registerRequest) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);

        if (registerRequest.getImageFile().isEmpty()) {
            response.setStatus(Status.BAD_REQUEST);
            return response;
        }

        if (registerRequest.isVaccinated() && registerRequest.getVaccineCertificateFile().isEmpty()) {
            response.setStatus(Status.BAD_REQUEST);
            return response;
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

            RegisterResponse registerResponse = new RegisterResponse(newUser.getId());
            response.setBody(new Gson().toJson(registerResponse));

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(Status.BAD_REQUEST);
        }

        return response;
    }
}
