package com.haidousm.rona.server.server;


import com.haidousm.rona.server.handlers.ClientHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Server
 * Accepts new TCP connection and hands it to the ClientHandler Runnable
 */

public class Server {

    private ServerSocket serverSocket;

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
            serverSocket.setReuseAddress(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread clientThread = new Thread(clientHandler);
                clientThread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {

        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
