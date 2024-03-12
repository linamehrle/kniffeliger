package client.manager;

import client.util.TerminalView;
import client.util.networking.ServerReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkManagerClient {
    private Socket socket;
    private InputStream in;
    private OutputStream out;

    private ServerReader serverReader;

    public NetworkManagerClient(String hostName, int port) {
        try {
            // create server socket
            socket = new Socket(hostName, port);

            // create io streams
            in = socket.getInputStream();
            out = socket.getOutputStream();

            // create input thread
            ServerReader th = new ServerReader(in);
            Thread iT = new Thread(th);
            iT.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendToServer(String msg) {
        try {
            out.write(msg.getBytes());

            String end = "\r\n";
            out.write(end.getBytes());
        } catch (IOException e) {
            TerminalView.printText(e.getMessage());
        }
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            TerminalView.printText(e.getMessage());
        }
    }
}
