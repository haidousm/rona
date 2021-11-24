package com.haidousm.rona;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Main {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("0.0.0.0", 1234);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        out.println("LOGIN;;{'username':'admin','password':'admin'}");
        out.flush();
        byte[] buffer = new byte[1024];
        int read = socket.getInputStream().read(buffer);
        System.out.println(new String(buffer, 0, read));
        socket.close();
    }
}
