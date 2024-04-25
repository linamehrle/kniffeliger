package client.gui;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

/**
 * Class that contains values for entries to facilitate implementation of entrySheet in GUI
 */
public class EntrySheetGUImplementation {
    //Identifier number beginning at 1 for ones
    private int idNumber;
    //Name of field
    private StringProperty idName;
    //Score of entry
    private IntegerProperty score;

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
        this.idName = new SimpleStringProperty(entryName);
        this.score = new SimpleIntegerProperty(0);
        this.savingStatus = false;
    }
    //Getters
    public int getIDnumber() {
        return idNumber;
    }

    public String getIDname() {
        return idName.get();
    }

    public int getScore() {
        return score.get();
    }

    public Boolean getSavingStatus () {
        return savingStatus;
    }

    //Setters
    public void setIDnumber(int number) {
        this.idNumber = number;
    }

    public void setIdName(String name) {
        this.idName.set(name);
    }

    public void setScore(int number) {
        this.score.set(number);
    }

    public void getSavingStatus(Boolean status) {
        this.savingStatus = status;
    }

    //Getters for observable values


    public IntegerProperty scoreProperty() {
        return score;
    }
    public StringProperty nameProperty() {
        return idName;
    }

}
