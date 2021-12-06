package com.haidousm.rona.server.handlers;

import com.google.gson.Gson;
import com.haidousm.rona.common.requests.GenericRequest;
import com.haidousm.rona.common.responses.GenericResponse;
import com.haidousm.rona.common.enums.Status;
import com.haidousm.rona.common.utils.MiscUtils;

import java.io.*;
import java.net.Socket;


/**
 * ClientHandler
 * Handles the client requests
 * Has 0 knowledge of the contents of the message, just the METHOD.
 */

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

                /*
                 * LINE looks like:
                 * {"method":"LOGIN", body: "jsonBody"}
                 * jsonBody looks like:
                 * {"username":"username", "password":"password"} for example
                 */

                GenericRequest request = MiscUtils.fromJson(line, GenericRequest.class);
                request.setIPAddress(socket.getInetAddress().getHostAddress());
                request.setPort(socket.getPort());

                /*
                 * GenericRequest looks like:
                 * - method: LOGIN
                 * - body: "{"username":"username", "password":"password"}"
                 */

                GenericResponse genericResponse = handleRequest(request);
                bufferedWriter.write(MiscUtils.toJson(genericResponse));
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

    private GenericResponse handleRequest(GenericRequest request) {
        /* We abstract away the contents of the request and only care about the method
         * The handlers are responsible for handling the request and returning a response
         * they are the ones that know the contents of the request
         */
        GenericResponse genericResponse;
        switch (request.getMethod()) {
            case LOGIN:
                genericResponse = LoginHandler.handleLogin(request);
                break;
            case REGISTER:
                genericResponse = RegisterHandler.handleRegister(request);
                break;
            case GET_USER:
                genericResponse = UserHandler.handleGetCurrentUserDetails(request);
                break;
            case FIND_USER_BY_USERNAME:
                genericResponse = UserHandler.handleGetUserDetails(request);
                break;
            case GET_ALL_USERS:
                genericResponse = UserHandler.handleGetAllUserDetails();
                break;
            case GET_HEALTH_STATUS:
                genericResponse = HealthStatusHandler.handleGetCurrentUserHealthStatus(request);
                break;
            case UPDATE_HEALTH_STATUS:
                genericResponse = HealthStatusHandler.handleUpdateCurrentUserHealthStatus(request);
                break;
            case GET_STATS:
                genericResponse = StatsHandler.handleGetStats(request);
                break;
            case GET_TRUSTED_USERS:
                genericResponse = TrustedUsersHandler.handleGetTrustedUsers(request);
                break;
            case GET_TRUSTED_BY_USERS:
                genericResponse = TrustedUsersHandler.handleGetTrustedByUsers(request);
                break;
            case UPDATE_USER_LOCATION:
                genericResponse = LocationHandler.handleUpdateUserLocation(request);
                break;
            case ADD_TRUSTED_USER:
                genericResponse = TrustedUsersHandler.handleAddTrustedUser(request);
                break;
            case REMOVE_TRUSTED_USER:
                genericResponse = TrustedUsersHandler.handleRemoveTrustedUser(request);
                break;
            default:
                genericResponse = new GenericResponse();
                genericResponse.setStatus(Status.BAD_REQUEST);
                break;
        }
        return genericResponse;
    }
}
