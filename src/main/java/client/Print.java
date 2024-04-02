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

    /**
     * Prints the current actions the player has acquired to the terminal
     * @param actions must have the form "ActionName: number, ActionName: number, ... "
     */
    public static void printActions(String actions) {
        String[] splitActions = actions.split(",");
        for (int i = 0; i < splitActions.length; i++) {
            System.out.println(splitActions[i]);
        }
    }
}
