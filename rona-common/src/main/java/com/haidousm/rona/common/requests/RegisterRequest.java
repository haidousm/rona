package com.haidousm.rona.common.requests;

import com.google.gson.annotations.Expose;
import com.haidousm.rona.common.enums.Method;

public class RegisterRequest extends GenericRequest {
    @Expose
    private String firstname;
    @Expose
    private String lastname;
    @Expose
    private String email;
    @Expose
    private String username;
    @Expose
    private String password;
    @Expose
    private boolean isVaccinated;
    @Expose
    private String vaccineCertificateFile;
    @Expose
    private String imageFile;

    public RegisterRequest(String firstname, String lastname, String email, String username, String password, boolean isVaccinated, String vaccineCertificateFile, String imageFile) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isVaccinated = isVaccinated;
        this.vaccineCertificateFile = vaccineCertificateFile;
        this.imageFile = imageFile;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public boolean isVaccinated() {
        return isVaccinated;
    }

    public String getVaccineCertificateFile() {
        return vaccineCertificateFile;
    }

    public String getImageFile() {
        return imageFile;
    }

    @Override
    public Method getMethod() {
        return Method.REGISTER;
    }
}
