package com.haidousm.rona.common.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Expose
    private int id;

    @Column(name = "first_name")
    @Expose
    private String firstName;

    @Column(name = "last_name")
    @Expose
    private String lastName;

    @Column(name = "email", unique = true)
    @Expose
    private String email;

    @Column(name = "username", unique = true)
    @Expose
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "is_vaccinated")
    @Expose
    private boolean isVaccinated;

    @Column(name = "vaccination_certificate_file_path")
    @Expose
    private String vaccinationCertificateFilePath;

    @Column(name = "image_file_path")
    @Expose
    private String imageFilePath;

    @Column(name = "health_statuses")
    @OneToMany(mappedBy = "user")
    @Expose
    private List<HealthStatus> healthStatuses;

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
        this.healthStatuses = new ArrayList<>();
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

    public List<HealthStatus> getHealthStatuses() {
        healthStatuses.sort(Comparator.comparing(HealthStatus::getTimestamp).reversed());
        return healthStatuses;
    }


}
