package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.common.entity.ConnectionDetails;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.responses.TokenResponse;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.RegisterRequest;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.server.utils.HibernateUtil;
import com.haidousm.rona.common.utils.MiscUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RegisterHandler {
    public static GenericResponse handleRegister(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            RegisterRequest registerRequest = MiscUtils.fromJson(request.getBody(), RegisterRequest.class);
            TokenResponse tokenResponse = register(registerRequest);
            genericResponse.setStatus(tokenResponse.getStatus());
            genericResponse.setResponse(MiscUtils.toJson(tokenResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static TokenResponse register(RegisterRequest registerRequest) {
        TokenResponse tokenResponse = new TokenResponse();

        if (registerRequest.getImageFile().isEmpty()) {
            tokenResponse.setStatus(Status.BAD_REQUEST);
            return tokenResponse;
        }

        if (registerRequest.isVaccinated() && registerRequest.getVaccineCertificateFile().isEmpty()) {
            tokenResponse.setStatus(Status.BAD_REQUEST);
            return tokenResponse;
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

            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction tx = session.beginTransaction();
            session.save(newUser);
            tx.commit();

            String token = LoginHandler.generateAuthToken(32);
            long expiryTimestamp = System.currentTimeMillis() + 604800000;

            tx = session.beginTransaction();

            HealthStatus healthStatus = new HealthStatus(Health.SAFE, newUser);
            ConnectionDetails connectionDetails = new ConnectionDetails(registerRequest.getIPAddress(), registerRequest.getPort(), newUser);
            UserAuthToken userAuthToken = new UserAuthToken(token, expiryTimestamp, newUser);
            session.save(userAuthToken);
            session.save(healthStatus);
            session.save(connectionDetails);
            tx.commit();
            tokenResponse = new TokenResponse(token, expiryTimestamp);
            tokenResponse.setStatus(Status.SUCCESS);
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            tokenResponse.setStatus(Status.BAD_REQUEST);
        }

        return tokenResponse;
    }
}
