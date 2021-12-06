package com.haidousm.rona.client.client;

import com.haidousm.rona.client.gui.Notifications;
import com.haidousm.rona.common.entity.HealthStatus;
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
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public class Client {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    private String token = "";
    private HealthStatus healthStatus;
    private Notifications currentFrame;

    private final ScheduledFuture<?> sixtySecondsFuture;
    private final ScheduledFuture<?> thirtySecondsFuture;

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(2);
        sixtySecondsFuture = executor.scheduleAtFixedRate(() -> {
            try {
                if (currentFrame != null) {
                    currentFrame.pollHealthStatus();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 30, java.util.concurrent.TimeUnit.SECONDS);

        thirtySecondsFuture = executor.scheduleAtFixedRate(() -> {
            try {
                if (currentFrame != null) {
                    currentFrame.transmitCurrentLocation();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, 0, 15, java.util.concurrent.TimeUnit.SECONDS);
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
            sixtySecondsFuture.cancel(true);
            thirtySecondsFuture.cancel(true);
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

    public HealthStatus getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(HealthStatus healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Integer[] getCurrentLocation() {
        List<Integer[]> possibleCoords = new ArrayList<>() {
            {
                add(new Integer[]{0, 0});
                add(new Integer[]{0, 1});
            }
        };
        return possibleCoords.get(new Random().nextInt(possibleCoords.size()));
    }

    public void setCurrentFrame(Notifications currentFrame) {
        this.currentFrame = currentFrame;
    }

    public Notifications getCurrentFrame() {
        return currentFrame;
    }

}
