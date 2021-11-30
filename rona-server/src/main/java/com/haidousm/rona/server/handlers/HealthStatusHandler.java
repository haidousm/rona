package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.builders.AuthorizedRequestBuilder;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;

public class HealthStatusHandler {
    public static GenericResponse handleGetCurrentUserHealthStatus(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = getCurrentUserHealthStatus(AuthorizedRequestBuilder.builder().build(request.getBody()));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getCurrentUserHealthStatus(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Transaction tx = HibernateUtil.beginTransaction();
        UserAuthToken userAuthToken = HibernateUtil.getSession().createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                HealthStatus healthStatus = currentUser.getHealthStatuses().get(0);
                genericResponse.setResponse(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(healthStatus));
                tx.commit();
            } else {
                genericResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }
        return genericResponse;
    }

    public static GenericResponse handleUpdateCurrentUserHealthStatus(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = updateCurrentUserHealthStatus(AuthorizedRequestBuilder.builder().build(request.getBody()));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse updateCurrentUserHealthStatus(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Transaction tx = HibernateUtil.beginTransaction();
        UserAuthToken userAuthToken = HibernateUtil.getSession().createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                JsonObject jsonObject = new Gson().fromJson(authorizedRequest.getBody(), JsonObject.class);
                Health health = Health.valueOf(jsonObject.get("status").getAsString());
                HealthStatus healthStatus = new HealthStatus(health, currentUser);
                HibernateUtil.getSession().save(healthStatus);
                tx.commit();
            } else {
                genericResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }
        return genericResponse;
    }
}
