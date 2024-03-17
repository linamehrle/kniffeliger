package server.gamelogic;

public class EntrySheet {

    // value with which every Entry starts with
    private final int defaultValue = 0;

    // name of user which also ist name of entry sheet
    private final String username;

    // total points per default 0
    private int totalPoints = 0;

    // entries used for a full entry sheet array
    private Entry ones = new Entry("ones", defaultValue);
    private Entry twos = new Entry("twos", defaultValue);
    private Entry threes = new Entry("threes", defaultValue);
    private Entry fours = new Entry("fours", defaultValue);
    private Entry fives = new Entry("fives", defaultValue);
    private Entry sixes = new Entry("sixes", defaultValue);
    private Entry threeOfAKind = new Entry("threeOfAKind", defaultValue);
    private Entry fourOfAKind = new Entry("fourOfAKind", defaultValue);
    private Entry fullHouse = new Entry("fullHouse", defaultValue);
    private Entry smallStraight = new Entry("smallStraight", defaultValue);
    private Entry largeStraight = new Entry("largeStraight", defaultValue);
    private Entry kniffeliger = new Entry("kniffeliger", defaultValue);
    private Entry chance = new Entry("chance", defaultValue);

    // entry sheet as an array
    private Entry[] entrySheet = new Entry[]{ones, twos, threes, fours, fives, sixes, threeOfAKind, fourOfAKind, fullHouse, smallStraight, largeStraight, kniffeliger, chance};

    /**
     * Constructor that builds new entry sheet with unique name which is handed to it as a parameter.
     *
     * @param username is unique so server knows exactly who entry sheet belongs to
     */
    public EntrySheet(String username) {
        this.username = username;
    }

    /**
     * Access username which is also name of entry sheet.
     *
     * @return username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Access entry sheet as an array.
     *
     * @return entry sheet array
     */
    public Entry[] getEntrySheetAsArray() {
        return entrySheet;
    }

    /**
     * Extracts point of entry sheet into an array.
     *
     * @return array with points of entries as values
     */
    public int[] getEntryValues(){
        int[] entryValues = new int[entrySheet.length];
        for (int i = 0; i < entrySheet.length; i++) {
            entryValues[i] = entrySheet[i].getValue();
        }
        return entryValues;
    }

    /**
     * Extracts names of entry sheet into an array.
     *
     * @return array with names of entries as values
     */
    public String[] getEntryNames(){
        String[] entryNames = new String[entrySheet.length];
        for (int i = 0; i < entrySheet.length; i++) {
            entryNames[i] = entrySheet[i].getName();
        }
        return entryNames;
    }

    /**
     * Access total points of entry sheet
     *
     * @return all the points of the entry sheet added together
     */
    public int getTotalPoints() {
        return totalPoints;
    }

    /**
     * Adds the new value for an entry at the right position of the entry sheet array. Total points get updated by the added value.
     *
     * @param newEntry contains the name we can compare and the new entry value
     */
    public void addEntry(Entry newEntry) throws Exception {
        // makes sure that we can throw an exception, if the entry does not appear in entrySheet array
        int notAppearedCounter = 0;
        for (Entry entry : entrySheet) {
            // if the correct entry has been detected, so if the names are the same, the value of this entry on entrySheet can be changed
            if (entry.getName().equals(newEntry.getName())) {
                entry.setValue(newEntry.getValue());
                totalPoints = totalPoints + entry.getValue();
            } else {
                notAppearedCounter = notAppearedCounter + 1;
            }
        }
        // throws exception if the entry name could not be detected in the entrySheet array because then the given entry is not valid
        if (notAppearedCounter == entrySheet.length) {
            throw new Exception("Your entry has no match in the entry sheet. You must have gotten the wrong name.");
        }
    }

    /**
     * Detects entry from sheet and deletes it.
     *
     * @param deletedEntry gives us name of entry that needs to be deleted
     * @throws Exception when the deletedEntry parameter does not appear in entry sheet
     */
    public void deleteEntry(Entry deletedEntry) throws Exception {
        // makes sure that we can throw an exception, if the entry does not appear in entrySheet array
        int notAppearedCounter = 0;
        for (Entry entry : entrySheet) {
            // if the correct entry has been detected, so if the names are the same, delete value from total points and set the value of this entry on entrySheet to 0
            if (entry.getName().equals(deletedEntry.getName())) {
                totalPoints = totalPoints - entry.getValue();
                entry.setValue(0);
            } else {
                notAppearedCounter = notAppearedCounter + 1;
            }
        }
        // throws exception if the entry name could not be detected in the entrySheet array because then the given entry is not valid
        if (notAppearedCounter == entrySheet.length) {
            throw new Exception("Your entry has no match in the entry sheet. You must have gotten the wrong name.");
        }
    }

    /**
     * Sets all entries on entry sheet to starting value 0.
     */
    public void resetEntrySheet() {
        for (Entry entry : entrySheet) {
            entry.setValue(defaultValue);
        }
        totalPoints = 0;
    }

}