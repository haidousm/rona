package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.utils.MiscUtils;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class UserHandler {

    public static GenericResponse handleGetUserDetails(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = MiscUtils.fromJson(request.getBody(), AuthorizedRequest.class);
            genericResponse = getUserDetailsByUsername(authorizedRequest);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getUserDetailsByUsername(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            JsonObject jsonObject = MiscUtils.fromJson(authorizedRequest.getBody(), JsonObject.class);
            String username = jsonObject.get("username").getAsString();
            User user = session.createQuery("from User where username = :username", User.class).setParameter("username", username).getSingleResult();
            if (user != null) {
                genericResponse.setResponse(MiscUtils.toJson(user));
            } else {
                genericResponse.setStatus(Status.USER_NOT_FOUND);
            }
            tx.commit();
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }
        session.close();
        return genericResponse;
    }

    public static GenericResponse handleGetAllUserDetails() {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = getAllUserDetails();
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getAllUserDetails() {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        List<User> users = session.createQuery("from User", User.class).getResultList();
        tx.commit();
        genericResponse.setResponse(MiscUtils.toJson(users));
        session.close();
        return genericResponse;
    }

    public static GenericResponse handleGetCurrentUserDetails(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = MiscUtils.fromJson(request.getBody(), AuthorizedRequest.class);
            genericResponse = getCurrentUserDetails(authorizedRequest);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getCurrentUserDetails(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        tx.commit();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                genericResponse.setResponse(MiscUtils.toJson(currentUser));
            } else {
                genericResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }
        session.close();
        return genericResponse;
    }
}
