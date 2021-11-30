package com.haidousm.rona.client.gui;

import com.haidousm.rona.client.client.Client;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.builders.AuthorizedRequestBuilder;
import com.haidousm.rona.common.responses.builders.HealthStatusResponseBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class TrustedByUsersListGUI extends JFrame {
    private JPanel mainPanel;
    private JTable trustedByUsersTable;
    private JButton backButton;

    private final Client client;

    public TrustedByUsersListGUI(String title, Client client) {
        super(title);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        setLocationRelativeTo(null);
        this.client = client;

        backButton.addActionListener(e -> {
            dispose();
            new HomeGUI("Home", client).setVisible(true);
        });

        setupTrustedByUsersTable();

    }

    private void setupTrustedByUsersTable() {
        String[] columnNames = {"Username", "First Name", "Last Name", "Health Status"};
        List<Object[]> data = new ArrayList<>();
        Request request = AuthorizedRequestBuilder.builder().setMethod(Method.GET_TRUSTED_BY_USERS).setToken(client.getToken()).build();
        List<HealthStatus> healthStatuses = HealthStatusResponseBuilder.builder().buildList(client.sendAndReceive(request));

        for (HealthStatus healthStatus : healthStatuses) {
            User user = healthStatus.getUser();
            data.add(new Object[]{user.getUsername(), user.getFirstName(), user.getLastName(), healthStatus.getStatus()});
        }

        trustedByUsersTable.setModel(new DefaultTableModel(data.toArray(new Object[data.size()][]), columnNames));
        trustedByUsersTable.setRowHeight(25);
        trustedByUsersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        trustedByUsersTable.setGridColor(Color.LIGHT_GRAY);
        trustedByUsersTable.setShowGrid(true);
        trustedByUsersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trustedByUsersTable.setRowSelectionAllowed(true);
        trustedByUsersTable.setColumnSelectionAllowed(false);
        trustedByUsersTable.setCellSelectionEnabled(false);
        trustedByUsersTable.setFocusable(false);

    }

}
