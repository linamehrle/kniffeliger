package server.gamelogic;

public class ActionDice {

    // name of action player can perform
    private String actionName;

    // saves action dice per default
    private boolean savingStatus;

    /**
     * Constructor that builds action dice with specific name. Name is later important to play the right dice/method.
     *
     * @param actionName takes specific action name that is important for action to be played
     */
    public ActionDice(String actionName) {
        this.actionName = actionName;
    }

    /*
     * #################################################################################################################
     * HANDLES ACTION DICE METHODS
     * ATTENTION: METHODS RETURN MOSTLY STRINGS SO WE CAN PLAY IT IN CONSOLE
     * #################################################################################################################
     */

    /**
     * Gets name of action a player can use.
     *
     * @return name of an action
     */
    public String getActionName() {
        return actionName;
    }

    /**
     * METHOD RETURNS STRING SO WE CAN PLAY IT IN CONSOLE ##############################################################
     * Lets you steal an entry from another player. Returns String with message, if entry could be stolen.
     *
     * @param entrySheetVillain player that steals entry
     * @param entrySheetVictim player that entry is stolen from
     * @param stolenEntry entry that thief wants to steal
     */
    public static String steal (EntrySheet entrySheetVillain, EntrySheet entrySheetVictim, String stolenEntry) {
        // assigns entry sheets to victim and thief
        Entry[] entriesThief = entrySheetVictim.getAsArray();
        Entry[] entriesVictim = entrySheetVictim.getAsArray();
        String message = entrySheetVillain.getUsername() + ", ";
        for (int i = 0; i < EntrySheet.getEntrySheetLength(); i++){
            if (entriesVictim[i].getName().equals(stolenEntry) && !(entriesThief[i].getIsFinal())){
                entrySheetVillain.addEntry(entriesVictim[i]);
                message = "you successfully stole the entry " + entriesVictim[i].getName() + " from " + entrySheetVillain.getUsername() + ".";
            } else {
                message = "this is not a valid entry or your entry is already final/taken/crossed out.";
            }
        }
        return message;
    }

    /**
     * METHOD RETURNS STRING SO WE CAN PLAY IT IN CONSOLE ##############################################################
     * Freezes (aka blocks) a player from choosing a specific combination as an entry. If, for example, the full house
     * gets frozen, the victim cannot choose this entry for next round.
     *
     * @return message for game in console
     */
    public static String freeze (EntrySheet entrySheetVictim, String frozenEntry){
        String message = "";
        for (Entry entry : entrySheetVictim.getAsArray()){
            if (entry.getName().equals(frozenEntry)){
                entry.setFrozenStatus(true);
                message = message + "The entry of " + entrySheetVictim.toString() + " has successfully been frozen.";
            } else {
                message = message + "Invalid entry. Try again.";
            }
        }
        return message;
    }

    /**
     * METHOD RETURNS STRING SO WE CAN PLAY IT IN CONSOLE ##############################################################
     * Crosses out/deletes entry from another player.
     *
     * @param entrySheetVictim entry sheet of victim
     * @param crossedOutEntry entry to be deleted
     * @return message for game in console
     */
    public static String crossOut(EntrySheet entrySheetVictim, String crossedOutEntry) {
        entrySheetVictim.deleteEntry(entrySheetVictim.getEntryByName(crossedOutEntry));
        return "You crossed out " + crossedOutEntry + " in " + entrySheetVictim.getUsername() + "'s entry sheet.";
    }

    /**
     * METHOD RETURNS STRING SO WE CAN PLAY IT IN CONSOLE ##############################################################
     * Shifts all entries one player.
     *
     * @param playersSheets all entry sheets of players as EntrySheet[]-array.
     * @return message for game in console
     */
    public static String shift(EntrySheet[] playersSheets){
        Player helper = playersSheets[0].getPlayer();
        for (int i = 0; i < playersSheets.length - 1; i++) {
            playersSheets[i].setPlayer(playersSheets[i + 1].getPlayer());
        }
        playersSheets[playersSheets.length - 1].setPlayer(helper);
        return "You successfully shifted the entry sheets.";
    }

    /**
     * METHOD RETURNS STRING SO WE CAN PLAY IT IN CONSOLE ##############################################################
     * Switches entry sheets of two players
     *
     * @param entrySheetVillain entry sheet of player who steals the entry.
     * @param entrySheetVictim entry sheet that gets stolen
     * @return message for game in console
     */
    public static String switchEntries(EntrySheet entrySheetVillain, EntrySheet entrySheetVictim){
        Player helper = entrySheetVillain.getPlayer();
        entrySheetVillain.setPlayer(entrySheetVictim.getPlayer());
        entrySheetVictim.setPlayer(helper);
        return entrySheetVillain.getUsername() + ", yu successfully switched your entry sheets with " + entrySheetVictim.getUsername();
    }

}