package com.haidousm.rona.client.gui;

import com.haidousm.rona.client.client.Client;
import com.haidousm.rona.client.controllers.AuthorizedRequestsController;
import com.haidousm.rona.client.controllers.UserController;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.StatsResponse;
import com.haidousm.rona.common.responses.builders.HealthStatusResponseBuilder;
import com.haidousm.rona.common.responses.builders.StatsResponseBuilder;
import com.haidousm.rona.common.responses.builders.UserResponseBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HomeGUI extends JFrame {
    private JLabel welcomeNameLabel;
    private JPanel mainPane;
    private JLabel statusLabel;
    private JButton declareHealthStatusButton;
    private JTable statsTable;
    private JButton friendsAndFamilyButton;
    private JButton logOutButton;
    private JButton addTrustedUserButton;

    private final Client client;
    private HealthStatus healthStatus;

    public HomeGUI(String name, Client client) {
        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPane);
        pack();
        setLocationRelativeTo(null);
        this.client = client;

        setupWelcomeMessage();
        setupHealthStatus();
        setupStatsTable();

        declareHealthStatusButton.addActionListener(e -> {
            handleDeclareHealthStatusClicked();
        });

        logOutButton.addActionListener(e -> {
            handleLogoutClicked();
        });

        friendsAndFamilyButton.addActionListener(e -> {
            handleFriendsAndFamilyClicked();
        });

        addTrustedUserButton.addActionListener(e -> {
            handleAddTrustedUserClicked();
        });

    }

    private void setupWelcomeMessage() {
        Request getCurrentUserRequest = UserController.prepareGetCurrentUser(client.getToken());
        client.send(getCurrentUserRequest);
        User currentUser = UserResponseBuilder.builder().build(client.receive());
        welcomeNameLabel.setText("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName());
    }

    private void setupHealthStatus() {
        Request getHealthStatusRequest = AuthorizedRequestsController.prepareGetHealthStatusRequest(client.getToken());
        client.send(getHealthStatusRequest);
        HealthStatus healthStatus = HealthStatusResponseBuilder.builder().build(client.receive());
        this.healthStatus = healthStatus;
        if (healthStatus.getStatus() == Health.CONTAGIOUS) {
            declareHealthStatusButton.setBackground(Color.GREEN);
            declareHealthStatusButton.setText("I'm SAFE!");
        } else {
            declareHealthStatusButton.setBackground(Color.RED);
            declareHealthStatusButton.setText("I'm POSITIVE!");
        }
        statusLabel.setText("Status: " + healthStatus.getStatus().name());
    }

    private void setupStatsTable() {
        String[] rowNames = {"Vaccinated", "Not Vaccinated", "Total"};
        String[] columnNames = {"", "Safe", "At Risk", "Contagious", "Total"};
        Object[][] data = new Object[rowNames.length + 1][columnNames.length];

        Request getStats = AuthorizedRequestsController.prepareGetStatsRequest(client.getToken());
        client.send(getStats);
        StatsResponse statsResponse = StatsResponseBuilder.builder().build(client.receive());

        data[0][0] = rowNames[0];
        data[1][0] = rowNames[1];
        data[2][0] = rowNames[2];

        data[0][1] = statsResponse.getNumberOfSafeAndVaccinatedUsers();
        data[0][2] = statsResponse.getNumberOfAtRiskAndVaccinatedUsers();
        data[0][3] = statsResponse.getNumberOfContagiousAndVaccinatedUsers();
        data[0][4] = (int) data[0][1] + (int) data[0][2] + (int) data[0][3];

        data[1][1] = statsResponse.getNumberOfSafeAndUnVaccinatedUsers();
        data[1][2] = statsResponse.getNumberOfAtRiskAndUnVaccinatedUsers();
        data[1][3] = statsResponse.getNumberOfContagiousAndUnVaccinatedUsers();
        data[1][4] = (int) data[1][1] + (int) data[1][2] + (int) data[1][3];

        data[2][1] = (int) data[0][1] + (int) data[1][1];
        data[2][2] = (int) data[0][2] + (int) data[1][2];
        data[2][3] = (int) data[0][3] + (int) data[1][3];
        data[2][4] = (int) data[0][4] + (int) data[1][4];


        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        statsTable.setModel(model);
        statsTable.setRowSelectionAllowed(false);
        statsTable.setCellSelectionEnabled(false);

    }

    private void handleDeclareHealthStatusClicked() {
        Request declareHealthStatusRequest;
        if (healthStatus.getStatus() == Health.CONTAGIOUS) {
            declareHealthStatusRequest = AuthorizedRequestsController.prepareUpdateHealthStatusRequest(client.getToken(), Health.SAFE);
            healthStatus.setStatus(Health.SAFE);
        } else {
            declareHealthStatusRequest = AuthorizedRequestsController.prepareUpdateHealthStatusRequest(client.getToken(), Health.CONTAGIOUS);
            healthStatus.setStatus(Health.CONTAGIOUS);

        }
        client.send(declareHealthStatusRequest);
        client.receive();
        statusLabel.setText("Status: " + healthStatus.getStatus().name());
        if (healthStatus.getStatus() == Health.CONTAGIOUS) {
            declareHealthStatusButton.setBackground(Color.GREEN);
            declareHealthStatusButton.setText("I'm SAFE!");
        } else {
            declareHealthStatusButton.setBackground(Color.RED);
            declareHealthStatusButton.setText("I'm POSITIVE!");
        }

        setupStatsTable();
    }

    private void handleLogoutClicked() {
        this.dispose();
        client.setToken("");
        new AuthGUI("Rona", client).setVisible(true);
    }

    private void handleAddTrustedUserClicked() {
        dispose();
        new TrustedUsersListGUI("Rona", client).setVisible(true);

    }

    private void handleFriendsAndFamilyClicked() {
        dispose();
        new TrustedByUsersListGUI("Rona", client).setVisible(true);
    }

    @Override
    public Insets getInsets() {
        return new Insets(50, 25, 25, 50);
    }
}
