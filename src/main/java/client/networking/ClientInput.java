package client.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import client.GameManager;

/**
 * This is the thread that reads input from the server.
 */
public class ClientInput implements Runnable {

    /**
     * This variable indicates whether the thread is running. It will be set true when the client disconnects.
     */
    private boolean stop = false;
    private Socket socket;
    private GameManager gameManager;

    /**
     * Constructor for ClientInput
     * @param socket
     * @param gameManager
     */
    public ClientInput(Socket socket, GameManager gameManager) {
        this.socket = socket;
        this.gameManager = gameManager;
    }

    /**
     * The run() method for the thread. It starts a BufferedReader to read the input from the server. The reader
     * reads a line and passes it along to the ClientInputHelper. The method runs as long as stop is false.
     */
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

    /**
     * This method sets the variable stop as true. It is used to handle a disconnect.
     */
    public void stop() {
        stop = true;
    }
}
