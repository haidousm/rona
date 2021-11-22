package com.haidousm.LAURona.server;

import com.haidousm.LAURona.enums.Status;

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
                response = login(request);
                break;
            case REGISTER:
                response = register(request);
                break;
            case GET_USER_INFO:
                response = getUserInfo(request);
                break;
            default:
                response = new Response();
                response.setStatus(Status.BAD_REQUEST);
                break;
        }
        return response;
    }

    private Response login(Request request) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        return response;
    }

    private Response register(Request request) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        return response;
    }

    private Response getUserInfo(Request request) {
        Response response = new Response();
        response.setStatus(Status.SUCCESS);
        return response;
    }

}
