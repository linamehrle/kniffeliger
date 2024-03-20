package client.networking;

import client.manager.GameManager;
import server.networking.CommandsServerToClient;

public class ClientInputHelper implements Runnable {
    GameManager gameManager;
    String message;
    Pong pong;
    ClientOutput clientOutput;

    public ClientInputHelper(GameManager gameManager, String message) {
        this.gameManager = gameManager;
        this.message = message;
        this.pong = gameManager.getPong();
        this.clientOutput = gameManager.getClientOutput();
    }

    @Override
    public void run() {

        String[] input = message.split(" ", 2);
        CommandsServerToClient cmd;

        try {
            cmd = CommandsServerToClient.valueOf(input[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Client: received invalid command " + input[0]);
            return;
        }

        switch (cmd) {

            case CHNA -> System.out.println("Your username is now " + input[1]);
            case QUIT -> gameManager.disconnect();
            case PING -> clientOutput.send(CommandsClientToServer.PONG, input[1]);
            case PONG -> pong.updatePong(input[1]);
            case CHAT -> System.out.println(input[1]);
            case PRNT -> System.out.println("Alfred: " + input[1]);
            default -> System.out.println("unknown command received from server " + message);
        }

    }
}
