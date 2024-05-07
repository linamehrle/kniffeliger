package server.gamelogic;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;
import server.HighScore;
import server.Player;
import server.networking.CommandsServerToClient;
import server.networking.Communication;
import starter.Starter;
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

    // logger
    private Logger logger = Starter.getLogger();
    private Level gameLogic = Level.getLevel("GAME_LOGIC");

    private Player currentPlayer;

    /**
     * Game gets constructed; dices get initiated in constructor.
     */
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
        logger.trace("starter()");

        //initializes the hashMap of action dices in the player and sets the boolean isInGame to true
        for (Player player : playerArraysList) {
            player.prepareForGame();
        }

        // initializes entry sheets for each player and saves all in an array
        Player[] players = new Player[playerArraysList.size()];
        for (int i = 0; i < playerArraysList.size(); i++) {
            players[i] = playerArraysList.get(i);
        }
        EntrySheet[] allEntrySheets = new EntrySheet[players.length];
        for (int i = 0; i < players.length; i++) {
            allEntrySheets[i] = new EntrySheet(players[i]);
        }

        // starting the game and sending all players in lobby a message
        logger.log(gameLogic, "Game lobby with " + playerArraysList + " started.");

        Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList, "The game starts.");

        // request to initialize clients
        String playerList = "";
        for (Player player : playerArraysList) {
            playerList += player.getUsername() + " ";
        }
        Communication.broadcastToAll(CommandsServerToClient.INES, playerArraysList, playerList);

        // starting 14 rounds
        for (int round = 0; round < 3; round++) {
            logger.log(gameLogic, "Round " + (round + 1) + " started");

            // loop through all the players
            for (EntrySheet currentEntrySheet : allEntrySheets) {
                // saves values of current entry sheet, so player and current action dice, so we can access it easily
                currentPlayer = currentEntrySheet.getPlayer();
                //ActionDice[] currentActionDice = currentPlayer.getActionDice();

                // conditions to check if game needs to go on or stop; this includes:
                // 1. if a cheat code has been played
                // 2. if an entry has been made
                boolean entryMade = false;
                boolean endTurn = false;

                // checks if player already started to because then stealing is not allowed anymore
                boolean aboutToRoll = false;

                // saves input for steal/freeze/cout
                String victimPlayerName = "";
                String selectedEntry = "";

                // notify players which turn is
                Communication.broadcastToAll(CommandsServerToClient.STRT, playerArraysList, currentPlayer.getUsername() + " Main");
                Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList, currentPlayer.getUsername() + "'s turn.");
                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "-- It's your turn!");

                logger.log(gameLogic, currentPlayer.getUsername() + "'s turn.");

                while (!entryMade || !endTurn) {
                    // wait for input
                    wait();
                    String[] inputArr = input.split("\\s+");
                    logger.log(gameLogic, "Input received: " + Arrays.toString(inputArr));

                    // check if input has more parameters
                    if (inputArr.length == 3) {
                        victimPlayerName = inputArr[1];
                        selectedEntry = inputArr[2];
                    }

                    switch (inputArr[0]) {
                        case "ROLL":
                            logger.trace("Entered ROLL case");

                            if (!entryMade && !allDiceSaved(allDice)) {
                                // if player did not steal yet then roll
                                // set about to roll to true so player cannot steal anymore
                                aboutToRoll = true;

                                // rolls dice
                                String rolledDice = rollDice(allDice);

                                logger.log(gameLogic, "Dices were rolled.");
                                logger.log(gameLogic, "Rolled: " + rolledDice);

                                // send dices to current player
                                Communication.sendToPlayer(CommandsServerToClient.ROLL, currentPlayer, rolledDice);

                                // send dices to all
                                Communication.broadcastToAll(CommandsServerToClient.ALDI, playerArraysList, rolledDice);

                            } else {
                                logger.log(gameLogic, "Dices were not rolled.");
                            }
                            break;
                        case "SAVE":
                            logger.trace("Entered SAVE case");

                            String[] savedDice = inputArr;

                            logger.log(gameLogic, "Save dices: " + Arrays.toString(savedDice));

                            // saves the rolled dice; if player does not want to save one, then "none" is sent
                            if (!savedDice[1].equals("none") && aboutToRoll) {
                                // turns the single String array entries into int and save the corresponding dice
                                for (int idx = 1; idx < savedDice.length; idx++) {
                                    logger.trace("Save dice " + savedDice[idx]);
                                    int idxDice = Integer.parseInt(savedDice[idx]);
                                    allDice[idxDice].saveDice();
                                }
                            } else {
                                logger.trace("None dices are selected to be saved.");
                            }
                            break;
                        case "ENTY":
                            logger.trace("Entered ENTY case");

                            if (entryMade == true) {
                                logger.info("ENTY case: player has already made an entry");
                                break;
                            }

                            if (allDiceSaved(allDice)) {
                                logger.log(gameLogic, "All dices of " + currentPlayer.getUsername() + " were saved.");

                                //Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "Select the entry to save dices to."); //TODO where to inform the player to choose an entry?

                                selectedEntry = inputArr[1];

                                logger.log(gameLogic, currentPlayer.getUsername() + " chose " + selectedEntry);

                                // validate entry
                                EntrySheet.entryValidation(currentEntrySheet, selectedEntry, allDice);

                                // sent updated entry sheet to currentPlayer
                                Communication.sendToPlayer(CommandsServerToClient.ENTY, currentPlayer, currentPlayer.getUsername() + " " + selectedEntry + ":"
                                        + currentEntrySheet.getEntryByName(selectedEntry).getValue());

                                Communication.sendToPlayer(CommandsServerToClient.PONT, currentPlayer, String.valueOf(currentEntrySheet.getTotalPoints()));

                                // sent updated sheet of the currentPlayer
                                Communication.broadcastToAll(CommandsServerToClient.ALES, playerArraysList, currentPlayer.getUsername() + " " + selectedEntry + ":"
                                        + currentEntrySheet.getEntryByName(selectedEntry).getValue());

                                logger.log(gameLogic, "Save entry " + selectedEntry + "(" + currentEntrySheet.getEntryByName(selectedEntry).getValue() + ") of " + currentPlayer.getUsername());

                                // adds action dice to player
                                addActionDice(allDice, currentPlayer);

                                entryMade = true;
                            } else {
                                logger.log(gameLogic, "Not all dices are selected to be saved.");
                            }
                            break;
                        case "STEA":
                            logger.trace("Entered STEA case");

                            if (!aboutToRoll && currentPlayer.getActionDiceCount(ActionDiceEnum.STEAL) > 0) {

                                boolean couldSteal = ActionDice.steal(currentEntrySheet, EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName), selectedEntry);

                                if (couldSteal) {
                                    logger.log(gameLogic, currentPlayer.getUsername() + " has stolen entry " + selectedEntry + " from " + victimPlayerName);

                                    // send player stolen entry
                                    Communication.sendToPlayer(CommandsServerToClient.ENTY, currentPlayer, currentPlayer.getUsername() + " " + selectedEntry + ":"
                                            + currentEntrySheet.getEntryByName(selectedEntry).getValue());

                                    Communication.broadcastToAll(CommandsServerToClient.ALES, playerArraysList, currentPlayer.getUsername() + " " + selectedEntry + ":"
                                            + currentEntrySheet.getEntryByName(selectedEntry).getValue());

                                    // send player crossed out entry
                                    Communication.sendToPlayer(CommandsServerToClient.ENTY, getPlayerByName(playerArraysList, victimPlayerName), victimPlayerName + " " + selectedEntry + ":"
                                            + EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName).getEntryByName(selectedEntry).getValue());

                                    Communication.broadcastToAll(CommandsServerToClient.ALES, playerArraysList, victimPlayerName + " " + selectedEntry + ":"
                                            + EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName).getEntryByName(selectedEntry).getValue());

                                    currentPlayer.decreaseActionDiceCount(ActionDiceEnum.STEAL);
                                    entryMade = true;
                                } else {
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "This is not a valid input, please try again!");
                                    logger.debug("false steal action tried");
                                }

                            } else {
                                logger.log(gameLogic, "No steal: aboutToRoll=" + aboutToRoll + ", stealCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.STEAL));
                            }
                            break;
                        case "FRZE":
                            logger.trace("Entered FRZE case");

                            if (currentPlayer.getActionDiceCount(ActionDiceEnum.FREEZE) > 0) {
                                boolean couldFreeze = ActionDice.freeze(currentEntrySheet, EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName), selectedEntry);

                                if (couldFreeze) {
                                    logger.log(gameLogic, currentPlayer.getUsername() + " has frozen entry " + selectedEntry + " from " + victimPlayerName);

                                    // send freeze state
                                    Communication.sendToPlayer(CommandsServerToClient.FRZE, getPlayerByName(playerArraysList, victimPlayerName), "freeze:" + selectedEntry);
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, getPlayerByName(playerArraysList, victimPlayerName),
                                            currentPlayer.getUsername() + " has frozen your " + selectedEntry + "!");

                                    currentPlayer.decreaseActionDiceCount(ActionDiceEnum.FREEZE);
                                } else {
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "This is not a valid input, please try again!");
                                    logger.debug("false freeze action tried");
                                }
                            } else {
                                logger.log(gameLogic, "No freeze: freezeCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.FREEZE));
                            }
                            break;
                        case "COUT":
                            logger.trace("Entered COUT case");

                            if (currentPlayer.getActionDiceCount(ActionDiceEnum.CROSSOUT) > 0) {
                                boolean couldCrossOut = ActionDice.crossOut(currentEntrySheet, EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName), selectedEntry);

                                if (couldCrossOut) {
                                    logger.log(gameLogic, currentPlayer + " has crossed out entry " + selectedEntry + " from " + victimPlayerName);

                                    // send cross out state
                                    Communication.sendToPlayer(CommandsServerToClient.ENTY, getPlayerByName(playerArraysList, victimPlayerName), victimPlayerName + " " + selectedEntry + ":"
                                            + EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName).getEntryByName(selectedEntry).getValue());

                                    Communication.broadcastToAll(CommandsServerToClient.ALES, playerArraysList, victimPlayerName + " " + selectedEntry + ":"
                                            + EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName).getEntryByName(selectedEntry).getValue());

                                    currentPlayer.decreaseActionDiceCount(ActionDiceEnum.CROSSOUT);
                                } else {
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "This is not a valid input, please try again!");
                                    logger.debug("false cross out action tried");
                                }
                            } else {
                                logger.log(gameLogic, "No cross out: crossOutCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.CROSSOUT));
                            }
                            break;
                        case "ENDT":
                            logger.trace("Entered ENDT case");

                            if (entryMade) {
                                logger.log(gameLogic, "Ending turn (" + currentPlayer.getUsername() + ")");
                                endTurn = true;
                            } else {
                                logger.log(gameLogic, "No ending turn; no entry was made.");
                            }
                            break;
                        default:
                            logger.trace("Entered unknown case: " + input);
                    }
                }
                // defreeze at and of turn
                currentEntrySheet.defreeze();
                Communication.sendToPlayer(CommandsServerToClient.FRZE, currentPlayer, "defreeze");
                logger.log(gameLogic, "Defreeze all entries of " + currentPlayer.getUsername());

                // reset all dice
                resetDice();
            }

            // shifting and swapping phase
            logger.log(gameLogic, "Shifting and Swapping phase started.");

            for (EntrySheet currentEntrySheet : allEntrySheets) {
                // saves values of current entry sheet, so player and current action dice, so we can access it easily
                currentPlayer = currentEntrySheet.getPlayer();

                // notify players which turn is
                Communication.broadcastToAll(CommandsServerToClient.STRT, playerArraysList, currentPlayer.getUsername() + " ShiftSwap");
                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "-- It's your turn!");

                logger.log(gameLogic, currentPlayer.getUsername() + "'s turn.");

                // checks if player wants to shift or swap
                boolean finishedSwapOrShift = false;

                while (!finishedSwapOrShift) {
                    // wait for input
                    wait();
                    String[] inputArr = input.split("\\s+");

                    logger.log(gameLogic, "Received " + input);

                    switch (inputArr[0]) {
                        case "SHFT":
                            logger.trace("Entered SHFT case");

                            if (currentPlayer.getActionDiceCount(ActionDiceEnum.SHIFT) > 0) {
                                ActionDice.shift(allEntrySheets);
                                // get player string
                                String playerShift = "";
                                for (Player player : playerArraysList) {
                                    playerShift += player.getUsername() + " ";
                                }

                                Communication.broadcastToAll(CommandsServerToClient.SHFT, playerArraysList, playerShift);
                                logger.log(gameLogic, "Shifting");

                                currentPlayer.decreaseActionDiceCount(ActionDiceEnum.SHIFT);
                            } else {
                                logger.log(gameLogic, "No shift: shiftCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.SHIFT));
                            }
                            break;
                        case "SWAP":
                            logger.trace("Entered SWAP case");

                            if (currentPlayer.getActionDiceCount(ActionDiceEnum.SWAP) > 0) {
                                ActionDice.swap(currentEntrySheet, EntrySheet.getEntrySheetByName(allEntrySheets, inputArr[1]));
                                Communication.broadcastToAll(CommandsServerToClient.SWAP, playerArraysList, currentPlayer.getUsername() + " " + inputArr[1]);

                                logger.log(gameLogic, "Swapping " + currentPlayer.getUsername() + " <-> " + inputArr[1]);

                                currentPlayer.decreaseActionDiceCount(ActionDiceEnum.SWAP);
                            } else {
                                logger.log(gameLogic, "No swap: swapCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.SWAP));
                            }
                            break;
                        case "ENDT":
                            logger.trace("Entered ENDT case");
                            logger.log(gameLogic, "Ending turn (" + currentPlayer.getUsername() + ")");
                            finishedSwapOrShift = true;

                            break;
                    }
                }
            }
        }
        logger.log(gameLogic, "Game finished.");

        //==== RANKING
        Player[] rankedPlayer = ranking(allEntrySheets);
        String rankingMsg = "";
        for (int i = 0; i < rankedPlayer.length; i++) {
            rankingMsg = rankingMsg + rankedPlayer[i].getUsername() + " "
                         + EntrySheet.getEntrySheetByName(allEntrySheets, rankedPlayer[i].getUsername()).getTotalPoints() + ",";
        }

        logger.log(gameLogic, "Ranking: " + rankingMsg);

        // sends ranking to all players in lobby
        Communication.broadcastToAll(CommandsServerToClient.RANK, playerArraysList, rankingMsg);

        // send the scores to the high score class to possibly update the highscore
        HighScore.updateHighScore(returnScoreAsString(allEntrySheets));
        logger.trace("Calling updateHighScore() on HighScore");

        // TODO: should end the game but wtf is happening (only indicates if lobby is closed or open, does not end lobby)
        logger.trace("Calling gameEnded() on lobby");
        players[0].getLobby().gameEnded();
    }

    /*
     * #################################################################################################################
     * ROLLS AND PRINTS DICE
     * #################################################################################################################
     */
    /**
     * Checks if all dice are saved.
     *
     * @param playersDice dice array of a player
     * @return true if all dice are saved and false if not
     */
    public static boolean allDiceSaved(Dice[] playersDice) {
        for (Dice d : playersDice) {
            if (!d.getSavingStatus()) {
                return false;
            }
        }
        return true;
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
     * @return string with rolled dices
     */
    public String rollDice(Dice[] playersDice) {
        String rolledDice = "";
        // handle NullPointerException if Dice array has only values null
        for (Dice dice : playersDice) {
            /* rollDice() already checks if dice has been saved or if it has been rolled 3 times already (because then dice
             * cannot be rolled. Initializes dice if it is not initialized yet to handle NullPointerException, so it can
             * be rolled after.
             */
            if (!dice.getSavingStatus()) {
                dice.rollSingleDice();
            }

            // create string of all dice values
            rolledDice = rolledDice + dice.getDiceValue() + " ";
        }
        return rolledDice;
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
    /*public boolean addActionDice(Dice[] playerDice, Player player) {
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
                //int random = 6;
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
                //int random = 6;

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
        return sum % DIVIDABLE_BY == 0;
    }*/

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

        // if the sum of all dice is dividable by 5 then add action dice
        if (sum % DIVIDABLE_BY == 0 && sum != 0) {
            // rolls action dice
            //int random = (int) Math.floor(Math.random() * 6 + 1);
            int random = 6; //for debugging purposes

            // adds action dice to existing dice of player
            switch (random) {
                case 1 -> player.increaseActionDiceCount(ActionDiceEnum.STEAL);
                case 2 -> player.increaseActionDiceCount(ActionDiceEnum.FREEZE);
                case 3 -> player.increaseActionDiceCount(ActionDiceEnum.CROSSOUT);
                case 4 -> player.increaseActionDiceCount(ActionDiceEnum.SHIFT);
                case 5 -> player.increaseActionDiceCount(ActionDiceEnum.SWAP);
                case 6 -> {
                    player.increaseActionDiceCount(ActionDiceEnum.STEAL);
                    player.increaseActionDiceCount(ActionDiceEnum.FREEZE);
                    player.increaseActionDiceCount(ActionDiceEnum.CROSSOUT);
                    player.increaseActionDiceCount(ActionDiceEnum.SHIFT);
                    player.increaseActionDiceCount(ActionDiceEnum.SWAP);
                }
            }
            Communication.sendToPlayer(CommandsServerToClient.ACTN, player, player.getActionDiceAsString());
        }
        return sum % DIVIDABLE_BY == 0;
    }

    /**
     * Deletes an action dice of players action dice list and assigns player the new list as action dice list.
     * Only one action dice gets deleted.
     *
     * @param player            player whose dice we delete
     * @param deletedActionDice dice that needs to be deleted
     */
    /*public void deleteActionDice(Player player, String deletedActionDice) {
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
    }*/

    /**
     * Gets answer as String and saves it in answer field, so it can be accessed in starter-method.
     *
     * @param input answer of player
     */
    public synchronized void getAnswer(String input, Player player) {
        logger.info("Message from " + player.getUsername() + " with <" + input + "> received");

        // Only update input if the message comes from currentPlayer
        if (player.equals(currentPlayer) && currentPlayer != null) {
            logger.info("Message from " + player.getUsername() + " accepted.");
            this.input = input;
            notify();
        } else {
            logger.info("Message from " + player.getUsername() + " denied.");
        }
    }

    /**
     * Gets the number that the dice needs to be dividable to get an action dice.
     *
     * @return number that the dice sum needs to be dividable by (important for testing)
     */
    public int getDIVIDABLE_BYE() {
        return DIVIDABLE_BY;
    }

    /**
     * Gets Player by its name when given a list as well.
     *
     * @param players list with all players in it
     * @param name of player that needs to be found
     * @return player if player exists, else null
     */
    public Player getPlayerByName(ArrayList<Player> players, String name) {
        for (Player player : players) {
            if (player.getUsername().equals(name)) {
                return player;
            }
        }
        return null;
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
     * Ranks the winners from lowest to highest (from loser to winner, so losers first rankedPlayer[0] and winner last).
     *
     * @param allEntrySheets final entry sheets of the players
     * @return Player-array with the ranked players
     */
    public Player[] ranking(EntrySheet[] allEntrySheets) {
        Player[] rankedPlayers = new Player[allEntrySheets.length];
        Arrays.sort(allEntrySheets, Comparator.comparing(EntrySheet::getTotalPoints));
        for (int i = 0; i < rankedPlayers.length; i++){
            rankedPlayers[i] = allEntrySheets[allEntrySheets.length - i - 1].getPlayer();
        }
        return rankedPlayers;
    }

    private String returnScoreAsString(EntrySheet[] allEntrySheets) {
        String ranking = "";
        for (EntrySheet sheet : allEntrySheets) {
            ranking = ranking + sheet.getTotalPoints() + ":" + sheet.getUsername() + ",";
        }
        return ranking;
    }
}