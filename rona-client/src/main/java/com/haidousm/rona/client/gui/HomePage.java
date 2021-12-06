package com.haidousm.rona.client.gui;

import com.haidousm.rona.client.client.Client;
import com.haidousm.rona.client.controllers.ClientController;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.responses.StatisticsResponse;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class HomePage extends JFrame implements Notifications {
    private JLabel welcomeNameLabel;
    private JPanel mainPane;
    private JLabel statusLabel;
    private JButton declarePositiveHealthStatus;
    private JTable statsTable;
    private JButton friendsAndFamilyButton;
    private JButton logOutButton;
    private JButton addTrustedUserButton;
    private JButton declareSafeHealthStatus;
    private JLabel instructionsLabel;

    private final Client client;

    public HomePage(String name, Client client) {
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
        User currentUser = ClientController.getCurrentUser(client);
        welcomeNameLabel.setText("Welcome " + currentUser.getFirstName() + " " + currentUser.getLastName());
    }

    private void setupHealthStatus() {
        HealthStatus healthStatus = ClientController.getUserHealthStatus(client);
        client.setHealthStatus(healthStatus);
        if (healthStatus.getStatus() == Health.CONTAGIOUS) {
            declarePositiveHealthStatus.setVisible(false);
            declareSafeHealthStatus.setVisible(true);
            instructionsLabel.setText("<html>You should:<br>" +
                    "1- QUARANTINE for at least 7 days!.<br>" +
                    "2- Order an at-home PCR Test<br>" +
                    "3- Keep in touch with a doctor in case of complications</html>");
        } else if (healthStatus.getStatus() == Health.SAFE) {
            declarePositiveHealthStatus.setVisible(true);
            declareSafeHealthStatus.setVisible(false);
            instructionsLabel.setText("<html>You should:<br>" +
                    "1- Keep wearing your mask at all times.<br>" +
                    "2- You should maintain social distancing.<br>" +
                    "3- Stay Safe!</html>");
        } else {
            declarePositiveHealthStatus.setVisible(true);
            declareSafeHealthStatus.setVisible(true);
            instructionsLabel.setText("<html>You should:<br>" +
                    "1- QUARANTINE!.<br>" +
                    "2- Take a PCR Test.<br>" +
                    "3- If negative, remain quarantined for 3 days</html>");
        }

        statusLabel.setText("Status: " + healthStatus.getStatus().name());

    }

    private void setupStatsTable() {
        String[] rowNames = {"Vaccinated", "Not Vaccinated", "Total"};
        String[] columnNames = {"", "Safe", "At Risk", "Contagious", "Total"};
        Object[][] data = new Object[rowNames.length + 1][columnNames.length];

        StatisticsResponse statisticsResponse = ClientController.getCampusStatistics(client);

        data[0][0] = rowNames[0];
        data[1][0] = rowNames[1];
        data[2][0] = rowNames[2];

        data[0][1] = statisticsResponse.getNumberOfSafeAndVaccinatedUsers();
        data[0][2] = statisticsResponse.getNumberOfAtRiskAndVaccinatedUsers();
        data[0][3] = statisticsResponse.getNumberOfContagiousAndVaccinatedUsers();
        data[0][4] = (int) data[0][1] + (int) data[0][2] + (int) data[0][3];

        data[1][1] = statisticsResponse.getNumberOfSafeAndUnVaccinatedUsers();
        data[1][2] = statisticsResponse.getNumberOfAtRiskAndUnVaccinatedUsers();
        data[1][3] = statisticsResponse.getNumberOfContagiousAndUnVaccinatedUsers();
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
        ClientController.updateUserHealthStatus(client, Health.CONTAGIOUS);

        client.getHealthStatus().setStatus(Health.CONTAGIOUS);
        statusLabel.setText("Status: " + client.getHealthStatus().getStatus().name());

        declareSafeHealthStatus.setVisible(true);
        declarePositiveHealthStatus.setVisible(false);
        setupStatsTable();
        instructionsLabel.setText("<html>You should:<br>" +
                "1- QUARANTINE for at least 7 days!.<br>" +
                "2- Order an at-home PCR Test<br>" +
                "3- Keep in touch with a doctor in case of complications</html>");

    }

    private void handleDeclareSafeStatus() {
        ClientController.updateUserHealthStatus(client, Health.SAFE);
        client.getHealthStatus().setStatus(Health.SAFE);
        statusLabel.setText("Status: " + client.getHealthStatus().getStatus().name());
        declarePositiveHealthStatus.setVisible(true);
        declareSafeHealthStatus.setVisible(false);
        setupStatsTable();
        instructionsLabel.setText("<html>You should:<br>" +
                "1- Keep wearing your mask at all times.<br>" +
                "2- You should maintain social distancing.<br>" +
                "3- Stay Safe!</html>");
    }

    private void handleLogoutClicked() {
        this.dispose();
        client.setToken("");
        new AuthenticatePage("Rona", client).setVisible(true);
    }

    private void handleAddTrustedUserClicked() {
        dispose();
        new TrustedUsersPage("Rona", client).setVisible(true);

    }

    private void handleFriendsAndFamilyClicked() {
        dispose();
        new FriendsAndFamilyPage("Rona", client).setVisible(true);
    }

    @Override
    public void pollHealthStatus() {
        HealthStatus healthStatus = ClientController.getUserHealthStatus(client);
        if (healthStatus.getStatus() == this.client.getHealthStatus().getStatus()) {
            return;
        }
        if (healthStatus.getStatus() == Health.AT_RISK) {
            JOptionPane.showMessageDialog(this, "You are at risk! Please begin quarantine immediately!", "Warning", JOptionPane.WARNING_MESSAGE);
            setupHealthStatus();
            setupStatsTable();
        } else if (healthStatus.getStatus() == Health.SAFE) {
            JOptionPane.showMessageDialog(this, "You are safe! Please continue to maintain social distancing!", "Warning", JOptionPane.WARNING_MESSAGE);
            setupHealthStatus();
            setupStatsTable();
        }
    }

    @Override
    public void transmitCurrentLocation() {
        ClientController.updateUserLocation(client, client.getCurrentLocation());
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
        instructionsLabel = new JLabel();
        instructionsLabel.setHorizontalAlignment(0);
        instructionsLabel.setHorizontalTextPosition(0);
        instructionsLabel.setText("<html>You should:<br>\" +                 \"1- Keep wearing your mask at all times.<br>\" +                 \"2- You should maintain social distancing.<br>\" +                 \"3- Stay Safe!</html>");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 100;
        mainPane.add(instructionsLabel, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPane;
    }
}
