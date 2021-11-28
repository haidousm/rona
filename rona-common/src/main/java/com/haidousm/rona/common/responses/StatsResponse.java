package com.haidousm.rona.common.responses;

import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.enums.Status;

public class StatsResponse implements Response {
    private Status status;

    private int numberOfSafeAndVaccinatedUsers;
    private int numberOfSafeAndUnVaccinatedUsers;
    private int numberOfAtRiskAndVaccinatedUsers;
    private int numberOfAtRiskAndUnVaccinatedUsers;
    private int numberOfContagiousAndVaccinatedUsers;
    private int numberOfContagiousAndUnVaccinatedUsers;

    public StatsResponse(int numberOfSafeAndVaccinatedUsers, int numberOfSafeAndUnVaccinatedUsers, int numberOfAtRiskAndVaccinatedUsers, int numberOfAtRiskAndUnVaccinatedUsers, int numberOfContagiousAndVaccinatedUsers, int numberOfContagiousAndUnVaccinatedUsers) {
        this.numberOfSafeAndVaccinatedUsers = numberOfSafeAndVaccinatedUsers;
        this.numberOfSafeAndUnVaccinatedUsers = numberOfSafeAndUnVaccinatedUsers;
        this.numberOfAtRiskAndVaccinatedUsers = numberOfAtRiskAndVaccinatedUsers;
        this.numberOfAtRiskAndUnVaccinatedUsers = numberOfAtRiskAndUnVaccinatedUsers;
        this.numberOfContagiousAndVaccinatedUsers = numberOfContagiousAndVaccinatedUsers;
        this.numberOfContagiousAndUnVaccinatedUsers = numberOfContagiousAndUnVaccinatedUsers;
    }


    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public Method getMethod() {
        return Method.GET_STATS;
    }

    public int getNumberOfSafeAndVaccinatedUsers() {
        return numberOfSafeAndVaccinatedUsers;
    }

    public int getNumberOfSafeAndUnVaccinatedUsers() {
        return numberOfSafeAndUnVaccinatedUsers;
    }

    public int getNumberOfAtRiskAndVaccinatedUsers() {
        return numberOfAtRiskAndVaccinatedUsers;
    }

    public int getNumberOfAtRiskAndUnVaccinatedUsers() {
        return numberOfAtRiskAndUnVaccinatedUsers;
    }

    public int getNumberOfContagiousAndVaccinatedUsers() {
        return numberOfContagiousAndVaccinatedUsers;
    }

    public int getNumberOfContagiousAndUnVaccinatedUsers() {
        return numberOfContagiousAndUnVaccinatedUsers;
    }



}
