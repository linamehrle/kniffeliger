package server.gamelogic;
import server.Player;


/**
 * Class that contains the action dice and all the methods that enforce the actions of the action dice.
 */
public class ActionDice {

    // name of action player can perform
    private final String actionName;

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
     * Lets you steal an entry from another player. Returns String with message, if entry could be stolen.
     *
     * @param entrySheetVillain player that steals entry
     * @param entrySheetVictim player that entry is stolen from
     * @param stolenEntry entry that thief wants to steal
     * @return true if entry could be stolen and false else
     */
    public static boolean steal (EntrySheet entrySheetVillain, EntrySheet entrySheetVictim, String stolenEntry) {
        // assigns names and entry sheets to villain and victim
        String nameVillain = entrySheetVillain.getUsername();
        String nameVictim = entrySheetVictim.getUsername();
        Entry[] entriesVillain = entrySheetVictim.getAsArray();
        Entry[] entriesVictim = entrySheetVictim.getAsArray();

        // return value
        boolean gotStolen = false;

        for (int i = 0; i < EntrySheet.getEntrySheetLength(); i++){
            // entries can only be stolen if the following things are true:
            // 1. Villain does not steal from his own sheet
            // 2. Villain does not steal an entry that is already final on his or her (or their) entry sheet
            // 3. Entry name is valid and exists on an entry sheet
            // 4. Chosen entry is not final on victims entry sheet
            if (!nameVictim.equals(nameVillain) && !entriesVillain[i].getIsFinal() && entriesVictim[i].getName().equals(stolenEntry) && entriesVictim[i].getIsFinal()){
                entrySheetVillain.addEntry(entriesVictim[i]);
                entrySheetVictim.deleteEntry(stolenEntry);
                gotStolen = true;
            }
        }
        return gotStolen;
    }

    /**
     * Freezes (aka blocks) a player from choosing a specific combination as an entry. If, for example, the full house
     * gets frozen, the victim cannot choose this entry for next round.
     *
     * @return true if entry could be frozen and false else
     */
    public static boolean freeze (EntrySheet entrySheetVillain, EntrySheet entrySheetVictim, String frozenEntry){
        // saves name of villain and victim
        String nameVillain = entrySheetVillain.getUsername();
        String nameVictim = entrySheetVictim.getUsername();

        // return value
        boolean gotFrozen = false;

        // entries can only be frozen if
        // 1. it is not an entry of the villain
        // 2. if the entry actually exists
        // 3. if the entry is not final, so no value has been set by the victim
        for (Entry entry : entrySheetVictim.getAsArray()){
            if (!nameVictim.equals(nameVillain) && entry.getName().equals(frozenEntry) && !entry.getIsFinal()){
                entry.setFrozenStatus(true);
                gotFrozen = true;
            }
        }
        return gotFrozen;
    }

    /**
     * Crosses out/deletes entry from another player.
     *
     * @param entrySheetVictim entry sheet of victim
     * @param crossedOutEntry entry to be deleted
     * @return true if entry could be crossed out and false else
     */
    public static boolean crossOut (EntrySheet entrySheetVillain, EntrySheet entrySheetVictim, String crossedOutEntry) {
        // saves name of villain and victim
        String nameVillain = entrySheetVillain.getUsername();
        String nameVictim = entrySheetVictim.getUsername();

        // return value
        boolean gotCrossedOut = false;

        // entries can only be crossed out if
        // 1. it is not an entry of the villain
        // 2. if the entry actually exists
        // 3. if the entry is final, so a value has been set
        for (Entry entry : entrySheetVictim.getAsArray()){
            if (!nameVictim.equals(nameVillain) && entry.getName().equals(crossedOutEntry) && entry.getIsFinal()) {
                entrySheetVictim.deleteEntry(crossedOutEntry);
                gotCrossedOut = true;
            }
        }
        return gotCrossedOut;
    }

    /**
     * Shifts all entries one player.
     *
     * @param playersSheets all entry sheets of players as EntrySheet[]-array.
     * @return message for game in console
     */
    public static boolean shift(EntrySheet[] playersSheets){
        Player helper = playersSheets[0].getPlayer();
        for (int i = 0; i < playersSheets.length - 1; i++) {
            playersSheets[i].setPlayer(playersSheets[i + 1].getPlayer());
        }
        playersSheets[playersSheets.length - 1].setPlayer(helper);
        return true;
    }

    /**
     * Swaps entry sheets of two players
     *
     * @param entrySheetVillain entry sheet of player who steals the entry.
     * @param entrySheetVictim entry sheet that gets stolen
     * @return message for game in console
     */
    public static boolean swap(EntrySheet entrySheetVillain, EntrySheet entrySheetVictim){
        boolean gotSwap = false;
        if (!entrySheetVillain.getUsername().equals(entrySheetVictim.getUsername())) {
            Player helper = entrySheetVillain.getPlayer();
            entrySheetVillain.setPlayer(entrySheetVictim.getPlayer());
            entrySheetVictim.setPlayer(helper);
            gotSwap = true;
        }
        return gotSwap;
    }

    /**
     * Prints the action dice array as String.
     *
     * @param actionDice to be printed
     * @return String to display array
     */
    public static String printActionDice(ActionDice[] actionDice){
        String result = "Your action dice is/are: ";
        if (actionDice == null) {
            result = result + "[]";
            return result;
        } else {
            for (ActionDice ad : actionDice){
                if (ad != null) {
                    result = result + " " + ad.getActionName();
                } else {
                    System.out.println("none");
                }
            }
            return result;
        }
    }

}