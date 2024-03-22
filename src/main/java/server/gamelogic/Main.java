package server.gamelogic;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
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
                boolean rolled = false;
                while (rolled == false) {
                    System.out.println("Please roll the dice with the command ´roll'.");
                    if (scanner.nextLine() == "roll"){
                        GameManager.rollDice(allDice);
                        rolled = true;
                    }
                }
                System.out.println("Your dice:");
                GameManager.printDice(allDice);
                System.out.println("Which dice do you want to keep? Write with a space in between the number of the dice you want to save: \n dice 1: 1 \n dice 2: 2 \n dice 3: 3 \n dice 4: 4 \n dice 5: 5 \n dice 6: 6");
                String savedDice = scanner.nextLine();



            }
        }


        // initialize scanner to
        String inputString = scanner.nextLine();

        //TODO: initialize users and

        System.out.println("Wähle");
    }
}
