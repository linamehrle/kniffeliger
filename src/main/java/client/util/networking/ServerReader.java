package client.util.networking;

import client.util.TerminalView;

import java.io.IOException;
import java.io.InputStream;

public class ServerReader implements Runnable {
    InputStream in;

    public ServerReader(InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        int length;
        byte[] b = new byte[100];

        try {
            while(true) {
                length = in.read(b);
                if(length == -1) {
                    break;
                }
                // print text
                System.out.write(b, 0, length);
            }
        } catch(IOException e) {
            TerminalView.printText(e.getMessage());
        }
    }
}
