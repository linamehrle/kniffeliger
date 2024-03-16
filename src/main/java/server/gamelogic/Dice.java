package server.gamelogic;

public class Dice {

    private boolean rolledStatus;
    private boolean savingStatus;
    private int numberOfRolls;
    private int diceValue;



    /**
     * Constructor that resets dice to the starting settings: we have a dice that has not been rolled (rolledStatus = false),
     * that has dice number 0 (diceValue = 0) and that has not been saved yet (savingStatus = false).
     */
    public Dice (){
        resetDice();
    }

    /**
     * Returns number of rolls.
     */
    public int getNumberOfRolls(){
        return numberOfRolls;
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
     * Will change the value of the dice to a random value between 1 and 6 including both.
     * It also increases the number of rolls by one (numberOfRolls++) and sets the variable rolledStatus to true, since the
     * dice has been rolled after calling this method. If the dice has been rolled three times, it gets saved automatically.
     * Method returns true if dice could be rolled and false, if it could not be rolled (so if dice has already been saved or if
     * dice has been rolled 3 times already).
     */
    //TODO: Tutor fragen, ob hier try-catch Sinn ergibt
    //TODO: Tutor fragen, ob es hier sinn ergibt true/false zurückzugeben (unter anderem für das unit testing)
    public boolean rollDice(){
        boolean couldRoll = false;
        if (!(savingStatus) && numberOfRolls < 3) {
            // adjusts variables after dice was rolled
            rolledStatus = true;
            numberOfRolls = numberOfRolls + 1;
            diceValue = (int)Math.floor(Math.random() * 6 + 1);
            // if dice has been rolled three times it gets saved automatically
            if(numberOfRolls == 3){
                saveDice();
            }
            couldRoll = true;
        }
        return couldRoll;
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