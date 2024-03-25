package server.gamelogic;

public class ActionDice {

    // name of action player can perform
    private String actionName;

    // saves action dice per default
    private boolean savingStatus;


    public ActionDice(String actionName){
        this.actionName = actionName;
    }

    /**
     * Gets name of action a player can use.
     *
     * @return name of an action
     */
    public String getActionName() {
        return actionName;
    }

    /*
     * #################################################################################################################
     * ACTION DICE
     * ATTENTION: METHODS RETURN MOSTLY STRINGS SO WE CAN PLAY IT IN CONSOLE
     * #################################################################################################################
     */
    /**
     * METHOD RETURNS STRING SO WE CAN PLAY IT IN CONSOLE ##############################################################
     * Lets you steal an entry from another player. Returns String with message, if entry could be stolen.
     *
     * @param entrySheetThief player that steals entry
     * @param entrySheetVictim player that entry is stolen from
     * @param stolenEntry entry that thief wants to steal
     */
    public static String steal (EntrySheet entrySheetThief, EntrySheet entrySheetVictim, Entry stolenEntry) {
        String message = entrySheetThief.getUsername() + ", ";
        for (int i = 0; i < EntrySheet.getEntrySheetLength(); i++){
            if (entrySheetVictim.getAsArray()[i].getName().equals(stolenEntry.getName())){
                entrySheetThief.addEntry(stolenEntry);
                message = "you successfully stole the entry " + stolenEntry.getName() + " from " + entrySheetThief.getUsername() + ".";
            } else {
                message = "this is not a valid entry.";
            }
        }
        return message;
    }

    /**
     * METHOD RETURNS STRING SO WE CAN PLAY IT IN CONSOLE ##############################################################
     * Freezes (aka blocks) a player from choosing a specific combination as an entry. If, for example, the full house
     * gets frozen, the victim cannot choose this entry for next round.
     *
     * @return String message if entry is successfully blocked
     */
    public static String freeze (EntrySheet entrySheetVictim, Entry frozenEntry){
        String message = "";
        for (Entry entry : entrySheetVictim.getAsArray()){
            if (entry.getName().equals(frozenEntry.getName())){
                entry.setFrozenStatus(true);
                message = message + "The entry of " + entrySheetVictim.toString() + " has successfully been frozen.";
            } else {
                message = message + "Invalid entry.";
            }
        }
        return message;
        // TODO: unfreeze after it has been frozen once
    }

    // TODO: shift, switch, cross out, all of the above


}