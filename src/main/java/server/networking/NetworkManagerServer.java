package server.networking;

import client.util.TerminalView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManagerServer {
    private ServerSocket socket;
    private int ctr;

    public NetworkManagerServer(int port) {
        try {
            // establish connection
            socket = new ServerSocket(port);

            // connection established
            TerminalView.printlnText("Waiting for connection on " + port);

            while(true) {
                // wait for connections and create new thread
                Socket clientSocket = socket.accept();
                ClientThread client = new ClientThread(++ctr, clientSocket);
                Thread echoClientThread = new Thread(client);
                echoClientThread.start();
            }
        } catch (IOException e) {
            TerminalView.printlnText(e.getMessage());
        }
    }
}
