package com.haidousm.rona;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Main {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("0.0.0.0", 1234);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        Path imagePath = Paths.get("/Users/moussa/Desktop/Fall 2021/CSC430/Project/LAU-CSC430-Final-Project/rona-client/src/main/java/com/haidousm/rona/profile-pic.png");
        String base64Image = encodeFileToBase64Binary(imagePath);
        out.println("REGISTER;;{'firstname':'Moussa','lastname':'Haidous','email':'moussa.haidous@lau.edu', 'username': 'haidousm', 'password':'123456','imageFile': '" + base64Image + "', 'isVaccinated': 'false', 'vaccineCertificateFile': ''}");
        out.flush();
        byte[] buffer = new byte[1024];
        int read = socket.getInputStream().read(buffer);
        System.out.println(new String(buffer, 0, read));
        getAllUsers(socket.getInputStream(), out);
        socket.close();
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
