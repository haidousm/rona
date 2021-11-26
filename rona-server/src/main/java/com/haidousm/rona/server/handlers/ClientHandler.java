package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.RequestFactory;
import com.haidousm.rona.common.responses.Response;
import com.haidousm.rona.common.enums.Status;

import java.io.*;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private final Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                Request request = RequestFactory.createRequest(line);
                Response response = handleRequest(request);
                bufferedWriter.write(response.toString());
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                bufferedWriter.close();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Response handleRequest(Request request) {
        Response response;
        switch (request.getMethod()) {
            case LOGIN:
                response = LoginHandler.handleLogin(request);
                break;
            case REGISTER:
                response = RegisterHandler.handleRegister(request);
                break;
            case GET_USER:
                response = UserDetailsHandler.handleGetUserDetails(request);
                break;
            case GET_ALL_USERS:
                response = UserDetailsHandler.handleGetAllUserDetails();
                break;
            default:
                response = new Response();
                response.setStatus(Status.BAD_REQUEST);
                break;
        }
        return response;
    }
}
