package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.common.entity.ConnectionDetails;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.responses.AuthResponse;
import com.haidousm.rona.common.responses.builders.AuthResponseBuilder;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.RegisterRequest;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.server.utils.HibernateUtil;
import com.haidousm.rona.common.utils.MiscUtils;
import org.hibernate.Transaction;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RegisterHandler {
    public static GenericResponse handleRegister(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthResponse authResponse = register((RegisterRequest) request);
            genericResponse.setStatus(authResponse.getStatus());
            genericResponse.setResponse(new Gson().toJson(authResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static AuthResponse register(RegisterRequest registerRequest) {
        AuthResponse authResponse = AuthResponseBuilder.builder().build();

        if (registerRequest.getImageFile().isEmpty()) {
            authResponse.setStatus(Status.BAD_REQUEST);
            return authResponse;
        }

        if (registerRequest.isVaccinated() && registerRequest.getVaccineCertificateFile().isEmpty()) {
            authResponse.setStatus(Status.BAD_REQUEST);
            return authResponse;
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


            String token = LoginHandler.generateAuthToken(32);
            long expiryTimestamp = System.currentTimeMillis() + 604800000;

            tx = HibernateUtil.beginTransaction();

            HealthStatus healthStatus = new HealthStatus(Health.SAFE, System.currentTimeMillis(), newUser);
            ConnectionDetails connectionDetails = new ConnectionDetails(registerRequest.getIPAddress(), registerRequest.getPort(), newUser);
            UserAuthToken userAuthToken = new UserAuthToken(token, expiryTimestamp, newUser);
            HibernateUtil.getSession().save(userAuthToken);
            HibernateUtil.getSession().save(healthStatus);
            HibernateUtil.getSession().save(connectionDetails);
            tx.commit();
            authResponse = AuthResponseBuilder.builder().setToken(token).setExpiryTimestamp(expiryTimestamp).build();
            authResponse.setStatus(Status.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            authResponse.setStatus(Status.BAD_REQUEST);
        }

        return authResponse;
    }
}
