package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {
    Entry entry1 = new Entry("ones", 0);
    Entry entry2 = new Entry("twos", 0);

    @Test
    @DisplayName("Checks if value can be set.")
    void setValueTest(){
        int randomNumber = (int) Math.floor(Math.random() * 100 + 5);
        entry2.setValue(randomNumber);
        assertAll(() -> assertEquals(0, entry1.getValue()),
                () -> assertEquals(randomNumber, entry2.getValue())
        );

    }

    @Test
    @DisplayName("Checks if name is correctly returned by methods.")
    void getNameTest(){
        assertAll(() -> assertEquals("ones", entry1.getName()),
                () -> assertEquals("twos", entry2.getName())
        );
    }

}