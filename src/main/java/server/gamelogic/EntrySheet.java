package server.gamelogic;

import java.util.Arrays;
import java.util.Scanner;
import server.Player;

/**
 * Entry sheet class to build entry sheets that are associated with a player.
 */
public class EntrySheet {
    // entry sheet length
    private static final int ENTRY_SHEET_LENGTH = 14;

    // value with which every Entry starts with
    private static final int DEFAULT_VALUE = 0;

    // default entry sheet as Entry-array
    private static Entry defaultOnes = new Entry("ones", DEFAULT_VALUE);
    private static Entry defaultTwos = new Entry("twos", DEFAULT_VALUE);
    private static Entry defaultThrees = new Entry("threes", DEFAULT_VALUE);
    private static Entry defaultFours = new Entry("fours", DEFAULT_VALUE);
    private static Entry defaultFives = new Entry("fives", DEFAULT_VALUE);
    private static Entry defaultSixes = new Entry("sixes", DEFAULT_VALUE);
    private static Entry defaultThreeOfAKind = new Entry("threeOfAKind", DEFAULT_VALUE);
    private static Entry defaultFourOfAKind = new Entry("fourOfAKind", DEFAULT_VALUE);
    private static Entry defaultFullHouse = new Entry("fullHouse", DEFAULT_VALUE);
    private static Entry defaultSmallStraight = new Entry("smallStraight", DEFAULT_VALUE);
    private static Entry defaultLargeStraight = new Entry("largeStraight", DEFAULT_VALUE);
    private static Entry defaultKniffeliger = new Entry("kniffeliger", DEFAULT_VALUE);
    private static Entry defaultChance = new Entry("chance", DEFAULT_VALUE);
    private static Entry defaultPi = new Entry("pi", DEFAULT_VALUE);
    private static Entry[] defaultEntrySheet = new Entry[]{defaultOnes, defaultTwos, defaultThrees, defaultFours, defaultFives, defaultSixes, defaultThreeOfAKind, defaultFourOfAKind, defaultFullHouse, defaultSmallStraight, defaultLargeStraight, defaultKniffeliger, defaultChance, defaultPi};


    // player that is associated with entry sheet
    private Player player;

    // total points per default 0
    private int totalPoints = 0;

    // username of player
    private String username;

    // entries used for a full entry sheet array
    private Entry ones = new Entry("ones", DEFAULT_VALUE);
    private Entry twos = new Entry("twos", DEFAULT_VALUE);
    private Entry threes = new Entry("threes", DEFAULT_VALUE);
    private Entry fours = new Entry("fours", DEFAULT_VALUE);
    private Entry fives = new Entry("fives", DEFAULT_VALUE);
    private Entry sixes = new Entry("sixes", DEFAULT_VALUE);
    private Entry threeOfAKind = new Entry("threeOfAKind", DEFAULT_VALUE);
    private Entry fourOfAKind = new Entry("fourOfAKind", DEFAULT_VALUE);
    private Entry fullHouse = new Entry("fullHouse", DEFAULT_VALUE);
    private Entry smallStraight = new Entry("smallStraight", DEFAULT_VALUE);
    private Entry largeStraight = new Entry("largeStraight", DEFAULT_VALUE);
    private Entry kniffeliger = new Entry("kniffeliger", DEFAULT_VALUE);
    private Entry chance = new Entry("chance", DEFAULT_VALUE);
    private Entry pi = new Entry("pi", DEFAULT_VALUE);

    // entry sheet as an Entry-array
    private Entry[] entrySheet = new Entry[]{ones, twos, threes, fours, fives, sixes, threeOfAKind, fourOfAKind, fullHouse, smallStraight, largeStraight, kniffeliger, chance, pi};


    /**
     * Constructor that builds new entry sheet with unique player (that has unique username and id) which is handed to
     * it as a parameter.
     *
     * @param player is unique so server knows exactly who entry sheet belongs to
     */
    public EntrySheet(Player player) {
        this.player = player;
        this.username = player.getUsername();
    }

    /*
     * ##################################################################################################################
     * METHODS THAT HANDLE THE ENTRY SHEET AND NEW ENTRIES
     * ##################################################################################################################
     */

    /**
     * Access entry sheet as an array.
     *
     * @return entry sheet array
     */
    public Entry[] getAsArray() {
        return entrySheet;
    }

    /**
     * Access a default entry sheet to loop through and check parameters.
     *
     * @return default entry sheet with all entry names and default value 0 for all entries
     */
    public static Entry[] getDefaultEntrySheet(){
        return defaultEntrySheet;
    }

