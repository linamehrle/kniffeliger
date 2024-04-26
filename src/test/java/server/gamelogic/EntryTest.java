package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntryTest {
    Entry entry1 = new Entry("ones", 0);
    Entry entry2 = new Entry("twos", 0);


    @Test
    @DisplayName("Checks if name is correctly returned by methods.")
    void getNameTest(){
        assertAll(() -> assertEquals("ones", entry1.getName()),
                () -> assertEquals("twos", entry2.getName())
        );
    }

    @Test
    @DisplayName("Check if final can be set.")
    void  setFinalTest(){
        entry1.setFinal();
        assertAll(() -> assertTrue(entry1.getIsFinal()),
                () -> assertFalse(entry2.getIsFinal())
        );
    }

    @Test
    @DisplayName("Check if final can be set.")
    void  setFrozenStatus(){
        entry1.setFinal();
        assertAll(() -> assertTrue(entry1.getIsFinal()),
                () -> assertFalse(entry2.getIsFinal())
        );
    }

    @Test
    @DisplayName("Checks if value can be set.")
    void setValueTest(){
        entry2.setValue(6);
        assertAll(() -> assertEquals(0, entry1.getValue()),
                () -> assertEquals(6, entry2.getValue())
        );

    }

}