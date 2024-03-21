package server;

public class Server {
    NetworkManagerServer networkManager;

    public static void main(String[] args) {
        new Server(Integer.parseInt(args[0]));
    }

    public Server(int port) {
        networkManager = new NetworkManagerServer(port);
    }
}
