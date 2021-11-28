package com.haidousm.rona.common.responses.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.StatsResponse;

public class StatsResponseBuilder {
    private int numberOfSafeAndVaccinatedUsers;
    private int numberOfSafeAndUnVaccinatedUsers;
    private int numberOfAtRiskAndVaccinatedUsers;
    private int numberOfAtRiskAndUnVaccinatedUsers;
    private int numberOfContagiousAndVaccinatedUsers;
    private int numberOfContagiousAndUnVaccinatedUsers;

    public StatsResponseBuilder setNumberOfSafeAndVaccinatedUsers(int numberOfSafeAndVaccinatedUsers) {
        this.numberOfSafeAndVaccinatedUsers = numberOfSafeAndVaccinatedUsers;
        return this;
    }

    public StatsResponseBuilder setNumberOfSafeAndUnVaccinatedUsers(int numberOfSafeAndUnVaccinatedUsers) {
        this.numberOfSafeAndUnVaccinatedUsers = numberOfSafeAndUnVaccinatedUsers;
        return this;
    }

    public StatsResponseBuilder setNumberOfAtRiskAndVaccinatedUsers(int numberOfAtRiskAndVaccinatedUsers) {
        this.numberOfAtRiskAndVaccinatedUsers = numberOfAtRiskAndVaccinatedUsers;
        return this;
    }

    public StatsResponseBuilder setNumberOfAtRiskAndUnVaccinatedUsers(int numberOfAtRiskAndUnVaccinatedUsers) {
        this.numberOfAtRiskAndUnVaccinatedUsers = numberOfAtRiskAndUnVaccinatedUsers;
        return this;
    }

    public StatsResponseBuilder setNumberOfContagiousAndVaccinatedUsers(int numberOfContagiousAndVaccinatedUsers) {
        this.numberOfContagiousAndVaccinatedUsers = numberOfContagiousAndVaccinatedUsers;
        return this;
    }

    public StatsResponseBuilder setNumberOfContagiousAndUnVaccinatedUsers(int numberOfContagiousAndUnVaccinatedUsers) {
        this.numberOfContagiousAndUnVaccinatedUsers = numberOfContagiousAndUnVaccinatedUsers;
        return this;
    }

    public StatsResponse build() {
        return new StatsResponse(numberOfSafeAndVaccinatedUsers, numberOfSafeAndUnVaccinatedUsers, numberOfAtRiskAndVaccinatedUsers, numberOfAtRiskAndUnVaccinatedUsers, numberOfContagiousAndVaccinatedUsers, numberOfContagiousAndUnVaccinatedUsers);
    }


    public StatsResponse build(GenericResponse genericResponse) {
        StatsResponse statsResponse = new Gson().fromJson(genericResponse.getResponse(), StatsResponse.class);
        statsResponse.setStatus(genericResponse.getStatus());
        return statsResponse;
    }

    public static StatsResponseBuilder builder() {
        return new StatsResponseBuilder();
    }
}
