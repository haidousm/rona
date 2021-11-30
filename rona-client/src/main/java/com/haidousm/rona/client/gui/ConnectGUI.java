package com.haidousm.rona.client.gui;

import com.haidousm.rona.client.client.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class ConnectGUI extends JFrame {
    private JLabel welcomeMessage;
    private JPanel mainPanel;
    private JTextField ipAddressTextField;
    private JTextField portNumberTextField;
    private JButton connectButton;

    public ConnectGUI(String title) {
        super(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(mainPanel);
        this.pack();
        setLocationRelativeTo(null);

        connectButton.addActionListener(e -> {
            handleConnectClicked();
        });
    }

    private void handleConnectClicked() {
        String ipAddress = ipAddressTextField.getText();
        String portNumber = portNumberTextField.getText();
        if (ipAddress.isEmpty() || portNumber.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter IP address and port number.");
        } else {
            int port = Integer.parseInt(portNumber);
            if (port < 0 || port > 65535) {
                JOptionPane.showMessageDialog(this, "Port number must be between 0 and 65535.");
            }
            try {
                Client client = new Client(ipAddress, port);
                JFrame AuthGUI = new AuthGUI("Rona", client);
                AuthGUI.setVisible(true);
                this.dispose();
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to connect to server.");
            }

        }
    }

    @Override
    public Insets getInsets() {
        return new Insets(10, 10, 10, 10);
    }

}
