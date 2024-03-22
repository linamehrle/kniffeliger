package server.gamelogic;

import java.util.Scanner;

public class Main {

    public static void main(String[] args){
        // initialize exactly 5 dice in a Dice-array
        Dice[] dice = new Dice[]{new Dice(), new Dice(), new Dice(), new Dice(), new Dice()};
        GameManager.rollDice(dice);

        // initialize scanner to
        Scanner scanner = new Scanner(System.in);
        String inputString = scanner.nextLine();


        System.out.println("Wähle");
    }
}
