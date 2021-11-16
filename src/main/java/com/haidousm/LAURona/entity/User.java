package com.haidousm.LAURona.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_vaccinated")
    private boolean isVaccinated;

    @Column(name = "vaccination_certificate_file_path")
    private String vaccinationCertificateFilePath;

    @Column(name = "image_file_path")
    private String imageFilePath;

    @ManyToMany
    @JoinTable(name = "trusted_users",
            joinColumns = @JoinColumn(name = "trusterId"),
            inverseJoinColumns = @JoinColumn(name = "trusteeId")
    )
    private List<User> trustedUsers;

    @ManyToMany
    @JoinTable(name = "trusted_users",
            joinColumns = @JoinColumn(name = "trusteeId"),
            inverseJoinColumns = @JoinColumn(name = "trusterId")
    )
    private List<User> trustedByUsers;

    public User() {
    }

    public User(String firstName, String lastName, String email, String username, String password, boolean isVaccinated, String vaccinationCertificateFilePath, String imageFilePath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.isVaccinated = isVaccinated;
        this.vaccinationCertificateFilePath = vaccinationCertificateFilePath;
        this.imageFilePath = imageFilePath;
        this.trustedUsers = new ArrayList<>();
        this.trustedByUsers = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
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

    public boolean getIsVaccinated() {
        return isVaccinated;
    }

    public String getVaccinationCertificateFilePath() {
        return vaccinationCertificateFilePath;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public List<User> getTrustedUsers() {
        return trustedUsers;
    }

    public List<User> getTrustedByUsers() {
        return trustedByUsers;
    }

    public void trustUser(User user) {
        trustedUsers.add(user);
        user.getTrustedByUsers().add(this);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isVaccinated=" + isVaccinated +
                ", vaccinationCertificateFilePath='" + vaccinationCertificateFilePath + '\'' +
                ", imageFilePath='" + imageFilePath + '\'' +
                ", trustedByUsers=" + trustedByUsers +
                '}';
    }

}
