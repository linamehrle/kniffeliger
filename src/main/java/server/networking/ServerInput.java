package server.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ServerInput implements Runnable {

    private Socket socket;
    private ClientThread client;

    public ServerInput(Socket socket, ClientThread client) {
        this.socket = socket;
        this.client = client;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String message;

            while (true) {
                if(in.ready()) {
                    message = in.readLine();
                    ServerInputHelper processor = new ServerInputHelper(client, message);
                    Thread processorThread = new Thread(processor);
                    processorThread.start();
                }
            }

            //in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
