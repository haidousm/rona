package com.haidousm.rona.server.handlers;

import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.requests.RequestFactory;
import com.haidousm.rona.common.responses.GenericResponse;
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
                request.setIPAddress(socket.getInetAddress().getHostAddress());
                request.setPort(socket.getPort());
                GenericResponse genericResponse = handleRequest(request);
                bufferedWriter.write(genericResponse.toString());
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

    private GenericResponse handleRequest(Request request) {
        GenericResponse genericResponse;
        switch (request.getMethod()) {
            case LOGIN:
                genericResponse = LoginHandler.handleLogin(request);
                break;
            case REGISTER:
                genericResponse = RegisterHandler.handleRegister(request);
                break;
            case GET_CURRENT_USER:
                genericResponse = UserDetailsHandler.handleGetCurrentUserDetails(request);
                break;
            case GET_USER:
                genericResponse = UserDetailsHandler.handleGetUserDetails(request);
                break;
            case GET_ALL_USERS:
                genericResponse = UserDetailsHandler.handleGetAllUserDetails();
                break;
            default:
                genericResponse = new GenericResponse();
                genericResponse.setStatus(Status.BAD_REQUEST);
                break;
        }
        return genericResponse;
    }
}
