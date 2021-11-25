package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haidousm.rona.server.entity.User;
import com.haidousm.rona.common.requests.LoginRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.server.response.Response;
import com.haidousm.rona.server.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;

public class LoginHandler {

    public static Response handleLogin(Request request) {
        Response response = new Response();
        try {
            LoginRequest loginRequest = new Gson().fromJson(request.getBody(), LoginRequest.class);
            response = login(loginRequest);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }

    private static Response login(LoginRequest loginRequest) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);

        Transaction tx;
        tx = HibernateUtil.beginTransaction();
        try {
            User user = (User) HibernateUtil.getSession().createQuery("from User where username = :username").setParameter("username", loginRequest.getUsername()).uniqueResult();
            tx.commit();
            if (user == null) {
                response.setStatus(Status.USER_NOT_FOUND);
            } else if (!user.getPassword().equals(loginRequest.getPassword())) {
                response.setStatus(Status.INCORRECT_CREDENTIALS);
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
            response.setBody(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(userAuthToken));

        } catch (Exception e) {
            response.setStatus(Status.INTERNAL_SERVER_ERROR);
        }

        return response;
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
