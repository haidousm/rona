package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Hibernate;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class TrustedUsersHandler {
    public static GenericResponse handleGetTrustedUsers(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = getTrustedUsers((AuthorizedRequest) request);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getTrustedUsers(AuthorizedRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);

        String token = request.getToken();
        Transaction tx = HibernateUtil.beginTransaction();
        UserAuthToken userAuthToken = HibernateUtil.getSession().createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                List<User> trustedUsers = currentUser.getTrustedUsers();
                if (trustedUsers == null) {
                    trustedUsers = Collections.emptyList();
                }
                genericResponse.setResponse(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(trustedUsers));
                tx.commit();
            } else {
                genericResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }

        return genericResponse;
    }

    public static GenericResponse handleAddTrustedUser(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = addTrustedUser((AuthorizedRequest) request);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse addTrustedUser(AuthorizedRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);

        String token = request.getToken();
        Transaction tx = HibernateUtil.beginTransaction();
        UserAuthToken userAuthToken = HibernateUtil.getSession().createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                String username = new Gson().fromJson(request.getBody(), JsonObject.class).get("username").getAsString();
                User user = HibernateUtil.getSession().createQuery("from User where username = :username", User.class).setParameter("username", username).getSingleResult();
                if (user != null) {
                    if (!currentUser.getTrustedUsers().contains(user)) {
                        currentUser.getTrustedUsers().add(user);
                        HibernateUtil.getSession().update(currentUser);
                        tx.commit();
                    } else {
                        genericResponse.setStatus(Status.BAD_REQUEST);
                    }

                } else {
                    genericResponse.setStatus(Status.USER_NOT_FOUND);
                }
            } else {
                genericResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }

        return genericResponse;
    }
}
