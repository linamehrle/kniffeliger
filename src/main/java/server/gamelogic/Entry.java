package server.gamelogic;

public class Entry {

    // name of an entry
    private final String name;

    // value of entry
    private int value;

    // keeps in track if entry is final of not
    private boolean isFinal = false;

    public Entry(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Access if entry is already final. It is false per default.
     *
     * @return true if it is final, false if not.
     */
    public boolean getIsFinal() {
        return isFinal;
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
     * Sets entry to frozen or not frozen.
     *
     * @param newFrozenStatus is true, if the entry should be frozen and false if not
     */
    public void setFrozenStatus(boolean newFrozenStatus) {
        frozenStatus = newFrozenStatus;
    }

    /**
     * Sets new value for entry value.
     *
     * @param newValue new value that entry should have
     */
    public void setValue(int newValue) {
        value = newValue;
    }
}