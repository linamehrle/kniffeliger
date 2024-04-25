package client.gui;


import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.Image;

import java.util.Map;

/**
 * Class that contains values for entries to facilitate implementation of entrySheet in GUI
 */
public class EntrySheetGUImplementation {
    //Identifier number beginning at 1 for ones
    private int idNumber;
    //Name of field
    private StringProperty idName;
    //Name that is displayed
    private String displayName;
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
        this.displayName = convertIDnameToDisplay(entryName);
    }
    //Getters
    public int getIDnumber() {
        return idNumber;
    }

    public String getIDname() {
        return idName.get();
    }

    public String getDisplayName() { return displayName; }

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

    /**
     * Method to convert idName to display name with separated words (e.g. "fourOfAKind" to "Four Of A Kind")
     * @param inputString String to convert
     * @return converted String
     */
    public static String convertIDnameToDisplay(String inputString) {
        StringBuilder result = new StringBuilder();
        boolean isNewWord = true;

        for (int i = 0; i < inputString.length(); i++) {
            char currentChar = inputString.charAt(i);
            char nextChar = (i + 1 < inputString.length()) ? inputString.charAt(i + 1) : ' ';

            if (Character.isUpperCase(currentChar)) {
                if (!isNewWord) {
                    result.append(" ");
                }
                result.append(Character.toUpperCase(currentChar));
            } else if (isNewWord && Character.isLowerCase(currentChar)) {
                result.append(Character.toUpperCase(currentChar));
            } else {
                result.append(currentChar);
            }

            if (Character.isLetter(currentChar) && !Character.isLetter(nextChar)) {
                isNewWord = true;
            } else {
                isNewWord = false;
            }
        }

        return result.toString();
    }


}
