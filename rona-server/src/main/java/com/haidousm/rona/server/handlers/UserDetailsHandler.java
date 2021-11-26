package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.server.entity.User;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.UserDetailsRequest;
import com.haidousm.rona.common.responses.Response;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;

import java.util.List;

public class UserDetailsHandler {

    public static Response handleGetUserDetails(Request request) {
        Response response = new Response();
        try {
            UserDetailsRequest userDetailsRequest = new Gson().fromJson(request.getBody(), UserDetailsRequest.class);
            response = getUserDetails(userDetailsRequest);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }

    private static Response getUserDetails(UserDetailsRequest userDetailsRequest) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        Gson gson = new Gson();
        if (userDetailsRequest.getID() != 0) {
            Transaction tx = HibernateUtil.beginTransaction();
            User user = HibernateUtil.getSession().get(User.class, userDetailsRequest.getID());
            tx.commit();
            response.setBody(gson.toJson(user));
        } else if (userDetailsRequest.getUsername() != null) {
            Transaction tx = HibernateUtil.beginTransaction();
            User user = HibernateUtil.getSession().createQuery("from User where username = :username", User.class)
                    .setParameter("username", userDetailsRequest.getUsername()).getSingleResult();
            tx.commit();
            response.setBody(gson.toJson(user));
        } else if (userDetailsRequest.getEmail() != null) {
            Transaction tx = HibernateUtil.beginTransaction();
            User user = HibernateUtil.getSession().createQuery("from User where email = :email", User.class)
                    .setParameter("email", userDetailsRequest.getEmail()).getSingleResult();
            tx.commit();
            response.setBody(gson.toJson(user));

        } else {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }

    public static Response handleGetAllUserDetails() {
        Response response = new Response();
        try {
            response = getAllUserDetails();
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }

    private static Response getAllUserDetails() {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        Transaction tx = HibernateUtil.beginTransaction();
        List<User> users = HibernateUtil.getSession().createQuery("from User", User.class).getResultList();
        tx.commit();
        response.setBody(new Gson().toJson(users));
        return response;
    }
}
