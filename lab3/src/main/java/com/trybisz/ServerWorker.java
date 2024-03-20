package com.trybisz;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerWorker implements Runnable{
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;
    public ServerWorker (Socket socket) {
        this.socket = socket;
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void run() {
        try {
            System.out.println("[ServerWorker] Connected to client at port " + socket.getPort());
            out.writeObject("ready");
            int numMessages = (int) in.readObject();
            System.out.println("[ServerWorker] Received number of messages from client: " + numMessages);
            out.writeObject("ready for messages");
            for(int i = 0; i < numMessages; i++) {
                Message message = (Message) in.readObject();
                System.out.println("[ServerWorker] Received message from client: " + message);
            }
            out.writeObject("finished");
            System.out.println("[ServerWorker] Sent confirmation of receipt of all messages to client");
            socket.close();
            System.out.println("[ServerWorker] Connection closed");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