    /**
     * Gets an entry of a sheet by name.
     *
     * @param entryName name of entry that should be found in sheet
     * @return entry which is associated with given name
     */
    public Entry getEntryByName(String entryName) {
        Entry result = null;
        for (Entry e : entrySheet) {
            if (e.getName().equals(entryName)){
                result = e;
            }
        }
        return result;
    }

    /**
     * Extracts names of entry sheet into an array. Does not change names but copies them into an array.
     *
     * @return array with names of entries as values
     */
    public String[] getEntryNames() {
        String[] entryNames = new String[entrySheet.length];
        for (int i = 0; i < entrySheet.length; i++) {
            entryNames[i] = entrySheet[i].getName();
        }
        return entryNames;
    }

    /**
     * Gets the entry sheet associated with a players unique name. If no player with this name is in entry sheet list
     * then it returns a default player entry sheet with name 'default player' and ID '001'.
     *
     * @param allEntrySheets list to look through
     * @param playerName entry sheet that is looked for in list
     * @return entry sheet associated with playerName
     */
    public static EntrySheet getEntrySheetByName(EntrySheet[] allEntrySheets, String playerName){
        for(EntrySheet e : allEntrySheets){
            if (e.getUsername().equals(playerName)){
                return e;
            }
        }
        return null;
    }

    /**
     * Access official length of an entry sheet, which is 13.
     *
     * @return length of entry sheet, which is 13.
     */
    public static int getEntrySheetLength() {
        return ENTRY_SHEET_LENGTH;
    }

    /**
     * Extracts point of entry sheet into an array.
     *
     * @return array with points of entries as values
     */
    public int[] getEntryValues() {
        int[] entryValues = new int[entrySheet.length];
        for (int i = 0; i < entrySheet.length; i++) {
            entryValues[i] = entrySheet[i].getValue();
        }
        return entryValues;
    }

    /**
     * Access player of entry sheet.
     *
     * @return player of entry sheet
     */
    public Player getPlayer() { return player;  }

    /**
     * Access total points of entry sheet
     *
     * @return all the points of the entry sheet added together
     */
    public int getTotalPoints() {
        return totalPoints;
    }

    /**
     * Access username which is also name of entry sheet.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Changes values of entries in entrySheet (Entry[]-array) to values given in the int-array.
     *
     * @param valuesEntries int-array with new values for entry sheet
     */
    public void setEntrySheet(int[] valuesEntries) {
        // change values in entry sheet to those in int-array
        for (int i = 0; i < entrySheet.length; i++){
            entrySheet[i].setValue(valuesEntries[i]);
            entrySheet[i].setFinal();
        }
    }

    /**
     * Changes player associated with entry sheet.
     *
     * @param newPlayer new owner of sheet
     */
    public void setPlayer(Player newPlayer) {
        player = newPlayer;
        username = newPlayer.getUsername();
    }

    /**
     * Sets all the entries in an entry sheet to not frozen.
     */
    public void defreeze () {
        for (Entry entry : entrySheet) {
            entry.setFrozenStatus(false);
        }
    }

    /**
     * Adds the new value for an entry at the right position of the entry sheet array. Total points get updated by the added value.
     *
     * @param newEntry contains the name we can compare and the new entry value
     */
    public void addEntry(Entry newEntry) {
        // makes sure that we can throw an exception, if the entry does not appear in entrySheet array
        int notAppearedCounter = 0;
        for (Entry entry : entrySheet) {
            // if the correct entry has been detected, so if the names are the same, the value of this entry on entrySheet can be changed
            if (entry.getName().equals(newEntry.getName())) {
                entry.setValue(newEntry.getValue());
                entry.setFinal();
                totalPoints = totalPoints + entry.getValue();
            } else {
                notAppearedCounter = notAppearedCounter + 1;
            }
        }
        // throws exception if the entry name could not be detected in the entrySheet array because then the given entry is not valid
        if (notAppearedCounter == entrySheet.length) {
            System.out.print("Your entry did not appear. Please try again.");
        }
    }

    /**
     * Detects entry from sheet and deletes it. Sets entry as final since the value should not change now.
     *
     * @param deletedEntry gives us name of entry that needs to be deleted
     */
    public void deleteEntry(String deletedEntry){
        for (Entry entry : entrySheet) {
            // if the correct entry has been detected, so if the names are the same, delete value from total points and set the value of this entry on entrySheet to 0
            if (entry.getName().equals(deletedEntry)) {
                totalPoints = totalPoints - entry.getValue();
                entry.setValue(0);
                entry.setFinal();
            }
        }
    }

