package server.gamelogic;

public class Entry {

    // name of an entry
    private final String name;

    // value of entry
    private int value;

    // entry is by default not frozen
    private boolean frozenStatus = false;

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
     * Access if an entry is frozen or not.
     *
     * @return true f frozen, false if not.
     */
    public boolean getFrozenStatus() {
        return frozenStatus;
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
    public void setValue(int newValue) {
        value = newValue;
    }
}