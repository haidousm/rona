package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.LocationDetails;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.utils.MiscUtils;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class HealthStatusHandler {
    public static GenericResponse handleGetCurrentUserHealthStatus(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = MiscUtils.fromJson(request.getBody(), AuthorizedRequest.class);
            genericResponse = getCurrentUserHealthStatus(authorizedRequest);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getCurrentUserHealthStatus(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                HealthStatus healthStatus = currentUser.getHealthStatuses().get(0);
                genericResponse.setResponse(MiscUtils.toJson(healthStatus));
                tx.commit();
            } else {
                genericResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }
        session.close();
        return genericResponse;
    }

    public static GenericResponse handleUpdateCurrentUserHealthStatus(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = MiscUtils.fromJson(request.getBody(), AuthorizedRequest.class);
            genericResponse = updateCurrentUserHealthStatus(authorizedRequest);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse updateCurrentUserHealthStatus(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                JsonObject jsonObject = MiscUtils.fromJson(authorizedRequest.getBody(), JsonObject.class);
                Health health = Health.valueOf(jsonObject.get("status").getAsString());
                HealthStatus healthStatus = new HealthStatus(health, currentUser);
                session.save(healthStatus);
                tx.commit();

                if (health == Health.CONTAGIOUS) {
                    updateNearbyUsersHealthStatus(currentUser);
                }

            } else {
                genericResponse.setStatus(Status.INTERNAL_SERVER_ERROR);
            }
        } else {
            genericResponse.setStatus(Status.UNAUTHORIZED);
        }

        session.close();
        return genericResponse;
    }

    private static void updateNearbyUsersHealthStatus(User currentUser) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        LocationDetails locationDetails = session.createQuery("from LocationDetails where user = :user and timestamp between :minimumTimestamp and :maximumTimestamp", LocationDetails.class).setParameter("user", currentUser).setParameter("minimumTimestamp", System.currentTimeMillis() - (1000 * 60)).setParameter("maximumTimestamp", System.currentTimeMillis()).getSingleResult();
        if (locationDetails != null) {
            List<LocationDetails> nearbyLocationDetails = session.createQuery("from LocationDetails where user != :user and latitude between :minimumLatitude and :maximumLatitude and longitude between :minimumLongitude and :maximumLongitude and timestamp between :minimumTimestamp and :maximumTimestamp", LocationDetails.class)
                    .setParameter("user", currentUser)
                    .setParameter("minimumLatitude", locationDetails.getLatitude() - 1)
                    .setParameter("maximumLatitude", locationDetails.getLatitude() + 1)
                    .setParameter("minimumLongitude", locationDetails.getLongitude() - 1)
                    .setParameter("maximumLongitude", locationDetails.getLongitude() + 1)
                    .setParameter("minimumTimestamp", System.currentTimeMillis() - (1000 * 180))
                    .setParameter("maximumTimestamp", System.currentTimeMillis())
                    .getResultList();
            if (nearbyLocationDetails != null) {
                for (LocationDetails nearbyLocationDetail : nearbyLocationDetails) {
                    User user = nearbyLocationDetail.getUser();
                    if (user != null) {
                        if (user.getHealthStatuses().get(0).getStatus() != Health.CONTAGIOUS) {
                            HealthStatus healthStatus = new HealthStatus(Health.AT_RISK, nearbyLocationDetail.getUser());
                            session.save(healthStatus);
                        }
                    }
                }
            }
        }
        session.close();
        tx.commit();
    }


}
