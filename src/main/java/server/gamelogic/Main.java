/*package server.gamelogic;

import java.util.Scanner;

// TODO javadoc for class
public class Main {

    public static void main(String[] args) {
        // initializes players
        Player lina = new Player("LINA", 007);
        Player riccardo = new Player("RICCARDO", 002);

        Player[] allPlayer = new Player[]{lina, riccardo};

        GameManager.starter(allPlayer);
    }
}
*/

/*package server.gamelogic;

import java.util.Scanner;

public class Main {

    /*public static void main(String[] args) throws Exception {
        // initializes players
        Player lina = new Player("lina", 007);
        Player riccardo = new Player("riccardo", 002);
        Player dominique = new Player("dominique", 003);
        Player anisja = new Player("anisja", 004);

        // initializes entry sheets for each player
        EntrySheet[] allEntrySheets = new EntrySheet[]{new EntrySheet(lina), new EntrySheet(riccardo), new EntrySheet(dominique), new EntrySheet(anisja)};

        // initialize exactly 5 dice in a Dice-array
        Dice[] allDice = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};

        // starting the game
        System.out.println("########## WELCOME TO KNIFFELIGER ##########");

        // initializes scanner
        Scanner scanner = new Scanner(System.in);

        for (int round = 0; round < 13; round++){
            for (EntrySheet entrySheet : allEntrySheets){
                System.out.println(entrySheet.getUsername() + ", it is your turn!");
                // resets dice
                GameManager.resetDice(allDice);

                boolean allDiceSaved = false;
                while (allDiceSaved == false) {
                    // rolls dice
                    System.out.println("Please roll the dice with the command ´roll'.");
                    String input = scanner.nextLine();
                    if (input.equals("roll") == true){
                        GameManager.rollDice(allDice);
                        System.out.println("Your dice:");
                        GameManager.printDice(allDice);

                        // saves dice
                        System.out.println("Which dice do you want to keep? Write with a space in between the number of the dice you want to save: \n dice 1: 1 \n dice 2: 2 \n dice 3: 3 \n dice 4: 4 \n dice 5: 5 \n dice 6: 6");
                        String savedDice = scanner.nextLine();
                        String[] splitStr = savedDice.split("\\s+");
                        for (String s : splitStr){
                            int i = Integer.parseInt(s);
                            allDice[i-1].saveDice();
                        }
                        System.out.println("You saved the dice:");
                        for (Dice d : allDice){
                            if (d.getSavingStatus()) {
                                System.out.println(d.getDiceValue());
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
                }

                // choosing entry
                System.out.println("You saved all your dice, now choose an entry:\nones: 'ones'\ntwos: 'twos'\nthrees: 'threes'\nfours: 'fours'\nfives: 'fives'\nsixes: 'sixes'\nthree of a kind: 'threeOfAKind'\nfour of a kind: 'fourOfAKind'\nfull house: 'fullHouse'\nsmall straight: 'smallStraight'\nlarge straight: 'largeStraight'\nkniffeliger: 'kniffeliger'\nchance: 'chance'");
                boolean entryChoiceValid = false;
                while (entryChoiceValid == false) {
                    String entryChoice = scanner.nextLine();
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
            // action dice
        }
    }
}*/