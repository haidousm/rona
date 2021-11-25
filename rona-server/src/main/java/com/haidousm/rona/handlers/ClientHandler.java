package com.haidousm.rona.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haidousm.rona.entity.User;
import com.haidousm.rona.enums.Status;
import com.haidousm.rona.requests.LoginRequest;
import com.haidousm.rona.requests.RegisterUserRequest;
import com.haidousm.rona.requests.Request;
import com.haidousm.rona.requests.UserDetailsRequest;
import com.haidousm.rona.response.Response;
import com.haidousm.rona.utils.HibernateUtil;
import com.haidousm.rona.utils.MiscUtils;
import org.hibernate.Transaction;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class ClientHandler implements Runnable {
    private final Socket socket;
    private final Gson gson;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public ClientHandler(Socket socket) {
        this.socket = socket;
        this.gson = new GsonBuilder().create();
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
