package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameManagerTest {

    /*
     * #################################################################################################################
     * TESTS IF EXCEPTIONS GET THROWN CORRECTLY
     * #################################################################################################################
     */
    @Test
    @DisplayName("Tests if exceptions int GameManager are handled correctly.")
    void gameManagerExceptionsTest() throws Exception {
        // Dice array to test rollDice() method
        Dice[] largeDiceArray = new Dice[(int) Math.floor(Math.random() * 100+ 6)];
        Dice[] smallDiceArray = new Dice[(int) Math.floor(Math.random() * 4+ 1)];

        // generate array of random length between 6 and 100 with numbers of values between 1 and 6 inside
        int[] largeRandomArray = new int[(int) Math.floor(Math.random() * 100+ 6)];
        for (int num : largeRandomArray){
            num = (int) Math.floor(Math.random() * 6+ 1);
        }

        // generate array of random length between 1 and 5 with numbers of values between 1 and 6 inside
        int[] smallRandomArray = new int[(int) Math.floor(Math.random() * 4+ 1)];
        for (int num : smallRandomArray){
            num = (int) Math.floor(Math.random() * 6+ 1);
        }

        assertAll(() -> assertThrows(Exception.class, () -> GameManager.rollDice(largeDiceArray)),
                () -> assertThrows(Exception.class, () -> GameManager.rollDice(smallDiceArray))
        );
    }

    //TODO: reset dice testing
    //TODO: check for action dice testing
    //TODO:
}