package client.gui;

import client.networking.ClientOutput;

/**
 * Class to launch ChatWindow as new Thread
 */
public class CWLauncher {
    public CWLauncher(ClientOutput clientOutput) {
        ChatWindow application = new ChatWindow();
        ChatWindow.setNetworkManager(clientOutput);
        new Thread(application).start();
    }


}
