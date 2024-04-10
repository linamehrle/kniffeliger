package server.gamelogic;


import java.util.Scanner;

// TODO javadoc for class
public class GameManager {
    // fixed number of rounds
    private static final int ROUNDS = EntrySheet.getEntrySheetLength();
    // number which all the dice should be dividable by so person gets the action dice
    private static final int DIVIDABLE_BY = 1;

    /*
     * #################################################################################################################
     * STARTER METHOD
     * #################################################################################################################
     */
    public static void starter(Player[] players){
        // initialize dice
        Dice[] allDice = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};

        // initializes entry sheets for each player and saves all in an array
        EntrySheet[] allEntrySheets = new EntrySheet[players.length];
        for (int i = 0; i < players.length; i++) {
            allEntrySheets[i] = new EntrySheet(players[i]);
        }

        Scanner scanner = new Scanner(System.in);

        // starting the game
        System.out.println("############################################");
        System.out.println("############ LET THE GAME BEGIN ############");
        System.out.println("############################################");

        // starting 14 rounds
        for (int round = 0; round < ROUNDS; round++) {
            System.out.println("############################################");
            System.out.println("################# ROUND " + (round + 1)  +  " ##################");
            System.out.println("############################################");

            // loop through all the players
            for (EntrySheet currentEntrySheet : allEntrySheets){

                // saves values of current entry sheet, so player and current action dice, so we can access it easily
                Player currentPlayer = currentEntrySheet.getPlayer();
                ActionDice[] currentActionDice = currentPlayer.getActionDice();

                // conditions to check if game needs to go on or stop; this includes:
                // 1. if a cheat code has been played
                // 2. if an entry has been made
                boolean cheatCode = false;
                boolean entryMade = false;

                // gets all the action dice of a player
                int stealCount = 0;
                int freezeCount = 0;
                int crossOutCount = 0;
                int shiftCount = 0;
                int swapCount = 0;
                for (ActionDice actionDice : currentActionDice ){
                    switch(actionDice.getActionName()) {
                        case "steal":
                            stealCount = stealCount + 1;
                            break;
                        case "freeze":
                            freezeCount = freezeCount + 1;
                            break;
                        case "crossOut":
                            crossOutCount = crossOutCount + 1;
                            break;
                        case "shift":
                            shiftCount = shiftCount + 1;
                            break;
                        case "swap":
                            swapCount = swapCount + 1;
                            break;
                    }

                    while(!cheatCode && !entryMade){
                        System.out.print("Please choose an action. ('roll', 'steal', 'freeze', 'crossOut')");
                        String answer = scanner.nextLine();

                        switch (answer){
                            case "roll":
                                rollDice(allDice);
                                for (Dice d : allDice){
                                    if (d.getSavingStatus()){

                                    }
                                }
                                boolean allDiceSaved = true;
                                while(!allDiceSaved){

                                }
                                entryMade = true;
                                break;
                            case "steal":

                                entryMade = true;
                                break;
                            case "freeze":

                                freezeCount = freezeCount - 1;
                                break;
                            case "crossOut":

                                crossOutCount = crossOutCount - 1;
                        }
                    }

                }

                // TODO: shift, swap



            }

        }


    }

