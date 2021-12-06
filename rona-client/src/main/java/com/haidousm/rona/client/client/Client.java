package com.haidousm.rona.client.client;

import com.haidousm.rona.client.controllers.ClientController;
import com.haidousm.rona.client.gui.NotificationGUI;
import com.haidousm.rona.common.entity.HealthStatus;
import com.haidousm.rona.common.enums.Health;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.requests.Request;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.utils.MiscUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private String ip;
    private int port;

    private ScheduledExecutorService locationExecutor;
    private ScheduledFuture<?> locationFuture;

    private ScheduledExecutorService healthStatusExecutor;
    private ScheduledFuture<?> healthStatusFuture;


    List<Integer[]> possibleCoords = new ArrayList<>() {
        {
            add(new Integer[]{0, 0});
            add(new Integer[]{0, 1});
        }
    };

    private String token = "";

    private NotificationGUI currentFrame;

    public Client(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        socket = new Socket(ip, port);
        in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

    }

    private void send(Request request) {
        GenericRequest genericRequest = new GenericRequest();
        genericRequest.setMethod(request.getMethod());
        genericRequest.setBody(MiscUtils.toJson(request));
        out.println(MiscUtils.toJson(genericRequest));
        out.flush();
    }

    private GenericResponse receive() {
        try {
            return MiscUtils.fromJson(in.readLine(), GenericResponse.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public GenericResponse sendAndReceive(Request request) {
        send(request);
        return receive();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

//    public void beginTransmittingLocation(int interval) {
//        Runnable transmitLocation = () -> {
//            if (!Thread.currentThread().isInterrupted()) {
//                Integer[] coords = possibleCoords.get(new Random().nextInt(possibleCoords.size()));
//                ClientController.updateUserLocation(this, coords);
//            }
//        };
//        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
//        this.locationFuture = exec.scheduleAtFixedRate(transmitLocation, 0, interval, java.util.concurrent.TimeUnit.SECONDS);
//        this.locationExecutor = exec;
//    }
//
//    public void stopTransmittingLocation() {
//        if (locationFuture != null) {
//            this.locationFuture.cancel(true);
//            this.locationExecutor.shutdown();
//        }
//    }

//    public void pollHealthStatus(int interval) {
//        Runnable pollHealthStatus = () -> {
//            if (!Thread.currentThread().isInterrupted()) {
//                HealthStatus healthStatus = ClientController.getUserHealthStatus(this);
//                if (healthStatus.getStatus() == Health.AT_RISK || healthStatus.getStatus() == Health.CONTAGIOUS) {
//
//                    this.currentFrame.atRisk();
//                    this.stopPollingHealthStatus();
//                    Thread.currentThread().interrupt();
//                }
//            }
//
//        };
//        ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
//        this.healthStatusExecutor = exec;
//        this.healthStatusFuture = exec.scheduleAtFixedRate(pollHealthStatus, 0, interval, java.util.concurrent.TimeUnit.SECONDS);
//    }
//
//    public void stopPollingHealthStatus() {
//        if (this.healthStatusFuture != null) {
//            this.healthStatusFuture.cancel(true);
//            this.healthStatusExecutor.shutdown();
//        }
//    }

    public void setCurrentFrame(NotificationGUI currentFrame) {
        this.currentFrame = currentFrame;
    }

    public NotificationGUI getCurrentFrame() {
        return currentFrame;
    }
}
