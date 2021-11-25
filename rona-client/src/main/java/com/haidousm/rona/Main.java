package com.haidousm.rona;


import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("0.0.0.0", 1234);
        InputStream in = socket.getInputStream();
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        registerDummyUser(in, out);
        loginDummyUser(in, out);
        loginDummyUser(in, out);
        socket.close();
    }

    public static void registerDummyUser(InputStream in, PrintWriter out) throws IOException {
        Path imagePath = Paths.get("/Users/moussa/Desktop/Fall 2021/CSC430/Project/LAU-CSC430-Final-Project/rona-client/src/main/java/com/haidousm/rona/profile-pic.png");
        String base64Image = encodeFileToBase64Binary(imagePath);
        out.println("REGISTER;;{'firstname':'Moussa','lastname':'Haidous','email':'moussa.haidous@lau.edu', 'username': 'haidousm', 'password':'123456','imageFile': '" + base64Image + "', 'isVaccinated': 'false', 'vaccineCertificateFile': ''}");
        out.flush();
        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        System.out.println(new String(buffer, 0, read));
    }

    public static void loginDummyUser(InputStream in, PrintWriter out) throws IOException {
        out.println("LOGIN;;{'username':'haidousm','password':'123456'}");
        out.flush();
        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        System.out.println(new String(buffer, 0, read));
    }

    public static void getAllUsers(InputStream in, PrintWriter out) throws IOException {
        out.println("GET_ALL_USERS");
        out.flush();
        byte[] buffer = new byte[1024];
        int read = in.read(buffer);
        System.out.println(new String(buffer, 0, read));
    }

    private static String encodeFileToBase64Binary(Path filepath) throws IOException {
        byte[] bytes = Files.readAllBytes(filepath);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
