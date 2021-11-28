package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;

public class HealthStatusHandler {
    public static GenericResponse handleGetCurrentUserHealthStatus(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = getCurrentUserHealthStatus((AuthorizedRequest) request);
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
                HealthStatus healthStatus = HibernateUtil.getSession().createQuery("from HealthStatus where user = :user", HealthStatus.class).setParameter("user", currentUser).getSingleResult();
                genericResponse.setResponse(new Gson().toJson(healthStatus));
                tx.commit();
            } else {
                genericResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }
        return genericResponse;
    }

    public static GenericResponse handleUpdateCurrentUserHealthStatus(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = updateCurrentUserHealthStatus((AuthorizedRequest) request);
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
                HealthStatus healthStatus = HibernateUtil.getSession().createQuery("from HealthStatus where user = :user", HealthStatus.class).setParameter("user", currentUser).getSingleResult();
                JsonObject jsonObject = new Gson().fromJson(authorizedRequest.getBody(), JsonObject.class);
                healthStatus.setStatus(Health.valueOf(jsonObject.get("status").getAsString()));
                HibernateUtil.getSession().update(healthStatus);
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
