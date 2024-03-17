package server.gamelogic;

import java.util.Arrays;

public class Entry {

    private final String name;
    private int value;

    public Entry(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Access name of entry.
     *
     * @return name of entry
     */
    public String getName() {
        return name;
    }

    /**
     * Access value of entry.
     *
     * @return value of entry
     */
    public int getValue() {
        return value;
    }

    /**
     * Sets new value for entry value.
     *
     * @param newValue new value that entry should have
     */
    public void setValue(int newValue){
        value = newValue;
    }
}