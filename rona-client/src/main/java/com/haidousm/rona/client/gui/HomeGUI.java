package com.haidousm.rona.client.gui;

import com.haidousm.rona.client.client.Client;

import javax.swing.*;

public class HomeGUI extends JFrame {
    private JLabel welcomeNameLabel;
    private JPanel mainPanel;

    public HomeGUI(String name, int width, int height, Client client) {
        super(name);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(mainPanel);

    }
}
