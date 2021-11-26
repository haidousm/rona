package com.haidousm.rona.common.requests;

public class RegisterUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private boolean isVaccinated;
    private String vaccineCertificateFile;
    private String imageFile;

    public RegisterUserRequest(String firstname, String lastname, String email, String username, String password, boolean isVaccinated, String vaccineCertificateFile, String imageFile) {
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


}
