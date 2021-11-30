package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.builders.AuthorizedRequestBuilder;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;

import java.util.List;

public class UserHandler {

    public static GenericResponse handleGetUserDetails(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = getUserDetailsByUsername(AuthorizedRequestBuilder.builder().build(request.getBody()));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getUserDetailsByUsername(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Transaction tx = HibernateUtil.beginTransaction();
        UserAuthToken userAuthToken = HibernateUtil.getSession().createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            JsonObject jsonObject = new Gson().fromJson(authorizedRequest.getBody(), JsonObject.class);
            String username = jsonObject.get("username").getAsString();
            User user = HibernateUtil.getSession().createQuery("from User where username = :username", User.class).setParameter("username", username).getSingleResult();
            if (user != null) {
                genericResponse.setResponse(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(user));
            } else {
                genericResponse.setStatus(Status.USER_NOT_FOUND);
            }
            tx.commit();
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }
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
        Transaction tx = HibernateUtil.beginTransaction();
        List<User> users = HibernateUtil.getSession().createQuery("from User", User.class).getResultList();
        tx.commit();
        genericResponse.setResponse(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(users));
        return genericResponse;
    }

    public static GenericResponse handleGetCurrentUserDetails(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = getCurrentUserDetails(AuthorizedRequestBuilder.builder().build(request.getBody()));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getCurrentUserDetails(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Transaction tx = HibernateUtil.beginTransaction();
        UserAuthToken userAuthToken = HibernateUtil.getSession().createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        tx.commit();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                genericResponse.setResponse(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(currentUser));
            } else {
                genericResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }
        return genericResponse;
    }
}
