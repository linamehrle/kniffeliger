package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EntrySheetTest {
    // default entry sheet
    Player p1 = new Player("uniqueName001", 001);
    Player p2 = new Player("uniqueName002", 002);
    // entry sheets
    EntrySheet entrySheet1 = new EntrySheet(p1);
    EntrySheet entrySheet2 = new EntrySheet(p2);
    int[] defaultEntrySheetValues = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    String[] defaultEntrySheetNames = {"ones", "twos", "threes", "fours", "fives", "sixes", "threeOfAKind", "fourOfAKind", "fullHouse", "smallStraight", "largeStraight", "kniffeliger", "chance", "pi"};

    @Test
    @DisplayName("Tests if exceptions in GameManager are handled correctly.")
    void entrySheetExceptionsTest() throws Exception {
        // generate array of random length between 6 and 100 with numbers of values between 1 and 6 inside
        int[] testArray = new int[(int) Math.floor(Math.random() * 6+ 1)];
        for (int num : testArray){
            num = (int) Math.floor(Math.random() * 100 + 7);
        }

        assertAll(() -> assertThrows(Exception.class, () -> EntrySheet.singleValueRolls(testArray, 1)),
                () -> assertThrows(Exception.class, () -> EntrySheet.singleValueRolls(testArray, 2)),
                () -> assertThrows(Exception.class, () -> EntrySheet.singleValueRolls(testArray, 3)),
                () -> assertThrows(Exception.class, () -> EntrySheet.singleValueRolls(testArray, 4)),
                () -> assertThrows(Exception.class, () -> EntrySheet.singleValueRolls(testArray, 5)),
                () -> assertThrows(Exception.class, () -> EntrySheet.singleValueRolls(testArray, 6))
        );
    }

    @Test
    @DisplayName("Tests if default of entry sheet is correct, so if resetEntrySheet() works and if methods getUsername(), getTotalPoints(), deleteEntry(), addEntry() work.")
    void getTest() {
        entrySheet2.addEntry(new Entry("ones", 1));

        assertAll(() -> assertArrayEquals(defaultEntrySheetNames, entrySheet1.getEntryNames()),
                () -> assertArrayEquals(defaultEntrySheetValues, entrySheet1.getEntryValues()),
                () -> assertEquals("uniqueName001", entrySheet1.getUsername()),
                () -> assertEquals("uniqueName002", entrySheet2.getUsername()),
                () -> assertNotEquals("username", entrySheet1.getUsername()),
                () -> assertEquals(1, entrySheet2.getEntryValues()[0]),
                () -> assertEquals(0, entrySheet1.getTotalPoints()),
                () -> assertEquals(1, entrySheet2.getTotalPoints())
        );

        // resets entrySheet2 to default
        entrySheet2.resetEntrySheet();
        assertAll(() -> assertArrayEquals(defaultEntrySheetNames, entrySheet2.getEntryNames()),
                () -> assertEquals(0, entrySheet2.getTotalPoints())
        );

        try {
            entrySheet2.addEntry(new Entry("twos", 6));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        try {
            entrySheet2.deleteEntry("twos");
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
        assertAll(() -> assertArrayEquals(defaultEntrySheetNames, entrySheet2.getEntryNames()),
                () -> assertEquals(0, entrySheet2.getTotalPoints())
        );
    }

    /*
     * #################################################################################################################
     * TESTS FOR ENTRY CHECKS
     * #################################################################################################################
     */

    //specific cases of dice rolls so the outcome can be determined.
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
        assertAll(() -> assertEquals(3, EntrySheet.singleValueRolls(ones, 1)),
                () -> assertEquals(4, EntrySheet.singleValueRolls(twos, 2)),
                () -> assertEquals(15, EntrySheet.singleValueRolls(threes, 3)),
                () -> assertEquals(8, EntrySheet.singleValueRolls(fours, 4)),
                () -> assertEquals(20, EntrySheet.singleValueRolls(fives, 5)),
                () -> assertEquals(12, EntrySheet.singleValueRolls(sixes, 6))
        );
    }

    @Test
    @DisplayName("Checks if three of a kind get detected.")
    void threeOfAKindTest(){
        assertAll(() -> assertEquals(15, EntrySheet.threeOfAKind(fives)),
                () -> assertEquals(9, EntrySheet.threeOfAKind(threes)),
                () -> assertEquals(0, EntrySheet.threeOfAKind(fours)),
                () -> assertEquals(12, EntrySheet.threeOfAKind(fourOfAKind1)),
                () -> assertEquals(15, EntrySheet.threeOfAKind(fourOfAKind2))
        );
    }

    @Test
    @DisplayName("Checks if four of a kind get detected.")
    void fourOfAKindTest(){
        assertAll(() -> assertEquals(0, EntrySheet.fourOfAKind(twos)),
                () -> assertEquals(12, EntrySheet.fourOfAKind(threes)),
                () -> assertEquals(0, EntrySheet.fourOfAKind(fours)),
                () -> assertEquals(16, EntrySheet.fourOfAKind(fourOfAKind1)),
                () -> assertEquals(20, EntrySheet.fourOfAKind(fourOfAKind2))
        );
    }

    @Test
    @DisplayName("Checks if small straight gets detected correctly.")
    void smallStraightTest(){
        assertAll(() -> assertEquals(30, EntrySheet.smallStraight(smallStraight1)),
                () -> assertEquals(30, EntrySheet.smallStraight(smallStraight2)),
                () -> assertEquals(30, EntrySheet.smallStraight(smallStraight3)),
                () -> assertEquals(0, EntrySheet.smallStraight(threes)),
                () -> assertEquals(30, EntrySheet.smallStraight(largeStraight1)),
                () -> assertEquals(30, EntrySheet.smallStraight(largeStraight2))
        );
    }

    @Test
    @DisplayName("Checks if small straight gets detected correctly.")
    void largeStraightTest(){
        assertAll(() -> assertEquals(0, EntrySheet.largeStraight(smallStraight1)),
                () -> assertEquals(0, EntrySheet.largeStraight(smallStraight2)),
                () -> assertEquals(0, EntrySheet.largeStraight(smallStraight3)),
                () -> assertEquals(0, EntrySheet.largeStraight(threes)),
                () -> assertEquals(40, EntrySheet.largeStraight(largeStraight1)),
                () -> assertEquals(40, EntrySheet.largeStraight(largeStraight2))
        );
    }

    @Test
    @DisplayName("Checks if full house gets detected.")
    void fullHouseTest(){
        assertAll(() -> assertEquals(0, EntrySheet.fullHouse(fives)),
                () -> assertEquals(25, EntrySheet.fullHouse(fullHouse)),
                () -> assertEquals(25, EntrySheet.fullHouse(threes))
        );
    }

    @Test
    @DisplayName("Checks kniffeliger methods.")
    void kniffeligerTest(){
        assertAll(() -> assertEquals(50, EntrySheet.kniffeliger(threes)),
                () -> assertEquals(0, EntrySheet.kniffeliger(fours))
        );
    }

    @Test
    @DisplayName("Checks if chance gets calculated correctly.")
    void chanceTest(){
        assertAll(() -> assertEquals(9, EntrySheet.chance(ones)),
                () -> assertEquals(10, EntrySheet.chance(twos)),
                () -> assertEquals(15, EntrySheet.chance(threes)),
                () -> assertEquals(17, EntrySheet.chance(fours)),
                () -> assertEquals(21, EntrySheet.chance(fives)),
                () -> assertEquals(23, EntrySheet.chance(sixes)),
                () -> assertEquals(19, EntrySheet.chance(fourOfAKind1)),
                () -> assertEquals(26, EntrySheet.chance(fourOfAKind2)),
                () -> assertEquals(26, EntrySheet.chance(fullHouse)),
                () -> assertEquals(11, EntrySheet.chance(smallStraight1)),
                () -> assertEquals(17, EntrySheet.chance(smallStraight2)),
                () -> assertEquals(14, EntrySheet.chance(smallStraight3)),
                () -> assertEquals(15, EntrySheet.chance(largeStraight1)),
                () -> assertEquals(20, EntrySheet.chance(largeStraight2))
        );
    }
}