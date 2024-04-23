package client.gui;

import client.networking.ClientOutput;

/**
 * Class to implement dice in GUI, might eventually be replaced by dice class of Game Logic
 */
public class DiceGUImplementation {
    private int diceID;
    //True if dice is saved
    private boolean savingStatus;
    //True if dice is selected for saving, but save is not yet relayed to server
    private boolean stashedForSaving;
    private int diceValue;


    public DiceGUImplementation(int diceID) {
        this.diceID = diceID;
        this.savingStatus = false;
        this.stashedForSaving = false;
        this.diceValue = 0;
    }

    public int getID(){return diceID;}

    public boolean getSavingStatus(){return savingStatus;}

    public int getDiceValue(){return diceValue;}

    public void setSavingStatus(boolean status){
        this.savingStatus = status;
    }

    public void setDiceValue(int value){
        this.diceValue = value;
    }

    public void resetDice() {
        this.savingStatus = false;
        this.diceValue = 0;
    }

    public boolean getStashStatus() {return this.stashedForSaving;
    }

    public void setStashStatus(boolean status) {
        this.stashedForSaving = status;
    }
}
