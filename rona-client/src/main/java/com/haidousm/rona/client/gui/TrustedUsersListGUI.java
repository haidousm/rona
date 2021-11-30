package com.haidousm.rona.client.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haidousm.rona.client.client.Client;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.builders.AuthorizedRequestBuilder;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.builders.GenericResponseBuilder;
import com.haidousm.rona.common.responses.builders.UserResponseBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TrustedUsersListGUI extends JFrame {
    private JPanel mainPanel;
    private JTable trustedUsersTable;
    private JButton backButton;
    private JButton addUserButton;
    private JTextField searchTextField;

    private final Client client;

    public TrustedUsersListGUI(String title, Client client) {
        super(title);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

        this.client = client;
        backButton.addActionListener(e -> {
            this.dispose();
            new HomeGUI("Home", client).setVisible(true);
        });

        addUserButton.addActionListener(e -> {
            handleAddUserClicked();
        });

        setupTrustedUsersTable();
    }

    private void setupTrustedUsersTable() {
        String[] columnNames = {"Username", "First Name", "Last Name", "Email", ""};
        List<Object[]> data = new ArrayList<>();

        Request request = AuthorizedRequestBuilder.builder().setMethod(Method.GET_TRUSTED_USERS).setToken(client.getToken()).build();
        List<User> trustedUsers = UserResponseBuilder.builder().buildList(client.sendAndReceive(request));
        for (User user : trustedUsers) {
            JButton removeButton = new JButton("Remove");
            data.add(new Object[]{user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), removeButton});
            removeButton.addActionListener(e -> {
                handleRemoveTrustedUser(user);
            });
        }

        trustedUsersTable.setModel(new DefaultTableModel(data.toArray(new Object[data.size()][]), columnNames));
        trustedUsersTable.setRowHeight(25);
        trustedUsersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        trustedUsersTable.setGridColor(Color.LIGHT_GRAY);
        trustedUsersTable.setShowGrid(true);
        trustedUsersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        trustedUsersTable.setRowSelectionAllowed(true);
        trustedUsersTable.setColumnSelectionAllowed(false);
        trustedUsersTable.setCellSelectionEnabled(false);
        trustedUsersTable.setFocusable(false);

        TableCellRenderer buttonRenderer = new JTableButtonRenderer();
        trustedUsersTable.getColumn("").setCellRenderer(buttonRenderer);

        trustedUsersTable.addMouseListener(new JTableButtonMouseListener(trustedUsersTable));


    }

    private void handleRemoveTrustedUser(User user) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", user.getUsername());
        Request request = AuthorizedRequestBuilder.builder().setMethod(Method.REMOVE_TRUSTED_USER).setToken(client.getToken()).setBody(new Gson().toJson(jsonObject)).build();
        GenericResponse response = client.sendAndReceive(request);
        if (response.getStatus() == Status.SUCCESS) {
            JOptionPane.showMessageDialog(this, "User removed from trusted users list", "Success", JOptionPane.INFORMATION_MESSAGE);
            setupTrustedUsersTable();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to remove user from trusted users list", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void handleAddUserClicked() {
        String username = searchTextField.getText();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("username", username);
        Request request = AuthorizedRequestBuilder.builder().setMethod(Method.ADD_TRUSTED_USER).setToken(client.getToken()).setBody(new Gson().toJson(jsonObject)).build();
        GenericResponse response = client.sendAndReceive(request);
        if (response.getStatus() == Status.USER_NOT_FOUND) {
            JOptionPane.showMessageDialog(this, "User not found", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (response.getStatus() == Status.SUCCESS) {
            JOptionPane.showMessageDialog(this, "User added", "Success", JOptionPane.INFORMATION_MESSAGE);
            setupTrustedUsersTable();
        } else {
            JOptionPane.showMessageDialog(this, "Some Error", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    @Override
    public Insets getInsets() {
        return new Insets(50, 25, 25, 50);
    }

    private static class JTableButtonRenderer implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JButton button = (JButton) value;
            return button;
        }
    }

    private static class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX());
            int row = e.getY() / table.getRowHeight();

            /*Checking the row or column is valid or not*/
            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    /*perform a click event*/
                    ((JButton) value).doClick();
                }
            }
        }
    }

}
