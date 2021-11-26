package com.haidousm.rona.server.handlers;

import com.google.gson.GsonBuilder;
import com.haidousm.rona.common.responses.LoginResponse;
import com.haidousm.rona.common.responses.Response;
import com.haidousm.rona.common.responses.builders.LoginResponseBuilder;
import com.haidousm.rona.server.entity.User;
import com.haidousm.rona.common.requests.LoginRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.server.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;

public class LoginHandler {

    public static GenericResponse handleLogin(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            LoginResponse loginResponse = login((LoginRequest) request);
            genericResponse.setStatus(loginResponse.getStatus());
            genericResponse.setBody(new GsonBuilder().create().toJson(loginResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static LoginResponse login(LoginRequest loginRequest) {
        LoginResponse loginResponse = LoginResponseBuilder.builder().build();
        loginResponse.setStatus(Status.SUCCESS);

        Transaction tx;
        tx = HibernateUtil.beginTransaction();
        try {
            User user = (User) HibernateUtil.getSession().createQuery("from User where username = :username").setParameter("username", loginRequest.getUsername()).uniqueResult();
            tx.commit();
            if (user == null) {
                loginResponse.setStatus(Status.USER_NOT_FOUND);
                return loginResponse;
            } else if (!user.getPassword().equals(loginRequest.getPassword())) {
                loginResponse.setStatus(Status.INCORRECT_CREDENTIALS);
                return loginResponse;
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
            loginResponse = LoginResponseBuilder.builder().setToken(token).setExpiryTimestamp(expiryTimestamp).build();
            loginResponse.setStatus(Status.SUCCESS);

        } catch (Exception e) {
            loginResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
        }

        return loginResponse;
    }

    private static String generateAuthToken(int length) {
        StringBuilder sb = new StringBuilder();
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        for (int i = 0; i < length; i++) {
            int index = (int) (Math.random() * characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }
}
