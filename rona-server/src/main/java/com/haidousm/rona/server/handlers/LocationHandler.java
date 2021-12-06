package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haidousm.rona.common.entity.LocationDetails;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class LocationHandler {

    public static GenericResponse handleUpdateUserLocation(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = new Gson().fromJson(request.getBody(), AuthorizedRequest.class);
            genericResponse = updateUserLocation(authorizedRequest);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse updateUserLocation(AuthorizedRequest authorizedRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        String token = authorizedRequest.getToken();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                JsonObject jsonObject = new Gson().fromJson(authorizedRequest.getBody(), JsonObject.class);
                LocationDetails locationDetails = new LocationDetails(jsonObject.get("longitude").getAsDouble(), jsonObject.get("latitude").getAsDouble(), System.currentTimeMillis(), currentUser);
                session.save(locationDetails);
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
}

