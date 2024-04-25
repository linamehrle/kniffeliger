package server.gamelogic;

import server.HighScore;
import server.Player;
import server.networking.CommandsServerToClient;
import server.networking.Communication;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * This class handles the process during the game. It contains the starter method that handles the whole game including
 * the game play.
 */
public class GameManager implements Runnable {
    // fixed number of rounds
    private final int ROUNDS = EntrySheet.getEntrySheetLength();
    // number which all the dice should be dividable by so person gets the action dice
    private final int DIVIDABLE_BY = 1;

    // answer of user during game
    private volatile String input;

    // initialize exactly 5 dice in a Dice-array
    private Dice[] allDice;

    // list of all player in game/lobby
    private ArrayList<Player> playerArraysList;

    // cheat code
    private boolean cheat_code_skip_used = false;


    /**
     * Game gets constructed; dices get initiated in constructor.
     */
    // TODO hand over playerArrayList??
    public GameManager() {
        allDice = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};
    }

    @Override
    // starts the game thread
    public void run() {
        try {
            starter();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    /*
     * #################################################################################################################
     * STARTER METHOD
     * #################################################################################################################
     */

    /**
     * This method runs the whole game, keeps the game play in the right order and checks all inputs.
     *
     * @throws InterruptedException exception when thread has an error
     */
    public synchronized void starter() throws InterruptedException {
        Player[] players = new Player[playerArraysList.size()];
        for (int i = 0; i < playerArraysList.size(); i++){
            players[i] = playerArraysList.get(i);
        }
        // preparing the game: initialize five dice and give every player an entry sheet

        // initializes entry sheets for each player and saves all in an array
        EntrySheet[] allEntrySheets = new EntrySheet[players.length];
        for (int i = 0; i < players.length; i++) {
            allEntrySheets[i] = new EntrySheet(players[i]);
        }

        // starting the game and sending all players in lobby a message
        Communication.broadcastToAll(CommandsServerToClient.GAME, playerArraysList, "############################################## LET THE GAME BEGIN ##############################################");

        // starting 14 rounds
        for (int round = 0; round < ROUNDS; round++) {
            Communication.broadcastToAll(CommandsServerToClient.GAME,playerArraysList, "################################################### ROUND " + (round + 1) + " ###################################################");

            // for each round we go through a player
            for (EntrySheet currentEntrySheet : allEntrySheets) {

                // current player of the entry sheet with its action dice
                Player currentPlayer = currentEntrySheet.getPlayer();
                ActionDice[] currentActionDice = currentPlayer.getActionDice();

                // helper Players ArrayList to broadcast to all players but the current one
                ArrayList<Player> helpersPlayersArrayList = new ArrayList<Player>();
                for (Player p : playerArraysList){
                    if (!(p.getUsername().equals(currentPlayer.getUsername()))){
                        helpersPlayersArrayList.add(p);
                    }
                }

                // prints whose current turn it is
                Communication.broadcastToAll(CommandsServerToClient.GAME,helpersPlayersArrayList, "It is " + currentPlayer.getUsername() + "'s turn!");
                Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                        "It is your turn! Your action dice are/is: " + ActionDice.printActionDice(currentEntrySheet.getPlayer().getActionDice()));

                // variable that checks at very end if all dice are saved
                boolean allDiceSaved = false;

                // variable that checks if action has been played
                boolean blockingDicePlayed = false;

                // variable that checks if action has been stolen
                boolean stealingDicePlayed = false;

                // resets all dice before rolling
                resetDice();

                while (!allDiceSaved && !blockingDicePlayed && !stealingDicePlayed) {
                    /*
                     * #1: check if player has action dice that is all time playable and ask if they want to roll or if they want to play an action
                     */
                    int allTimePlayableActions = 0;
                    boolean existsStealingDice = false;
                    if (currentActionDice != null) {
                        // counts actions that can be played at any time, if there is at least one, then player gets asked if it should be played
                        // sets all-time and infinitely many playable action true if the player has the action steal, freeze or crossOut
                        for (ActionDice a : currentActionDice) {
                            if (a != null) {
                                if (!(a.getActionName().equals("shift") && !a.getActionName().equals("swap")) && !a.getActionName().equals("steal")) {
                                    allTimePlayableActions = allTimePlayableActions + 1;
                                } else if (a.getActionName().equals("steal")) {
                                    existsStealingDice = true;
                                }
                            }
                        }
                    }
                    /*
                     * #1.1: handles all time playable and infinitely many action dice aka "freeze" and "crossOut"
                     */
                    // if it exists an all-time playable action, then the player can choose to play it
                    while (allTimePlayableActions != 0) {
                        // ask player if they want to play the action dice
                        Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                                "Do you want to play an all-time playable action dice (aka freeze/crossOut)? Answer with 'yes' or 'no'.");

                        // wait for input
                        wait();

                        if (input.equals("yes")) {
                            boolean typo = true;
                            String nameOfAction = "";
                            String nameOfVictim = "";
                            String nameOfEntry = "";
                            // if player wants to play action, then we check the input String for typos
                            // if there are none, then we split input-string and assign the values to an action, victim and an entry to variables
                            Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                                    "These are your action dice: " + ActionDice.printActionDice(currentActionDice));
                            while (typo) {
                                Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                                        "Choose your action with the following command: 'freeze'/'crossOut' <username victim> <entry name> or 'none'.");

                                wait();
                                String[] splitStr = input.split("\\s+");

                                if (splitStr.length > 1) {
                                    nameOfAction = splitStr[0];
                                    nameOfVictim = splitStr[1];
                                    nameOfEntry = splitStr[2];
                                    // checks if there are typos in input of player
                                    typo = !Helper.checkActionName(nameOfAction) && !Helper.checkPlayerName(players, nameOfVictim) && !Helper.checkEntryName(nameOfEntry);
                                } else if (input.equals("none")) {
                                    typo = false;
                                }
                            }
                            EntrySheet sheetOfVictim = Helper.getEntrySheetByName(allEntrySheets, nameOfVictim);

                            // since player wants to play the action dice, it needs to check which function should be played (done with the switch case
                            // and the methods in ActionDice-class)
                            switch (nameOfAction) {
                                case "freeze":
                                    ActionDice.freeze(sheetOfVictim, nameOfEntry);
                                    break;
                                case "crossOut":
                                    ActionDice.crossOut(sheetOfVictim, nameOfEntry);
                                    break;
                                case "none":
                                    allTimePlayableActions = 0;
                            }
                            deleteActionDice(currentPlayer, nameOfAction);
                            allTimePlayableActions = allTimePlayableActions - 1;

                            // asks player if more action dice should be played
                            Communication.sendToLobby(CommandsServerToClient.GAME, currentPlayer,
                                    "Do you want to play more action dice? Answer with 'yes' or 'no'.");
                            wait();
                            if (input.equals("no")) {
                                allTimePlayableActions = 0;
                            }
                        } else if (!cheat_code_skip_used && round < ROUNDS - 1) {
                            // fast-forward to last round
                            round = ROUNDS - 1;
                            cheat_code_skip_used = true;
                            Communication.broadcastToAll(CommandsServerToClient.GAME, playerArraysList, "FAST-FORWARD to last round used.");
                            Communication.broadcastToAll(CommandsServerToClient.GAME, playerArraysList, "################################################### ROUND " + (round + 1) + " ###################################################");
                        }
                        allTimePlayableActions = 0;
                        blockingDicePlayed = true;
                    }

                    /*
                     * #2: roll dice or use steal dice
                     */
                    // handle stealing dice
                    Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                            "Do you want to steal an entry or do you want to roll the dice? Answer 'want to steal' or 'want to roll'.");
                    wait();
                    if (input.equals("want to steal") && existsStealingDice) {
                        Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                                "Who do you want to steal from and what entry? Answer with:<username> <entry name>.");

                        wait();
                        String[] splitString = input.split("\\s+");

                        // checks for typo
                        boolean typo = true;
                        String nameOfVictim = "";
                        String nameOfEntry = "";
                        // if player wants to play action, then we check the input String for typos
                        // if there are none, then we split input-string and assign the values to victim and an entry to variables
                        while (typo) {

                            // checks if there are typos in input of player
                            nameOfVictim = splitString[0];
                            nameOfEntry = splitString[1];
                            typo = !Helper.checkPlayerName(players, nameOfVictim) && !Helper.checkEntryName(nameOfEntry);
                        }
                        EntrySheet sheetOfVictim = Helper.getEntrySheetByName(allEntrySheets, nameOfVictim);
                        ActionDice.steal(currentEntrySheet, sheetOfVictim, nameOfEntry);
                        Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                                "You just stole " + nameOfEntry + " from " + nameOfVictim);
                        Communication.broadcastToAll(CommandsServerToClient.GAME,helpersPlayersArrayList,
                                currentPlayer.getUsername() + " just stole the entry '" + nameOfEntry + "' from " + nameOfVictim);
                        deleteActionDice(currentPlayer, "steal");
                        stealingDicePlayed = true;

                        // roll dice
                    } else if (input.equals("want to roll")) {
                        Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer, "Please roll the dice.");
                        while (!allDiceSaved) {

                            wait();
                            if (input.equals("roll")) {
                                // rolls all dice
                                rollDice(allDice);

                                // prints all rolled dice
                                String rolledDiceAsString = "";
                                for (int i = 0; i < allDice.length - 1; i++) {
                                    int diceNumber = i + 1;
                                    rolledDiceAsString = rolledDiceAsString + allDice[i].getDiceValue() + " ";
                                }
                                rolledDiceAsString = rolledDiceAsString + allDice[4].getDiceValue();
                                Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer, "Your dice: " + rolledDiceAsString);
                                Communication.broadcastToAll(CommandsServerToClient.GAME,helpersPlayersArrayList,
                                        currentPlayer.getUsername() + " rolled: " + rolledDiceAsString);

                                // saves dice player wants to save
                                Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                                        "Which dice do you want to keep? Write with a space in between the name/number of the dice you want to save.");

                                wait();

                                if (input.equals("none")){
                                    // do nothing
                                } else {
                                    String[] splitStr = input.split("\\s+");
                                    // turn string array to int array
                                    for (String s : splitStr) {
                                        int i = Integer.parseInt(s);
                                        allDice[i - 1].saveDice();
                                    }
                                }

                                // shows player the saved dice
                                String savedDiceAsString = "";
                                for (Dice d : allDice) {
                                    if (d.getSavingStatus()) {
                                        savedDiceAsString = savedDiceAsString + d.getDiceValue() + " ";
                                    }
                                }
                                Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer, "You saved the dice: " + savedDiceAsString);
                                Communication.broadcastToAll(CommandsServerToClient.GAME,helpersPlayersArrayList,
                                        currentPlayer.getUsername() + " saved: " + savedDiceAsString);
                                // checks if any unsaved dice is available to roll
                                allDiceSaved = true;
                                for (Dice d : allDice) {
                                    if (!d.getSavingStatus()) {
                                        allDiceSaved = false;
                                    }
                                }
                            }
                        }
                        // choosing entry
                        Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                                "You saved all your dice, now choose an entry: 'ones', 'twos', 'threes', 'fours', 'fives', 'sixes', 'threeOfAKind', 'fourOfAKind', 'fullHouse', 'smallStraight', 'largeStraight', 'kniffeliger', 'chance', 'pi'");
                        boolean entryChoiceValid = false;
                        while (entryChoiceValid == false) {
                            wait();

                            try {
                                EntrySheet.entryValidation(currentEntrySheet, input, allDice);
                                entryChoiceValid = true;
                            } catch (Exception e) {
                                e.getMessage();
                            }

                        }
                        Communication.sendToPlayer(CommandsServerToClient.GAME, currentPlayer,
                                "This is your entry sheet:" + currentEntrySheet.printEntrySheet());
                        Communication.broadcastToAll(CommandsServerToClient.GAME,helpersPlayersArrayList, currentPlayer.getUsername() + "'s entry sheet: " + currentEntrySheet.printEntrySheet());
                    }
                }
                // hand player the action dice and let player know the action dice (everybody else just knows that they got an action dice but not what kind)
                boolean getActionDice = addActionDice(allDice, currentPlayer);
                if (getActionDice) {
                    Communication.sendToPlayer(CommandsServerToClient.GAME,currentPlayer,
                            "Your action dice is/are now: " + ActionDice.printActionDice(currentEntrySheet.getPlayer().getActionDice()));
                    Communication.broadcastToAll(CommandsServerToClient.GAME,helpersPlayersArrayList, currentPlayer.getUsername() + " got an action dice.");
                }
            }
            Communication.broadcastToAll(CommandsServerToClient.GAME,playerArraysList,
                    "############################################## ALL ENTRY SHEETS ##############################################");
            for (EntrySheet e : allEntrySheets) {
                Communication.broadcastToAll(CommandsServerToClient.GAME,playerArraysList,
                        e.getUsername() + "-------------------" + e.printEntrySheet());
            }

            /*
             * #3: Asking all player if they want to shift and/or swap entry sheets.
             */
            for (Player player : players) {
                // don't ask if at the end of the last round
                if (round == ROUNDS - 1) {
                    break;
                }

                Communication.sendToPlayer(CommandsServerToClient.GAME, player,
                        player.getUsername() + ", your action dice: " + ActionDice.printActionDice(player.getActionDice()));

                // check for swaps and shifts
                int numberOfSwaps = 0;
                int numberOfShifts = 0;
                if (player.getActionDice() != null) {
                    // counts shift and swaps actions
                    for (ActionDice a : player.getActionDice()) {
                        if (a != null) {
                            if (a.getActionName().equals("shift")) {
                                numberOfShifts = numberOfShifts + 1;
                            } else if (a.getActionName().equals("swap")) {
                                numberOfSwaps = numberOfSwaps + 1;
                            }
                        }
                    }
                }
                // variable to stop the shifts and swaps
                boolean continueShiftsAndSwaps = true;
                // prevents player from getting asked if they want to shift or swap, if they do not have one of those actions
                if (numberOfSwaps == 0 && numberOfShifts == 0) {
                    continueShiftsAndSwaps = false;
                } else {
                    while ((numberOfShifts > 0 || numberOfSwaps > 0) && continueShiftsAndSwaps) {
                        Communication.sendToPlayer(CommandsServerToClient.GAME, player,
                                player.getUsername() + ", do you want to shift or swap entry sheets? Answer 'want to shift', 'want to swap' or 'none'.");

                        wait();

                        if (input.equals("want to shift")) {
                            if (numberOfShifts > 0) {
                                ActionDice.shift(allEntrySheets);
                                deleteActionDice(player, "shift");
                                numberOfShifts = numberOfShifts - 1;
                            }
                        } else if (input.equals("want to swap")) {
                            if (numberOfSwaps > 0) {
                                Communication.sendToPlayer(CommandsServerToClient.GAME, player,
                                        "Who do you want to swap with? Answer with the username.");

                                wait();

                                while (!Helper.checkPlayerName(players, input)) {
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, player,
                                            "This is an invalid username. Please try again.");

                                    wait();
                                }
                                // gets entry sheet of victim
                                EntrySheet victimsEntrySheet = EntrySheet.getEntrySheetByName(allEntrySheets, input);
                                // gets entry sheet of villain
                                EntrySheet villainsEntrySheet = EntrySheet.getEntrySheetByName(allEntrySheets, player.getUsername());
                                // swap entry sheets
                                ActionDice.swap(villainsEntrySheet, victimsEntrySheet);
                                deleteActionDice(player, "swap");
                                numberOfSwaps = numberOfSwaps - 1;
                            }
                        } else if (input.equals("none")) {
                            continueShiftsAndSwaps = false;
                        }
                        // stops loop of player does not want to play shifts or swaps
                        if (numberOfSwaps == 0 && numberOfShifts == 0) {
                            continueShiftsAndSwaps = false;
                        }
                    }
                }
            }
        }
        Player[] rankedPlayer = ranking(allEntrySheets);
        String ranking = "";
        for (int i = 0; i < rankedPlayer.length; i++) {
            ranking = ranking + "Number " + (i + 1)+ " is " + rankedPlayer[i].getUsername() + ".";
        }
        //System.out.println(ranking);
        // sends ranking to all players in lobby
        Communication.broadcastToAll(CommandsServerToClient.GAME, playerArraysList, ranking);

        //send the scores to the high score class to possibly update the highscore
        HighScore.updateHighScore(returnScoreAsString(allEntrySheets));

        // TODO: should end the game but wtf is happening (only indicates if lobby is closed or open, does not end lobby)
        players[0].getLobby().gameEnded();
    }

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
    public void printDice(Dice[] playersDice) {
        for (Dice d : playersDice) {
            System.out.println(d.getDiceValue() + " ");
        }
    }

    /**
     * Resets all the five dice.
     */
    public void resetDice() {
        for (Dice dice : allDice) {
            dice.resetDice();
        }
    }

    /**
     * Rolls 5 dice using the rollDice() method from class Dice. This class already checks if player is allowed to roll the
     * dice, so it has not been saved and if it has less than 3 rolls. Saves dice automatically if it has been rolled 3 times.
     *
     * @param playersDice dice client hands to server
     */
    public void rollDice(Dice[] playersDice) {
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
     */
    public String stringsAndRockNRoll() {
        String res = "";
        rollDice(allDice);
        for (Dice dice : allDice) {
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
    public boolean addActionDice(Dice[] playerDice, Player player) {
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
    public void deleteActionDice(Player player, String deletedActionDice) {
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

    /**addActionDice(allDice, currentPlayer
     * Gets answer as String and saves it in answer field, so it can be accessed in starter-method.
     *
     * @param input answer of player
     */
    public synchronized void getAnswer(String input) {
        this.input = input;
        notify();
    }

    /**
     * Adds the connected players to the lobby to the game.
     *
     * @param players connected players
     */
    public void setPlayers(ArrayList<Player> players) {
        playerArraysList = players;
    }

    /*
     * #################################################################################################################
     * HANDLES ACTION DICE
     * #################################################################################################################
     */
    /**
     * Ranks the winners.
     *
     * @param allEntrySheets final entry sheets of the players
     * @return Player-array with the ranked players
     */
    public Player[] ranking(EntrySheet[] allEntrySheets) {
        Player[] rankedPlayer = new Player[allEntrySheets.length];
        Arrays.sort(allEntrySheets, Comparator.comparing(EntrySheet::getTotalPoints));
        for (int i = 0; i < rankedPlayer.length; i++){
            rankedPlayer[i] = allEntrySheets[allEntrySheets.length - i - 1].getPlayer();
        }
        return rankedPlayer;
    }

    private String returnScoreAsString(EntrySheet[] allEntrySheets) {
        String ranking = "";
        for (EntrySheet sheet : allEntrySheets) {
            ranking = ranking + sheet.getTotalPoints() + ":" + sheet.getUsername() + ",";
        }
        return ranking;
    }



}