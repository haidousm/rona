package com.haidousm.rona.server;

import com.haidousm.rona.server.server.Server;

public class Main {
    public static void main(String[] args) {
        Server server = new Server(1234);
        server.start();
    }
}
