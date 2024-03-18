package server.gamelogic;

import java.util.Arrays;


public class GameManager {
    /*
     * ##################################################################################################################
     * ROLLS DICE
     * ##################################################################################################################
     */

    /**
     * Rolls 5 dice using the rollDice() method from class Dice. This class already checks if player is allowed to roll the
     * dice, so it has not been saved and if it has less than 3 rolls. Saves dice automatically if it has been rolled 3 times.
     *
     * @param playersDice dice client hands to server
     * @return new rolled dice
     */
    //TODO: write a unit test for this method
    public static Dice[] rollDice(Dice[] playersDice) throws Exception {
        if (!(playersDice.length == 5)) {
            throw new Exception("There are 5 dice but you handed me more or less.");
        }
        for (Dice dice : playersDice){
            /* rollDice() already checks if dice has been saved or if it has been rolled 3 times already (because then dice
            * cannot be rolled
            */
            dice.rollDice();
        }
        return playersDice;
    }

    /*
    * ##################################################################################################################
    * METHODS TO CHECK IF ENTRY IS VALID AND HOW MANY POINTS A PLAYER GETS
    * ##################################################################################################################
    */

    // TODO: handed needs to be of type Dice not array (because need to check if it has been saved. So it gets final dice
    //  result, checks if the all have been saved and then transforms them to int[] array
    //  (with static Dice.getAsIntArray(Dice[] diceArray) method) to use them in the entry validation
    //  methods.
    //  So maybe write a method with switch case (to choose the right entry but before applying the check of the
    //  entry transform Dice-array to int-array. Like in the example below. (Also maybe put functions like threeOfAKind() on EntrySheet or Entry??

    public void entryValidation (String nameOfEntry, Dice[] finalDiceValues) throws Exception {
        // checks if all dice have been saved
        boolean allDiceSaved = true;
        for (Dice d : finalDiceValues){
            if (d.getSavingStatus() == false){
                allDiceSaved = false;
            }
        }
        // throws exception if not all dice have been saved
        if (allDiceSaved == false){
            throw new Exception("Not all dice have been saved.");
        }

        // transforms Dice-array into int-array if all dice have been saved, so we can apply methods below to it
        int[] finalDiceInt = Dice.getAsIntArray(finalDiceValues);

        switch (nameOfEntry) {
            case "ones":
                singleValueRolls(finalDiceInt, 1);
                break;
            case "twos":
                singleValueRolls(finalDiceInt, 2);
                break;
            case "threes":
                singleValueRolls(finalDiceInt, 3);
                break;
            case "fours":
                singleValueRolls(finalDiceInt, 4);
                break;
            case "fives":
                singleValueRolls(finalDiceInt, 5);
                break;
            case "sixes":
                singleValueRolls(finalDiceInt, 5);
                break;
            case "threeOfAKind":
                threeOfAKind(finalDiceInt);
                break;
            case "fourOfAKind":
                fourOfAKind(finalDiceInt);
                break;
            case "fullHouse":
                fullHouse(finalDiceInt);
                break;
            case "smallStraight":
                smallStraight(finalDiceInt);
                break;
            case "largeStraight":
                largeStraight(finalDiceInt);
                break;
            case "kniffeliger":
                kniffeliger(finalDiceInt);
                break;
            case "chance":
                chance(finalDiceInt);
                break;
        }
    }

    /**
     * Checks rolled dice for a specific value and adds all dice with this value up. The sum is then returned.
     * (1er, 2er, 3er, 4er, 5er, 6er)
     *
     * @param rolledDice are the dice that have been rolled and saved
     * @param value      method dice for this value (for example checks how many 6 it has, so value = 6)
     * @return the sum of dice
     * @throws Exception if there are less than 5 dice saved
     * @throws Exception if the value we need to compare the dice value with is not between 1 and 6
     */
    public int singleValueRolls(int[] rolledDice, int value) throws Exception {
        // checks if there are 5 rolled dice
        if (!(rolledDice.length == 5)) {
            throw new Exception("There are 5 dice but you handed me more or less.");
        }
        // checks if we inserted a valid value for dice
        if (!(value >= 1 && value <= 6)) {
            throw new Exception("A dice can only have values 1 to 6.");
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
    public int threeOfAKind(int[] rolledDice) throws Exception {
        // checks if there are 5 rolled dice
        if (!(rolledDice.length == 5)) {
            throw new Exception("There are 5 dice but you handed me more or less.");
        }

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
    public int fourOfAKind(int[] rolledDice) throws Exception {
        // checks if there are 5 rolled dice
        if (!(rolledDice.length == 5)) {
            throw new Exception("There are 5 dice but you handed me more or less.");
        }

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
     * @throws Exception if there are less than 5 dice saved
     */
    public int fullHouse(int[] rolledDice) throws Exception {
        // checks if there are 5 rolled dice
        if (!(rolledDice.length == 5)) {
            throw new Exception("There are 5 dice but you handed me more or less.");
        }

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
     * @throws Exception if there are less than 5 dice saved
     */
    public int smallStraight(int[] rolledDice) throws Exception {
        // checks if there are 5 rolled dice
        if (!(rolledDice.length == 5)) {
            throw new Exception("There are 5 dice but you handed me more or less.");
        }

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
     * @throws Exception if there are less than 5 dice saved
     */
    public int largeStraight(int[] rolledDice) throws Exception {
        // checks if there are 5 rolled dice
        if (!(rolledDice.length == 5)) {
            throw new Exception("There are 5 dice but you handed me more or less.");
        }

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
    public int kniffeliger(int[] rolledDice) throws Exception {
        // checks if there are 5 rolled dice
        if (!(rolledDice.length == 5)) {
            throw new Exception("There are 5 dice but you handed me more or less.");
        }

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
     * @throws Exception if there are less than 5 dice saved
     */
    public int chance(int[] rolledDice) throws Exception {
        // checks if there are 5 rolled dice
        if (!(rolledDice.length == 5)) {
            throw new Exception("There are 5 dice but you handed me more or less.");
        }

        // adds dice up
        int sum = 0;
        for (int d : rolledDice) {
            sum = sum + d;
        }
        return sum;
    }
}