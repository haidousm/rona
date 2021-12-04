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
import com.haidousm.rona.common.requests.builders.AuthorizedRequestBuilder;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.StatsResponse;
import com.haidousm.rona.common.responses.builders.StatsResponseBuilder;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;
import java.util.List;

public class LocationHandler {

    public static GenericResponse handleGetLocations(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = getLocations(AuthorizedRequestBuilder.builder().build(request.getBody()));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getLocations(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Transaction tx = HibernateUtil.beginTransaction();
        UserAuthToken userAuthToken = HibernateUtil.getSession().createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                LocationDetails locationDetails = currentUser.getLatitude().get(0);
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

    public static GenericResponse handleUpdateLocations(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = updateLocations(AuthorizedRequestBuilder.builder().build(request.getBody()));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse updateLocations(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Transaction tx = HibernateUtil.beginTransaction();
        UserAuthToken userAuthToken = HibernateUtil.getSession().createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                JsonObject jsonObject = new Gson().fromJson(authorizedRequest.getBody(), JsonObject.class);
                LocationDetails locationDetails = new LocationDetails(jsonObject.get("longitude").getAsDouble(),jsonObject.get("latitude").getAsDouble(),System.currentTimeMillis() ,currentUser);
                HibernateUtil.getSession().save(locationDetails);
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

