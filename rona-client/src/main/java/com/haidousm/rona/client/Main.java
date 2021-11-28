package com.haidousm.rona.client;


import com.haidousm.rona.client.gui.ConnectServerGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame connectServerGUI = new ConnectServerGUI("Rona", 500, 500);
        connectServerGUI.setVisible(true);
    }
}