    /**
     * Sets all entries on entry sheet to starting value 0.
     */
    public void resetEntrySheet() {
        for (Entry entry : entrySheet) {
            entry.setValue(DEFAULT_VALUE);
        }
        totalPoints = 0;
    }

    /**
     * METHOD RETURNS STRING SO WE CAN PLAY IT IN CONSOLE ###############################################################
     * Method to print entry sheet (only used to play it in console).
     */
    public String printEntrySheet() {
        String message = "Name: " + username + ",";
        for (int i = 0; i < ENTRY_SHEET_LENGTH - 1; i++) {
            message = message + entrySheet[i].getName() + ": " + entrySheet[i].getValue() + ",";
        }
        message = message + entrySheet[ENTRY_SHEET_LENGTH - 1].getName() + ": " + entrySheet[ENTRY_SHEET_LENGTH - 1].getValue();
        return message;
    }

    /*
     * ##################################################################################################################
     * GENERAL METHODS FOR ENTRY SHEET (DELETE, ADD, GET, RESET, PRINT)
     * ##################################################################################################################
     */

    /**
     * Sees if entry is valid and adds it to entry sheet
     *
     * @param nameOfEntry     entry name which player wants to save the dice/points for.
     * @param finalDiceValues the dice values after the player is done rolling.
     */
    public static void entryValidation(EntrySheet entrySheet, String nameOfEntry, Dice[] finalDiceValues) {
        // if entry is not valid it enters 0 for this entry (this is handled in single methods for entries)
        // checks if all dice have been saved, if one is not, then save them
        for (Dice d : finalDiceValues) {
            if (d.getSavingStatus() == false) {
                d.saveDice();
            }
        }

        // transforms Dice-array into int-array if all dice have been saved, so we can apply methods below to it
        int[] finalDiceInt = Dice.getAsIntArray(finalDiceValues);

        // when entry player want to make is not final then add it to entry sheet
        // else: aks for different entry
        if (entrySheet.getEntryByName(nameOfEntry).getIsFinal() || entrySheet.getEntryByName(nameOfEntry).getFrozenStatus()) {
            // TODO REMOVE
            System.out.println("This is not a valid choice. Please try again.");
            Scanner scanner = new Scanner(System.in);
            entryValidation(entrySheet, scanner.nextLine(), finalDiceValues);
        } else {
            switch (nameOfEntry) {
                case "ones":
                    Entry ones = new Entry("ones", singleValueRolls(finalDiceInt, 1));
                    entrySheet.addEntry(ones);
                    break;
                case "twos":
                    Entry twos = new Entry("twos", singleValueRolls(finalDiceInt, 2));
                    entrySheet.addEntry(twos);
                    break;
                case "threes":
                    Entry threes = new Entry("threes", singleValueRolls(finalDiceInt, 3));
                    entrySheet.addEntry(threes);
                    break;
                case "fours":
                    Entry fours = new Entry("fours", singleValueRolls(finalDiceInt, 4));
                    entrySheet.addEntry(fours);
                    break;
                case "fives":
                    Entry fives = new Entry("fives", singleValueRolls(finalDiceInt, 5));
                    entrySheet.addEntry(fives);
                    break;
                case "sixes":
                    Entry sixes = new Entry("sixes", singleValueRolls(finalDiceInt, 6));
                    entrySheet.addEntry(sixes);
                    break;
                case "threeOfAKind":
                    Entry threeOfAKind = new Entry("threeOfAKind", threeOfAKind(finalDiceInt));
                    entrySheet.addEntry(threeOfAKind);
                    break;
                case "fourOfAKind":
                    Entry fourOfAKind = new Entry("fourOfAKind", fourOfAKind(finalDiceInt));
                    entrySheet.addEntry(fourOfAKind);
                    break;
                case "fullHouse":
                    Entry fullHouse = new Entry("fullHouse", fullHouse(finalDiceInt));
                    entrySheet.addEntry(fullHouse);
                    break;
                case "smallStraight":
                    Entry smallStraight = new Entry("smallStraight", smallStraight(finalDiceInt));
                    entrySheet.addEntry(smallStraight);
                    break;
                case "largeStraight":
                    Entry largeStraight = new Entry("largeStraight", largeStraight(finalDiceInt));
                    entrySheet.addEntry(largeStraight);
                    largeStraight(finalDiceInt);
                    break;
                case "kniffeliger":
                    Entry kniffeliger = new Entry("kniffeliger", kniffeliger(finalDiceInt));
                    entrySheet.addEntry(kniffeliger);
                    break;
                case "chance":
                    Entry chance = new Entry("chance", chance(finalDiceInt));
                    entrySheet.addEntry(chance);
                    break;
                case "pi":
                    Entry pi = new Entry("pi", pi(finalDiceInt));
                    entrySheet.addEntry(pi);
                    break;
                default:
                    // TODO: ACHTUNG, ÄNDERE DAS FÜR SPIEL AUSSERHALB DER KONSOLE
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Your entry name is wrong. Please try again.");
                    String entryName = scanner.nextLine();
                    entryValidation(entrySheet, entryName, finalDiceValues);
            }
        }
        // entrySheet.getEntryByName(nameOfEntry).setFinal();
    }

