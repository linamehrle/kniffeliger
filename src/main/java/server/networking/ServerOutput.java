package server.networking;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ServerOutput {

    private BufferedWriter out;
    //private DataOutputStream out;

    public ServerOutput(Socket socket) {

        try {
            //out = new DataOutputStream(socket.getOutputStream());
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized void send(CommandsServerToClient cmd, String message) {

        switch (cmd) {

            case CHNA -> sendToClient("CHNA " + message);
            case QUIT -> sendToClient("QUIT " + message);
            case PING -> sendToClient("PING " + message);
            case PONG -> sendToClient("PONG " + message);
            case CHAT -> sendToClient("CHAT " + message);
            case BRCT -> sendToClient("BRCT " + message);
            default -> System.out.println("unknown command to send from server to client " + message);

        }
    }

    private synchronized void sendToClient(String message) {
        try {
            out.write(message);
            out.newLine();
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //where did we use this again?
    public void stop() throws IOException {
        out.close();
    }
}
