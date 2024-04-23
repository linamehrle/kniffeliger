package client.gui;


import javafx.scene.image.Image;

/**
 * Class that contains values for entries to facilitate implementation of entrySheet in GUI
 */
public class EntrySheetGUImplementation {
    //Identifier number beginning at 1 for ones
    private int idNumber;
    //Name of field
    private String idName;
    //Score of entry
    private int score;

    //Specifies if entry still can be modified
    private Boolean savingStatus;

    public Image icon;

    /**
     * Constructor for class EntrySheetGUImplementation
     * sets default values: score=0 and savingStatus = false
     * @param entryNumber number to identify entry, should be different for each entry
     * @param entryName name to identify entry, e.q. ones, twos,large straight etc.
     */
    EntrySheetGUImplementation (int entryNumber, String entryName){
        this.idNumber = entryNumber;
        this.idName = entryName;
        this.score = 0;
        this.savingStatus = false;
    }
    //Getters
    public int getIDnumber() {
        return idNumber;
    }

    public String getIDname() {
        return idName;
    }

    public int getScore() {
        return score;
    }

    public Boolean getSavingStatus () {
        return savingStatus;
    }

    //Setters
    public void setIDnumber(int number) {
        this.idNumber = number;
    }

    public void setIdName(String name) {
        this.idName = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void getSavingStatus(Boolean status) {
        this.savingStatus = status;
    }

}
