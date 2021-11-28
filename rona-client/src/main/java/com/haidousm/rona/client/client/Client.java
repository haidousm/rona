package com.haidousm.rona.client.client;

import com.google.gson.Gson;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.RequestFactory;
import com.haidousm.rona.common.responses.GenericResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String ip;
    private int port;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        try {
            socket = new Socket(ip, port);
            in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(Request request) {
        out.println(RequestFactory.createRequestString(request));
        out.flush();
    }

    public GenericResponse receive() {
        try {
            return new Gson().fromJson(in.readLine(), GenericResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
