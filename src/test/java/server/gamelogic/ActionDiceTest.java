package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import server.Player;

import java.net.Socket;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

// get methods are not tested separately since they are used in tests anyway and tested with the other tests
// not all set methods are tested since some of them are very simple
class ActionDiceTest {

    /*
     * ##################################################################################################################
     * PLAYER INITIATION
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
            return username;
        }

        /**
         * Access private field id.
         *
         * @return id of user
         */
        public int getId() {
            return id;
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

    /**
     * Helper method to initiate random entry sheets with random values from 5 to 50.
     *
     * @param player
     * @return
     */
    public EntrySheet initiateRandomEntrySheet(Player player){
        EntrySheet playersEntrySheet = new EntrySheet(player);
        int[] randomArray = new int[EntrySheet.getEntrySheetLength()];
        for (int i : randomArray){
            i = (int) Math.floor(Math.random() * 50 + 5);
        }
        playersEntrySheet.setEntrySheet(randomArray);
        return playersEntrySheet;
    }

    // TODO: test steal when no entry was made
    @Test
    @DisplayName("Checks steal method.")
    void stealTest(){
        // initiates players with entry sheets with random values between 5 and 50
        // this method only checks if methods work, if the entries have a valid value is not important
        DummyPlayer lina = new DummyPlayer("lina");
        DummyPlayer riccardo = new DummyPlayer("riccardo");
        EntrySheet linasEntrySheet = initiateRandomEntrySheet(lina);
        EntrySheet riccardosEntrySheet = initiateRandomEntrySheet(riccardo);

        // helper entry sheet to assure right values are correct
        DummyPlayer linasHelper = new DummyPlayer("linasHelper");
        DummyPlayer riccardosHelper = new DummyPlayer("riccardosHelper");
        EntrySheet linasHelpersEntrySheet = new EntrySheet(linasHelper);
        EntrySheet riccardosHelpersEntrySheet = new EntrySheet(riccardosHelper);
        linasHelpersEntrySheet.setEntrySheet(linasEntrySheet.getEntryValues());
        riccardosHelpersEntrySheet.setEntrySheet(riccardosEntrySheet.getEntryValues());

        int randomIndex = (int) Math.floor(Math.random() * 13 + 0);
        ActionDice.steal(linasEntrySheet, riccardosEntrySheet, riccardosEntrySheet.getEntryNames()[randomIndex]);
        assertAll(() -> assertEquals(riccardosHelpersEntrySheet.getAsArray()[randomIndex].getValue(), linasEntrySheet.getAsArray()[randomIndex].getValue()),
                () -> assertEquals(0, riccardosEntrySheet.getAsArray()[randomIndex].getValue())
        );
    }

    @Test
    @DisplayName("Checks freeze method.")
    void freezeTest(){
        // initiates players with entry sheets with random values between 5 and 50
        // this method only checks if methods work, if the entries have a valid value is not important
        DummyPlayer lina = new DummyPlayer("lina");
        DummyPlayer riccardo = new DummyPlayer("riccardo");
        EntrySheet linasEntrySheet = new EntrySheet(lina);
        EntrySheet riccardosEntrySheet = new EntrySheet(riccardo);

        // set random entry to a random value
        int randomIndex = (int) Math.floor(Math.random() * 13 + 0);
        int randomValue = (int) Math.floor(Math.random() * 133 + 0);
        linasEntrySheet.getAsArray()[randomIndex].setValue(randomValue);


        assertAll(() -> assertFalse(linasEntrySheet.getAsArray()[randomIndex + 1].getFrozenStatus()),
                () -> assertTrue(ActionDice.freeze(riccardosEntrySheet, linasEntrySheet, linasEntrySheet.getEntryNames()[randomIndex])),
                () -> assertTrue(linasEntrySheet.getAsArray()[randomIndex].getFrozenStatus())
        );
    }

    @Test
    @DisplayName("Checks crossOut method.")
    void crossOutTest(){
        // initiates players with entry sheets with random values between 5 and 50
        // this method only checks if methods work, if the entries have a valid value is not important
        DummyPlayer lina = new DummyPlayer("lina");
        DummyPlayer riccardo = new DummyPlayer("riccardo");
        EntrySheet linasEntrySheet = initiateRandomEntrySheet(lina);
        EntrySheet riccardosEntrySheet = initiateRandomEntrySheet(riccardo);

        // helper entry sheet to assure right values are correct
        DummyPlayer linasHelper = new DummyPlayer("linasHelper");
        DummyPlayer riccardosHelper = new DummyPlayer("riccardosHelper");
        EntrySheet linasHelpersEntrySheet = new EntrySheet(linasHelper);
        EntrySheet riccardosHelpersEntrySheet = new EntrySheet(riccardosHelper);
        linasHelpersEntrySheet.setEntrySheet(linasEntrySheet.getEntryValues());
        riccardosHelpersEntrySheet.setEntrySheet(riccardosEntrySheet.getEntryValues());

        int randomIndex = (int) Math.floor(Math.random() * 13 + 0);
        ActionDice.crossOut(linasEntrySheet, riccardosEntrySheet, riccardosEntrySheet.getEntryNames()[randomIndex]);
        assertAll(() -> assertEquals(0, riccardosEntrySheet.getAsArray()[randomIndex].getValue())
        );
    }

    @Test
    @DisplayName("Checks shift method.")
    void shiftTest(){
        // initiates players with entry sheets with random values between 5 and 50
        // this method only checks if methods work, if the entries have a valid value is not important
        DummyPlayer lina = new DummyPlayer("lina");
        DummyPlayer riccardo = new DummyPlayer("riccardo");
        EntrySheet linasEntrySheet = initiateRandomEntrySheet(lina);
        EntrySheet riccardosEntrySheet = initiateRandomEntrySheet(riccardo);

        EntrySheet[] allEntrySheets = new EntrySheet[]{linasEntrySheet, riccardosEntrySheet};

        // helper entry sheet to assure right values are correct
        DummyPlayer linasHelper = new DummyPlayer("linasHelper");
        DummyPlayer riccardosHelper = new DummyPlayer("riccardosHelper");
        EntrySheet linasHelpersEntrySheet = new EntrySheet(linasHelper);
        EntrySheet riccardosHelpersEntrySheet = new EntrySheet(riccardosHelper);
        linasHelpersEntrySheet.setEntrySheet(linasEntrySheet.getEntryValues());
        riccardosHelpersEntrySheet.setEntrySheet(riccardosEntrySheet.getEntryValues());

        int randomIndex = (int) Math.floor(Math.random() * 13 + 0);
        ActionDice.shift(allEntrySheets);
        assertAll(() -> assertEquals(riccardosHelpersEntrySheet.getEntryValues()[randomIndex], linasEntrySheet.getEntryValues()[randomIndex]),
                () -> assertEquals(linasHelpersEntrySheet.getEntryValues()[randomIndex], riccardosEntrySheet.getEntryValues()[randomIndex])
        );
    }

    @Test
    @DisplayName("Checks switchEntries method.")
    void switchEntriesTest(){
        // initiates players with entry sheets with random values between 5 and 50
        // this method only checks if methods work, if the entries have a valid value is not important
        DummyPlayer lina = new DummyPlayer("lina");
        DummyPlayer riccardo = new DummyPlayer("riccardo");
        EntrySheet linasEntrySheet = initiateRandomEntrySheet(lina);
        EntrySheet riccardosEntrySheet = initiateRandomEntrySheet(riccardo);

        EntrySheet[] allEntrySheets = new EntrySheet[]{linasEntrySheet, riccardosEntrySheet};

        // helper entry sheet to assure right values are correct
        DummyPlayer linasHelper = new DummyPlayer("linasHelper");
        DummyPlayer riccardosHelper = new DummyPlayer("riccardosHelper");
        EntrySheet linasHelpersEntrySheet = new EntrySheet(linasHelper);
        EntrySheet riccardosHelpersEntrySheet = new EntrySheet(riccardosHelper);
        linasHelpersEntrySheet.setEntrySheet(linasEntrySheet.getEntryValues());
        riccardosHelpersEntrySheet.setEntrySheet(riccardosEntrySheet.getEntryValues());

        int randomIndex = (int) Math.floor(Math.random() * 13 + 0);
        ActionDice.swap(linasEntrySheet, riccardosEntrySheet);
        assertAll(() -> assertEquals(riccardosHelpersEntrySheet.getEntryValues()[randomIndex], linasEntrySheet.getEntryValues()[randomIndex]),
                () -> assertEquals(linasHelpersEntrySheet.getEntryValues()[randomIndex], riccardosEntrySheet.getEntryValues()[randomIndex])
        );
    }

}