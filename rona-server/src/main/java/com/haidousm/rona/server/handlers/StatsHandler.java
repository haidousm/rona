package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.common.entity.UserAuthToken;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.StatsResponse;
import com.haidousm.rona.common.responses.builders.StatsResponseBuilder;
import com.haidousm.rona.server.utils.HibernateUtil;
import org.hibernate.Transaction;

public class StatsHandler {
    public static GenericResponse handleGetStats(Request request) {
        GenericResponse genericResponse = new GenericResponse();
        try {
            StatsResponse statsResponse = getStats((AuthorizedRequest) request);
            genericResponse.setStatus(statsResponse.getStatus());
            genericResponse.setResponse(new Gson().toJson(statsResponse));
        } catch (Exception e) {
            genericResponse.setStatus(Status.BAD_REQUEST);
        }
        return genericResponse;
    }

    private static StatsResponse getStats(AuthorizedRequest request) {
        StatsResponse statsResponse = StatsResponseBuilder.builder().build();
        Transaction tx = HibernateUtil.beginTransaction();
        try {
            UserAuthToken userAuthToken = HibernateUtil.getSession().createQuery("from UserAuthToken where token = :token", UserAuthToken.class).setParameter("token", request.getToken()).getSingleResult();
            if (userAuthToken == null) {
                statsResponse.setStatus(Status.UNAUTHORIZED);
                return statsResponse;
            }
            int numberOfSafeAndVaccinatedUsers = HibernateUtil.getSession().createQuery("select count(*) from HealthStatus where status = :status and user.isVaccinated = true", Long.class).setParameter("status", Health.SAFE).getSingleResult().intValue();
            int numberOfSafeAndUnVaccinatedUsers = HibernateUtil.getSession().createQuery("select count(*) from HealthStatus where status = :status and user.isVaccinated = false", Long.class).setParameter("status", Health.SAFE).getSingleResult().intValue();
            int numberOfAtRiskAndVaccinatedUsers = HibernateUtil.getSession().createQuery("select count(*) from HealthStatus where status = :status and user.isVaccinated = true", Long.class).setParameter("status", Health.AT_RISK).getSingleResult().intValue();
            int numberOfAtRiskAndUnVaccinatedUsers = HibernateUtil.getSession().createQuery("select count(*) from HealthStatus where status = :status and user.isVaccinated = false", Long.class).setParameter("status", Health.AT_RISK).getSingleResult().intValue();
            int numberOfContagiousAndVaccinatedUsers = HibernateUtil.getSession().createQuery("select count(*) from HealthStatus where status = :status and user.isVaccinated = true", Long.class).setParameter("status", Health.CONTAGIOUS).getSingleResult().intValue();
            int numberOfContagiousAndUnVaccinatedUsers = HibernateUtil.getSession().createQuery("select count(*) from HealthStatus where status = :status and user.isVaccinated = false", Long.class).setParameter("status", Health.CONTAGIOUS).getSingleResult().intValue();

            statsResponse = StatsResponseBuilder.builder().setNumberOfSafeAndVaccinatedUsers(numberOfSafeAndVaccinatedUsers).setNumberOfSafeAndUnVaccinatedUsers(numberOfSafeAndUnVaccinatedUsers).setNumberOfAtRiskAndVaccinatedUsers(numberOfAtRiskAndVaccinatedUsers).setNumberOfAtRiskAndUnVaccinatedUsers(numberOfAtRiskAndUnVaccinatedUsers).setNumberOfContagiousAndVaccinatedUsers(numberOfContagiousAndVaccinatedUsers).setNumberOfContagiousAndUnVaccinatedUsers(numberOfContagiousAndUnVaccinatedUsers).build();
            tx.commit();
            statsResponse.setStatus(Status.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            statsResponse.setStatus(Status.BAD_REQUEST);
        }
        return statsResponse;
    }
}
