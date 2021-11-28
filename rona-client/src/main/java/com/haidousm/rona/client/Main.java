package com.haidousm.rona.client;


import com.haidousm.rona.client.gui.ConnectServerGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame connectServerGUI = new ConnectServerGUI("Rona");
        connectServerGUI.setVisible(true);
    }
}
