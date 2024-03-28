package server.gamelogic;


import java.util.Scanner;

public class GameManager {
    // fixed number of rounds
    private static final int ROUNDS  = 13;

    /*
     * #################################################################################################################
     * STARTER METHOD
     * #################################################################################################################
     */
    public static void starter(Player[] players) {
        // preparing the game: initialize five dice and give every player an entry sheet
        // initialize exactly 5 dice in a Dice-array
        Dice[] allDice = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};

        // initializes entry sheets for each player and saves all in an array
        EntrySheet[] allEntrySheets = new EntrySheet[players.length];
        for (int i = 0; i < players.length; i++) {
            allEntrySheets[i] = new EntrySheet(players[i]);
        }

        //TODO: GET DIFFERENT INPUT FROM USER WITH LINA
        Scanner scanner = new Scanner(System.in);

        // starting the game
        System.out.println("############################################");
        System.out.println("############ LET THE GAME BEGIN ############");
        System.out.println("############################################");

        // starting 13 rounds
        for (int round = 0; round < ROUNDS; round++) {

            // for each round we go through a player
            for (EntrySheet currentEntrySheet : allEntrySheets) {

                // current player of the entry sheet with its action dice
                Player currentPlayer = currentEntrySheet.getPlayer();
                ActionDice[] currentActionDice = currentPlayer.getActionDice();

                // variable that checks at very end if all dice are saved
                boolean allDiceSaved = false;

                // variable that checks if action has been played
                boolean blockingDicePlayed = false;

                // variable that checks if action has been stolen
                boolean stealingDicePlayerd = false;

                // resets all dice before rolling
                resetDice(allDice);

                while (!allDiceSaved && !blockingDicePlayed && !stealingDicePlayerd) {
                    /*
                     * #1: check if player has action dice that is all time playable and ask if they want to roll or if they want to play an action
                     */
                    if (currentActionDice.length != 0) {
                        // counts actions that can be played at any time, if there is at least one, then player gets asked if it should be played
                        int allTimePlayableActions = 0;
                        boolean existsStealingDice = false;
                        // sets all-time and infinitely many playable action true if the player has the action steal, freeze or crossOut
                        for (ActionDice a : currentActionDice) {
                            if (!(a.getActionName().equals("shift") && !a.getActionName().equals("switch")) && !a.getActionName().equals("steal")) {
                                allTimePlayableActions = allTimePlayableActions + 1;
                            } else if (a.getActionName().equals("steal")) {
                                existsStealingDice = true;
                            }
                        }
                        /*
                         * #1.1: handles all time playable and infinitely many action dice aka "freeze" and "crossOur"
                         */
                        // if it exists an all-time playable action, then the player can choose to play it
                        while (allTimePlayableActions != 0) {
                            // ask player if they want to play the action dice
                            System.out.println("Do you want to play an action dice? Answer with 'yes' or 'no'.");
                            if (scanner.nextLine().equals("yes")) {
                                boolean typo = true;
                                String nameOfAction = "";
                                String nameOfVictim = "";
                                String nameOfEntry = "";
                                // if player wants to play action, then we check the input String for typos
                                // if there are none, then we split input-string and assign the values to an action, victim and an entry
                                while (typo) {
                                    System.out.println("Choose your action with the following command:\n'freeze'/'crossOut' <username victim> <entry name>).");
                                    String input = scanner.nextLine();
                                    String[] splitStr = input.split("\\s+");

                                    // checks if there are typos in input of player
                                    nameOfAction = splitStr[0];
                                    nameOfVictim = splitStr[1];
                                    nameOfEntry = splitStr[2];
                                    typo = !Helper.checkActionName(nameOfAction) && !Helper.checkPlayerName(players, nameOfVictim) && !Helper.checkEntryName(nameOfEntry);
                                }
                                EntrySheet sheetOfVictim = Helper.getEntrySheetByName(allEntrySheets, nameOfVictim);

                                // since player wants to play the action dice, it needs to check which function should be played (done with the switch case
                                // and the methods in ActionDice-class)
                                switch (nameOfAction) {
                                    case "freeze":
                                        ActionDice.freeze(sheetOfVictim, sheetOfVictim.getEntryByName(nameOfEntry));
                                    case "crossOut":
                                        ActionDice.crossOut(sheetOfVictim, sheetOfVictim.getEntryByName(nameOfEntry));
                                }
                                allTimePlayableActions = allTimePlayableActions - 1;

                                // asks player if more action dice should be played
                                System.out.println("Do you want to play more action dice? Answer with 'yes' or 'no'.");
                                if(scanner.nextLine().equals("no")){
                                    allTimePlayableActions = 0;
                                }
                            }
                            blockingDicePlayed = true;
                        }
                        /*
                         * #1.2: handles action dice "steal"
                         */
                        System.out.println("Do you want to steal an entry or do you want to roll the dice? Answer 'steal' or 'roll'.");
                        if(scanner.nextLine().equals("steal")){
                            System.out.println("Who do you want to steal from and what entry? Answer with:<username> <entry name>.");
                            String decision = scanner.nextLine();
                            String[] splitString = decision.split("\\s+");


                            //TODO:
                            boolean typo = true;
                            String nameOfAction = "";
                            String nameOfVictim = "";
                            String nameOfEntry = "";
                            // if player wants to play action, then we check the input String for typos
                            // if there are none, then we split input-string and assign the values to an action, victim and an entry
                            while (typo) {
                                System.out.println("Choose your action with the following command:\n'freeze'/'crossOut' <username victim> <entry name>).");
                                String input = scanner.nextLine();
                                String[] splitStr = input.split("\\s+");

                                // checks if there are typos in input of player
                                nameOfAction = splitStr[0];
                                nameOfVictim = splitStr[1];
                                nameOfEntry = splitStr[2];
                                typo = !Helper.checkActionName(nameOfAction) && !Helper.checkPlayerName(players, nameOfVictim) && !Helper.checkEntryName(nameOfEntry);
                            }
                            EntrySheet sheetOfVictim = Helper.getEntrySheetByName(allEntrySheets, nameOfVictim);

                        } else if (scanner.nextLine().equals("roll")) {

                        }



                        /*
                         * #2: roll dice
                         */











                        // rolls dice
                        String input = "";
                        if (input.equals("roll") == true) {
                            GameManager.rollDice(allDice);
                            System.out.println("Your dice:");

                            // saves dice
                            System.out.println("Which dice do you want to keep? Write with a space in between the number of the dice you want to save: \n dice 1: 1 \n dice 2: 2 \n dice 3: 3 \n dice 4: 4 \n dice 5: 5 \n dice 6: 6");
                            String savedDice = "";
                            String[] splitStr = savedDice.split("\\s+");
                            for (String s : splitStr) {
                                int i = Integer.parseInt(s);
                                allDice[i - 1].saveDice();
                            }
                            // System.out.println("You saved the dice:");
                            for (Dice d : allDice) {
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

                        }
                    }

                    // choosing entry
                    System.out.println("You saved all your dice, now choose an entry:\nones: 'ones'\ntwos: 'twos'\nthrees: 'threes'\nfours: 'fours'\nfives: 'fives'\nsixes: 'sixes'\nthree of a kind: 'threeOfAKind'\nfour of a kind: 'fourOfAKind'\nfull house: 'fullHouse'\nsmall straight: 'smallStraight'\nlarge straight: 'largeStraight'\nkniffeliger: 'kniffeliger'\nchance: 'chance'");
                    boolean entryChoiceValid = false;
                    while (entryChoiceValid == false) {
                        String entryChoice = "input";
                        try {
                            EntrySheet.entryValidation(currentEntrySheet, entryChoice, allDice);
                            entryChoiceValid = true;
                        } catch (Exception e) {
                            System.out.println("Try another entry please!");
                        }
                    }

                    System.out.println("This is your entry sheet:");
                    currentEntrySheet.printEntrySheet();
                }
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

    /*
     * #################################################################################################################
     * HANDLES ACTION DICE
     * #################################################################################################################
     */

    /**
     * Checks if sum of all dice is modulo 5 and randomly adds action dice, if so.
     *
     * @param playerDice final dice value of a player
     * @return true, if player gets action dice, false, if not
     */
    public static boolean addActionDice(Dice[] playerDice, Player player) {
        // current action dice and the number
        ActionDice[] currentActionDice = player.getActionDice();
        int currentNumberOfActionDice = currentActionDice.length;

        int sum = 0;
        for (Dice dice : playerDice) {
            sum = sum + dice.getDiceValue();
        }

        // if the sum of all dice is dividable by 5 then add action dice
        if(sum % 5 == 0){
            // rolls action dice
            int random = (int) Math.floor(Math.random() * 6 + 1);

            ActionDice[] newActionDice;

            // new action dice array is 1 dice longer or 5 dice longer (if we get the "allAbove" method
            if(random == 6) {
                newActionDice = new ActionDice[currentNumberOfActionDice + 5];
            } else {
                newActionDice = new ActionDice[currentNumberOfActionDice + 1];
            }

            // adds action dice to existing dice of player
            switch (random) {
                case 1:
                    for (int i = 0; i < currentNumberOfActionDice; i++) {
                        newActionDice[i] = currentActionDice[i];
                    }
                    newActionDice[newActionDice.length - 1] = new ActionDice("steal");
                case 2:
                    for (int i = 0; i < currentNumberOfActionDice; i++) {
                        newActionDice[i] = currentActionDice[i];
                    }
                    newActionDice[newActionDice.length - 1] = new ActionDice("freeze");
                case 3:
                    for (int i = 0; i < currentNumberOfActionDice; i++) {
                        newActionDice[i] = currentActionDice[i];
                    }
                    newActionDice[newActionDice.length - 1] = new ActionDice("crossOut");
                case 4:
                    for (int i = 0; i < currentNumberOfActionDice; i++) {
                        newActionDice[i] = currentActionDice[i];
                    }
                    newActionDice[newActionDice.length - 1] = new ActionDice("shift");
                case 5:
                    for (int i = 0; i < currentNumberOfActionDice; i++) {
                        newActionDice[i] = currentActionDice[i];
                    }
                    newActionDice[newActionDice.length - 1] = new ActionDice("switch");
                case 6:
                    for (int i = 0; i < currentNumberOfActionDice; i++) {
                        newActionDice[i] = currentActionDice[i];
                    }
                    newActionDice[newActionDice.length - 5] = new ActionDice("steal");
                    newActionDice[newActionDice.length - 4] = new ActionDice("freeze");
                    newActionDice[newActionDice.length - 3] = new ActionDice("crossOut");
                    newActionDice[newActionDice.length - 2] = new ActionDice("shift");
                    newActionDice[newActionDice.length - 1] = new ActionDice("switch");
            }
        }
        return sum % 5 == 0;
    }

    //TODO: delete action dice from array

    /**
     * Deletes an action dice of players action dice list and assigns player the new list as action dice list.
     * Only one action dice gets deleted.
     *
     * @param player player whose dice we delete
     * @param deletedActionDice dice that needs to be deleted
     */
    public static void deleteActionDice(Player player, String deletedActionDice){
        ActionDice[] playersActionDice = player.getActionDice();
        ActionDice[] newPlayersActionDice = new ActionDice[playersActionDice.length];
        // variable that checks if only one entry gets deleted
        boolean deleteOnce = false;
        int newIndex = 0;
        for (int i = 0; i < playersActionDice.length; i++){
            if (!(playersActionDice[i].getActionName().equals(deletedActionDice)) && !deleteOnce){
                newPlayersActionDice[newIndex] = playersActionDice[i];
                newIndex = newIndex + 1;
                deleteOnce = true;
            }
        }
        player.setActionDices(newPlayersActionDice);
    }



}