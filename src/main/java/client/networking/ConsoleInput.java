package client.networking;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * this class reads the terminal input for the client
 */
public class ConsoleInput implements Runnable {

    private boolean stop;

    ClientOutput clientOutput;

    public ConsoleInput(ClientOutput networkManager) {
        this.stop = false;
        this.clientOutput = networkManager;
    }

    /**
     * the client input is send to ClientOutput to be handled
     */
    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
        //DataInputStream in = new DataInputStream(System.in);
        try {
            while (!stop) {
                if(in.ready()) {
                    String message = in.readLine();
                    //System.out.println("read the following message: " + message);
                    clientOutput.sendFromConsoleIn(message);
                }
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stop = true;
    }
}
