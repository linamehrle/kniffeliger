package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class EntrySheetTest {
    // default entry sheet
    EntrySheet entrySheet1 = new EntrySheet("uniqueName001");
    EntrySheet entrySheet2 = new EntrySheet("uniqueName002");
    int[] defaultEntrySheetValues = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    String[] defaultEntrySheetNames = {"ones", "twos", "threes", "fours", "fives", "sixes", "threeOfAKind", "fourOfAKind", "fullHouse", "smallStraight", "largeStraight", "kniffeliger", "chance"};

    @Test
    @DisplayName("Tests if default of entry sheet is correct, so if resetEntrySheet() works and if methods getUsername(), getTotalPoints(), deleteEntry(), addEntry() work.")
    void getTest() {
        try {
            entrySheet2.addEntry(new Entry("ones", 1));
        } catch (Exception e){
            e.getMessage();
        }
        assertAll(() -> assertTrue(Arrays.equals(defaultEntrySheetNames, entrySheet1.getEntryNames())),
                () -> assertTrue(Arrays.equals(defaultEntrySheetValues, entrySheet1.getEntryValues())),
                () -> assertEquals("uniqueName001", entrySheet1.getUsername()),
                () -> assertEquals("uniqueName002", entrySheet2.getUsername()),
                () -> assertFalse("username".equals(entrySheet1.getUsername())),
                () -> assertEquals(1, entrySheet2.getEntryValues()[0]),
                () -> assertEquals(0, entrySheet1.getTotalPoints()),
                () -> assertEquals(1, entrySheet2.getTotalPoints())
        );

        // resets entrySheet2 to default
        entrySheet2.resetEntrySheet();
        assertAll(() -> assertTrue(Arrays.equals(defaultEntrySheetNames, entrySheet2.getEntryNames())),
                () -> assertEquals(0, entrySheet2.getTotalPoints())
        );

        try {
            entrySheet2.addEntry(new Entry("twos", 6));
        } catch (Exception e){
            e.getMessage();
        }
        try {
            entrySheet2.deleteEntry(new Entry("twos", 6));
        } catch (Exception e){
            e.getMessage();
        }
        assertAll(() -> assertTrue(Arrays.equals(defaultEntrySheetNames, entrySheet2.getEntryNames())),
                () -> assertEquals(0, entrySheet2.getTotalPoints())
        );

    }
}