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

    private EntrySheet[] allEntrySheets;

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

    public void prepareForStart() {
        logger.info("Preparing for the game start");
        //initializes the hashMap of action dices in the player and sets the boolean isInGame to true
        for (Player player : playerArraysList) {
            player.prepareForGame();
            Communication.sendToPlayer(CommandsServerToClient.STRG, player, "prepare the game start");
        }
    }

    /**
     * This method runs the whole game, keeps the game play in the right order and checks all inputs.
     *
     * @throws InterruptedException exception when thread has an error
     */
    public synchronized void starter() throws InterruptedException {
        logger.trace("starter()");
        wait(100);
        logger.trace("finished waiting");

        //initializes the hashMap of action dices in the player and sets the boolean isInGame to true
        for (Player player : playerArraysList) {
            player.prepareForGame();
        }

        // initializes entry sheets for each player and saves all in an array
        Player[] players = new Player[playerArraysList.size()];
        for (int i = 0; i < playerArraysList.size(); i++) {
            players[i] = playerArraysList.get(i);
        }
        allEntrySheets = new EntrySheet[players.length];
        for (int i = 0; i < players.length; i++) {
            allEntrySheets[i] = new EntrySheet(players[i]);
            //initiates all entry sheets in the gui
            Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList, allEntrySheets[i].printEntrySheet());
            //the player has 0 points at the beginning of the game (needed for restart)
            Communication.sendToPlayer(CommandsServerToClient.PONT, players[i], "0");
            //the player has 0 action dice at the beginning of the game (needed for restart)
            Communication.sendToPlayer(CommandsServerToClient.ACTN, players[i], players[i].getActionDiceAsString());
        }

        // starting the game and sending all players in lobby a message
        logger.log(gameLogic, "Game lobby with " + playerArraysList + " started.");

        Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList, "The game starts.");

        // starting 14 rounds
        for (int round = 0; round < ROUNDS; round++) {
            logger.log(gameLogic, "Round " + (round + 1) + " started");

            // loop through all the players
            for (EntrySheet currentEntrySheet : allEntrySheets) {
                logger.debug("new main phase with " + currentEntrySheet.getUsername());
                // saves values of current entry sheet, so player and current action dice, so we can access it easily
                currentPlayer = currentEntrySheet.getPlayer();

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
                Communication.broadcastToAll(CommandsServerToClient.STRT, playerArraysList,
                        currentPlayer.getUsername() + " Main");
                Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList, "It's the main phase of the game!");
                Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList,
                        currentPlayer.getUsername() + "'s turn.");
                Communication.sendToPlayer(CommandsServerToClient.BRCT,
                        currentPlayer, "-- It's your turn!");

                logger.log(gameLogic, currentPlayer.getUsername() + "'s turn.");


                // if player is not connected anymore, then the game manager handles it with playForPlayer method
                // and sets it true that an entry was made and the turn is ended
                if (!currentPlayer.isOnline()) {
                    logger.info("Current player is not online");

                    Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList,
                            "The player " + currentPlayer.getUsername() + " is not online anymore, playing for them.");

                    // entry is made for player and communicated afterwards
                    playForPlayer(currentPlayer, currentEntrySheet, allDice);
                    Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList, currentEntrySheet.printEntrySheet());
                    entryMade = true;
                    endTurn = true;
                    wait(100);
                }

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
                                //Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList, "ALEA IACTA EST! (the die is cast)");

                                if (allDiceSaved(allDice)) {
                                    Communication.sendToPlayer(CommandsServerToClient.SAVE, currentPlayer, "0 1 2 3 4 ");
                                }

                            } else {
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "You can not roll at the moment!");
                                logger.log(gameLogic, "Dices were not rolled.");
                            }
                            break;
                        case "SAVE":
                            logger.trace("Entered SAVE case");

                            String[] savedDice = inputArr;

                            logger.log(gameLogic, "Save dices: " + Arrays.toString(savedDice));

                            logger.debug("abutToRoll: " + aboutToRoll);
                            // saves the rolled dice; if player does not want to save one, then "none" is sent
                            if (!savedDice[1].equals("none") && aboutToRoll) {
                                // turns the single String array entries into int and save the corresponding dice
                                for (int idx = 1; idx < savedDice.length; idx++) {
                                    logger.trace("Save dice " + savedDice[idx]);
                                    int idxDice = Integer.parseInt(savedDice[idx]);
                                    allDice[idxDice].saveDice();
                                }
                            } else {
                                logger.trace("No dices are selected to be saved.");
                            }

                            //return all dice that are currently saved to the client to possibly update the gui
                            String allSavedDice = "";
                            for (int i = 0; i < allDice.length; i++) {
                                if (allDice[i].getSavingStatus()) {
                                    allSavedDice = allSavedDice + i + " ";
                                }
                            }
                            Communication.sendToPlayer(CommandsServerToClient.SAVE, currentPlayer, allSavedDice);

                            break;
                        case "ENTY":
                            logger.trace("Entered ENTY case");
                            logger.debug("The current entry sheets player is: " + currentPlayer.getUsername());

                            if (entryMade) {
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer,
                                        "You already made an entry");
                                logger.info("ENTY case: player has already made an entry");
                                break;
                            }

                            if (allDiceSaved(allDice)) {
                                logger.log(gameLogic, "All dices of " + currentPlayer.getUsername() + " were saved.");

                                //Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "Select the entry to save dices to."); //TODO where to inform the player to choose an entry?

                                selectedEntry = inputArr[1];

                                logger.log(gameLogic, currentPlayer.getUsername() + " chose " + selectedEntry);

                                // validate entry
                                boolean madeEntry = EntrySheet.entryValidation(currentEntrySheet, selectedEntry, allDice);

                                if (madeEntry) {
                                    entryMade = true;
                                    Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList, currentEntrySheet.printEntrySheet());
                                    Communication.sendToPlayer(CommandsServerToClient.PONT, currentPlayer,
                                            String.valueOf(currentEntrySheet.getTotalPoints()));

                                    logger.log(gameLogic, "Save entry " + selectedEntry + "(" + currentEntrySheet.getEntryByName(selectedEntry).getValue() + ") of " + currentPlayer.getUsername());

                                    // adds action dice to player
                                    if (addActionDice(allDice, currentPlayer)) {
                                        Communication.sendToPlayer(CommandsServerToClient.ACTN, currentPlayer, currentPlayer.getActionDiceAsString());
                                        Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "You got a new action die.");
                                    }

                                    entryMade = true;
                                } else {
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "You can't choose this entry, please try again!");
                                    logger.debug("false enter action tried");
                                }
                            } else {
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "Save all your dice first");
                                logger.log(gameLogic, "Not all dices are selected to be saved.");
                            }
                            break;
                        case "STEA":
                            logger.trace("Entered STEA case");

                            if (entryMade) {
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "You already made an entry");
                                break;
                            }

                            if (aboutToRoll) {
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer,
                                        "You cannot use the steal action once you started to roll your dice");
                            }

                            if (!aboutToRoll && currentPlayer.getActionDiceCount(ActionDiceEnum.STEAL) > 0) {

                                boolean couldSteal = ActionDice.steal(currentEntrySheet, EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName), selectedEntry);

                                if (couldSteal) {
                                    logger.log(gameLogic, currentPlayer.getUsername() + " has stolen entry " + selectedEntry + " from " + victimPlayerName);

                                    //Communicates the new entry sheets
                                    Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList, currentEntrySheet.printEntrySheet());
                                    Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList,
                                            EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName).printEntrySheet());

                                    //Communicates the new Points
                                    Communication.sendToPlayer(CommandsServerToClient.PONT, currentPlayer, String.valueOf(currentEntrySheet.getTotalPoints()));
                                    Communication.sendToPlayer(CommandsServerToClient.PONT, getPlayerByName(playerArraysList, victimPlayerName),
                                            String.valueOf(EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName).getTotalPoints()));

                                    currentPlayer.decreaseActionDiceCount(ActionDiceEnum.STEAL);
                                    Communication.sendToPlayer(CommandsServerToClient.ACTN, currentPlayer, currentPlayer.getActionDiceAsString());
                                    entryMade = true;
                                } else {
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "This is not a valid input, please try again!");
                                    logger.debug("false steal action tried");
                                }

                            } else {
                                logger.log(gameLogic, "No steal: aboutToRoll=" + aboutToRoll + ", stealCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.STEAL));
                                Communication.sendToPlayer(CommandsServerToClient.BRCT,currentPlayer, "You don't have this action die!");
                            }
                            break;
                        case "FRZE":
                            logger.trace("Entered FRZE case");

                            //you cannot freeze in the round before the last round or the last round or else the person cannot
                            // make an entry at all and we have a problem
                            if (round >= ROUNDS - 2) {
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "You cannot freeze an entry in this round");
                                break;
                            }

                            if (currentPlayer.getActionDiceCount(ActionDiceEnum.FREEZE) > 0) {
                                boolean couldFreeze = ActionDice.freeze(currentEntrySheet, EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName), selectedEntry);

                                if (couldFreeze) {
                                    logger.log(gameLogic, currentPlayer.getUsername() + " has frozen entry " + selectedEntry + " from " + victimPlayerName);

                                    // send freeze state
                                    Communication.sendToPlayer(CommandsServerToClient.FRZE, getPlayerByName(playerArraysList, victimPlayerName), "freeze:" + selectedEntry);
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, getPlayerByName(playerArraysList, victimPlayerName),
                                            currentPlayer.getUsername() + " has frozen your " + selectedEntry + "!");

                                    currentPlayer.decreaseActionDiceCount(ActionDiceEnum.FREEZE);
                                    Communication.sendToPlayer(CommandsServerToClient.ACTN, currentPlayer, currentPlayer.getActionDiceAsString());
                                } else {
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "This is not a valid input, please try again!");
                                    logger.debug("false freeze action tried");
                                }
                            } else {
                                logger.log(gameLogic, "No freeze: freezeCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.FREEZE));
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "You don't have this action die!");
                            }
                            break;
                        case "COUT":
                            logger.trace("Entered COUT case");

                            if (currentPlayer.getActionDiceCount(ActionDiceEnum.CROSSOUT) > 0) {
                                boolean couldCrossOut = ActionDice.crossOut(currentEntrySheet, EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName), selectedEntry);

                                if (couldCrossOut) {
                                    logger.log(gameLogic, currentPlayer.getUsername() + " has crossed out entry " + selectedEntry + " from " + victimPlayerName);

                                    //Communicates the new entry sheets
                                    Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList, currentEntrySheet.printEntrySheet());
                                    Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList,
                                            EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName).printEntrySheet());

                                    //Communicates the new Points
                                    Communication.sendToPlayer(CommandsServerToClient.PONT, currentPlayer, String.valueOf(currentEntrySheet.getTotalPoints()));
                                    Communication.sendToPlayer(CommandsServerToClient.PONT, getPlayerByName(playerArraysList, victimPlayerName),
                                            String.valueOf(EntrySheet.getEntrySheetByName(allEntrySheets, victimPlayerName).getTotalPoints()));

                                    currentPlayer.decreaseActionDiceCount(ActionDiceEnum.CROSSOUT);
                                    Communication.sendToPlayer(CommandsServerToClient.ACTN, currentPlayer, currentPlayer.getActionDiceAsString());
                                } else {
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "This is not a valid input, please try again!");
                                    logger.debug("false cross out action tried");
                                }
                            } else {
                                logger.log(gameLogic, "No cross out: crossOutCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.CROSSOUT));
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "You don't have this action die!");
                            }
                            break;
                        case "ENDT":
                            logger.trace("Entered ENDT case");

                            // TODO: ENDT should always work: if no entry was made then make one entry 0 and continue the game

                            if (entryMade) {
                                logger.log(gameLogic, "Ending turn (" + currentPlayer.getUsername() + ")");
                                endTurn = true;
                                Communication.sendToPlayer(CommandsServerToClient.ENDT, currentPlayer, "turn endet");
                            } else {
                                logger.log(gameLogic, "No ending turn; no entry was made.");
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "Make an entry first");
                                for (Entry entry : currentEntrySheet.getAsArray()) {
                                    if (!entry.getIsFinal()){
                                        Entry newEntry = new Entry(entry.getName(), 0);
                                        currentEntrySheet.addEntry(newEntry);
                                        endTurn = true;
                                        break;
                                    }
                                }
                                logger.log(gameLogic, "Ending turn (" + currentPlayer.getUsername() + ")");
                            }
                            break;
                        case "NOTONLINE":
                            Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList,
                                    "The player " + currentPlayer.getUsername() + " is not online anymore, playing for them.");
                            // entry is made for player and communicated afterward
                            playForPlayer(currentPlayer, currentEntrySheet, allDice);
                            Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList, currentEntrySheet.printEntrySheet());
                            entryMade = true;
                            endTurn = true;
                            break;
                        default:
                            logger.trace("Entered unknown case: " + input);
                            break;
                    }
                }
                // defreeze at and of turn
                currentEntrySheet.defreeze();
                Communication.sendToPlayer(CommandsServerToClient.FRZE, currentPlayer, "defreeze");
                logger.log(gameLogic, "Defreeze all entries of " +currentPlayer.getUsername());

                // reset all dice
                resetDice();
            }

            // shifting and swapping phase
            logger.log(gameLogic, "Shifting and Swapping phase started.");

            for (Player player : playerArraysList) {
                logger.debug("new shift and swap round with player " + player.getUsername());
                currentPlayer = player;

                // saves values of current entry sheet, so player and current action dice, so we can access it easily
                EntrySheet currentEntrySheet = EntrySheet.getEntrySheetByName(allEntrySheets, currentPlayer.getUsername());

                // notify players which turn is
                Communication.broadcastToAll(CommandsServerToClient.STRT, playerArraysList, currentPlayer.getUsername() + " ShiftSwap");
                Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList, "It's the shift and swap phase of the game!");
                Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList, "Its " + currentPlayer.getUsername() + "'s turn!");
                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "-- It's your turn!");

                logger.log(gameLogic, currentPlayer.getUsername() + "'s turn.");

                // checks if player wants to shift or swap
                boolean finishedSwapOrShift = false;

                //again connection loss handling
                if (!currentPlayer.isOnline()) {
                    logger.info("Shift and swap phase: current player is not online");
                    finishedSwapOrShift = true;
                }

                while (!finishedSwapOrShift && currentPlayer.isOnline()) {
                    logger.debug("Entered while loop for shift and swap");
                    // wait for input
                    wait();
                    String[] inputArr = input.split("\\s+");

                    logger.log(gameLogic, "Received " + input);

                    switch (inputArr[0]) {
                        case "SHFT":
                            logger.trace("Entered SHFT case");

                            if (currentPlayer.getActionDiceCount(ActionDiceEnum.SHIFT) > 0) {
                                ActionDice.shift(allEntrySheets);

                                //current entry sheet has to be updated since it changed
                                currentEntrySheet = EntrySheet.getEntrySheetByName(allEntrySheets, currentPlayer.getUsername());

                                for (EntrySheet sheet : allEntrySheets) {
                                    Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList, sheet.printEntrySheet());
                                    Communication.sendToPlayer(CommandsServerToClient.PONT, sheet.getPlayer(), String.valueOf(sheet.getTotalPoints()));
                                }

                                logger.log(gameLogic, "Shifting");

                                currentPlayer.decreaseActionDiceCount(ActionDiceEnum.SHIFT);
                                Communication.sendToPlayer(CommandsServerToClient.ACTN, currentPlayer, currentPlayer.getActionDiceAsString());

                            } else {
                                logger.log(gameLogic, "No shift: shiftCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.SHIFT));
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "You don't have this action die!");
                            }
                            break;
                        case "SWAP":
                            logger.trace("Entered SWAP case");

                            if (currentPlayer.getActionDiceCount(ActionDiceEnum.SWAP) > 0) {
                                boolean couldSwap = ActionDice.swap(currentEntrySheet, EntrySheet.getEntrySheetByName(allEntrySheets, inputArr[1]));
                                if (couldSwap) {
                                    //current entry sheet has to be updated since it changed
                                    currentEntrySheet = EntrySheet.getEntrySheetByName(allEntrySheets, currentPlayer.getUsername());

                                    //Update the entry sheets
                                    Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList,
                                            EntrySheet.getEntrySheetByName(allEntrySheets, currentPlayer.getUsername()).printEntrySheet());
                                    Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList,
                                            EntrySheet.getEntrySheetByName(allEntrySheets, inputArr[1]).printEntrySheet());

                                    //update the points
                                    Communication.sendToPlayer(CommandsServerToClient.PONT, currentPlayer,
                                            String.valueOf(EntrySheet.getEntrySheetByName(allEntrySheets, currentPlayer.getUsername()).getTotalPoints()));
                                    Communication.sendToPlayer(CommandsServerToClient.PONT, getPlayerByName(playerArraysList, inputArr[1]),
                                            String.valueOf(EntrySheet.getEntrySheetByName(allEntrySheets, inputArr[1]).getTotalPoints()));

                                    logger.log(gameLogic, "Swapping " +currentPlayer.getUsername() + " <-> " + inputArr[1]);

                                    currentPlayer.decreaseActionDiceCount(ActionDiceEnum.SWAP);
                                    Communication.sendToPlayer(CommandsServerToClient.ACTN, currentPlayer, currentPlayer.getActionDiceAsString());
                                } else {
                                    Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "This is not a valid input, please try again!");
                                    logger.debug("false swap action tried");
                                }
                            } else {
                                Communication.sendToPlayer(CommandsServerToClient.BRCT, currentPlayer, "You don't have this action die!");
                                logger.log(gameLogic, "No swap: swapCount=" + currentPlayer.getActionDiceCount(ActionDiceEnum.SWAP));
                            }
                            break;
                        case "ENDT":
                            logger.trace("Entered ENDT case of shift and swap");
                            logger.log(gameLogic, "Ending turn (" + currentPlayer.getUsername() + ")");
                            finishedSwapOrShift = true;

                            break;
                        case "NOTONLINE":
                            Communication.broadcastToAll(CommandsServerToClient.BRCT, playerArraysList,
                                    "The player " + currentPlayer.getUsername() + " is not online anymore, playing for them.");
                            // entry is made for player and communicated afterward
                            playForPlayer(currentPlayer, currentEntrySheet, allDice);
                            Communication.broadcastToAll(CommandsServerToClient.ENTY, playerArraysList, currentEntrySheet.printEntrySheet());
                            finishedSwapOrShift = true;
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
        playerArraysList.get(1).getLobby().gameEnded();
    }

    /*
     * #################################################################################################################
     * ROLLS DICE
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
     * Checks if sum of all dice is modulo 3 and randomly adds action dice, if so.
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
            int random = (int) Math.floor(Math.random() * 6 + 1);
            //int random = 6; //for debugging purposes

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
            return true;
        }
        return false;
    }


    /*
     * #################################################################################################################
     * HANDLES EVERYTHING ELSE (GET METHODS, RANKING, CONNECTION LOSS HANDLING)
     * #################################################################################################################
     */

    /**
     * Handles connection loss: if a player is not connected then the game manager plays automatically for him/her.
     */
    public synchronized void playForPlayer(Player player, EntrySheet playersEntrySheet, Dice[] allDice) {
        // rolls all dice and saves them after first roll
        rollDice(allDice);
        for (Dice d : allDice) {
            d.saveDice();
        }

        // goes through entry sheet and checks for the first entry that is not final or frozen to add the entry
        // through the
        // entry validation
        for (Entry entry : playersEntrySheet.getAsArray()) {
            if (!entry.getIsFinal() && !entry.getFrozenStatus()) {
                EntrySheet.entryValidation(playersEntrySheet, entry.getName(), allDice);
                break;
            }
        }

        logger.debug("finished playing for disconnected player");

        //TODO also add entry dice here? For a possible reconnect (that wont happen lol)
    }

    /**
     * Gets answer as String and saves it in answer field, so it can be accessed in starter-method.
     * @param input answer of player
     */
    public synchronized void getAnswer(String input, Player player) {
        logger.info("Message from " + player.getUsername() + " with <" + input + "> received");

        // Only update input if the message comes from currentPlayer
        if (currentPlayer != null && player.equals(currentPlayer)) {
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
    public int getDIVIDABLE_BY() {
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

    public void cheatCode(Player player) {
        //if the player uses the cheat code for the first time, they get all action dice once
        if (player.getCheatCodesUsed() == 0) {
            player.increaseActionDiceCount(ActionDiceEnum.STEAL);
            player.increaseActionDiceCount(ActionDiceEnum.FREEZE);
            player.increaseActionDiceCount(ActionDiceEnum.CROSSOUT);
            player.increaseActionDiceCount(ActionDiceEnum.SHIFT);
            player.increaseActionDiceCount(ActionDiceEnum.SWAP);
        } else {
            player.removeAllActionDice();
            EntrySheet.getEntrySheetByName(allEntrySheets, player.getUsername()).punishCheatCodes();
            Communication.sendToPlayer(CommandsServerToClient.ENTY, player,
                    EntrySheet.getEntrySheetByName(allEntrySheets, player.getUsername()).printEntrySheet());
            Communication.sendToPlayer(CommandsServerToClient.PONT, player,
                    String.valueOf(EntrySheet.getEntrySheetByName(allEntrySheets, player.getUsername()).getTotalPoints()));
            Communication.sendToPlayer(CommandsServerToClient.BRCT, player, "You got the malus incomplete lvl 3: -50 Points!");
        }
        Communication.sendToPlayer(CommandsServerToClient.ACTN, player, player.getActionDiceAsString());
        player.setCheatCodesUsed(player.getCheatCodesUsed() + 1);
    }
}