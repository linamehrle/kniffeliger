package client;

/**
 * This class contains useful methods to print information from the server to the console
 * of the client
 */
public class Print {

    /**
     * This method prints a list of alle existing Lobbies and their status to the console
     * @param lobbies A string with lobby(status) lobby(status) ... from the server
     */
    public static void printLobbies(String lobbies) {

        if(lobbies.equals("")) {
            System.out.println("There are no lobbies yet");
            return;
        }

        String[] splitLobbie = lobbies.split(" ");

        System.out.println("The existing Lobbies are:");
        for (int i = 0; i < splitLobbie.length; i++) {
            System.out.println(splitLobbie[i]);
        }
    }
}
