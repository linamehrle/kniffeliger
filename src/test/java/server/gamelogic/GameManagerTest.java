package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// asserThrows
// augenzahl <= 25
class GameManagerTest {

    /*
     * Tests specific cases of dice rolls so the outcome can be determined.
    */
    int[] ones = {2, 1, 1, 4, 1};
    int[] twos = {2, 1, 1, 4, 2};
    int[] threes = {3, 3, 3, 3, 3};
    int[] fours = {2, 6, 4, 4, 1};
    int[] fives = {5, 1, 5, 5, 5};
    int[] sixes = {3, 6, 3, 6, 5};
    int[] fourOfAKind1 = {4, 3, 4, 4, 4};
    int[] fourOfAKind2 = {6, 5, 5, 5, 5};
    int[] fullHouse = {6, 4, 6, 4, 6};
    int[] smallStraight1 = {1, 2, 3, 4, 1};
    int[] smallStraight2 = {2, 3, 3, 5, 4};
    int[] smallStraight3 = {4, 4, 3, 2, 1};
    int[] largeStraight1 = {1, 2, 3, 4, 5};
    int[] largeStraight2 = {2, 3, 4, 5, 6};

    @Test
    @DisplayName("Tests if exceptions get thrown correctly.")
    void gameManagerExceptionsTest() {
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

        assertAll(() -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(largeRandomArray, 1)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(largeRandomArray, 2)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(largeRandomArray, 3)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(largeRandomArray, 4)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(largeRandomArray, 5)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(largeRandomArray, 6)),
                () -> assertThrows(Exception.class, () -> GameManager.threeOfAKind(largeRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.fourOfAKind(largeRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.fullHouse(largeRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.smallStraight(largeRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.largeStraight(largeRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.kniffeliger(largeRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.chance(largeRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(smallRandomArray, 1)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(smallRandomArray, 2)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(smallRandomArray, 3)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(smallRandomArray, 4)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(smallRandomArray, 5)),
                () -> assertThrows(Exception.class, () -> GameManager.singleValueRolls(smallRandomArray, 6)),
                () -> assertThrows(Exception.class, () -> GameManager.threeOfAKind(smallRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.fourOfAKind(smallRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.fullHouse(smallRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.smallStraight(smallStraight1)),
                () -> assertThrows(Exception.class, () -> GameManager.largeStraight(smallRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.kniffeliger(smallRandomArray)),
                () -> assertThrows(Exception.class, () -> GameManager.chance(smallRandomArray))
        );
    }


    @Test
    @DisplayName("Checks if single value entries work.")
    void singleValueRollsTest(){
        assertAll(() -> assertEquals(3, GameManager.singleValueRolls(ones, 1)),
                () -> assertEquals(4, GameManager.singleValueRolls(twos, 2)),
                () -> assertEquals(15, GameManager.singleValueRolls(threes, 3)),
                () -> assertEquals(8, GameManager.singleValueRolls(fours, 4)),
                () -> assertEquals(20, GameManager.singleValueRolls(fives, 5)),
                () -> assertEquals(12, GameManager.singleValueRolls(sixes, 6))
        );
    }

    @Test
    @DisplayName("Checks if three of a kind get detected.")
    void threeOfAKindTest(){
        assertAll(() -> assertEquals(15, GameManager.threeOfAKind(fives)),
                () -> assertEquals(9, GameManager.threeOfAKind(threes)),
                () -> assertEquals(0, GameManager.threeOfAKind(fours)),
                () -> assertEquals(12, GameManager.threeOfAKind(fourOfAKind1)),
                () -> assertEquals(15, GameManager.threeOfAKind(fourOfAKind2))
        );
    }

    @Test
    @DisplayName("Checks if four of a kind get detected.")
    void fourOfAKindTest(){
        assertAll(() -> assertEquals(0, GameManager.fourOfAKind(twos)),
                () -> assertEquals(12, GameManager.fourOfAKind(threes)),
                () -> assertEquals(0, GameManager.fourOfAKind(fours)),
                () -> assertEquals(16, GameManager.fourOfAKind(fourOfAKind1)),
                () -> assertEquals(20, GameManager.fourOfAKind(fourOfAKind2))
        );
    }

    @Test
    @DisplayName("Checks if small straight gets detected correctly.")
    void smallStraightTest(){
        assertAll(() -> assertEquals(30, GameManager.smallStraight(smallStraight1)),
                () -> assertEquals(30, GameManager.smallStraight(smallStraight2)),
                () -> assertEquals(30, GameManager.smallStraight(smallStraight3)),
                () -> assertEquals(0, GameManager.smallStraight(threes)),
                () -> assertEquals(30, GameManager.smallStraight(largeStraight1)),
                () -> assertEquals(30, GameManager.smallStraight(largeStraight2))
        );
    }

    @Test
    @DisplayName("Checks if small straight gets detected correctly.")
    void largeStraightTest(){
        assertAll(() -> assertEquals(0, GameManager.largeStraight(smallStraight1)),
                () -> assertEquals(0, GameManager.largeStraight(smallStraight2)),
                () -> assertEquals(0, GameManager.largeStraight(smallStraight3)),
                () -> assertEquals(0, GameManager.largeStraight(threes)),
                () -> assertEquals(40, GameManager.largeStraight(largeStraight1)),
                () -> assertEquals(40, GameManager.largeStraight(largeStraight2))
        );
    }

    @Test
    @DisplayName("Checks if full house gets detected.")
    void fullHouseTest(){
        assertAll(() -> assertEquals(0, GameManager.fullHouse(fives)),
                () -> assertEquals(25, GameManager.fullHouse(fullHouse)),
                () -> assertEquals(25, GameManager.fullHouse(threes))
        );
    }

    @Test
    @DisplayName("Checks kniffeliger methods.")
    void kniffeligerTest(){
        assertAll(() -> assertEquals(50, GameManager.kniffeliger(threes)),
                () -> assertEquals(0, GameManager.kniffeliger(fours))
        );
    }

    @Test
    @DisplayName("Checks if chance gets calculated correctly.")
    void chanceTest(){
        assertAll(() -> assertEquals(9, GameManager.chance(ones)),
                () -> assertEquals(10, GameManager.chance(twos)),
                () -> assertEquals(15, GameManager.chance(threes)),
                () -> assertEquals(17, GameManager.chance(fours)),
                () -> assertEquals(21, GameManager.chance(fives)),
                () -> assertEquals(23, GameManager.chance(sixes)),
                () -> assertEquals(19, GameManager.chance(fourOfAKind1)),
                () -> assertEquals(26, GameManager.chance(fourOfAKind2)),
                () -> assertEquals(26, GameManager.chance(fullHouse)),
                () -> assertEquals(11, GameManager.chance(smallStraight1)),
                () -> assertEquals(17, GameManager.chance(smallStraight2)),
                () -> assertEquals(14, GameManager.chance(smallStraight3)),
                () -> assertEquals(15, GameManager.chance(largeStraight1)),
                () -> assertEquals(20, GameManager.chance(largeStraight2))
        );
    }
}