package client.networking;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import client.manager.NetworkManagerClient;

public class ConsoleInput implements Runnable {

    NetworkManagerClient networkManager;

    public ConsoleInput(NetworkManagerClient networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        try {
            while (true) {
                String message = in.readLine();

                // Hier consoleIn splitten in Command und Message Teil.
                // Dann Command zu inum mit valueOf oder so an network manager übergeben
                String[] input = message.split(" ", 2);
                CommandsClientToServer cmd;

                try {
                    cmd = CommandsClientToServer.valueOf(input[0].toUpperCase());
                } catch (IllegalArgumentException e) {
                    System.out.println("Alfred: received invalid command " + input[0]);
                    return;
                }

                // if quit was entered
                if (message.equalsIgnoreCase("QUIT")) {
                    // quit an server geben
                    break;
                }

                networkManager.send(cmd, input[1]);
            }
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
