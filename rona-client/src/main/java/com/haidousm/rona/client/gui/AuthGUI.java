package com.haidousm.rona.client.gui;

import com.haidousm.rona.client.client.Client;
import com.haidousm.rona.client.controllers.UserController;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.AuthResponse;
import com.haidousm.rona.common.responses.builders.AuthResponseBuilder;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AuthGUI extends JFrame {
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel mainPanel;
    private JButton uploadImageButton;
    private JCheckBox isVaccinatedCheckbox;
    private JButton uploadPDFButton;
    private JTextField emailTextField;
    private JTextField firstNameTextField;
    private JTextField lastNameTextField;
    private JPasswordField registerPasswordTextField;
    private JTextField registerUsernameTextField;

    private Path imageFilePath = Paths.get("");
    private Path pdfFilePath = Paths.get("");

    private final Client client;

    public AuthGUI(String title, Client client) {
        super(title);
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        this.client = client;

        isVaccinatedCheckbox.addActionListener(e -> {
            if (isVaccinatedCheckbox.isSelected()) {
                uploadPDFButton.setEnabled(true);
            } else {
                uploadPDFButton.setEnabled(false);
            }
        });

        loginButton.addActionListener(e -> {
            loginClicked();
        });

        registerButton.addActionListener(e -> {
            registerClicked();
        });

        uploadImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new java.io.File("."));
            fileChooser.setDialogTitle("Select an image");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                imageFilePath = fileChooser.getSelectedFile().toPath();
            }
        });

        uploadPDFButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new java.io.File("."));
            fileChooser.setDialogTitle("Select a PDF");
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false);
            if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                pdfFilePath = fileChooser.getSelectedFile().toPath();
            }
        });

    }

    private void loginClicked() {
        String username = usernameTextField.getText();
        String password = new String(passwordTextField.getPassword());
        Request request = UserController.prepareLogin(username, password);
        client.send(request);
        AuthResponse response = AuthResponseBuilder.builder().build(client.receive());
        if (response.getStatus() == Status.SUCCESS) {
            goToHome(client, response);
        } else {
            JOptionPane.showMessageDialog(this, "Login failed");
        }
    }

    private void registerClicked() {
        String username = registerUsernameTextField.getText();
        String password = new String(registerPasswordTextField.getPassword());
        String email = emailTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        boolean isVaccinated = isVaccinatedCheckbox.isSelected();
        Request request = UserController.prepareRegisterUser(firstName, lastName, email, username, password, isVaccinated, pdfFilePath, imageFilePath);
        client.send(request);
        AuthResponse response = AuthResponseBuilder.builder().build(client.receive());
        if (response.getStatus() == Status.SUCCESS) {
            goToHome(client, response);
        } else {
            JOptionPane.showMessageDialog(this, "Register failed");
        }
    }

    private void goToHome(Client client, AuthResponse response) {
        dispose();
        HomeGUI homeGUI = new HomeGUI("Home", client, response);
        homeGUI.setVisible(true);
    }


}
