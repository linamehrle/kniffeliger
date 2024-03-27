package server.gamelogic;

import java.util.Arrays;


public class GameManager {

    private static final int ROUNDS  = 13;

    /*
     * #################################################################################################################
     * STARTER METHOD
     * #################################################################################################################
     */
    public static void starter(Player[] players){
        // initialize exactly 5 dice in a Dice-array
        Dice[] allDice = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};

        // initializes entry sheets for each player and saves all in an array
        EntrySheet[] allEntrySheets = new EntrySheet[players.length];
        for (int i = 0; i < players.length; i++) {
            allEntrySheets[i] = new EntrySheet(players[i]);
        }

        // starting the game
        System.out.println("############################################");
        System.out.println("############ LET THE GAME BEGIN ############");
        System.out.println("############################################");

        for (int round = 0; round < ROUNDS; round++){
            for (EntrySheet entrySheet : allEntrySheets){

                // resets all dice before rolling
                resetDice(allDice);

                // variable that checks if all dice are saved
                boolean allDiceSaved = false;

                while (!allDiceSaved) {
                    // rolls dice
                    // TODO: listen to input
                    String input = "";
                    if (input.equals("roll") == true){
                        GameManager.rollDice(allDice);
                        System.out.println("Your dice:");
                        // TODO: Should print dice??? --> GameManager.printDice(allDice);

                        // saves dice
                        System.out.println("Which dice do you want to keep? Write with a space in between the number of the dice you want to save: \n dice 1: 1 \n dice 2: 2 \n dice 3: 3 \n dice 4: 4 \n dice 5: 5 \n dice 6: 6");
                        // TODO: listen to input and how to split it
                        //  gets saved dice as String and splits it
                        String savedDice = "";
                        String[] splitStr = savedDice.split("\\s+");
                        for (String s : splitStr){
                            int i = Integer.parseInt(s);
                            allDice[i-1].saveDice();
                        }
                        // System.out.println("You saved the dice:");
                        for (Dice d : allDice){
                            if (d.getSavingStatus()) {
                                // System.out.println(d.getDiceValue());
                            }
                        }
                    }

                    // checks if any unsaved dice is available to roll
                    allDiceSaved = true;
                    for (Dice d : allDice) {
                        if (!d.getSavingStatus()) {
                            allDiceSaved = false;
                        }
                    }

                    // checks for action dice
                    if (allDiceSaved) {
                        if (checkForActionDice(allDice)) {
                            ActionDice actionDice = new ActionDice();
                            // TODO: save action dice and make switch case if played
                        }
                    }
                }

                // choosing entry
                System.out.println("You saved all your dice, now choose an entry:\nones: 'ones'\ntwos: 'twos'\nthrees: 'threes'\nfours: 'fours'\nfives: 'fives'\nsixes: 'sixes'\nthree of a kind: 'threeOfAKind'\nfour of a kind: 'fourOfAKind'\nfull house: 'fullHouse'\nsmall straight: 'smallStraight'\nlarge straight: 'largeStraight'\nkniffeliger: 'kniffeliger'\nchance: 'chance'");
                boolean entryChoiceValid = false;
                while (entryChoiceValid == false) {
                    // TODO: change scanner
                    String entryChoice = "input";
                    try{
                        EntrySheet.entryValidation(entrySheet, entryChoice, allDice);
                        entryChoiceValid = true;
                    } catch (Exception e) {
                        System.out.println("Try another entry please!");
                    }
                }

                System.out.println("This is your entry sheet:");
                entrySheet.printEntrySheet();
            }
        }

    }

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
     */
    public static void rollDice(Dice[] playersDice) {
        // handle NullPointerException if Dice array has only values null
        for (Dice dice : playersDice){
            /* rollDice() already checks if dice has been saved or if it has been rolled 3 times already (because then dice
             * cannot be rolled. Initializes dice if it is not initialized yet to handle NullPointerException, so it can
             * be rolled after.
             */
            if (!dice.getSavingStatus()) {
                dice.rollSingleDice();
            }
        }
    }

    /**
     * Resets all the five dice.
     *
     * @param playersDice the five dice a player can roll
     */
    public static void resetDice(Dice[] playersDice) {
        for (Dice dice : playersDice){
            dice.resetDice();
        }
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
     * Rolls players dice and returns them as String, so we can print it in console.
     * This is only for playing the game in the console.
     *
     * @param playersDice dice the player has and rolled
     */
    public static String stringsAndRockNRoll(Dice[] playersDice){
        String res = "";
        GameManager.rollDice(playersDice);
        for (Dice dice : playersDice){
            res = dice.getDiceValue() + "\n";
        }
        return res;
    }

    /**
     * Prints values of all dice.
     *
     * @param playersDice plays all dice
     */
    public static void printDice(Dice[] playersDice){
        for (Dice d : playersDice) {
            System.out.println(d.getDiceValue() + " ");
        }
    }

}