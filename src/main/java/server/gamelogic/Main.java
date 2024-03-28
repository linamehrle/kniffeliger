package server.gamelogic;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // initializes players
        Player lina = new Player("lina", 007);
        Player riccardo = new Player("riccardo", 002);
        Player dominique = new Player("dominique", 003);
        Player anisja = new Player("anisja", 004);

        Player[] allPlayer = new Player[]{lina, riccardo, dominique, anisja};

        GameManager.starter(allPlayer);
    }
}