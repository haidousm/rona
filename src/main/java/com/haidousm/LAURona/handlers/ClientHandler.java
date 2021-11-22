package com.haidousm.LAURona.handlers;

import com.google.gson.Gson;
import com.haidousm.LAURona.enums.Status;
import com.haidousm.LAURona.requests.LoginRequest;
import com.haidousm.LAURona.requests.Request;
import com.haidousm.LAURona.response.Response;

import java.io.*;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private Socket socket;
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
                Request request = new Request(line);
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
                response = handleLogin(request);
                break;
            case REGISTER:
                response = handleRegister(request);
                break;
            case GET_USER_INFO:
                response = handleGetUserInfo(request);
                break;
            default:
                response = new Response();
                response.setStatus(Status.BAD_REQUEST);
                break;
        }
        return response;
    }

    private Response handleLogin(Request request) {
        Response response = new Response();
        try {
            LoginRequest loginRequest = new Gson().fromJson(request.getBody(), LoginRequest.class);
            response = login(loginRequest);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }

    private Response login(LoginRequest loginRequest) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        if (loginRequest.getUsername().equals("admin") && loginRequest.getPassword().equals("admin")) {
            response.setBody("{\"token\":\"admin\"}");
        } else {
            response.setStatus(Status.UNAUTHORIZED);
        }
        return response;
    }

    private Response handleRegister(Request request) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        return response;
    }

    private Response handleGetUserInfo(Request request) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        return response;
    }

}
