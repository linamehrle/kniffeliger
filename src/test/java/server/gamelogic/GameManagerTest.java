package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.Player;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

// get methods are not tested separately since they are used in tests anyway and tested with the other tests
// not all set methods are tested since some of them are very simple
class GameManagerTest {
    // starter method cannot be tested with unit tests but only with play-through

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
         * @return all action dice of player saved in a Hashmap.
         */
        public HashMap<ActionDiceEnum, Integer> getActionDice() {
            return super.actionDice;
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
         * Sets number of action dices according to the given number.
         *
         * @param stealCount
         * @param freezeCount
         * @param crossOutCount
         * @param swapCount
         * @param shiftCount
         */
        public void setActionDice(int stealCount, int freezeCount, int crossOutCount, int swapCount, int shiftCount) {
            super.actionDice.put(ActionDiceEnum.STEAL, stealCount);
            super.actionDice.put(ActionDiceEnum.FREEZE, freezeCount);
            super.actionDice.put(ActionDiceEnum.CROSSOUT, crossOutCount);
            super.actionDice.put(ActionDiceEnum.SHIFT, shiftCount);
            super.actionDice.put(ActionDiceEnum.SWAP, swapCount);
        }
    }

    /**
     * Turns action dice array into String array with names of actions as array
     *
     * @param actionDiceArray array to extract names from
     * @return name of actions as String
     */
    public static String turnActionDiceToString(ActionDice[] actionDiceArray){
        String result = "";
        for (int i = 0; i < actionDiceArray.length; i++){
            if (actionDiceArray[i] != null) {
                result = result + " " + actionDiceArray[i].getActionName();
            }
        }
        return result;
    }

    @Test
    @DisplayName("Tests if it properly checks if all dice are saved.")
    void allDiceSavedTest() {
        // random index from 0 to 4 of a dice that will not be saved
        int randomIndex = (int) (Math.random() * 4);

        // generate a game manager, so we can use entry validation method
        GameManager gm = new GameManager();
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        Dice d3 = new Dice();
        Dice d4 = new Dice();
        Dice d5 = new Dice();
        Dice[] notAllSavedDice = new Dice[]{d1, d2, d3, d4, d5};
        // saves all dice except for the one with randomIndex
        for (int i = 0; i < 5; i++) {
            if (i != randomIndex) {
                notAllSavedDice[i].saveDice();
            }
        }

        Dice d11 = new Dice();
        Dice d21 = new Dice();
        Dice d31 = new Dice();
        Dice d41 = new Dice();
        Dice d51 = new Dice();
        Dice[] allSavedDice = new Dice[]{d11, d21, d31, d41, d51};
        // saves all dice
        for (int i = 0; i < 5; i++) {
            allSavedDice[i].saveDice();
        }

        // test
        assertAll(() -> assertFalse(GameManager.allDiceSaved(notAllSavedDice)),
                () -> assertFalse(notAllSavedDice[randomIndex].getSavingStatus()),
                () -> assertTrue(GameManager.allDiceSaved(allSavedDice))
        );

    }

    @Test
    @DisplayName("Checks if dice are properly reset, so savingStatus is false and numberOfRolls and diceValue are 0.")
    void resetDiceTest() {
        // generate a game manager, so we can use entry validation method
        GameManager gm = new GameManager();
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        Dice d3 = new Dice();
        Dice d4 = new Dice();
        Dice d5 = new Dice();
        Dice[] allDice = new Dice[]{d1, d2, d3, d4, d5};
        gm.rollDice(allDice);
        // saves all dice except for the one with randomIndex
        int[] valuesArray = Dice.getAsIntArray(allDice);

        for (int i = 0; i < 5; i++) {
            Dice d = allDice[i];
            int randomRollValue = valuesArray[i];
            d.saveDice();
            assertAll(() -> assertTrue(d.getSavingStatus()),
                    () -> assertEquals(1, d.getNumberOfRolls()),
                    () -> assertEquals(randomRollValue, d.getDiceValue())
            );
        }

        // implementation of reset dice copied to test it with junit
        for (Dice dice : allDice) {
            dice.resetDice();
        }

        for (int i = 0; i < 5; i++) {
            Dice d = allDice[i];
            assertAll(() -> assertFalse(d.getSavingStatus()),
                    () -> assertEquals(0, d.getNumberOfRolls()),
                    () -> assertEquals(0, d.getDiceValue())
            );
        }
    }

    @Test
    @DisplayName("Checks if dice are rolled correctly.")
    void rolledDiceTest() {
        GameManager gm = new GameManager();
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        Dice d3 = new Dice();
        Dice d4 = new Dice();
        Dice d5 = new Dice();
        Dice[] allDice = new Dice[]{d1, d2, d3, d4, d5};
        gm.rollDice(allDice);

        for (Dice dice : allDice) {
            assertTrue(0 != dice.getDiceValue());
        }
    }

