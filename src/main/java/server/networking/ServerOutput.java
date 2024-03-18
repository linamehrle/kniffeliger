package server.networking;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ServerOutput {

    private BufferedWriter out;

    public ServerOutput(Socket socket) {

        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized void send(CommandsServerToClient cmd, String message) {

        switch (cmd) {

            case CHNA -> sendToClient("CHNA " + message);
            case QUIT -> sendToClient("QUIT " + message);
            case PING -> sendToClient("PING " + message);
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

    public void stop() throws IOException {
        out.close();
    }
}
