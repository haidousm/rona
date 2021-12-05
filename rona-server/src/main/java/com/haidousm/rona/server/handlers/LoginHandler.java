package com.haidousm.rona.server.handlers;

import com.google.gson.GsonBuilder;
import com.haidousm.rona.common.entity.ConnectionDetails;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.requests.builders.LoginRequestBuilder;
import com.haidousm.rona.common.responses.AuthResponse;
import com.haidousm.rona.common.responses.builders.AuthResponseBuilder;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.requests.LoginRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LoginHandler {

    public static GenericResponse handleLogin(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthResponse authResponse = login(LoginRequestBuilder.builder().build(request.getBody()));
            genericResponse.setStatus(authResponse.getStatus());
            genericResponse.setResponse(new GsonBuilder().create().toJson(authResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static AuthResponse login(LoginRequest loginRequest) {
        AuthResponse authResponse = AuthResponseBuilder.builder().build();


        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            User user = (User) session.createQuery("from User where username = :username").setParameter("username", loginRequest.getUsername()).uniqueResult();
            tx.commit();
            if (user == null) {
                authResponse.setStatus(Status.USER_NOT_FOUND);
                session.close();
                return authResponse;
            } else if (!user.getPassword().equals(loginRequest.getPassword())) {
                authResponse.setStatus(Status.INCORRECT_CREDENTIALS);
                session.close();
                return authResponse;
            }

            tx = session.beginTransaction();
            String token = generateAuthToken(32);
            long expiryTimestamp = System.currentTimeMillis() + 604800000;

            UserAuthToken userAuthToken = (UserAuthToken) session.createQuery("from UserAuthToken where user = :user").setParameter("user", user).uniqueResult();
            if (userAuthToken == null) {
                userAuthToken = new UserAuthToken(token, expiryTimestamp, user);
            } else {
                userAuthToken.setToken(token);
                userAuthToken.setExpiryTimestamp(expiryTimestamp);
            }
            ConnectionDetails connectionDetails = new ConnectionDetails(loginRequest.getIPAddress(), loginRequest.getPort(), user);
            session.save(userAuthToken);
            session.saveOrUpdate(connectionDetails);
            tx.commit();
            authResponse = AuthResponseBuilder.builder().setToken(token).setExpiryTimestamp(expiryTimestamp).build();
            authResponse.setStatus(Status.SUCCESS);

        } catch (Exception e) {
            authResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
        }

        session.close();
        return authResponse;
    }

    public static String generateAuthToken(int length) {
        StringBuilder sb = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
