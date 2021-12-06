package com.haidousm.rona.client.controllers;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.haidousm.rona.client.client.Client;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.LoginRequest;
import com.haidousm.rona.common.requests.RegisterRequest;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.StatisticsResponse;
import com.haidousm.rona.common.responses.TokenResponse;
import com.haidousm.rona.common.utils.MiscUtils;

import java.lang.reflect.Type;
import java.nio.file.Path;
import java.util.List;

public class ClientController {
    public static TokenResponse registerUser(Client client, String firstname, String lastname, String email, String username, String password, boolean isVaccinated, Path vaccineCertificateFilePath, Path imageFilePath) {
        String vaccineCertificateFile = "";
        if (vaccineCertificateFilePath.toFile().exists()) {
            vaccineCertificateFile = MiscUtils.encodeFileToBase64(vaccineCertificateFilePath);
        }

        String imageFile = "";
        if (imageFilePath.toFile().exists()) {
            imageFile = MiscUtils.encodeFileToBase64(imageFilePath);
        }
        RegisterRequest registerRequest = new RegisterRequest(firstname, lastname, email, username, password, isVaccinated, vaccineCertificateFile, imageFile);
        GenericResponse genericResponse = client.sendAndReceive(registerRequest);
        TokenResponse tokenResponse = MiscUtils.fromJson(genericResponse.getResponse(), TokenResponse.class);
        tokenResponse.setStatus(genericResponse.getStatus());
        return tokenResponse;
    }

    public static TokenResponse login(Client client, String username, String password) {
        LoginRequest loginRequest = new LoginRequest(username, password);
        GenericResponse genericResponse = client.sendAndReceive(loginRequest);
        TokenResponse tokenResponse = MiscUtils.fromJson(genericResponse.getResponse(), TokenResponse.class);
        tokenResponse.setStatus(genericResponse.getStatus());
        return tokenResponse;
    }

    public static User getCurrentUser(Client client) {
        AuthorizedRequest authorizedRequest = new AuthorizedRequest(Method.GET_USER, client.getToken());
        GenericResponse genericResponse = client.sendAndReceive(authorizedRequest);
        return MiscUtils.fromJson(genericResponse.getResponse(), User.class);
    }

    public static HealthStatus getUserHealthStatus(Client client) {
        AuthorizedRequest authorizedRequest = new AuthorizedRequest(Method.GET_HEALTH_STATUS, client.getToken());
        GenericResponse genericResponse = client.sendAndReceive(authorizedRequest);
        return MiscUtils.fromJson(genericResponse.getResponse(), HealthStatus.class);
    }

    public static void updateUserHealthStatus(Client client, Health healthStatus) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", healthStatus.name());

        AuthorizedRequest authorizedRequest = new AuthorizedRequest(Method.UPDATE_HEALTH_STATUS, client.getToken(), jsonObject.toString());
        client.sendAndReceive(authorizedRequest);

    }

    public static StatisticsResponse getCampusStatistics(Client client) {
        AuthorizedRequest authorizedRequest = new AuthorizedRequest(Method.GET_STATS, client.getToken());
        GenericResponse genericResponse = client.sendAndReceive(authorizedRequest);
        StatisticsResponse statisticsResponse = MiscUtils.fromJson(genericResponse.getResponse(), StatisticsResponse.class);
        statisticsResponse.setStatus(genericResponse.getStatus());
        return statisticsResponse;
    }

    public static List<User> getTrustedUsers(Client client) {
        AuthorizedRequest authorizedRequest = new AuthorizedRequest(Method.GET_TRUSTED_USERS, client.getToken());
        Type listOfUsers = new TypeToken<List<User>>() {
        }.getType();
        return MiscUtils.fromJson(client.sendAndReceive(authorizedRequest).getResponse(), listOfUsers);
    }

    public static void updateUserLocation(Client client, Integer[] coords) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("latitude", coords[0]);
        jsonObject.addProperty("longitude", coords[1]);
        AuthorizedRequest authorizedRequest = new AuthorizedRequest(Method.UPDATE_USER_LOCATION, client.getToken(), MiscUtils.toJson(jsonObject));
        client.sendAndReceive(authorizedRequest);
    }
}
