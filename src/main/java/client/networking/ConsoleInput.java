package client.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This is the thread that reads input from the console.
 */
public class ConsoleInput implements Runnable {
    Logger logger = Starter.getLogger();

    /**
     * This variable indicates whether the thread is running. It will be set true when the client disconnects.
     */
    private boolean stop = false;
    ClientOutput clientOutput;

    /**
     * constructor for ConsoleInput
     * @param networkManager
     */
    public ConsoleInput(ClientOutput networkManager) {
        this.clientOutput = networkManager;
    }

    /**
     * The run() method for the thread. It starts a BufferedReader to read the input from the console. The reader
     * reads a line and passes it along to the ClientInputHelper. The method runs as long as stop is false.
     */
    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        try {
            while (!stop) {
                if(in.ready()) {
                    String message = in.readLine();
                    clientOutput.sendFromConsoleIn(message);
                }
            }
        } catch(IOException e) {
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
