package client;

import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This class contains useful methods to print information from the server to the console
 * of the client
 */
public class Print {
    static Logger logger = Starter.logger;

    /**
     * This method prints a list of alle existing Lobbies and their status to the console
     * @param lobbies A string with lobby(status) lobby(status) ... from the server
     */
    public static void printLobbies(String lobbies) {

        if(lobbies.equals("")) {
            logger.info("There are no lobbies yet");
            return;
        }

        String[] splitLobbie = lobbies.split(",");

        System.out.println("The existing Lobbies are:");
        for (int i = 0; i < splitLobbie.length; i++) {
            String[] splitInLobbyAndPlayer = splitLobbie[i].split(":");
            int l = splitInLobbyAndPlayer.length;
            System.out.println(splitInLobbyAndPlayer[0] + ":");
            for (int j = 1; j < l; j++) {
                System.out.println(" " + splitInLobbyAndPlayer[j]);
            }
        }
    }

    /**
     * Prints the current entry sheet of a Player
     * @param entrySheet must have the form "Name username,EntryName: points,EntryName: points, ... "
     */
    public static void printEntrySheet(String entrySheet){
        String[] splitEntrySheet = entrySheet.split(",");
        System.out.println("##############################");
        System.out.println("Your Entry Sheet");
        System.out.println(splitEntrySheet[0]);
        System.out.println("##############################");
        for (int i = 1; i < splitEntrySheet.length; i++) {
            System.out.println(splitEntrySheet[i]);
        }
    }

    public static void printPlayerList(String playerList) {
        String[] players = playerList.split(",");

        System.out.println("The connected players are:");
        for (int i = 0; i < players.length; i++) {
            System.out.println(players[i]);
        }
    }

}
