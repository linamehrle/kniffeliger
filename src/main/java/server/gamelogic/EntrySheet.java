package server.gamelogic;

import java.util.Arrays;

public class EntrySheet {
    // TODO: NullpointerException handling

    // value with which every Entry starts with
    private final int defaultValue = 0;

    // player that is associated with entry sheet
    private Player player;

    // username of player
    private String username;

    // total points per default 0
    private int totalPoints = 0;

    // entries used for a full entry sheet array
    private Entry ones = new Entry("ones", defaultValue);
    private Entry twos = new Entry("twos", defaultValue);
    private Entry threes = new Entry("threes", defaultValue);
    private Entry fours = new Entry("fours", defaultValue);
    private Entry fives = new Entry("fives", defaultValue);
    private Entry sixes = new Entry("sixes", defaultValue);
    private Entry threeOfAKind = new Entry("threeOfAKind", defaultValue);
    private Entry fourOfAKind = new Entry("fourOfAKind", defaultValue);
    private Entry fullHouse = new Entry("fullHouse", defaultValue);
    private Entry smallStraight = new Entry("smallStraight", defaultValue);
    private Entry largeStraight = new Entry("largeStraight", defaultValue);
    private Entry kniffeliger = new Entry("kniffeliger", defaultValue);
    private Entry chance = new Entry("chance", defaultValue);

    // entry sheet as an Entry-array
    private Entry[] entrySheet = new Entry[]{ones, twos, threes, fours, fives, sixes, threeOfAKind, fourOfAKind, fullHouse, smallStraight, largeStraight, kniffeliger, chance};

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
     * GENERAL METHODS FOR ENTRY SHEET (DELETE, ADD, GET, RESET, PRINT)
     * ##################################################################################################################
     */

    /**
     * Access username which is also name of entry sheet.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Access total points of entry sheet
     *
     * @return all the points of the entry sheet added together
     */
    public int getTotalPoints() {
        return totalPoints;
    }

    /**
     * Access entry sheet as an array.
     *
     * @return entry sheet array
     */
    public Entry[] getEntrySheetAsArray() {
        return entrySheet;
    }

    /**
     * Extracts point of entry sheet into an array.
     *
     * @return array with points of entries as values
     */
    public int[] getEntryValues(){
        int[] entryValues = new int[entrySheet.length];
        for (int i = 0; i < entrySheet.length; i++) {
            entryValues[i] = entrySheet[i].getValue();
        }
        return entryValues;
    }

    /**
     * Extracts names of entry sheet into an array. Does not change names but copies them into an array.
     *
     * @return array with names of entries as values
     */
    public String[] getEntryNames(){
        String[] entryNames = new String[entrySheet.length];
        for (int i = 0; i < entrySheet.length; i++) {
            entryNames[i] = entrySheet[i].getName();
        }
        return entryNames;
    }

    /**
     * Adds the new value for an entry at the right position of the entry sheet array. Total points get updated by the added value.
     *
     * @param newEntry contains the name we can compare and the new entry value
     */
    public void addEntry(Entry newEntry) throws Exception {
        // makes sure that we can throw an exception, if the entry does not appear in entrySheet array
        int notAppearedCounter = 0;
        for (Entry entry : entrySheet) {
            // if the correct entry has been detected, so if the names are the same, the value of this entry on entrySheet can be changed
            if (entry.getName().equals(newEntry.getName())) {
                entry.setValue(newEntry.getValue());
                totalPoints = totalPoints + entry.getValue();
            } else {
                notAppearedCounter = notAppearedCounter + 1;
            }
        }
        // throws exception if the entry name could not be detected in the entrySheet array because then the given entry is not valid
        if (notAppearedCounter == entrySheet.length) {
            throw new Exception("Your entry has no match in the entry sheet. You must have gotten the wrong name.");
        }
    }

    /**
     * Detects entry from sheet and deletes it.
     *
     * @param deletedEntry gives us name of entry that needs to be deleted
     * @throws Exception when the deletedEntry parameter does not appear in entry sheet
     */
    public void deleteEntry(Entry deletedEntry) throws Exception {
        // makes sure that we can throw an exception, if the entry does not appear in entrySheet array
        int notAppearedCounter = 0;
        for (Entry entry : entrySheet) {
            // if the correct entry has been detected, so if the names are the same, delete value from total points and set the value of this entry on entrySheet to 0
            if (entry.getName().equals(deletedEntry.getName())) {
                totalPoints = totalPoints - entry.getValue();
                entry.setValue(0);
            } else {
                notAppearedCounter = notAppearedCounter + 1;
            }
        }
        // throws exception if the entry name could not be detected in the entrySheet array because then the given entry is not valid
        if (notAppearedCounter == entrySheet.length) {
            throw new Exception("Your entry has no match in the entry sheet. You must have gotten the wrong name.");
        }
    }

    /**
     * Sets all entries on entry sheet to starting value 0.
     */
    public void resetEntrySheet() {
        for (Entry entry : entrySheet) {
            entry.setValue(defaultValue);
        }
        totalPoints = 0;
    }

