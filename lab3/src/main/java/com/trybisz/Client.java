package com.trybisz;

import java.io.*;
import java.net.Socket;

public class Client {
    public static void main(String[] args) {
        try {
            Socket sock = new Socket("localhost", 2137);
            ObjectOutputStream out = new ObjectOutputStream(sock.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(sock.getInputStream());
            System.out.println("[Client] Connected to server at port " + sock.getPort());
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            if(in.readObject().equals("ready"))
                System.out.println("[Client] Server is ready, enter number of messages to send: ");
            else
                throw new RuntimeException("[Client] Server is not ready");
            int numMessages = Integer.parseInt(reader.readLine());
            out.writeObject(numMessages);
            System.out.println("[Client] Sent number of messages to server: " + numMessages);
            if(in.readObject().equals("ready for messages"))
                System.out.println("[Client] Server confirmed readiness to receive messages");
            else
                throw new RuntimeException("[Client] Server is not ready to receive messages");
            for(int i = 0; i < numMessages; i++) {
                String content = reader.readLine();
                Message message = new Message(i, content);
                out.writeObject(message);
                System.out.println("[Client] Sent message to server");
            }
            System.out.println("[Client] Waiting for server confirmation of receipt of all messages");
            if(in.readObject().equals("finished"))
                System.out.println("[Client] Server confirmed receipt of all messages");
            else
                throw new RuntimeException("[Client] Server did not confirm receipt of all messages");
            sock.close();
            System.out.println("[Client] Connection closed");
        } catch (IOException | ClassNotFoundException e ) {
            throw new RuntimeException(e);
        }
    }
}