//    public static void starter(Player[] players) {
//        // preparing the game: initialize five dice and give every player an entry sheet
//        // initialize exactly 5 dice in a Dice-array
//        Dice[] allDice = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};
//
//        // initializes entry sheets for each player and saves all in an array
//        EntrySheet[] allEntrySheets = new EntrySheet[players.length];
//        for (int i = 0; i < players.length; i++) {
//            allEntrySheets[i] = new EntrySheet(players[i]);
//        }
//
//        //TODO: GET DIFFERENT INPUT FROM USER WITH LINA
//        Scanner scanner = new Scanner(System.in);
//
//        // starting the game
//        System.out.println("############################################");
//        System.out.println("############ LET THE GAME BEGIN ############");
//        System.out.println("############################################");
//
//        // starting 14 rounds
//        for (int round = 0; round < ROUNDS; round++) {
//            System.out.println("############################################");
//            System.out.println("################# ROUND " + (round + 1)  +  " ##################");
//            System.out.println("############################################");
//
//            // for each round we go through a player
//            for (EntrySheet currentEntrySheet : allEntrySheets) {
//                System.out.println(currentEntrySheet.getUsername() + " it is your turn!");
//                System.out.println("Your action dice: " + ActionDice.printActionDice(currentEntrySheet.getPlayer().getActionDice()));
//
//                // current player of the entry sheet with its action dice
//                Player currentPlayer = currentEntrySheet.getPlayer();
//                ActionDice[] currentActionDice = currentPlayer.getActionDice();
//
//                // variable that checks at very end if all dice are saved
//                boolean allDiceSaved = false;
//
//                // variable that checks if action has been played
//                boolean blockingDicePlayed = false;
//
//                // variable that checks if action has been stolen
//                boolean stealingDicePlayed = false;
//
//                // resets all dice before rolling
//                resetDice(allDice);
//
//                while (!allDiceSaved && !blockingDicePlayed && !stealingDicePlayed) {
//                    /*
//                     * #1: check if player has action dice that is all time playable and ask if they want to roll or if they want to play an action
//                     */
//                    int allTimePlayableActions = 0;
//                    boolean existsStealingDice = false;
//                    if (currentActionDice != null) {
//                        // counts actions that can be played at any time, if there is at least one, then player gets asked if it should be played
//                        // sets all-time and infinitely many playable action true if the player has the action steal, freeze or crossOut
//                        for (ActionDice a : currentActionDice) {
//                            if (a != null) {
//                                if (!(a.getActionName().equals("shift") && !a.getActionName().equals("swap")) && !a.getActionName().equals("steal")) {
//                                    allTimePlayableActions = allTimePlayableActions + 1;
//                                } else if (a.getActionName().equals("steal")) {
//                                    existsStealingDice = true;
//                                }
//                            }
//                        }
//                    }
//                    /*
//                     * #1.1: handles all time playable and infinitely many action dice aka "freeze" and "crossOur"
//                     */
//                    // if it exists an all-time playable action, then the player can choose to play it
//                    while (allTimePlayableActions != 0) {
//                        // ask player if they want to play the action dice
//                        System.out.println("Do you want to play an action dice? Answer with 'yes' or 'no'.");
//                        String answer = scanner.nextLine();
//                        if (answer.equals("yes")) {
//                            boolean typo = true;
//                            String nameOfAction = "";
//                            String nameOfVictim = "";
//                            String nameOfEntry = "";
//                            // if player wants to play action, then we check the input String for typos
//                            // if there are none, then we split input-string and assign the values to an action, victim and an entry to variables
//                            System.out.println("These are your action dice: " + ActionDice.printActionDice(currentActionDice));
//                            while (typo) {
//                                System.out.println("Choose your action with the following command:\n'freeze'/'crossOut' <username victim> <entry name> or 'none'.");
//                                String input = scanner.nextLine();
//                                String[] splitStr = input.split("\\s+");
//
//                                if (splitStr.length > 1) {
//                                    nameOfAction = splitStr[0];
//                                    nameOfVictim = splitStr[1];
//                                    nameOfEntry = splitStr[2];
//                                    // checks if there are typos in input of player
//                                    typo = !Helper.checkActionName(nameOfAction) && !Helper.checkPlayerName(players, nameOfVictim) && !Helper.checkEntryName(nameOfEntry);
//                                } else if (input.equals("none")) {
//                                    typo = false;
//                                }
//                            }
//                            EntrySheet sheetOfVictim = Helper.getEntrySheetByName(allEntrySheets, nameOfVictim);
//
//                            // since player wants to play the action dice, it needs to check which function should be played (done with the switch case
//                            // and the methods in ActionDice-class)
//                            switch (nameOfAction) {
//                                case "freeze":
//                                    ActionDice.freeze(sheetOfVictim, nameOfEntry);
//                                    break;
//                                case "crossOut":
//                                    ActionDice.crossOut(sheetOfVictim, nameOfEntry);
//                                    break;
//                                case "none":
//                                    allTimePlayableActions = 0;
//                            }
//                            deleteActionDice(currentPlayer, nameOfAction);
//                            allTimePlayableActions = allTimePlayableActions - 1;
//
//                            // asks player if more action dice should be played
//                            System.out.println("Do you want to play more action dice? Answer with 'yes' or 'no'.");
//                            if (scanner.nextLine().equals("no")) {
//                                allTimePlayableActions = 0;
//                            }
//                        }
//                        allTimePlayableActions = 0;
//                        blockingDicePlayed = true;
//                    }
//
//                    /*
//                     * #2: roll dice or use steal dice
//                     */
//                    // handle stealing dice
//                    System.out.println("Do you want to steal an entry or do you want to roll the dice? Answer 'want to steal' or 'want to roll'.");
//                    String answer = scanner.nextLine();
//                    if (answer.equals("want to steal") && existsStealingDice) {
//                        System.out.println("Who do you want to steal from and what entry? Answer with:<username> <entry name>.");
//                        String decision = scanner.nextLine();
//                        String[] splitString = decision.split("\\s+");
//
//                        // checks for typo
//                        boolean typo = true;
//                        String nameOfVictim = "";
//                        String nameOfEntry = "";
//                        // if player wants to play action, then we check the input String for typos
//                        // if there are none, then we split input-string and assign the values to victim and an entry to variables
//                        while (typo) {
//
//                            // checks if there are typos in input of player
//                            nameOfVictim = splitString[0];
//                            nameOfEntry = splitString[1];
//                            typo = !Helper.checkPlayerName(players, nameOfVictim) && !Helper.checkEntryName(nameOfEntry);
//                        }
//                        EntrySheet sheetOfVictim = Helper.getEntrySheetByName(allEntrySheets, nameOfVictim);
//                        ActionDice.steal(currentEntrySheet, sheetOfVictim, nameOfEntry);
//                        System.out.println(currentEntrySheet.printEntrySheet());
//                        System.out.println(sheetOfVictim.printEntrySheet());
//                        deleteActionDice(currentPlayer, "steal");
//                        stealingDicePlayed = true;
//
//                        // roll dice
//                    } else if (answer.equals("want to roll")) {
//                        System.out.println("Please roll the dice.");
//                        while (!allDiceSaved) {
//                            if (scanner.nextLine().equals("roll")) {
//                                // rolls all dice
//                                GameManager.rollDice(allDice);
//
//                                // prints all rolled dice
//                                System.out.println("Your dice: ");
//                                for (int i = 0; i < allDice.length - 1; i++) {
//                                    int diceNumber = i + 1;
//                                    System.out.println("Dice " + diceNumber + ": " + allDice[i].getDiceValue() + " ");
//                                }
//                                System.out.println("Dice " + 5 + ": " + allDice[4].getDiceValue());
//
//                                // saves dice player wants to save
//                                System.out.println("Which dice do you want to keep? Write with a space in between the name/number of the dice you want to save (for example to save dice 1, write '1').");
//                                String savedDice = scanner.nextLine();
//
//                                if (savedDice.equals("none")){
//                                    // do nothing
//                                } else {
//                                    String[] splitStr = savedDice.split("\\s+");
//                                    // turn string array to int array
//                                    for (String s : splitStr) {
//                                        int i = Integer.parseInt(s);
//                                        allDice[i - 1].saveDice();
//                                    }
//                                }
//
//                                // shows player the saved dice
//                                System.out.println("You saved the dice:");
//                                for (Dice d : allDice) {
//                                    if (d.getSavingStatus()) {
//                                        System.out.print(d.getDiceValue() + " ");
//                                    }
//                                }
//                                // checks if any unsaved dice is available to roll
//                                allDiceSaved = true;
//                                for (Dice d : allDice) {
//                                    if (!d.getSavingStatus()) {
//                                        allDiceSaved = false;
//                                    }
//                                }
//                            }
//                        }
//                        // choosing entry
//                        System.out.println("You saved all your dice, now choose an entry:\nones: 'ones'\ntwos: 'twos'\nthrees: 'threes'\nfours: 'fours'\nfives: 'fives'\nsixes: 'sixes'\nthree of a kind: 'threeOfAKind'\nfour of a kind: 'fourOfAKind'\nfull house: 'fullHouse'\nsmall straight: 'smallStraight'\nlarge straight: 'largeStraight'\nkniffeliger: 'kniffeliger'\nchance: 'chance'\npi: 'pi'");
//                        boolean entryChoiceValid = false;
//                        while (entryChoiceValid == false) {
//                            String entryChoice = scanner.nextLine();
//                            try {
//                                EntrySheet.entryValidation(currentEntrySheet, entryChoice, allDice);
//                                entryChoiceValid = true;
//                            } catch (Exception e) {
//                                e.getMessage();
//                            }
//
//                        }
//                        System.out.println("This is your entry sheet:");
//                        System.out.println(currentEntrySheet.printEntrySheet());
//                    }
//                }
//                addActionDice(allDice, currentPlayer);
//            }
//            System.out.println("##################### ALL ENTRY SHEETS #####################");
//            for (EntrySheet e : allEntrySheets) {
//                System.out.println(e.getUsername() + "---- " + e.printEntrySheet());
//            }
//
//            /*
//             * #3: Asking all player if they want to shift and/or swap entry sheets.
//             */
//            for (Player player : players) {
//                System.out.println(player.getUsername() + ", your action dice: " + ActionDice.printActionDice(player.getActionDice()));
//                // check for swaps and shifts
//                int numberOfSwaps = 0;
//                int numberOfShifts = 0;
//                if (player.getActionDice() != null) {
//                    // counts shift and swaps actions
//                    for (ActionDice a : player.getActionDice()) {
//                        if (a != null) {
//                            if (a.getActionName().equals("shift")) {
//                                numberOfShifts = numberOfShifts + 1;
//                            } else if (a.getActionName().equals("swap")) {
//                                numberOfSwaps = numberOfSwaps + 1;
//                            }
//                        }
//                    }
//                }
//                // variable to stop the shifts and swaps
//                boolean continueShiftsAndSwaps = true;
//                // prevents player from getting asked if they want to shift or swap, if they do not have one of those actions
//                if (numberOfSwaps == 0 && numberOfShifts == 0) {
//                    continueShiftsAndSwaps = false;
//                } else {
//                    while ((numberOfShifts > 0 || numberOfSwaps > 0) && continueShiftsAndSwaps) {
//                        System.out.println(player.getUsername() + ", do you want to shift or swap entry sheets? Answer 'want to shift', 'want to swap' or 'none'.");
//                        String answer = scanner.nextLine();
//                        if (answer.equals("want to shift")) {
//                            if (numberOfShifts > 0) {
//                                ActionDice.shift(allEntrySheets);
//                                deleteActionDice(player, "shift");
//                                numberOfShifts = numberOfShifts - 1;
//                            }
//                        } else if (answer.equals("want to swap")) {
//                            if (numberOfSwaps > 0) {
//                                System.out.println("Who do you want to swap with? Answer with the username.");
//                                String victim = scanner.nextLine();
//                                while (!Helper.checkPlayerName(players, victim)) {
//                                    System.out.println("This is an invalid username. Please try again.");
//                                    victim = scanner.nextLine();
//                                }
//                                // gets entry sheet of victim
//                                EntrySheet victimsEntrySheet = EntrySheet.getEntrySheetByName(allEntrySheets, victim);
//                                // gets entry sheet of villain
//                                EntrySheet villainsEntrySheet = EntrySheet.getEntrySheetByName(allEntrySheets, player.getUsername());
//                                // swap entry sheets
//                                ActionDice.swap(villainsEntrySheet, victimsEntrySheet);
//                                deleteActionDice(player, "swap");
//                                numberOfSwaps = numberOfSwaps - 1;
//                            }
//                        } else if (answer.equals("none")) {
//                            continueShiftsAndSwaps = false;
//                        }
//                        // stops loop of player does not want to play shifts or swaps
//                        if (numberOfSwaps == 0 && numberOfShifts == 0) {
//                            continueShiftsAndSwaps = false;
//                        }
//                    }
//                }
//            }
//        }
//    }

    /*
     * #################################################################################################################
     * ROLLS AND PRINTS DICE
     * #################################################################################################################
     */
    /**
     * Prints values of all dice.
     *
     * @param playersDice plays all dice
     */
    public static void printDice(Dice[] playersDice) {
        for (Dice d : playersDice) {
            System.out.println(d.getDiceValue() + " ");
        }
    }

    /**
     * Resets all the five dice.
     *
     * @param playersDice the five dice a player can roll
     */
    public static void resetDice(Dice[] playersDice) {
        for (Dice dice : playersDice) {
            dice.resetDice();
        }
    }

    /**
     * Rolls 5 dice using the rollDice() method from class Dice. This class already checks if player is allowed to roll the
     * dice, so it has not been saved and if it has less than 3 rolls. Saves dice automatically if it has been rolled 3 times.
     *
     * @param playersDice dice client hands to server
     */
    public static void rollDice(Dice[] playersDice) {
        // handle NullPointerException if Dice array has only values null
        for (Dice dice : playersDice) {
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
     * Rolls players dice and returns them as String, so we can print it in console.
     * This is only for playing the game in the console.
     *
     * @param playersDice dice the player has and rolled
     */
    public static String stringsAndRockNRoll(Dice[] playersDice) {
        String res = "";
        GameManager.rollDice(playersDice);
        for (Dice dice : playersDice) {
            res = dice.getDiceValue() + "\n";
        }
        return res;
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
        int sum = 0;
        for (Dice dice : playerDice) {
            sum = sum + dice.getDiceValue();
        }

        // current action dice and the number
        ActionDice[] currentActionDice = player.getActionDice();

        // if the sum of all dice is dividable by 5 then add action dice
        if (sum % DIVIDABLE_BY == 0 && sum != 0) {
            // if player does not have any action dice yet, then the first one gets initialized
            ActionDice[] newActionDice;
            if (currentActionDice == null) {
                int random = (int) Math.floor(Math.random() * 6 + 1);
                if (random == 6) {
                    newActionDice = new ActionDice[5];
                } else {
                    newActionDice = new ActionDice[1];
                }

                switch (random) {
                    case 1:
                        newActionDice[0] = new ActionDice("steal");
                        break;
                    case 2:
                        newActionDice[0] = new ActionDice("freeze");
                        break;
                    case 3:
                        newActionDice[0] = new ActionDice("crossOut");
                        break;
                    case 4:
                        newActionDice[0] = new ActionDice("shift");
                        break;
                    case 5:
                        newActionDice[0] = new ActionDice("swap");
                        break;
                    case 6:
                        newActionDice[0] = new ActionDice("steal");
                        newActionDice[1] = new ActionDice("freeze");
                        newActionDice[2] = new ActionDice("crossOut");
                        newActionDice[3] = new ActionDice("shift");
                        newActionDice[4] = new ActionDice("swap");
                        break;
                }
            } else {
                // number of action dice the player has
                int currentNumberOfActionDice = currentActionDice.length;
                // add action dice to existing action dice
                // rolls action dice
                int random = (int) Math.floor(Math.random() * 6 + 1);

                // new action dice array is 1 dice longer or 5 dice longer (if we get the "allAbove" method
                if (random == 6) {
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
                        break;
                    case 2:
                        for (int i = 0; i < currentNumberOfActionDice; i++) {
                            newActionDice[i] = currentActionDice[i];
                        }
                        newActionDice[newActionDice.length - 1] = new ActionDice("freeze");
                        break;
                    case 3:
                        for (int i = 0; i < currentNumberOfActionDice; i++) {
                            newActionDice[i] = currentActionDice[i];
                        }
                        newActionDice[newActionDice.length - 1] = new ActionDice("crossOut");
                        break;
                    case 4:
                        for (int i = 0; i < currentNumberOfActionDice; i++) {
                            newActionDice[i] = currentActionDice[i];
                        }
                        newActionDice[newActionDice.length - 1] = new ActionDice("shift");
                    case 5:
                        for (int i = 0; i < currentNumberOfActionDice; i++) {
                            newActionDice[i] = currentActionDice[i];
                        }
                        newActionDice[newActionDice.length - 1] = new ActionDice("swap");
                        break;
                    case 6:
                        for (int i = 0; i < currentNumberOfActionDice; i++) {
                            newActionDice[i] = currentActionDice[i];
                        }
                        newActionDice[newActionDice.length - 5] = new ActionDice("steal");
                        newActionDice[newActionDice.length - 4] = new ActionDice("freeze");
                        newActionDice[newActionDice.length - 3] = new ActionDice("crossOut");
                        newActionDice[newActionDice.length - 2] = new ActionDice("shift");
                        newActionDice[newActionDice.length - 1] = new ActionDice("swap");
                        break;
                }
            }
            // add action dice to the array and replace associated action dice array of player with new action dice array
            player.setActionDices(newActionDice);
        }
        return sum % 5 == 0;
    }

    /**
     * Deletes an action dice of players action dice list and assigns player the new list as action dice list.
     * Only one action dice gets deleted.
     *
     * @param player            player whose dice we delete
     * @param deletedActionDice dice that needs to be deleted
     */
    public static void deleteActionDice(Player player, String deletedActionDice) {
        ActionDice[] playersActionDice = player.getActionDice();
        if (playersActionDice != null) {
            // initiate new action dice array
            ActionDice[] newPlayersActionDice = new ActionDice[playersActionDice.length - 1];
            // variable that checks if only one entry gets deleted
            boolean deleteOnce = false;
            // index of new array
            int newIndex = 0;
            for (int i = 0; i < playersActionDice.length; i++) {
                if (!(playersActionDice[i].getActionName().equals(deletedActionDice)) || deleteOnce) {
                    newPlayersActionDice[newIndex] = new ActionDice(playersActionDice[i].getActionName());
                    newIndex = newIndex + 1;
                } else {
                    deleteOnce = true;
                }
            }
            player.setActionDices(newPlayersActionDice);
        }
    }

}