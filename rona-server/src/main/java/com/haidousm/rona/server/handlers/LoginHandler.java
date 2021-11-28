package com.haidousm.rona.server.handlers;

import com.google.gson.GsonBuilder;
import com.haidousm.rona.common.responses.AuthResponse;
import com.haidousm.rona.common.responses.builders.AuthResponseBuilder;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.requests.LoginRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;

public class LoginHandler {

    public static GenericResponse handleLogin(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthResponse authResponse = login((LoginRequest) request);
            genericResponse.setStatus(authResponse.getStatus());
            genericResponse.setResponse(new GsonBuilder().create().toJson(authResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static AuthResponse login(LoginRequest loginRequest) {
        AuthResponse authResponse = AuthResponseBuilder.builder().build();

        Transaction tx;
        tx = HibernateUtil.beginTransaction();
        try {
            User user = (User) HibernateUtil.getSession().createQuery("from User where username = :username").setParameter("username", loginRequest.getUsername()).uniqueResult();
            tx.commit();
            if (user == null) {
                authResponse.setStatus(Status.USER_NOT_FOUND);
                return authResponse;
            } else if (!user.getPassword().equals(loginRequest.getPassword())) {
                authResponse.setStatus(Status.INCORRECT_CREDENTIALS);
                return authResponse;
            }

            tx = HibernateUtil.beginTransaction();
            String token = generateAuthToken(32);
            long expiryTimestamp = System.currentTimeMillis() + 604800000;

            UserAuthToken userAuthToken = (UserAuthToken) HibernateUtil.getSession().createQuery("from UserAuthToken where user = :user").setParameter("user", user).uniqueResult();
            if (userAuthToken == null) {
                userAuthToken = new UserAuthToken(token, expiryTimestamp, user);
            } else {
                userAuthToken.setToken(token);
                userAuthToken.setExpiryTimestamp(expiryTimestamp);
            }

            HibernateUtil.getSession().save(userAuthToken);
            tx.commit();
            authResponse = AuthResponseBuilder.builder().setToken(token).setExpiryTimestamp(expiryTimestamp).build();
            authResponse.setStatus(Status.SUCCESS);

        } catch (Exception e) {
            authResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
        }

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
