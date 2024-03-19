package client.networking;

import client.networking.CommandsClientToServer;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * handles commands and messages from client to server
 */
public class ClientOutput {
    private BufferedWriter out;
    //DataOutputStream out;

    public ClientOutput(Socket socket) throws IOException {

        //out = new DataOutputStream(socket.getOutputStream());
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));

    }

    /**
     * handles commands that are entered in the console
     * @param message the whole String read from the console
     */
    public synchronized void sendFromConsoleIn(String message) {

        //TODO: what happens when " " or /newline is entered only, i.e. if input[0] doesnt exist
        //TODO: input in the console must not be the exact protocol

        String[] input = message.split(" ", 2);
        CommandsClientToServer cmd;

        /*try {
            cmd = CommandsClientToServer.valueOf(input[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid command " + input[0]);
            return;
        }*/

        //case: only command without message is entered
        if(input.length == 1) {

            switch (input[0]) {

                case "QUIT" -> sendToServer("QUIT goodbye!"); //TODO how to end the client in this case

            }


        //command with message is entered
        } else if (input.length == 2) {

            switch (input[0]) {

                case "CHNA" -> sendToServer("CHNA " + input[1]);
                case "CHAT" -> sendToServer("CHAT " + input[1]);

                //TODO: add other cases
            }

        } else {
            System.out.println("Invalid input");
        }

    }

    /**
     * handles internal commands to server
     * @param cmd command as defined by the network protocol
     * @param message
     */
    public synchronized void send(CommandsClientToServer cmd, String message) {

        switch (cmd) {

            case CHNA -> sendToServer("CHNA " + message);
            case PONG -> sendToServer("PONG " + message);
            default -> System.out.println("unknown command to send from client to server " + message);

        }
    }

    /**
     * writes the message to the server on the out-stream
     * @param message has to contain a command first, then a blank followed by a non-empty message
     */
    private synchronized void sendToServer(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
            /*if (!message.contains("PONG")) {
                System.out.println("message send to server: " + message);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() throws IOException {
        out.close();
    }
}
