package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {

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
    @DisplayName("Checks if single value entries work.")
    void singleValueRollsTest(){
        assertAll(() -> assertEquals(3, Entry.singleValueRolls(ones, 1)),
                () -> assertEquals(4, Entry.singleValueRolls(twos, 2)),
                () -> assertEquals(15, Entry.singleValueRolls(threes, 3)),
                () -> assertEquals(8, Entry.singleValueRolls(fours, 4)),
                () -> assertEquals(20, Entry.singleValueRolls(fives, 5)),
                () -> assertEquals(12, Entry.singleValueRolls(sixes, 6))
        );
    }

    @Test
    @DisplayName("Checks if three of a kind get detected.")
    void threeOfAKindTest(){
        assertAll(() -> assertEquals(15, Entry.threeOfAKind(fives)),
                () -> assertEquals(9, Entry.threeOfAKind(threes)),
                () -> assertEquals(0, Entry.threeOfAKind(fours)),
                () -> assertEquals(12, Entry.threeOfAKind(fourOfAKind1)),
                () -> assertEquals(15, Entry.threeOfAKind(fourOfAKind2))
        );
    }

    @Test
    @DisplayName("Checks if four of a kind get detected.")
    void fourOfAKindTest(){
        assertAll(() -> assertEquals(0, Entry.fourOfAKind(twos)),
                () -> assertEquals(12, Entry.fourOfAKind(threes)),
                () -> assertEquals(0, Entry.fourOfAKind(fours)),
                () -> assertEquals(16, Entry.fourOfAKind(fourOfAKind1)),
                () -> assertEquals(20, Entry.fourOfAKind(fourOfAKind2))
        );
    }

    @Test
    @DisplayName("Checks if small straight gets detected correctly.")
    void smallStraightTest(){
        assertAll(() -> assertEquals(30, Entry.smallStraight(smallStraight1)),
                () -> assertEquals(30, Entry.smallStraight(smallStraight2)),
                () -> assertEquals(30, Entry.smallStraight(smallStraight3)),
                () -> assertEquals(0, Entry.smallStraight(threes)),
                () -> assertEquals(30, Entry.smallStraight(largeStraight1)),
                () -> assertEquals(30, Entry.smallStraight(largeStraight2))
        );
    }

    @Test
    @DisplayName("Checks if small straight gets detected correctly.")
    void largeStraightTest(){
        assertAll(() -> assertEquals(0, Entry.largeStraight(smallStraight1)),
                () -> assertEquals(0, Entry.largeStraight(smallStraight2)),
                () -> assertEquals(0, Entry.largeStraight(smallStraight3)),
                () -> assertEquals(0, Entry.largeStraight(threes)),
                () -> assertEquals(40, Entry.largeStraight(largeStraight1)),
                () -> assertEquals(40, Entry.largeStraight(largeStraight2))
        );
    }

    @Test
    @DisplayName("Checks if full house gets detected.")
    void fullHouseTest(){
        assertAll(() -> assertEquals(0, Entry.fullHouse(fives)),
                () -> assertEquals(25, Entry.fullHouse(fullHouse)),
                () -> assertEquals(25, Entry.fullHouse(threes))
        );
    }

    @Test
    @DisplayName("Checks kniffeliger methods.")
    void kniffeligerTest(){
        assertAll(() -> assertEquals(50, Entry.kniffeliger(threes)),
                () -> assertEquals(0, Entry.kniffeliger(fours))
        );
    }

    @Test
    @DisplayName("Checks if chance gets calculated correctly.")
    void chanceTest(){
        assertAll(() -> assertEquals(9, Entry.chance(ones)),
                () -> assertEquals(10, Entry.chance(twos)),
                () -> assertEquals(15, Entry.chance(threes)),
                () -> assertEquals(17, Entry.chance(fours)),
                () -> assertEquals(21, Entry.chance(fives)),
                () -> assertEquals(23, Entry.chance(sixes)),
                () -> assertEquals(19, Entry.chance(fourOfAKind1)),
                () -> assertEquals(26, Entry.chance(fourOfAKind2)),
                () -> assertEquals(26, Entry.chance(fullHouse)),
                () -> assertEquals(11, Entry.chance(smallStraight1)),
                () -> assertEquals(17, Entry.chance(smallStraight2)),
                () -> assertEquals(14, Entry.chance(smallStraight3)),
                () -> assertEquals(15, Entry.chance(largeStraight1)),
                () -> assertEquals(20, Entry.chance(largeStraight2))
        );
    }
}