    /**
     * Checks rolled dice for a specific value and adds all dice with this value up. The sum is then returned.
     * (1er, 2er, 3er, 4er, 5er, 6er)
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @param value      method dice for this value (for example checks how many 6 it has, so value = 6)
     * @return the sum of dice
     */
    public static int singleValueRolls(int[] rolledDice, int value) {
        // checks if we inserted a valid value for dice
        int sum = 0;
        for (int d : rolledDice) {
            if (d == value) {
                sum = sum + d;
            }
        }
        return sum;
    }

    /**
     * Checks for 3 of a kind by sorting the rolledDice array first and then checking where the three of a kind are,
     * because there are only three options after sorting: {t, t, t, x, y}, {x, t, t, t, y}, {x, y, t, t, t}.
     * Attention: rolledDice array is sorted after applying this method.
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @return returns value of three same dice
     */
    public static int threeOfAKind(int[] rolledDice) {

        // sorts rolled dice first
        Arrays.sort(rolledDice);
        int sum = 0;
        if (rolledDice[0] == rolledDice[2]) {
            sum = 3 * rolledDice[0];
        } else if (rolledDice[1] == rolledDice[3]) {
            sum = 3 * rolledDice[1];
        } else if (rolledDice[2] == rolledDice[4]) {
            sum = 3 * rolledDice[4];
        }
        return sum;
    }

    /**
     * Checks for 4 of a kind by sorting the rolledDice array first and then checking where the four of a kind are,
     * because there are only two options after sorting: {f, f, f, f, x}, {x, f, f, f, f}.
     * Attention: rolledDice array is sorted after applying this method.
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @return returns value of four same dice
     */
    public static int fourOfAKind(int[] rolledDice) {

        // sorts rolled dice first
        Arrays.sort(rolledDice);
        int sum = 0;
        if (rolledDice[0] == rolledDice[3]) {
            sum = 4 * rolledDice[0];
        } else if (rolledDice[1] == rolledDice[4]) {
            sum = 4 * rolledDice[4];
        }
        return sum;
    }

    /**
     * Checks rolled dice for a full house (=a pair and a triplet) by first sorting the rolled dice, then checking if
     * there is a pair and a triplet, because after sorting, there are only two options left for them to occur
     * {t, t, t, p, p} or {p, p, t, t, t}. Returns 25 if it is a full house, returns 0 otherwise.
     * Attention: rolledDice array is sorted after applying this method.
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @return return 25 if it is a full house and 0 if not
     */
    public static int fullHouse(int[] rolledDice) {

        int res = 0;
        // sorts array first
        Arrays.sort(rolledDice);
        /*
         * There are have two options for a full house after sorting the array {t, t, t, p, p} of {p, p, t, t, t}.
         * If the rolledDice[0] == rolledDice[2] && rolledDice[3] == rolledDice[4] (then we have the triplet first and
         * the pair second) OR the rolledDice[0] == rolledDice[1] && rolledDice[2] == rolledDice[4] (then we have the
         * pair first and the triplet last). If it is a full house we return 25, in all the other cases we do not have
         * a full house and the res = 0 gets returned.
         */
        boolean tripletFirstPairLast = rolledDice[0] == rolledDice[2] && rolledDice[3] == rolledDice[4];
        boolean pairFirstTripletLast = rolledDice[0] == rolledDice[1] && rolledDice[2] == rolledDice[4];
        if (tripletFirstPairLast || pairFirstTripletLast) {
            res = 25;
        }
        return res;
    }

