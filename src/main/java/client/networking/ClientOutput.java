package client.networking;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This class handles outgoing commands and messages from client to server.
 */
public class ClientOutput {
    private static Logger logger = Starter.getLogger();
    private static BufferedWriter out;

    /**
     * The constructor for ClientOutput, it starts the output stream to the server.
     * @param socket
     * @throws IOException
     */
    public ClientOutput(Socket socket) throws IOException {
        out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
    }

    /**
     * This method handles internal commands to server.
     * @param cmd command as defined by the network protocol
     * @param message
     */
    public static synchronized void send(CommandsClientToServer cmd, String message) {
        sendToServer(cmd.toString() + " " + message);
    }

    /**
     * This method writes the message to the server on the out-stream
     * @param message has to contain a command first, then a blank followed by a non-empty message
     */
    public static synchronized void sendToServer(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            logger.warn(e.getMessage());
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
