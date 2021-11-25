package com.haidousm.rona.server.entity;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

@Entity
@Table(name = "user_auth_token")
public class UserAuthToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Expose
    @Column(name = "token")
    private String token;

    @Expose
    @Column(name = "expiry_timestamp")
    private long expiryTimestamp;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public UserAuthToken() {
    }

    public UserAuthToken(String token, long expiryTimestamp, User user) {
        this.token = token;
        this.expiryTimestamp = expiryTimestamp;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(long expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }

    public User getUser() {
        return user;
    }
}
