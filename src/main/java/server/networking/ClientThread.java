package server.networking;

import client.util.TerminalView;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientThread implements Runnable{
    private int id;
    private Socket socket;

    public ClientThread(int id, Socket socket) {
        this.id = id;
        this.socket = socket;
    }

    public void run() {
        // connection
        String msg = "EchoServer: Connection " + id;
        TerminalView.printText(msg);

        try {
            // create io streams
            InputStream in = socket.getInputStream();
            OutputStream out = socket.getOutputStream();

            // send welcome
            out.write(("Alfred: " + msg + "\r\n").getBytes());

            int c;
            while((c = in.read()) != -1) {
                out.write((char) c);
                System.out.print((char) c);
            }

            // terminate con
            TerminalView.printText("Terminate: " + id);
            socket.close();
        } catch (Exception e) {
            TerminalView.printText(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
