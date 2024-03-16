package server.gamelogic;

public class Dice {

    private int diceValue;
    private int numberOfRolls;
    private boolean rolledStatus;
    private boolean savingStatus;


    /**
     * Constructor that resets dice to the starting settings: we have a dice that has not been rolled (rolledStatus = false),
     * that has dice number 0 (diceValue = 0) and that has not been saved yet (savingStatus = false).
     */
    public Dice (){
        resetDice();
    }

    /**
     * Returns the current value rolled with a dice. If the dice hasn't been rolled yet, it returns 0.
     */
    public int getDiceValue(){
        return diceValue;
    }

    /**
     * Returns the rolledStatus of the dice. Returns true, if the dice has been rolled and false, if is has not been rolled.
     */
    public boolean getRolledStatus(){
        return rolledStatus;
    }

    /**
     * Returns the savingStatus of the dice. Returns true, if a dice has been saved and false, if you want to continue playing with it.
     */
    public boolean getSavingStatus(){
        return savingStatus;
    }

    /**
     * Rolls the dice if it has not been saved yet and if the dice has been rolled less than 3 times.
     * Will change the value of the dice to a random value between 1 and 6 includinc both.
     * It also increases the number of rolls by one (numberOfRolls++) and sets the variable rolledStatus to true, since the
     * dice has been rolled after calling this method.
     */
    public void rollDice(){
        if (!(savingStatus) && numberOfRolls < 3) {
            rolledStatus = true;
            numberOfRolls = numberOfRolls + 1;
            diceValue = (int)Math.floor(Math.random() * 6 + 1);
        }
    }

    /**
     * Checkes it two dices are of equal value.
     */
    public boolean isEqual(Dice otherDice){
        return diceValue == otherDice.getDiceValue();
    }

    /**
     * Saves the dice so it cannot get rolled again.
     */
    public void saveDice(){
        savingStatus = true;
    }

    /**
     * Resets the dice to starting settings:
     */
    public void resetDice(){
        rolledStatus = false;
        savingStatus = false;
        numberOfRolls = 0;
        diceValue = 0;
    }

}