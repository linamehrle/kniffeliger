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
        Dice[] dice = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};

        // starting the game
        System.out.println("########## WELCOME TO KNIFFELIGER ##########");

        for (int round = 0; round < 13; round++){
            for (EntrySheet entrySheet : allEntrySheets){

            }
        }


        // initialize scanner to
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.nextLine();

        //TODO: initialize users and

        System.out.println("Wähle");
    }
}