    /**
     * Checks rolled dice for small straight by sorting them first and then checking, if it should check first 4 or last 4 values of the rolledDice array and then
     * loops through each of the values inside the array to checks if it contains at most 1 repetition and if each value is the previous value + 1.
     * Attention: rolledDice array is sorted after applying this method.
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @return return 30 if it is a small straight and 0 if not
     */
    public static int smallStraight(int[] rolledDice) {

        int res = 30;
        /*
         * counts for repeated values in between such as {1,2,2,3,4}. If there are more than 1 repetition, then we do not
         * have a small straight.
         */
        int repetitionCounter = 0;
        // sorts rolled dice in ascending order, so we can loop over it and check conditions for a small straight.
        Arrays.sort(rolledDice);
        /*
         * First check if the first 4 values in rolledDice-array or last 4 values are supposed to be the small straight.
         * If is one of those two cases since we sorted the array. That means we check:
         * if rolledDice[1] == rolledDice[0] + 1, then it is the first 4 values,
         * if rolledDice[4] == rolledDice[3] + 1, then it is the last 4 values.
         * If it is none of those two above, then we do not have a small straight and return 0.
         * Then in the loop we check if we have at most 1 repetition then we can continue loop as long as we have only
         * one. Also, we check if the next value is the previous vale + 1. If so, if continues the loop (else) and if
         * not, it returns 0. If it made it through the loop without return then we have a small straight and return 30.
         */
        if (rolledDice[1] == rolledDice[0] + 1) {
            for (int i = 0; i < rolledDice.length - 2; i++) {
                if (rolledDice[i + 1] == rolledDice[i] && repetitionCounter < 1) {
                    repetitionCounter = repetitionCounter + 1;
                } else if (rolledDice[i + 1] != rolledDice[i] + 1) {
                    return 0;
                } //else: continue loop
            }
            // if it made it through the loop without returning 0, then we have a small straight
        } else if (rolledDice[4] == rolledDice[3] + 1) {
            for (int i = 1; i < rolledDice.length - 1; i++) {
                if (rolledDice[i + 1] == rolledDice[i] && repetitionCounter < 1) {
                    repetitionCounter = repetitionCounter + 1;
                } else if (rolledDice[i + 1] != rolledDice[i] + 1) {
                    return 0;
                } //else: continue loop
            }
        } else {
            return 0;
        }
        return res;
    }

    /**
     * Checks rolled dice for large straight by sorting them first and then checking, if each of the values inside the
     * array is the previous value + 1.
     * Attention: rolledDice array is sorted after applying this method.
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @return return 40 if it is a large straight and 0 if not
     */
    public static int largeStraight(int[] rolledDice) {

        int res = 40;
        // sorts rolled dice in ascending order, so we can loop over it and check conditions for a large straight.
        Arrays.sort(rolledDice);
        /*
         * First check if array starts with 1 or 2 because if not, then it is definitely not a straight, so it returns
         * the res, which at that point is 0. To check a straight, we loop through rolled dice and check if next value
         * is the previous value + 1. If we reached at the last entry it does not check the mentioned condition since
         * this has already been checked by the leap before.
         */
        if (rolledDice[0] == 1 || rolledDice[0] == 2) {
            for (int i = 0; i < rolledDice.length - 1; i++) {
                if (rolledDice[i + 1] != rolledDice[i] + 1) {
                    return 0;
                }
            }
            // if it made it through loop without returning 0, then it is a large straight, so it returns res = 40
        } else {
            return 0;
        }
        return res;
    }

    /**
     * Checks for Kniffeliger/Yahtzee (= 5 of a kind) by sorting the array first and then checking if there are five of
     * a kind, because if there are, the first and the last value are the same: {f, f, f, f, f}.
     * Attention: rolledDice array is sorted after applying this method.
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @return 50 if it is a Kniffeliger/Yathzee, 0 otherwise
     */
    public static int kniffeliger(int[] rolledDice) {

        // we do not need to sort rolled dice first because it should be all the same value
        int res = 0;
        if (rolledDice[0] == rolledDice[4]) {
            res = 50;
        }
        return res;
    }

    /**
     * Sums up all five dice.
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @return return the sum of all dice
     */
    public static int chance(int[] rolledDice) {

        // adds dice up
        int sum = 0;
        for (int d : rolledDice) {
            sum = sum + d;
        }
        return sum;
    }

    /**
     * Checks for pi (or rather first digits of pi) in dice. First the five dice get sorted, then check the
     * second to fifth entry of array for numbers.
     * Attention: rolledDice array is sorted after applying this method.
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @return 31, if it is pi, 0 otherwise
     */
    public static int pi(int[] rolledDice){
        int res = 0;
        // sorts rolled dice in ascending order, so we can loop over it and check conditions for a large straight.
        Arrays.sort(rolledDice);
        // checks if array is {1,1,3,4,5}
        if (rolledDice[1] == 1 && rolledDice[2] == 3 && rolledDice[3] == 4 && rolledDice[4] == 5) {
            res = 31;
        }
        return res;
    }

}