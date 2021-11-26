package com.haidousm.rona.common.requests.builders;

import com.google.gson.Gson;
import com.haidousm.rona.common.requests.RegisterRequest;

public class RegisterRequestBuilder {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private boolean isVaccinated;
    private String vaccineCertificateFile;
    private String imageFile;

    public RegisterRequestBuilder setFirstname(String firstname) {
        this.firstname = firstname;
        return this;
    }

    public RegisterRequestBuilder setLastname(String lastname) {
        this.lastname = lastname;
        return this;
    }

    public RegisterRequestBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public RegisterRequestBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public RegisterRequestBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public RegisterRequestBuilder setVaccinated(boolean isVaccinated) {
        this.isVaccinated = isVaccinated;
        return this;
    }

    public RegisterRequestBuilder setVaccineCertificateFile(String vaccineCertificateFile) {
        this.vaccineCertificateFile = vaccineCertificateFile;
        return this;
    }

    public RegisterRequestBuilder setImageFile(String imageFile) {
        this.imageFile = imageFile;
        return this;
    }

    public RegisterRequest build() {
        return new RegisterRequest(firstname, lastname, email, username, password, isVaccinated, vaccineCertificateFile, imageFile);
    }

    public RegisterRequest build(String body) {
        return new Gson().fromJson(body, RegisterRequest.class);
    }

    public static RegisterRequestBuilder builder() {
        return new RegisterRequestBuilder();
    }

}
