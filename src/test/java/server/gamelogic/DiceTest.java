package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DiceTest {

    @Test
    @DisplayName("Checks if dice gets rolled correctly and all the variables get adjusted.")
    void rollDiceTest(){
        // three dices, one gets rolled three times, one once, the other doesn't
        // should return three rolls at most
        // should return values between 1 and 6
        Dice dice1 = new Dice();
        Dice dice2 = new Dice();
        Dice dice3 = new Dice();
        dice2.rollDice();
        dice3.rollDice();
        dice3.rollDice();
        dice3.rollDice();

        assertAll(() -> assertEquals(0, dice1.getNumberOfRolls()),
                () -> assertEquals(1, dice2.getNumberOfRolls()),
                () -> assertEquals(3, dice3.getNumberOfRolls()),
                () -> assertEquals(0, dice1.getNumberOfRolls()),
                () -> assertEquals(1, dice2.getNumberOfRolls()),
                () -> assertEquals(3, dice3.getNumberOfRolls()),
                () -> assertTrue(dice1.rollDice()),
                () -> assertTrue(dice2.rollDice()),
                () -> assertFalse(dice3.rollDice()),
                () -> assertFalse(dice1.getSavingStatus()),
                () -> assertFalse(dice2.getSavingStatus()),
                () -> assertTrue(dice3.getSavingStatus()),
                () -> assertTrue(dice1.getDiceValue() <= 6 && dice1.getDiceValue() >= 1),
                () -> assertTrue(dice2.getDiceValue() <= 6 && dice2.getDiceValue() >= 1),
                () -> assertTrue(dice3.getDiceValue() <= 6 && dice3.getDiceValue() >= 1)
                );
    }

    @Test
    @DisplayName("Checks if you can roll dice after saving it.")
    void saveDiceTest(){
        Dice dice1 = new Dice();
        Dice dice2 = new Dice();
        dice2.saveDice();

        assertAll(() -> assertFalse(dice1.getSavingStatus()),
                () -> assertTrue(dice2.getSavingStatus()),
                () -> assertTrue(dice1.rollDice()),
                () -> assertFalse(dice2.rollDice())
        );
    }

    @Test
    @DisplayName("Checks if reset function works.")
    void resetDiceTest(){
        Dice dice = new Dice();
        dice.rollDice();
        dice.resetDice();

        assertAll(() -> assertEquals(0, dice.getNumberOfRolls()),
                () -> assertFalse(dice.getSavingStatus()),
                () -> assertEquals(0, dice.getNumberOfRolls()),
                () -> assertEquals(0, dice.getDiceValue()),
                () -> assertTrue(dice.rollDice())
        );

    }
}