package server.gamelogic;

public class Helper {

    /*
     * #################################################################################################################
     * CHECKING METHODS
     * #################################################################################################################
     */

    /**
     * Gets a player's name as a String and a players list and checks, if player with that playerName exists in list.
     *
     * @param players list of existing players
     * @param playerName playerName of the player that is assumed to be in the players list
     * @return true, if player exists and false, if not
     */
    public static boolean checkPlayerName(Player[] players, String playerName){
        boolean playerNameChecker = false;
        for (Player p : players){
            if (p.getUsername().equals(playerName)){
                playerNameChecker = true;
            }
        }
        return playerNameChecker;
    }

    /**
     * Gets an entries name a String and an entry sheet and checks if there exists an entry with that name.
     *
     * @param entryName name of the entry that is assumed to be in an entry sheet
     * @return true, if entry with this name exists, false if not
     */
    public static boolean checkEntryName(String entryName){
        boolean entryNameChecker = false;
        // default entry sheet that is only used to check the entry names
        EntrySheet entrySheet = new EntrySheet(new Player("Default Dan", 0));
        for (Entry e : entrySheet.getAsArray()){
            if (e.getName().equals(entryName)){
                entryNameChecker = true;
            }
        }
        return entryNameChecker;
    }

    /**
     * Gets an action name and checks if it is an existing ection.
     *
     * @param actionName name that is assumed to be an action
     * @return true, if name is an existing action, false if not
     */
    public static boolean checkActionName(String actionName){
        boolean actionNameChecker = false;
        boolean isSteal = actionName.equalsIgnoreCase("steal");
        boolean isFreeze = actionName.equalsIgnoreCase("freeze");
        boolean isCrossOut = actionName.equalsIgnoreCase("crossOut");
        boolean isSwitchEntries = actionName.equalsIgnoreCase("switchEntries");
        if (isSteal || isFreeze || isCrossOut || isSwitchEntries) {
            actionNameChecker = true;
        }
        return actionNameChecker;
    }

    /*
     * #################################################################################################################
     * GETTER FUNCTIONS
     * #################################################################################################################
     */

    /**
     * Gets an entry sheet list and looks through the list checking if there is an entry sheet associated with the players name.
     *
     * @param entrySheets list of all entry sheets
     * @param playerName name of player whose entry sheet we want from
     * @return entry sheet of player with name playerName
     * @throws Exception
     */
    public static EntrySheet getEntrySheetByName (EntrySheet[] entrySheets, String playerName) {
        EntrySheet playersEntrySheet = null;
        for (EntrySheet e : entrySheets) {
            if (e.getUsername().equals(playerName)){
                playersEntrySheet = e;
            }
        }
        return playersEntrySheet;
    }

}
