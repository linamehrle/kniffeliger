package server.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerInput implements Runnable {

    private boolean stop;
    private Socket socket;
    private ClientThread clientThread;

    public ServerInput(ClientThread clientThread) {
        this.stop = false;
        this.socket = clientThread.getSocket();
        this.clientThread = clientThread;
    }

    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            String message;

            while (!stop) {
                if(in.ready()) {
                    message = in.readLine();
                    //System.out.println("received message: " + message);
                    ServerInputHelper processor = new ServerInputHelper(clientThread, message);
                    Thread processorThread = new Thread(processor);
                    processorThread.start();
                }
            }

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stop = true;
    }
}
