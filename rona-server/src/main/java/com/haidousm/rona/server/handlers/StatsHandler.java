package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
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
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StatsHandler {
    public static GenericResponse handleGetStats(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            StatsResponse statsResponse = getStats(AuthorizedRequestBuilder.builder().build(request.getBody()));
            genericResponse.setStatus(statsResponse.getStatus());
            genericResponse.setResponse(new Gson().toJson(statsResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static StatsResponse getStats(AuthorizedRequest request) {
        StatsResponse statsResponse = StatsResponseBuilder.builder().build();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", request.getToken()).getSingleResult();
            if (userAuthToken == null) {
                statsResponse.setStatus(Status.UNAUTHORIZED);
                session.close();
                return statsResponse;
            }
            int numberOfSafeAndVaccinatedUsers = 0;
            int numberOfSafeAndUnVaccinatedUsers = 0;
            int numberOfAtRiskAndVaccinatedUsers = 0;
            int numberOfAtRiskAndUnVaccinatedUsers = 0;
            int numberOfContagiousAndVaccinatedUsers = 0;
            int numberOfContagiousAndUnVaccinatedUsers = 0;
            List<User> users = session.createQuery("from User", User.class).getResultList();
            tx.commit();

            for (User user : users) {
                if (user.getHealthStatuses().get(0).getStatus() == Health.SAFE && user.getIsVaccinated()) {
                    numberOfSafeAndVaccinatedUsers++;
                } else if (user.getHealthStatuses().get(0).getStatus() == Health.SAFE && !user.getIsVaccinated()) {
                    numberOfSafeAndUnVaccinatedUsers++;
                } else if (user.getHealthStatuses().get(0).getStatus() == Health.AT_RISK && user.getIsVaccinated()) {
                    numberOfAtRiskAndVaccinatedUsers++;
                } else if (user.getHealthStatuses().get(0).getStatus() == Health.AT_RISK && !user.getIsVaccinated()) {
                    numberOfAtRiskAndUnVaccinatedUsers++;
                } else if (user.getHealthStatuses().get(0).getStatus() == Health.CONTAGIOUS && user.getIsVaccinated()) {
                    numberOfContagiousAndVaccinatedUsers++;
                } else if (user.getHealthStatuses().get(0).getStatus() == Health.CONTAGIOUS && !user.getIsVaccinated()) {
                    numberOfContagiousAndUnVaccinatedUsers++;
                }
            }

            statsResponse = StatsResponseBuilder.builder().setNumberOfSafeAndVaccinatedUsers(numberOfSafeAndVaccinatedUsers).setNumberOfSafeAndUnVaccinatedUsers(numberOfSafeAndUnVaccinatedUsers).setNumberOfAtRiskAndVaccinatedUsers(numberOfAtRiskAndVaccinatedUsers).setNumberOfAtRiskAndUnVaccinatedUsers(numberOfAtRiskAndUnVaccinatedUsers).setNumberOfContagiousAndVaccinatedUsers(numberOfContagiousAndVaccinatedUsers).setNumberOfContagiousAndUnVaccinatedUsers(numberOfContagiousAndUnVaccinatedUsers).build();
            statsResponse.setStatus(Status.SUCCESS);
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            statsResponse.setStatus(Status.BAD_REQUEST);
        }
        return statsResponse;
    }
}
