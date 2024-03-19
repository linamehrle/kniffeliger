package client.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import client.manager.GameManager;


public class ClientInput implements Runnable {

    private boolean stop;

    private Socket socket;
    private GameManager gameManager;

    public ClientInput(Socket socket, GameManager gameManager) {
        this.stop = false;
        this.socket = socket;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));

            while (!stop) {
                if(in.ready()) {
                    String message = in.readLine();
                    ClientInputHelper processor = new ClientInputHelper(gameManager, message);
                    Thread processorThread = new Thread(processor);
                    processorThread.start();
                }
            }

            in.close();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        stop = true;
    }
}
