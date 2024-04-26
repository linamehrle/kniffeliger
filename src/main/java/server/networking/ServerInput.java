package server.networking;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This thread reads the input coming from the client.
 */
public class ServerInput implements Runnable {

    private Logger logger = Starter.getLogger();

    /**
     * This variable indicates whether the thread is running. It will be set true when the client disconnects.
     */
    private boolean stop;
    private Socket socket;
    private ClientThread clientThread;

    /**
     * Constructor for ServerInput
     * @param clientThread
     */
    public ServerInput(ClientThread clientThread) {
        this.stop = false;
        this.socket = clientThread.getSocket();
        this.clientThread = clientThread;
    }

    /**
     * The run() method for the thread. It starts a BufferedReader to read the input from the server. The reader
     * reads a line and passes it along to the ServerInputHelper. The method runs as long as stop is false.
     */
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
            logger.error(e.getMessage());
        }
    }

    /**
     * This method sets the variable stop as true. It is used to handle a disconnect.
     */
    public void stop() {
        stop = true;
    }
}
