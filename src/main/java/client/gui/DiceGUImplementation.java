package client.gui;

/**
 * Class to implement dice in GUI, might eventually be replaced by dice class of Game Logic
 */
public class DiceGUImplementation {
    private int diceID;
    private boolean savingStatus;
    private int diceValue;


    public DiceGUImplementation(int diceID) {
        this.diceID = diceID;
        this.savingStatus = false;
        this.diceValue = 0;
    }

    public int getID(){return diceID;}

    public boolean getSavingStatus(){return savingStatus;}

    public int getDiceValue(){return diceValue;}
}
