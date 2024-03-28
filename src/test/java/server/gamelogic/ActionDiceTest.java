package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionDiceTest {

    /**
     * Helper method to initiate random entry sheets with random values from 5 to 50
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

    @Test
    @DisplayName("Checks steal method.")
    void stealTest(){
        // initiates players with entry sheets with random values between 5 and 50
        // this method only checks if methods work, if the entries have a valid value is not important
        Player lina = new Player("lina", 007);
        Player riccardo = new Player("riccardo", 002);
        EntrySheet linasEntrySheet = initiateRandomEntrySheet(lina);
        EntrySheet riccardosEntrySheet = initiateRandomEntrySheet(riccardo);

        // helper entry sheet to assure right values are correct
        Player linasHelper = new Player("linasHelper", 117);
        Player riccardosHelper = new Player("riccardosHelper", 112);
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
        Player lina = new Player("lina", 007);
        Player riccardo = new Player("riccardo", 002);
        EntrySheet linasEntrySheet = initiateRandomEntrySheet(lina);
        EntrySheet riccardosEntrySheet = initiateRandomEntrySheet(riccardo);

        // helper entry sheet to assure right values are correct
        Player linasHelper = new Player("linasHelper", 117);
        Player riccardosHelper = new Player("riccardosHelper", 112);
        EntrySheet linasHelpersEntrySheet = new EntrySheet(linasHelper);
        EntrySheet riccardosHelpersEntrySheet = new EntrySheet(riccardosHelper);
        linasHelpersEntrySheet.setEntrySheet(linasEntrySheet.getEntryValues());
        riccardosHelpersEntrySheet.setEntrySheet(riccardosEntrySheet.getEntryValues());

        int randomIndex = (int) Math.floor(Math.random() * 13 + 0);
        ActionDice.freeze(riccardosEntrySheet, riccardosEntrySheet.getEntryNames()[randomIndex]);
        assertAll(() -> assertTrue(riccardosEntrySheet.getAsArray()[randomIndex].getFrozenStatus())
        );
    }

    @Test
    @DisplayName("Checks crossOut method.")
    void crossOutTest(){
        // initiates players with entry sheets with random values between 5 and 50
        // this method only checks if methods work, if the entries have a valid value is not important
        Player lina = new Player("lina", 007);
        Player riccardo = new Player("riccardo", 002);
        EntrySheet linasEntrySheet = initiateRandomEntrySheet(lina);
        EntrySheet riccardosEntrySheet = initiateRandomEntrySheet(riccardo);

        // helper entry sheet to assure right values are correct
        Player linasHelper = new Player("linasHelper", 117);
        Player riccardosHelper = new Player("riccardosHelper", 112);
        EntrySheet linasHelpersEntrySheet = new EntrySheet(linasHelper);
        EntrySheet riccardosHelpersEntrySheet = new EntrySheet(riccardosHelper);
        linasHelpersEntrySheet.setEntrySheet(linasEntrySheet.getEntryValues());
        riccardosHelpersEntrySheet.setEntrySheet(riccardosEntrySheet.getEntryValues());

        int randomIndex = (int) Math.floor(Math.random() * 13 + 0);
        ActionDice.crossOut(riccardosEntrySheet, riccardosEntrySheet.getEntryNames()[randomIndex]);
        assertAll(() -> assertEquals(0, riccardosEntrySheet.getAsArray()[randomIndex].getValue())
        );
    }

    @Test
    @DisplayName("Checks shift method.")
    void shiftTest(){
        // initiates players with entry sheets with random values between 5 and 50
        // this method only checks if methods work, if the entries have a valid value is not important
        Player lina = new Player("lina", 007);
        Player riccardo = new Player("riccardo", 002);
        EntrySheet linasEntrySheet = initiateRandomEntrySheet(lina);
        EntrySheet riccardosEntrySheet = initiateRandomEntrySheet(riccardo);

        EntrySheet[] allEntrySheets = new EntrySheet[]{linasEntrySheet, riccardosEntrySheet};

        // helper entry sheet to assure right values are correct
        Player linasHelper = new Player("linasHelper", 117);
        Player riccardosHelper = new Player("riccardosHelper", 112);
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
        Player lina = new Player("lina", 007);
        Player riccardo = new Player("riccardo", 002);
        EntrySheet linasEntrySheet = initiateRandomEntrySheet(lina);
        EntrySheet riccardosEntrySheet = initiateRandomEntrySheet(riccardo);

        EntrySheet[] allEntrySheets = new EntrySheet[]{linasEntrySheet, riccardosEntrySheet};

        // helper entry sheet to assure right values are correct
        Player linasHelper = new Player("linasHelper", 117);
        Player riccardosHelper = new Player("riccardosHelper", 112);
        EntrySheet linasHelpersEntrySheet = new EntrySheet(linasHelper);
        EntrySheet riccardosHelpersEntrySheet = new EntrySheet(riccardosHelper);
        linasHelpersEntrySheet.setEntrySheet(linasEntrySheet.getEntryValues());
        riccardosHelpersEntrySheet.setEntrySheet(riccardosEntrySheet.getEntryValues());

        int randomIndex = (int) Math.floor(Math.random() * 13 + 0);
        ActionDice.switchEntries(linasEntrySheet, riccardosEntrySheet);
        assertAll(() -> assertEquals(riccardosHelpersEntrySheet.getEntryValues()[randomIndex], linasEntrySheet.getEntryValues()[randomIndex]),
                () -> assertEquals(linasHelpersEntrySheet.getEntryValues()[randomIndex], riccardosEntrySheet.getEntryValues()[randomIndex])
        );
    }

}