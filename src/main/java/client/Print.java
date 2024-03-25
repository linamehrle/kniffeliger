package client;

/**
 * This class contains useful methods to print information from the server to the cnsole
 * of the client
 */
public class Print {

    public static void printLobbies(String lobbies) {
        String[] splitLobbie = lobbies.split(" ");

        System.out.println("The existing Lobbies are:");
        for (int i = 0; i < splitLobbie.length; i++) {
            System.out.println(splitLobbie[i]);
        }
    }
}
