package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.server.entity.User;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.UserDetailsRequest;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;

import java.util.List;

public class UserDetailsHandler {

    public static GenericResponse handleGetUserDetails(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            genericResponse = getUserDetails((UserDetailsRequest) request);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getUserDetails(UserDetailsRequest userDetailsRequest) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);
        Gson gson = new Gson();
        if (userDetailsRequest.getID() != 0) {
            Transaction tx = HibernateUtil.beginTransaction();
            User user = HibernateUtil.getSession().get(User.class, userDetailsRequest.getID());
            tx.commit();
            genericResponse.setBody(gson.toJson(user));
        } else if (userDetailsRequest.getUsername() != null) {
            Transaction tx = HibernateUtil.beginTransaction();
            User user = HibernateUtil.getSession().createQuery("from User where username = :username", User.class)
                    .setParameter("username", userDetailsRequest.getUsername()).getSingleResult();
            tx.commit();
            genericResponse.setBody(gson.toJson(user));
        } else if (userDetailsRequest.getEmail() != null) {
            Transaction tx = HibernateUtil.beginTransaction();
            User user = HibernateUtil.getSession().createQuery("from User where email = :email", User.class)
                    .setParameter("email", userDetailsRequest.getEmail()).getSingleResult();
            tx.commit();
            genericResponse.setBody(gson.toJson(user));

        } else {
            genericResponse.setStatus(Status.BAD_REQUEST);
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
        genericResponse.setBody(new Gson().toJson(users));
        return genericResponse;
    }
}