    // depends on constant DIVISIBLE_BY field in GameManager
    // at the moment it is DIVISIBLE_BY = 1
    @Test
    @DisplayName("Adds action dice to players action dice set.")
    void addActionDiceTest() {
        // initiate game manager to test methods
        GameManager gm = new GameManager();
        // divisible by DIVIDABLE_BY constant in game manager, then get an action dice
        int DIVIDABLE_BY = gm.getDIVIDABLE_BY();
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        Dice d3 = new Dice();
        Dice d4 = new Dice();
        Dice d5 = new Dice();
        Dice[] allDice = new Dice[]{d1, d2, d3, d4, d5};
        gm.rollDice(allDice);

        // get false if sum of dice is not dividable by DIVIDABLE_BY and true if it is dividable by DIVIDABLE_BY
        boolean res = false;
        int sum = 0;
        for (Dice dice : allDice) {
            sum += dice.getDiceValue();
        }
        if (sum % DIVIDABLE_BY == 0) {
            res = true;
        }

        // generate players which have action dice
        DummyPlayer lina = new DummyPlayer("lina");
        DummyPlayer riccardo = new DummyPlayer("riccardo");

        // set linas action dice to array below, riccardo's action dice stay 0
        lina.setActionDice(1, 1, 1,1 ,1);

        // add action dice
        boolean addedToLinasActionDice = gm.addActionDice(allDice, lina);
        boolean addedToRiccardosActionDice = gm.addActionDice(allDice, riccardo);

        // test if action dice can be added
        assertEquals(res, addedToLinasActionDice);

        // check number of actn dices
        int linaNumActDice = 0;
        for (ActionDiceEnum actDice : lina.getActionDice().keySet()) {
            linaNumActDice += lina.getActionDice().get(actDice);
        }

        assertTrue(5 <= linaNumActDice && linaNumActDice <= 11);
        assertEquals(res, addedToRiccardosActionDice);

        // check number of actn dices
        int riccardoActDice = 0;
        for (ActionDiceEnum actDice : riccardo.getActionDice().keySet()) {
            riccardoActDice += riccardo.getActionDice().get(actDice);
        }
        assertTrue(0 <= riccardoActDice && riccardoActDice <= 5);
    }

    @Test
    @DisplayName("Tests the method that deletes an action dice out of an array")
    void deleteActionDiceTest(){
        // players that hold action dice (needed to apply the delete entry method)
        DummyPlayer lina = new DummyPlayer("lina");
        DummyPlayer riccardo = new DummyPlayer("riccardo");

        // action dice for player
        lina.setActionDice(1, 1, 1,1,1);
        riccardo.setActionDice(0, 0, 1, 1,1);

        // delete action dice
        GameManager gm = new GameManager();
        lina.decreaseActionDiceCount(ActionDiceEnum.SHIFT);
        riccardo.decreaseActionDiceCount(ActionDiceEnum.CROSSOUT);

        // control action dice to check

        HashMap<ActionDiceEnum, Integer> controlActionDiceLina = generateActionDice(1, 1, 1, 1, 0);
        HashMap<ActionDiceEnum, Integer> controlActionDiceRiccardo = generateActionDice(0, 0, 0, 1, 1);

        assertAll(() -> assertEquals(controlActionDiceLina, lina.getActionDice()),
                () -> assertEquals(controlActionDiceRiccardo, riccardo.getActionDice())
        );
    }

    @Test
    @DisplayName("Checks if ranking is properly done.")
    void rankingTest() {
        // generate game manager
        GameManager gm = new GameManager();

        // generate two players with random entry sheets with one clearly having more total points (since range of randomly generated numbers is different)
        // save entry sheets in an array
        DummyPlayer lina = new DummyPlayer("lina");
        EntrySheet linasEntrySheet = new EntrySheet(lina);
        DummyPlayer loris = new DummyPlayer("loris");
        EntrySheet  lorisEntrySheet = new EntrySheet(loris);
        EntrySheet[] allEntrySheets = new EntrySheet[]{linasEntrySheet, lorisEntrySheet};

        // random values for entry sheets
        int[] winnerSheet = new int[14];
        int[] loserSheet = new int[14];
        for (int i = 0; i < winnerSheet.length; i++) {
            winnerSheet[i] = (int) (Math.random() * 100 + 16);
            loserSheet[i] = (int) (Math.random() * 15 + 1);
        }

        // add values to entry sheets
        linasEntrySheet.setEntrySheet(winnerSheet);
        lorisEntrySheet.setEntrySheet(loserSheet);

        // get ranking
        Player[] rankedPlayers = gm.ranking(allEntrySheets);

        assertAll(() -> assertEquals("lina", rankedPlayers[1].getUsername()),
                () -> assertEquals("loris", rankedPlayers[0].getUsername())
        );

    }

    /**
     * Method to create a Action Dice HashMap given counts.
     * @param stealCount
     * @param freezeCount
     * @param crossOutCount
     * @param swapCount
     * @param shiftCount
     * @return
     */
    public static HashMap<ActionDiceEnum, Integer> generateActionDice(int stealCount, int freezeCount, int crossOutCount, int swapCount, int shiftCount) {
        HashMap<ActionDiceEnum, Integer> actionDice = new HashMap<>();

        actionDice.put(ActionDiceEnum.STEAL, stealCount);
        actionDice.put(ActionDiceEnum.FREEZE, freezeCount);
        actionDice.put(ActionDiceEnum.CROSSOUT, crossOutCount);
        actionDice.put(ActionDiceEnum.SHIFT, shiftCount);
        actionDice.put(ActionDiceEnum.SWAP, swapCount);

        return actionDice;
    }
}