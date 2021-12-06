package com.haidousm.rona.client.gui;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.haidousm.rona.client.client.Client;
import com.haidousm.rona.client.controllers.AuthorizedRequestsController;
import com.haidousm.rona.common.entity.User;
import com.haidousm.rona.common.enums.Method;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.AuthorizedRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.responses.builders.UserResponseBuilder;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class TrustedUsersListGUI extends JFrame implements NotificationGUI {
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
        client.setCurrentFrame(this);

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

        Request request = AuthorizedRequestsController.prepareGetTrustedUsersRequest(client.getToken());
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
        Request request = new AuthorizedRequest(Method.REMOVE_TRUSTED_USER, client.getToken(), new Gson().toJson(jsonObject));
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
        Request request = new AuthorizedRequest(Method.ADD_TRUSTED_USER, client.getToken(), new Gson().toJson(jsonObject));
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridBagLayout());
        final JScrollPane scrollPane1 = new JScrollPane();
        GridBagConstraints gbc;
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 6;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollPane1, gbc);
        trustedUsersTable = new JTable();
        scrollPane1.setViewportView(trustedUsersTable);
        searchTextField = new JTextField();
        searchTextField.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 4;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(searchTextField, gbc);
        addUserButton = new JButton();
        addUserButton.setText("Add User");
        addUserButton.setVisible(true);
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        mainPanel.add(addUserButton, gbc);
        backButton = new JButton();
        backButton.setText("Back");
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(backButton, gbc);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
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

    @Override
    public void atRisk() {
        JOptionPane.showMessageDialog(mainPanel, "You are at risk of being infected with the virus. Please begin the quarantine immediately.", "Warning", JOptionPane.WARNING_MESSAGE);
    }
}
