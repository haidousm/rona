package com.haidousm.rona.client;


import com.haidousm.rona.client.gui.ConnectGUI;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame connectServerGUI = new ConnectGUI("Rona");
        connectServerGUI.setVisible(true);
    }
}
