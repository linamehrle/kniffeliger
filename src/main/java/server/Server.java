package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import server.networking.ClientThread;

public class Server {

    private ServerSocket socket;

    public static void main(String[] args) {
        new Server(Integer.parseInt(args[0]));
    }

    public Server(int port) {
        try {
            // establish connection
            socket = new ServerSocket(port);

            // connection established
            System.out.println("Waiting for connection on " + port);

            while(true) {
                // wait for connections and create new thread
                Socket clientSocket = socket.accept();
                ClientThread client = new ClientThread(clientSocket);
                Thread clientThread = new Thread(client);
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
