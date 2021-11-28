package com.haidousm.rona.client.gui;

import com.haidousm.rona.client.client.Client;
import com.haidousm.rona.client.controllers.UserController;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.requests.LoginRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.RequestFactory;
import com.haidousm.rona.common.requests.builders.LoginRequestBuilder;
import com.haidousm.rona.common.responses.LoginResponse;
import com.haidousm.rona.common.responses.RegisterResponse;
import com.haidousm.rona.common.responses.builders.LoginResponseBuilder;
import com.haidousm.rona.common.responses.builders.RegisterResponseBuilder;
import com.haidousm.rona.common.utils.MiscUtils;

import javax.swing.*;
import java.nio.file.Path;
import java.nio.file.Paths;

public class AuthGUI extends JFrame {
    private JTextField usernameTextField;
    private JPasswordField passwordTextField;
    private JButton loginButton;
    private JButton registerButton;
    private JPanel mainPane;
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

    public AuthGUI(String title, Client client) {
        super(title);
        setContentPane(mainPane);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();

        isVaccinatedCheckbox.addActionListener(e -> {
            if (isVaccinatedCheckbox.isSelected()) {
                uploadPDFButton.setEnabled(true);
            } else {
                uploadPDFButton.setEnabled(false);
            }
        });

        loginButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = new String(passwordTextField.getPassword());
            Request request = UserController.prepareLogin(username, password);
            client.send(request);
            LoginResponse response = LoginResponseBuilder.builder().build(client.receive());
            if (response.getStatus() == Status.SUCCESS) {
                System.out.println("Login success");
            } else {
                JOptionPane.showMessageDialog(this, "Login failed");
            }
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

        registerButton.addActionListener(e -> {
            String username = registerUsernameTextField.getText();
            String password = new String(registerPasswordTextField.getPassword());
            String email = emailTextField.getText();
            String firstName = firstNameTextField.getText();
            String lastName = lastNameTextField.getText();
            boolean isVaccinated = isVaccinatedCheckbox.isSelected();
            Request request = UserController.prepareRegisterUser(firstName, lastName, email, username, password, isVaccinated, pdfFilePath, imageFilePath);
            client.send(request);
            RegisterResponse response = RegisterResponseBuilder.builder().build(client.receive());
            if (response.getStatus() == Status.SUCCESS) {
                System.out.println("Register success");
            } else {
                JOptionPane.showMessageDialog(this, "Register failed");
            }
        });

    }


}
