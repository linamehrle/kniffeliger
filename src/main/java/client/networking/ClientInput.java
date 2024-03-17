package client.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import client.manager.GameManager;


public class ClientInput implements Runnable {

    private Socket socket;
    private GameManager gameManager;

    public ClientInput(Socket socket, GameManager gameManager) {
        this.socket = socket;
        this.gameManager = gameManager;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while (true) {
                String message = in.readLine();
                ClientInputHelper processor = new ClientInputHelper(gameManager, message);
                Thread processorThread = new Thread(processor);
                processorThread.run();
            }

            //in.close();

        } catch (
                Exception e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
