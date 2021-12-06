package com.haidousm.rona.server.handlers;

import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.StatisticsResponse;
import com.haidousm.rona.common.utils.MiscUtils;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class StatsHandler {
    public static GenericResponse handleGetStats(GenericRequest request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            AuthorizedRequest authorizedRequest = MiscUtils.fromJson(request.getBody(), AuthorizedRequest.class);
            StatisticsResponse statisticsResponse = getStats(authorizedRequest);
            genericResponse.setStatus(statisticsResponse.getStatus());
            genericResponse.setResponse(MiscUtils.toJson(statisticsResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static StatisticsResponse getStats(AuthorizedRequest request) {
        StatisticsResponse statisticsResponse = new StatisticsResponse();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        try {
            UserAuthToken userAuthToken = session.createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", request.getToken()).getSingleResult();
            if (userAuthToken == null) {
                statisticsResponse.setStatus(Status.UNAUTHORIZED);
                session.close();
                return statisticsResponse;
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

            statisticsResponse = new StatisticsResponse(numberOfSafeAndVaccinatedUsers, numberOfSafeAndUnVaccinatedUsers, numberOfAtRiskAndVaccinatedUsers, numberOfAtRiskAndUnVaccinatedUsers, numberOfContagiousAndVaccinatedUsers, numberOfContagiousAndUnVaccinatedUsers);
            statisticsResponse.setStatus(Status.SUCCESS);
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
            statisticsResponse.setStatus(Status.BAD_REQUEST);
        }
        return statisticsResponse;
    }
}
