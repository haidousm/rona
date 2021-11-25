package com.haidousm.rona.requests;

public class RegisterUserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String username;
    private String password;
    private boolean isVaccinated;
    private String vaccineCertificateFile;
    private String imageFile;

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
