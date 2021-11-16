package com.haidousm.LAURona.entity;

import javax.persistence.*;

@Entity
@Table(name = "location_details")
public class LocationDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "latitude")
    private double latitude;

    @Column(name = "longitude")
    private double longitude;

    @Column(name = "timestamp")
    private long timestamp;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public LocationDetails() {
    }

    public LocationDetails(double latitude, double longitude, long timestamp, User user) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.timestamp = timestamp;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "LocationDetails{" +
                "id=" + id +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timestamp=" + timestamp +
                ", user_id=" + user.getId() +
                '}';
    }
}
