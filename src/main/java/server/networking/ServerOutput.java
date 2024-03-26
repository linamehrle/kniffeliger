package server.networking;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * This class handles outgoing commands and messages from server to client.
 */
public class ServerOutput {

    private BufferedWriter out;

    /**
     * The constructor for ServerOutput, it starts the output stream to the client.
     * @param socket
     */
    public ServerOutput(Socket socket) {

        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This message sends a command with message to the client
     * @param cmd
     * @param message
     */
    public synchronized void send(CommandsServerToClient cmd, String message) {
        sendToClient(cmd.toString() + " " + message);
    }

    /**
     * This method writes the message to the client on the out-stream
     * @param message has to contain a command first, then a blank followed by a non-empty message
     */
    private synchronized void sendToClient(String message) {
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
