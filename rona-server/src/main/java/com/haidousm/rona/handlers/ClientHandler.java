package com.haidousm.rona.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.haidousm.rona.entity.User;
import com.haidousm.rona.enums.Status;
import com.haidousm.rona.requests.LoginRequest;
import com.haidousm.rona.requests.Request;
import com.haidousm.rona.requests.UserDetailsRequest;
import com.haidousm.rona.response.Response;
import com.haidousm.rona.utils.HibernateUtil;
import org.hibernate.Transaction;

import java.io.*;
import java.net.Socket;


public class ClientHandler implements Runnable {
    private Socket socket;
    private Gson gson;
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
                response = handleLogin(request);
                break;
            case REGISTER:
                response = handleRegister(request);
                break;
            case GET_USER:
                response = handleGetUserDetails(request);
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
            LoginRequest loginRequest = gson.fromJson(request.getBody(), LoginRequest.class);
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

    private Response handleGetUserDetails(Request request) {
        Response response = new Response();
        try {
            UserDetailsRequest userDetailsRequest = gson.fromJson(request.getBody(), UserDetailsRequest.class);
            response = getUserDetails(userDetailsRequest);
        } catch (Exception e) {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }

    private Response getUserDetails(UserDetailsRequest userDetailsRequest) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        if (userDetailsRequest.getID() != 0) {
            Transaction tx = HibernateUtil.beginTransaction();
            User user = HibernateUtil.getSession().get(User.class, userDetailsRequest.getID());
            tx.commit();
            response.setBody(gson.toJson(user));
        } else if (userDetailsRequest.getUsername() != null) {
            Transaction tx = HibernateUtil.beginTransaction();
            User user = HibernateUtil.getSession().createQuery("from User where username = :username", User.class)
                    .setParameter("username", userDetailsRequest.getUsername()).getSingleResult();
            tx.commit();
            response.setBody(gson.toJson(user));
        } else if (userDetailsRequest.getEmail() != null) {
            Transaction tx = HibernateUtil.beginTransaction();
            User user = HibernateUtil.getSession().createQuery("from User where email = :email", User.class)
                    .setParameter("email", userDetailsRequest.getEmail()).getSingleResult();
            tx.commit();
            response.setBody(gson.toJson(user));

        } else {
            response.setStatus(Status.BAD_REQUEST);
        }
        return response;
    }
}
