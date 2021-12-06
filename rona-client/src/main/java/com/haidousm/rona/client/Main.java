package com.haidousm.rona.client;


import com.haidousm.rona.client.gui.ConnectPage;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        JFrame connectServerGUI = new ConnectPage("Rona");
        connectServerGUI.setVisible(true);
    }
}
