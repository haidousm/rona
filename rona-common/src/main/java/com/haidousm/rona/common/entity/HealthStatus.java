package com.haidousm.rona.common.entity;

import com.google.gson.annotations.Expose;
import com.haidousm.rona.common.enums.Health;

import javax.persistence.*;

@Entity
@Table(name = "health_status")
public class HealthStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    @Expose
    private int id;

    @Column(name = "status")
    @Expose
    private Health status;

    @Column(name = "timestamp")
    @Expose
    private long timestamp;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public HealthStatus() {
    }

    public HealthStatus(Health status, User user) {
        this.status = status;
        this.timestamp = System.currentTimeMillis();
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public Health getStatus() {
        return status;
    }

    public void setStatus(Health status) {
        this.status = status;
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

}
