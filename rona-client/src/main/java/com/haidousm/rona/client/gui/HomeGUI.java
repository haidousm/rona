package com.haidousm.rona.client.gui;

import com.haidousm.rona.client.client.Client;
import com.haidousm.rona.client.controllers.HealthStatusController;
import com.haidousm.rona.client.controllers.UserController;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.AuthResponse;
import com.haidousm.rona.common.responses.builders.HealthStatusResponseBuilder;
import com.haidousm.rona.common.responses.builders.UserResponseBuilder;

import javax.swing.*;

public class HomeGUI extends JFrame {
    private JLabel welcomeNameLabel;
    private JPanel mainPane;
    private JLabel statusLabel;
    private JButton declareInfectionButton;
    private JTable statsTable;
    private JButton friendsAndFamilyButton;

    public HomeGUI(String name, Client client, AuthResponse authResponse) {
        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPane);

        Request getCurrentUserRequest = UserController.prepareGetCurrentUser(authResponse.getToken());
        client.send(getCurrentUserRequest);
        User currentUser = UserResponseBuilder.builder().build(client.receive());
        welcomeNameLabel.setText("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName());

        Request getHealthStatusRequest = HealthStatusController.prepareGetHealthStatusRequest(authResponse.getToken());
        client.send(getHealthStatusRequest);
        HealthStatus healthStatus = HealthStatusResponseBuilder.builder().build(client.receive());
        statusLabel.setText("Status: " + healthStatus.getStatus().name());
    }
}
