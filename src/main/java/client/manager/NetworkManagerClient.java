package client.manager;

import client.networking.CommandsClientToServer;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class NetworkManagerClient {
    private BufferedWriter out;

    public NetworkManagerClient(Socket socket) throws IOException {

        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

    }

    public synchronized void send(CommandsClientToServer cmd, String message) {

        switch (cmd) {

            case CHNA -> sendToServer("CHNA " + message);
            default -> System.out.println("unknown command to send from client to server " + message);

        }
    }

    private synchronized void sendToServer(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
            System.out.println("Sent a message to server");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
