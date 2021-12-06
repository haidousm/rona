package com.haidousm.rona.server.handlers;

import com.google.gson.GsonBuilder;
import com.haidousm.rona.common.entity.ConnectionDetails;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.responses.TokenResponse;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.requests.LoginRequest;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.utils.MiscUtils;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LoginHandler {

    public static GenericResponse handleLogin(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            LoginRequest loginRequest = MiscUtils.fromJson(request.getBody(), LoginRequest.class);
            TokenResponse tokenResponse = login(loginRequest);
            genericResponse.setStatus(tokenResponse.getStatus());
            genericResponse.setResponse(MiscUtils.toJson(tokenResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static TokenResponse login(LoginRequest loginRequest) {
        TokenResponse tokenResponse = new TokenResponse();

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            User user = (User) session.createQuery("from User where username = :username").setParameter("username", loginRequest.getUsername()).uniqueResult();
            tx.commit();
            if (user == null) {
                tokenResponse.setStatus(Status.USER_NOT_FOUND);
                session.close();
                return tokenResponse;
            } else if (!user.getPassword().equals(loginRequest.getPassword())) {
                tokenResponse.setStatus(Status.INCORRECT_CREDENTIALS);
                session.close();
                return tokenResponse;
            }

            tx = session.beginTransaction();
            String newToken = generateAuthToken(32);
            long expiryTimestamp = System.currentTimeMillis() + 604800000;

            UserAuthToken userAuthToken = (UserAuthToken) session.createQuery("from UserAuthToken where user = :user").setParameter("user", user).uniqueResult();
            if (userAuthToken == null) {
                userAuthToken = new UserAuthToken(newToken, expiryTimestamp, user);
            } else {
                userAuthToken.setToken(newToken);
                userAuthToken.setExpiryTimestamp(expiryTimestamp);
            }

            ConnectionDetails connectionDetails = new ConnectionDetails(loginRequest.getIPAddress(), loginRequest.getPort(), user);

            session.saveOrUpdate(userAuthToken);
            session.saveOrUpdate(connectionDetails);
            tx.commit();
            tokenResponse = new TokenResponse(newToken, expiryTimestamp);
            tokenResponse.setStatus(Status.SUCCESS);

        } catch (Exception e) {
            tokenResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
        }

        session.close();
        return tokenResponse;
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
