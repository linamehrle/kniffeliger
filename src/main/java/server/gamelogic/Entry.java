package server.gamelogic;

// TODO javadoc for class

/**
 * This class can create entries with a value and a name. An entry is a specific dice combination that gives the player
 * a points. An entry can be final, if a combination of dice was assigned to it. It can also be frozen, if another
 * player uses a dice to block this entry.
 */
public class Entry {

    // keeps in track if entry is final of not
    private boolean isFinal = false;

    // keeps in track if entry is frozen
    private boolean frozenStatus = false;

    // name of an entry
    private final String name;

    // value of entry
    private int value;

    public Entry(String name, int value) {
        this.name = name;
        this.value = value;
    }

    /**
     * Access frozen status of entry.
     *
     * @return true, if frozen, false otherwise
     */
    public boolean getFrozenStatus() {
        return frozenStatus;
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
     * Makes entry final, so it cannot be changed anymore.
     */
    public void setFinal(){
        isFinal = true;
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