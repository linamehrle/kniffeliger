package client.networking;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This class handles outgoing commands and messages from client to server.
 */
public class ClientOutput {
    private BufferedWriter out;

    /**
     * The constructor for ClientOutput, it starts the output stream to the server.
     * @param socket
     * @throws IOException
     */
    public ClientOutput(Socket socket) throws IOException {
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    /**
     * This method handles commands that are entered in the console.
     * @param message the whole String read from the console
     */
    public synchronized void sendFromConsoleIn(String message) {

        String[] input = message.split(" ", 2);

        if(input.length == 1) { //case: only command without message is entered

            switch (input[0]) {

                case "\\quit" -> sendToServer("QUIT goodbye!");
                case "\\showLobbies" -> sendToServer("LOLI show me all lobbies");
                default -> System.out.println("Invalid input entered");

            }

        } else if (input.length == 2) { //case: the command is followed by a message

            switch (input[0]) {

                case "\\changeUsername" -> sendToServer("CHNA " + input[1]);
                case "\\chat" -> sendToServer("CHAT " + input[1]);
                case "\\whisper" -> sendToServer("WHSP " + input[1]);
                case "\\newLobby" -> sendToServer("CRLO " + input[1]);
                case "\\enterLobby" -> sendToServer("ENLO " + input[1]);
                case "\\leaveLobby" -> sendToServer("LELO " + input[1]);
                default -> System.out.println("Invalid command or message entered: command " + input[0] + " message " + input[1]);
            }
        }

    }

    /**
     * This method handles internal commands to server.
     * @param cmd command as defined by the network protocol
     * @param message
     */
    public synchronized void send(CommandsClientToServer cmd, String message) {
        sendToServer(cmd.toString() + " " + message);
    }

    /**
     * This method writes the message to the server on the out-stream
     * @param message has to contain a command first, then a blank followed by a non-empty message
     */
    private synchronized void sendToServer(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method closes the output stream when the client disconnects.
     * @throws IOException
     */
    public void stop() throws IOException {
        out.close();
    }
}
