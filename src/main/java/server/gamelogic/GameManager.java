package server.gamelogic;

import java.util.Arrays;


public class GameManager {
    /*
     * #################################################################################################################
     * ROLLS AND PRINTS DICE
     * #################################################################################################################
     */
    /**
     * Rolls 5 dice using the rollDice() method from class Dice. This class already checks if player is allowed to roll the
     * dice, so it has not been saved and if it has less than 3 rolls. Saves dice automatically if it has been rolled 3 times.
     *
     * @param playersDice dice client hands to server
     * @return new rolled dice
     */
    public static Dice[] rollDice(Dice[] playersDice) {
        // handle NullPointerException if Dice array has only values null
        for (Dice dice : playersDice){
            /* rollDice() already checks if dice has been saved or if it has been rolled 3 times already (because then dice
             * cannot be rolled. Initializes dice if it is not initialized yet to handle NullPointerException, so it can
             * be rolled after.
             */
            if (dice.getSavingStatus() == false) {
                dice.rollSingleDice();
            }
        }
        return playersDice;
    }

    /**
     * Resets all the five dice.
     *
     * @param playersDice the five dice a player can roll
     * @return reset dice array
     */
    public static Dice[] resetDice(Dice[] playersDice) {
        for (Dice dice : playersDice){
            dice.resetDice();
        }
        return playersDice;
    }

    /**
     * Checks if sum of all dice is modulo 5. If so, it gets an action dice.
     *
     * @param playerDice final dice value of a player
     * @return true, if player gets action dice, false, if not.
     */
    public static boolean checkForActionDice(Dice[] playerDice) {
        int sum = 0;
        for (Dice dice : playerDice) {
            sum = sum + dice.getDiceValue();
        }
        return sum % 5 == 0;
    }

    /**
     * Prints dice values.
     *
     * @param playersDice
     */
    public static String stringsAndRockNRoll(Dice[] playersDice){
        String res = "";
        GameManager.rollDice(playersDice);
        for (Dice dice : playersDice){
            res = dice.getDiceValue() + "\n";
        }
        return res;
    }


}