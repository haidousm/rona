package com.haidousm.rona.server.handlers;

import com.google.gson.JsonObject;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.utils.MiscUtils;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;

public class TrustedUsersHandler {
    public static GenericResponse handleGetTrustedUsers(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = MiscUtils.fromJson(request.getBody(), AuthorizedRequest.class);
            genericResponse = getTrustedUsers(authorizedRequest);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getTrustedUsers(AuthorizedRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);

        String token = request.getToken();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                List<User> trustedUsers = currentUser.getTrustedUsers();
                if (trustedUsers == null) {
                    trustedUsers = Collections.emptyList();
                }
                genericResponse.setResponse(MiscUtils.toJson(trustedUsers));
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

    public static GenericResponse handleAddTrustedUser(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = MiscUtils.fromJson(request.getBody(), AuthorizedRequest.class);
            genericResponse = addTrustedUser(authorizedRequest);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse addTrustedUser(AuthorizedRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);

        String token = request.getToken();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                String username = ((JsonObject) MiscUtils.fromJson(request.getBody(), JsonObject.class)).get("username").getAsString();
                User user = session.createQuery("from User where username = :username", User.class).setParameter("username", username).getSingleResult();
                if (user != null) {
                    if (!currentUser.getTrustedUsers().contains(user)) {
                        currentUser.getTrustedUsers().add(user);
                        session.update(currentUser);
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
        session.close();
        return genericResponse;
    }

    public static GenericResponse handleGetTrustedByUsers(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = MiscUtils.fromJson(request.getBody(), AuthorizedRequest.class);
            genericResponse = getTrustedByUsers(authorizedRequest);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse getTrustedByUsers(AuthorizedRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);

        String token = request.getToken();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                List<User> trustedByUsers = currentUser.getTrustedByUsers();
                genericResponse.setResponse(MiscUtils.toJson(trustedByUsers));
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

    public static GenericResponse handleRemoveTrustedUser(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = MiscUtils.fromJson(request.getBody(), AuthorizedRequest.class);
            genericResponse = removeTrustedUser(authorizedRequest);
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static GenericResponse removeTrustedUser(AuthorizedRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        genericResponse.setStatus(Status.SUCCESS);

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        String token = request.getToken();
        UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", token).getSingleResult();
        if (userAuthToken != null) {
            User currentUser = userAuthToken.getUser();
            if (currentUser != null) {
                String username = ((JsonObject) MiscUtils.fromJson(request.getBody(), JsonObject.class)).get("username").getAsString();
                User user = session.createQuery("from User where username = :username", User.class).setParameter("username", username).getSingleResult();
                if (user != null) {
                    if (currentUser.getTrustedUsers().contains(user)) {
                        currentUser.getTrustedUsers().remove(user);
                        session.update(currentUser);
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
        session.close();
        return genericResponse;
    }
}
