package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EntrySheetTest {
    /*
     * ##################################################################################################################
     * DummyPlayer INITIATION
     * ##################################################################################################################
     */

    /**
     * Dummy player class to test methods. Contains all important fields and methods of a player.
     */
    public class DummyPlayer extends server.Player{
        private ActionDice[] actionDice;

        /**
         * The constructor for the Player class. It starts a new ClientThread per Player.
         */
        public DummyPlayer(String name) {
            super(name);
        }

        /**
         * Get the action dice of player.
         *
         * @return all action dice of player saved in array.
         */
        public ActionDice[] getActionDice() {
            return actionDice;
        }

        /**
         * Access private field username.
         *
         * @return name of user
         */
        public String getUsername() {
            return super.username;
        }

        /**
         * Access private field id.
         *
         * @return id of user
         */
        public int getId() {
            return super.id;
        }

        /**
         * Set a new set of action dice.
         *
         * @param newActionDice new array of action dice a player can use.
         */
        public void setActionDices(ActionDice[] newActionDice) {
            actionDice = newActionDice;
        }

    }

    /*
     * ##################################################################################################################
     * ACTUAL TESTS
     * ##################################################################################################################
     */
    // default entry sheet
    DummyPlayer p1 = new DummyPlayer("uniqueName001");
    DummyPlayer p2 = new DummyPlayer("uniqueName002");
    // entry sheets
    EntrySheet entrySheet1 = new EntrySheet(p1);
    EntrySheet entrySheet2 = new EntrySheet(p2);
    int[] defaultEntrySheetValues = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    String[] defaultEntrySheetNames = {"ones", "twos", "threes", "fours", "fives", "sixes", "threeOfAKind", "fourOfAKind", "fullHouse", "smallStraight", "largeStraight", "kniffeliger", "chance", "pi"};

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

    @Test
    @DisplayName("Checks if entry sheets are properly set when given an int array of values.")
    void setEntrySheetTest(){
        // generate a random array for the entries
        int[] randomArray = new int[14];
        for (int i = 0; i < 14; i++){
            randomArray[i] = (int) Math.floor(Math.random() * 100 + 5);
        }

        // initiate player and corresponding entry sheet
        DummyPlayer lina = new DummyPlayer("lina");
        EntrySheet linasEntrySheet = new EntrySheet(lina);

        // set entry sheet values to random int array generated before
        linasEntrySheet.setEntrySheet(randomArray);

        for (int i = 0; i < 14; i++) {
            // System.out.println();
            // System.out.println();
            int randomValue = randomArray[i];
            int linasValue = linasEntrySheet.getEntryValues()[i];
            assertEquals(randomValue, linasValue);
        }

    }

    @Test
    @DisplayName("Test if defreezing works properly.")
    void defreezeTest() {
        // generate dummy player and its entry sheet to test defreezing
        DummyPlayer lina = new DummyPlayer("lina");
        EntrySheet linasEntrySheet = new EntrySheet(lina);

        // random index between 0 and 13 to access an entry of the entry sheet and freeze and defreeze it
        int randomIndex = (int) Math.floor(Math.random() * 13);

        linasEntrySheet.getAsArray()[randomIndex].setFrozenStatus(true);

        assertAll(() -> assertTrue(linasEntrySheet.getAsArray()[randomIndex].getFrozenStatus())
        );

        linasEntrySheet.defreeze();

        assertFalse(linasEntrySheet.getAsArray()[randomIndex].getFrozenStatus());
    }

    @Test
    @DisplayName("Checks if entry is properly added and if it is set final.")
    void addEntryTest() {
        // generate dummy player and its entry sheet to test adding entries
        DummyPlayer lina = new DummyPlayer("lina");
        EntrySheet linasEntrySheet = new EntrySheet(lina);

        // random index between 0 and 13 to access a random entry of the entry sheet and add an entry value (aka set its value)
        int randomIndex = (int) Math.floor(Math.random() * 13);
        int randomValue = (int) Math.floor(Math.random() * 100 + 5);
        Entry[] defaultEntrySheet = EntrySheet.getDefaultEntrySheet();
        Entry randomEntry = defaultEntrySheet[randomIndex];
        randomEntry.setValue(randomValue);
        linasEntrySheet.addEntry(randomEntry);

        // test
        assertAll(() -> assertEquals(randomEntry.getValue(), linasEntrySheet.getEntryByName(randomEntry.getName()).getValue()),
                () -> assertTrue(linasEntrySheet.getEntryByName(randomEntry.getName()).getIsFinal())
        );
    }

    @Test
    @DisplayName("Checks if entries are properly deleted and if setEntrySheet and deleteEntry properly set final status.")
    void deleteEntryTest() {
        // generate dummy player and its entry sheet to test deleting entries
        DummyPlayer lina = new DummyPlayer("lina");
        EntrySheet linasEntrySheet = new EntrySheet(lina);

        // generate a random array for the entry values
        int[] randomArray = new int[14];
        for (int i = 0; i < 14; i++){
            randomArray[i] = (int) Math.floor(Math.random() * 100 + 5);
        }

        // set entry sheet values to random int array generated before
        linasEntrySheet.setEntrySheet(randomArray);

        // random index so random entry will be deleted
        int randomlyDeletedEntryIndex = (int) Math.floor(Math.random() * 13);
        // get name of random entry
        String randomlyDeletedEntryName = EntrySheet.getDefaultEntrySheet()[randomlyDeletedEntryIndex].getName();

        // deletes random entry
        linasEntrySheet.deleteEntry(randomlyDeletedEntryName);

        // tests if all values are set properly and if the deleted one is zero
        // also tests if all entries are final
        for (int i = 0; i < 14; i++) {
            if (i == randomlyDeletedEntryIndex) {
                assertEquals(0, linasEntrySheet.getEntryByName(randomlyDeletedEntryName).getValue());
            } else {
                assertEquals(randomArray[i], linasEntrySheet.getAsArray()[i].getValue());
            }
            assertTrue(linasEntrySheet.getEntryByName(randomlyDeletedEntryName).getIsFinal());
        }
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
    int[] smallStraight4 = {1, 6, 3, 4, 5};
    int[] largeStraight1 = {1, 2, 3, 4, 5};
    int[] largeStraight2 = {2, 3, 4, 5, 6};
    int[] pi1 = {1, 3, 1, 4, 5};
    int[] pi2 = {1, 1, 3, 4, 5};

    @Test
    @DisplayName("Checks if an entry is properly validated and is set to 0 if it is not of this pattern.")
    void entryValidationTest() {
        // generate dummy player and its entry sheet to test deleting entries
        DummyPlayer lina = new DummyPlayer("lina");
        EntrySheet linasEntrySheet = new EntrySheet(lina);

        // genereate dice and also saves them as int array

        // generate a game manager, so we can use entry validation method
        GameManager gm = new GameManager();
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        Dice d3 = new Dice();
        Dice d4 = new Dice();
        Dice d5 = new Dice();
        Dice[] allDice = new Dice[]{d1, d2, d3, d4, d5};
        // roll dice for random values
        gm.rollDice(allDice);
        int[] allDiceAsIntArray = Dice.getAsIntArray(allDice);

        // takes rolled dice value and makes entry validation for every single possible pattern
        for (String entryName : linasEntrySheet.getEntryNames()) {
            EntrySheet.entryValidation(linasEntrySheet, entryName, allDice);
        }
        // saves entries in an array, so we can address the values in our test and compare them to the values we get
        // from the already tested methods like singleValueRoll, fullHouse, etc.
        int[] linasEntryValues = linasEntrySheet.getEntryValues();

//        for (int i : allDiceAsIntArray) {
//            System.out.println(i);
//        }

        // since the single methods are tested and function already we can test entry validation with these already tested methods
        assertAll(() -> assertEquals(EntrySheet.singleValueRolls(allDiceAsIntArray, 1), linasEntryValues[0]),
                () -> assertEquals(EntrySheet.singleValueRolls(allDiceAsIntArray, 2), linasEntryValues[1]),
                () -> assertEquals(EntrySheet.singleValueRolls(allDiceAsIntArray, 3), linasEntryValues[2]),
                () -> assertEquals(EntrySheet.singleValueRolls(allDiceAsIntArray, 4), linasEntryValues[3]),
                () -> assertEquals(EntrySheet.singleValueRolls(allDiceAsIntArray, 5), linasEntryValues[4]),
                () -> assertEquals(EntrySheet.singleValueRolls(allDiceAsIntArray, 6), linasEntryValues[5]),
                () -> assertEquals(EntrySheet.threeOfAKind(allDiceAsIntArray), linasEntryValues[6]),
                () -> assertEquals(EntrySheet.fourOfAKind(allDiceAsIntArray), linasEntryValues[7]),
                () -> assertEquals(EntrySheet.fullHouse(allDiceAsIntArray), linasEntryValues[8]),
                () -> assertEquals(EntrySheet.smallStraight(allDiceAsIntArray), linasEntryValues[9]),
                () -> assertEquals(EntrySheet.largeStraight(allDiceAsIntArray), linasEntryValues[10]),
                () -> assertEquals(EntrySheet.kniffeliger(allDiceAsIntArray), linasEntryValues[11]),
                () -> assertEquals(EntrySheet.chance(allDiceAsIntArray), linasEntryValues[12]),
                () -> assertEquals(EntrySheet.pi(allDiceAsIntArray), linasEntryValues[13])
        );
    }

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
                () -> assertEquals(30, EntrySheet.smallStraight(smallStraight4)),
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

    @Test
    @DisplayName("Checks if pi method gets calculated correctly.")
    void piTest() {
        assertAll(() -> assertEquals(31, EntrySheet.pi(pi1)),
                () -> assertEquals(31, EntrySheet.pi(pi2))
        );
    }

}