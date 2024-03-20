package com.trybisz;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(2137)){
            System.out.println("[Server] Listening at " + server.getLocalPort());
            while(true) {
                Socket socket = server.accept();
                System.out.println("[Server] Accepted connection from client at port " + socket.getPort());
                new Thread(new ServerWorker(socket)).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
