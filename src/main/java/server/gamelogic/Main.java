package server.gamelogic;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws Exception {
//        // initializes players
//        Player lina = new Player("lina", 007);
//        Player riccardo = new Player("riccardo", 002);
//        Player dominique = new Player("dominique", 003);
//        Player anisja = new Player("anisja", 004);
//
//        // initializes entry sheets for each player
//        EntrySheet[] allEntrySheets = new EntrySheet[]{new EntrySheet(lina), new EntrySheet(riccardo), new EntrySheet(dominique), new EntrySheet(anisja)};
//
//        // initialize exactly 5 dice in a Dice-array
//        Dice[] allDice = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};
//
//        // starting the game
//        System.out.println("########## WELCOME TO KNIFFELIGER ##########");
//
//        // initializes scanner
//        Scanner scanner = new Scanner(System.in);
//
//        // starting the game
//        System.out.println("############################################");
//        System.out.println("############ LET THE GAME BEGIN ############");
//        System.out.println("############################################");
//
//        for (int round = 0; round < 13; round++){
//            for (EntrySheet currentEntrySheet : allEntrySheets){
//
//                // resets all dice before rolling
//                GameManager.resetDice(allDice);
//
//                // variable that checks if all dice are saved
//                boolean allDiceSaved = false;
//
//                // player associated with the current entry sheet
//                Player currentPlayer = currentEntrySheet.getPlayer();
//
//
//                while (!allDiceSaved) {
//                    // rolls dice
//                    // TODO: listen to input
//                    String input = "";
//                    if (input.equals("roll") == true){
//                        GameManager.rollDice(allDice);
//                        System.out.println("Your dice:");
//                        // TODO: Should print dice??? --> GameManager.printDice(allDice);
//
//                        // saves dice
//                        System.out.println("Which dice do you want to keep? Write with a space in between the number of the dice you want to save: \n dice 1: 1 \n dice 2: 2 \n dice 3: 3 \n dice 4: 4 \n dice 5: 5 \n dice 6: 6");
//                        // TODO: listen to input and how to split it
//                        //  gets saved dice as String and splits it
//                        String savedDice = "";
//                        String[] splitStr = savedDice.split("\\s+");
//                        for (String s : splitStr){
//                            int i = Integer.parseInt(s);
//                            allDice[i-1].saveDice();
//                        }
//                        // System.out.println("You saved the dice:");
//                        for (Dice d : allDice){
//                            if (d.getSavingStatus()) {
//                                // System.out.println(d.getDiceValue());
//                            }
//                        }
//                    }
//
//                    // checks if any unsaved dice is available to roll
//                    allDiceSaved = true;
//                    for (Dice d : allDice) {
//                        if (!d.getSavingStatus()) {
//                            allDiceSaved = false;
//                        }
//                    }
//
//                    // checks for action dice
//                    if (allDiceSaved) {
//                        if (GameManager.addActionDice(allDice)) {
//                            // TODO: Action dice is list
//                            int rolledActionDice = (int) Math.floor(Math.random() * 6 + 1);
//                            switch(rolledActionDice) {
//                                case 1:
//                                    ActionDice[] actions = new ActionDice[]("steal");
//                                case 2:
//                                    ActionDice[] freezingDice = new ActionDice[]("freeze");
//                                case 3:
//                                    ActionDice[] crossOutDice = new ActionDice[]("crossOut");
//                                case 4:
//                                    ActionDice[] shiftingDice = new ActionDice[]("shift");
//                                case 5:
//                                    ActionDice[] switchingDice = new ActionDice[]("switchEntries");
//                                case 6:
//
//                            }
//                            // ActionDice actionDice = new ActionDice();
//
//                        }
//                    }
//                }
//
//                // choosing entry
//                System.out.println("You saved all your dice, now choose an entry:\nones: 'ones'\ntwos: 'twos'\nthrees: 'threes'\nfours: 'fours'\nfives: 'fives'\nsixes: 'sixes'\nthree of a kind: 'threeOfAKind'\nfour of a kind: 'fourOfAKind'\nfull house: 'fullHouse'\nsmall straight: 'smallStraight'\nlarge straight: 'largeStraight'\nkniffeliger: 'kniffeliger'\nchance: 'chance'");
//                boolean entryChoiceValid = false;
//                while (entryChoiceValid == false) {
//                    // TODO: change scanner
//                    String entryChoice = "input";
//                    try{
//                        EntrySheet.entryValidation(currentEntrySheet, entryChoice, allDice);
//                        entryChoiceValid = true;
//                    } catch (Exception e) {
//                        System.out.println("Try another entry please!");
//                    }
//                }
//
//                System.out.println("This is your entry sheet:");
//                currentEntrySheet.printEntrySheet();
//            }
//        }
//
    }
}