    public void printEntrySheet(){
        System.out.println("##############################");
        System.out.println("Your Entry Sheet");
        System.out.println("Name" + username);
        System.out.println("##############################");
        for (Entry e : entrySheet) {
            System.out.println(e.getName() + ": " + e.getValue());
        }
    }

    /*
     * ##################################################################################################################
     * METHODS THAT HANDLES NEW ENTRIES
     * ##################################################################################################################
     */

    /**
     * Sees if entry is valid and adds it to entry sheet
     *
     * @param nameOfEntry entry name which player wants to save the dice/points for.
     * @param finalDiceValues the dice values after the player is done rolling.
     * @throws Exception if entry cannot be found in sheet
     */
    public static void entryValidation (EntrySheet entrySheet, String nameOfEntry, Dice[] finalDiceValues) throws Exception {
        // checks if all dice have been saved, if one is not, then save them
        for (Dice d : finalDiceValues){
            if (d.getSavingStatus() == false){
                d.saveDice();
            }
        }

        // transforms Dice-array into int-array if all dice have been saved, so we can apply methods below to it
        int[] finalDiceInt = Dice.getAsIntArray(finalDiceValues);

        switch (nameOfEntry) {
            case "ones":
                try {
                    Entry ones = new Entry("ones", singleValueRolls(finalDiceInt, 1));
                    entrySheet.addEntry(ones);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "twos":
                try{
                    Entry twos = new Entry("twos", singleValueRolls(finalDiceInt, 2));
                    entrySheet.addEntry(twos);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "threes":
                try {
                    Entry threes = new Entry("threes", singleValueRolls(finalDiceInt, 3));
                    entrySheet.addEntry(threes);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "fours":
                try {
                    Entry fours = new Entry("fours", singleValueRolls(finalDiceInt, 4));
                    entrySheet.addEntry(fours);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "fives":
                try {
                    Entry fives = new Entry("fives", singleValueRolls(finalDiceInt, 5));
                    entrySheet.addEntry(fives);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "sixes":
                try {
                    Entry sixes = new Entry("sixes", singleValueRolls(finalDiceInt, 6));
                    entrySheet.addEntry(sixes);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "threeOfAKind":
                try {
                    Entry threeOfAKind = new Entry("threeOfAKind", threeOfAKind(finalDiceInt));
                    entrySheet.addEntry(threeOfAKind);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "fourOfAKind":
                try {
                    Entry fourOfAKind = new Entry("fourOfAKind", fourOfAKind(finalDiceInt));
                    entrySheet.addEntry(fourOfAKind);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "fullHouse":
                try {
                    Entry fullHouse = new Entry("fullHouse", fullHouse(finalDiceInt));
                    entrySheet.addEntry(fullHouse);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "smallStraight":
                try {
                    Entry smallStraight = new Entry("smallStraight", smallStraight(finalDiceInt));
                    entrySheet.addEntry(smallStraight);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "largeStraight":
                try {
                    Entry largeStraight = new Entry("largeStraight", largeStraight(finalDiceInt));
                    entrySheet.addEntry(largeStraight);
                } catch (Exception e) {
                    e.getMessage();
                }
                largeStraight(finalDiceInt);
                break;
            case "kniffeliger":
                try {
                    Entry kniffeliger = new Entry("kniffeliger", kniffeliger(finalDiceInt));
                    entrySheet.addEntry(kniffeliger);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            case "chance":
                try {
                    Entry chance = new Entry("chance", chance(finalDiceInt));
                    entrySheet.addEntry(chance);
                } catch (Exception e) {
                    e.getMessage();
                }
                break;
            /*
            default:
                new Exception("Your entry choice is not valid.");
             */


        }
    }

    /**
     * Checks rolled dice for a specific value and adds all dice with this value up. The sum is then returned.
     * (1er, 2er, 3er, 4er, 5er, 6er)
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @param value      method dice for this value (for example checks how many 6 it has, so value = 6)
     * @return the sum of dice
     * @throws Exception if the value we need to compare the dice value with is not between 1 and 6
     */
    public static int singleValueRolls(int[] rolledDice, int value) throws Exception {
        // checks if we inserted a valid value for dice
        if (!(value >= 1 && value <= 6)) {
            throw new Exception("Only the values 1 to 6 can be checked.");
        }

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
    public static int fourOfAKind(int[] rolledDice)  {

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
        if ((rolledDice[1] == rolledDice[0] + 1) || (rolledDice[4] == rolledDice[3] + 1)) {
            for (int i = 0; i < rolledDice.length - 1; i++) {
                if (rolledDice[i + 1] == rolledDice[i] && repetitionCounter < 1) {
                    repetitionCounter = repetitionCounter + 1;
                } else if (rolledDice[i + 1] != rolledDice[i] + 1) {
                    return 0;
                } //else: continue loop
            }
            // if it made it through the loop without returning 0, then we have a small straight
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
     * @return returns 50 if it is a Kniffeliger/Yathzee, 0 otherwise
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

}