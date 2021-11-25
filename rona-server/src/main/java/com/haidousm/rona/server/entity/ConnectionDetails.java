package com.haidousm.rona.server.entity;

import javax.persistence.*;

@Entity
@Table(name = "connection_details")
public class ConnectionDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "port")
    private int port;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public ConnectionDetails() {

    }

    public ConnectionDetails(String ipAddress, int port, User user) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "ConnectionDetails{" +
                "id=" + id +
                ", ipAddress='" + ipAddress + '\'' +
                ", port=" + port +
                ", user_id=" + user.getId() +
                '}';
    }
}
