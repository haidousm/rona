package com.haidousm.rona.entity;

import com.haidousm.rona.enums.Health;

import javax.persistence.*;

@Entity
@Table(name = "health_status")
public class HealthStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "status")
    private Health status;

    @Column(name = "timestamp")
    private long timestamp;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public HealthStatus() {
    }

    public HealthStatus(Health status, long timestamp, User user) {
        this.status = status;
        this.timestamp = timestamp;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public Health getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "HealthStatus{" +
                "id=" + id +
                ", status=" + status +
                ", timestamp=" + timestamp +
                ", user_id=" + user.getId() +
                '}';
    }
}
