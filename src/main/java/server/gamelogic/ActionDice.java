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
     * #################################################################################################################
     */
    /**
     * Lets you steal an entry from another player.
     *
     * @param entrySheetThief person you
     * @param entrySheetVictim
     * @param stolenEntry
     */
    public static void steal (EntrySheet entrySheetThief, EntrySheet entrySheetVictim, Entry stolenEntry) {
        Entry newEntry = stolenEntry;
        for (Entry entry : entrySheetVictim.getEntrySheetAsArray()) {
            if (entry.getName().equals(stolenEntry.getName())) {
                newEntry = entry;
                try {
                    entrySheetVictim.deleteEntry(stolenEntry);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
        for (Entry entry : entrySheetThief.getEntrySheetAsArray()){
            if (entry.getName().equals(stolenEntry.getName())) {
                try {
                    entrySheetThief.addEntry(newEntry);
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }
    }

}