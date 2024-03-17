package server.gamelogic;

public class Dice {

    private boolean savingStatus;
    private int numberOfRolls;
    private int diceValue;


    /**
     * Constructor that resets die to the starting settings: dice that has not been rolled,
     * that has dice number 0 and that has not been saved yet.
     */
    public Dice() {
        resetDice();
    }

    /**
     * Returns the savingStatus of the dice. Returns true, if dice has been saved and false, if it has not been saved
     * and has been rolled less than three times.
     *
     * @return saving status of the dice as boolean
     */
    public boolean getSavingStatus() {
        return savingStatus;
    }

    /**
     * Returns number of rolls as an integer.
     *
     * @return number of rolls as integer
     */
    public int getNumberOfRolls() {
        return numberOfRolls;
    }

    /**
     * Returns the current value rolled with a dice. If the dice hasn't been rolled yet, it returns 0.
     */
    public int getDiceValue() {
        return diceValue;
    }

    /**
     * Rolls the dice if it has not been saved yet and if the dice has been rolled less than 3 times.
     * With rolling the dice comes the increase of number of rolls (numberOfRolls), the change of the value of the dice
     * (diceValue) and it automatically saves the dice after rolling it three times.
     *
     * @return true if dice can be rolled and false if it cannot be rolled
     */
    public boolean rollDice() {
        boolean couldRoll = false;
        if (!(savingStatus) && numberOfRolls < 3) {
            // adjusts variables after dice was rolled
            numberOfRolls = numberOfRolls + 1;
            diceValue = (int) Math.floor(Math.random() * 6 + 1);
            // if dice has been rolled three times it gets saved automatically
            if (numberOfRolls == 3) {
                saveDice();
            }
            couldRoll = true;
        }
        return couldRoll;
    }

    /**
     * Saves the dice so it cannot get rolled again.
     */
    public void saveDice() {
        savingStatus = true;
    }

    /**
     * Resets the dice to starting settings: a dice that has not been rolled, saved and that has value 0 yet.
     */
    public void resetDice() {
        savingStatus = false;
        numberOfRolls = 0;
        diceValue = 0;
    }

}