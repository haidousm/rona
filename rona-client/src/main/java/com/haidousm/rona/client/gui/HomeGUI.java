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
import java.util.concurrent.ThreadLocalRandom;

public class HomeGUI extends JFrame {
    private JLabel welcomeNameLabel;
    private JPanel mainPane;
    private JLabel statusLabel;
    private JButton declarePositiveHealthStatus;
    private JTable statsTable;
    private JButton friendsAndFamilyButton;
    private JButton logOutButton;
    private JButton addTrustedUserButton;
    private JButton declareSafeHealthStatus;

    private final Client client;
    private HealthStatus healthStatus;

    public HomeGUI(String name, Client client) {
        super(name);
        $$$setupUI$$$();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPane);
        pack();
        setLocationRelativeTo(null);
        this.client = client;

        client.setCurrentFrame(this);


        setupWelcomeMessage();
        setupHealthStatus();
        setupStatsTable();

        declarePositiveHealthStatus.addActionListener(e -> {
            handleDeclarePositiveStatus();
        });

        declareSafeHealthStatus.addActionListener(e -> {
            handleDeclareSafeStatus();
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
        Request request = UserController.prepareGetCurrentUser(client.getToken());
        User currentUser = UserResponseBuilder.builder().build(client.sendAndReceive(request));
        welcomeNameLabel.setText("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName());
    }

    private void setupHealthStatus() {
        Request request = AuthorizedRequestsController.prepareGetHealthStatusRequest(client.getToken());
        HealthStatus healthStatus = HealthStatusResponseBuilder.builder().build(client.sendAndReceive(request));
        this.healthStatus = healthStatus;
        if (healthStatus.getStatus() == Health.CONTAGIOUS) {
            declarePositiveHealthStatus.setVisible(false);
            declareSafeHealthStatus.setVisible(true);
            client.stopPollingHealthStatus();
        } else if (healthStatus.getStatus() == Health.SAFE) {
            declarePositiveHealthStatus.setVisible(true);
            declareSafeHealthStatus.setVisible(false);
        } else {
            declarePositiveHealthStatus.setVisible(true);
            declareSafeHealthStatus.setVisible(true);
            int randomLat = ThreadLocalRandom.current().nextInt(10, 1000 + 1);
            int randomLong = ThreadLocalRandom.current().nextInt(10, 1000 + 1);
            Request locationRequest = AuthorizedRequestsController.prepareUpdateUserLocationRequest(client.getToken(), new Integer[]{randomLat, randomLong});
            client.sendAndReceive(locationRequest);
            client.stopTransmittingLocation();
            client.stopPollingHealthStatus();
        }
        statusLabel.setText("Status: " + healthStatus.getStatus().name());
    }

    private void setupStatsTable() {
        String[] rowNames = {"Vaccinated", "Not Vaccinated", "Total"};
        String[] columnNames = {"", "Safe", "At Risk", "Contagious", "Total"};
        Object[][] data = new Object[rowNames.length + 1][columnNames.length];

        Request request = AuthorizedRequestsController.prepareGetStatsRequest(client.getToken());
        StatsResponse statsResponse = StatsResponseBuilder.builder().build(client.sendAndReceive(request));

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

    private void handleDeclarePositiveStatus() {
        Request request = AuthorizedRequestsController.prepareUpdateHealthStatusRequest(client.getToken(), Health.CONTAGIOUS);
        client.sendAndReceive(request);
        healthStatus.setStatus(Health.CONTAGIOUS);
        statusLabel.setText("Status: " + healthStatus.getStatus().name());

        int randomLat = ThreadLocalRandom.current().nextInt(10, 1000 + 1);
        int randomLong = ThreadLocalRandom.current().nextInt(10, 1000 + 1);
        Request locationRequest = AuthorizedRequestsController.prepareUpdateUserLocationRequest(client.getToken(), new Integer[]{randomLat, randomLong});
        client.sendAndReceive(locationRequest);
        client.stopTransmittingLocation();
        client.stopPollingHealthStatus();
        declareSafeHealthStatus.setVisible(true);
        declarePositiveHealthStatus.setVisible(false);
        setupStatsTable();
    }

    private void handleDeclareSafeStatus() {
        Request request = AuthorizedRequestsController.prepareUpdateHealthStatusRequest(client.getToken(), Health.SAFE);
        client.sendAndReceive(request);
        healthStatus.setStatus(Health.SAFE);
        statusLabel.setText("Status: " + healthStatus.getStatus().name());
        client.pollHealthStatus(60);
        client.beginTransmittingLocation(60);
        declarePositiveHealthStatus.setVisible(true);
        declareSafeHealthStatus.setVisible(false);
        setupStatsTable();
    }

    private void handleLogoutClicked() {
        this.dispose();
        client.setToken("");
        new AuthGUI("Rona", client).setVisible(true);
    }

    private void handleAddTrustedUserClicked() {
        dispose();
        client.stopTransmittingLocation();
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

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPane = new JPanel();
        mainPane.setLayout(new GridBagLayout());
        friendsAndFamilyButton = new JButton();
        friendsAndFamilyButton.setText("Friends and Family");
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPane.add(friendsAndFamilyButton, gbc);
        final JScrollPane scrollPane1 = new JScrollPane();
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridheight = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPane.add(scrollPane1, gbc);
        statsTable = new JTable();
        scrollPane1.setViewportView(statsTable);
        logOutButton = new JButton();
        logOutButton.setText("Log out");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPane.add(logOutButton, gbc);
        addTrustedUserButton = new JButton();
        addTrustedUserButton.setText("Add Trusted User");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPane.add(addTrustedUserButton, gbc);
        statusLabel = new JLabel();
        statusLabel.setText("Status: ");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPane.add(statusLabel, gbc);
        welcomeNameLabel = new JLabel();
        welcomeNameLabel.setText("Label");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPane.add(welcomeNameLabel, gbc);
        declareSafeHealthStatus = new JButton();
        declareSafeHealthStatus.setBackground(new Color(-16711936));
        declareSafeHealthStatus.setBorderPainted(false);
        declareSafeHealthStatus.setContentAreaFilled(true);
        declareSafeHealthStatus.setFocusPainted(false);
        declareSafeHealthStatus.setForeground(new Color(-1));
        declareSafeHealthStatus.setHideActionText(false);
        declareSafeHealthStatus.setOpaque(true);
        declareSafeHealthStatus.setText("Declare SAFE status!");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPane.add(declareSafeHealthStatus, gbc);
        declarePositiveHealthStatus = new JButton();
        declarePositiveHealthStatus.setBackground(new Color(-64000));
        declarePositiveHealthStatus.setBorderPainted(false);
        declarePositiveHealthStatus.setContentAreaFilled(true);
        declarePositiveHealthStatus.setFocusPainted(false);
        declarePositiveHealthStatus.setForeground(new Color(-1));
        declarePositiveHealthStatus.setHideActionText(false);
        declarePositiveHealthStatus.setOpaque(true);
        declarePositiveHealthStatus.setText("Declare POSITIVE status!");
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPane.add(declarePositiveHealthStatus, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }

}
