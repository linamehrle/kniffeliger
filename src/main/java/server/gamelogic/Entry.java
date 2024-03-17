package server.gamelogic;

import java.util.Arrays;

public class Entry {


    /**
     * Checks rolled dices for the value 'value' and add all dices with h´this value up. Sum is then returned. (1er, 2er, 3er, 4er, 5er, 6er)
     * @param rolledDices are the dices that have been rolled and saved
     * @param value method dices for this value (for example checks how many 6 it has, so value = 6)
     * @return the sum of dices
     * @throws Exception if there are less than 5 dices saved
     * @throws Exception if the value we need to compare the dices value with is not between 1 and 6
     */
    public static int singleValueRolls(int[] rolledDices, int value) throws Exception {
        // checks if there are 5 rolled dices
        if(!(rolledDices.length == 5)){
            throw new Exception("There are 5 dices but you handed me more or less.");
        }
        // checks if we inserted a valid value for dice
        if(!(value >=1 && value <= 6)){
            throw new Exception("A dice can only have values 1 to 6.");
        }

        int sum = 0;
        for (int d : rolledDices){
            if(d == value){
                sum = sum + d;
            }
        }
        return sum;
    }

    /**
     * Checks for 3 of a kind by sorting the array first and then checking where the three of a kind are,
     * because there are only three options after sorting: {t, t, t, x, y}, {x, t, t, t, y}, {x, y, t, t, t}
     * @param rolledDices are the dices that have been rolled and saved
     * @return returns value of three same dices
     */
    public static int threeOfAKind(int[] rolledDices) throws Exception {
        // checks if there are 5 rolled dices
        if(!(rolledDices.length == 5)){
            throw new Exception("There are 5 dices but you handed me more or less.");
        }

        // sorts rolled dices first
        Arrays.sort(rolledDices);
        int sum = 0;
        if(rolledDices[0] == rolledDices[2]){
            sum = 3 * rolledDices[0];
        } else if (rolledDices[1] == rolledDices[3]){
            sum = 3 * rolledDices[1];
        } else if (rolledDices[2] == rolledDices[4]){
            sum = 3 * rolledDices[4];
        }
        return sum;
    }

    /**
     * Checks for 4 of a kind by sorting the array first and then checking where the four of a kind are,
     * because there are only two options after sorting: {f, f, f, f, x}, {x, f, f, f, f}
     * @param rolledDices are the dices that have been rolled and saved
     * @return returns value of four same dices
     */
    public static int fourOfAKind(int[] rolledDices) throws Exception {
        // checks if there are 5 rolled dices
        if(!(rolledDices.length == 5)){
            throw new Exception("There are 5 dices but you handed me more or less.");
        }

        // sorts rolled dices first
        Arrays.sort(rolledDices);
        int sum = 0;
        if(rolledDices[0] == rolledDices[3]){
            sum = 4 * rolledDices[0];
        } else if (rolledDices[1] == rolledDices[4]){
            sum = 4 * rolledDices[4];
        }
        return sum;
    }

    /**
     * Checks rolled dices for small straight by sorting them first and then checking, if it should check first 4 or last 4 values of rolledDices and then
     * loops through each of the values inside the array and checks if it contains at most 1 repetition and if each value is followed previous + 1.
     * @param rolledDices are the dices that have been rolled and saved
     * @return return 30 if it is a small straight and 0 if not
     * @throws Exception if there are less than 5 dices saved
     */
    public static int smallStraight(int[] rolledDices) throws Exception {
        // checks if there are 5 rolled dices
        if (!(rolledDices.length == 5)) {
            throw new Exception("There are 5 dices but you handed me more or less.");
        }

        int res = 30;
        // counts for repeated values in between such as {1,2,2,3,4}. If there are more than 1 repetition, then we do not have a small straight.
        int repetitionCounter = 0;
        // sorts rolled dices in ascending order, so we can loop over it and check conditions for a small straight.
        Arrays.sort(rolledDices);
        /*
        First check if the first 4 values in rolledDice-array or last 4 values are supposed to be the small straight. If is one of those two cases since we sorted the array.
        That means we check: if rolledDices[1] == rolledDices[0] + 1, then it is the first 4 values, if rolledDices[4] == rolledDices[3] + 1, then it is the last 4 values.
        If it is none of those two above, then we do not have a small straight and return 0.
        Then in the loop we check if we have at most 1 repetition then we can continue loop as long as we have only one. Also, we check if the next value is the previous vale + 1.
        If so, if continues the loop (else) and if not, it returns 0. If it made it through the loop without return then we have a small straight and return 30.
        */
        if ((rolledDices[1] == rolledDices[0] + 1) || (rolledDices[4] == rolledDices[3] + 1)){
            for(int i = 0; i < rolledDices.length - 1; i++){
                if(rolledDices[i+1] == rolledDices[i] && repetitionCounter < 1){
                    repetitionCounter = repetitionCounter + 1;
                } else if (rolledDices[i+1] != rolledDices[i] + 1){
                    return 0;
                } //else: continue loop
            }
            // if it made it through the loop without returning 0, then we have a small straight
        } else {
            return 0;
        }
        return res;
    }

    /**
     * Checks rolled dices for large straight by sorting them first and then checking, if each of the values inside the array
     * is followed by a value that is the previous + 1.
     * @param rolledDices are the dices that have been rolled and saved
     * @return return 40 if it is a large straight and 0 if not
     * @throws Exception if there are less than 5 dices saved
     */
    public static int largeStraight(int[] rolledDices) throws Exception {
        // checks if there are 5 rolled dices
        if(!(rolledDices.length == 5)){
            throw new Exception("There are 5 dices but you handed me more or less.");
        }

        int res = 40;
        // sorts rolled dices in ascending order, so we can loop over it and check conditions for a large straight.
        Arrays.sort(rolledDices);
        /*
        First check if array starts with 1 or 2 because if not, then it is definitely not a straight, so it returns the res, which at that point is 0.
        To check a straight, we loop through rolled dices and check if next value is the previous value + 1.
        If we reached at the last entry it does not check the mentioned condition since this has already been checked by the leap before.
        */
        if(rolledDices[0] == 1 || rolledDices[0] == 2){
            for(int i = 0; i < rolledDices.length - 1; i++){
                if(rolledDices[i+1] != rolledDices[i] + 1){
                    return 0;
                }
            }
            // if it made it through loop without returning 0, then it is a large straight, so it returns res = 40
        } else {
            return 0;
        }
        return res;
    }

    /**
     * Checks rolled dices for a full house (=a pair and a triplet) by first sorting the rolled dices, then checking if there is a pair and a triplet.
     * Returns 25 if it is a full house, returns 0 otherwise.
     * @param rolledDices are the dices that have been rolled and saved
     * @return return 25 if it is a full house and 0 if not
     * @throws Exception if there are less than 5 dices saved
     */
    public static int fullHouse(int[] rolledDices) throws Exception {
        // checks if there are 5 rolled dices
        if(!(rolledDices.length == 5)){
            throw new Exception("There are 5 dices but you handed me more or less.");
        }

        int res = 0;
        // sorts array first
        Arrays.sort(rolledDices);
        /*
        There are have two options for a full house after sorting the array {t, t, t, p, p} of {p, p, t, t, t}.
        If the rolledDices[0] == rolledDices[2] && rolledDices[3] == rolledDices[4] (then we have the triplet first and the pair second)
        OR the rolledDices[0] == rolledDices[1] && rolledDices[2] == rolledDices[4] (then we have the pair first and the triplet last).
        If it is a full house we return 25, in all the other cases we do not have a full house and the res = 0 gets returned.
        */
        if((rolledDices[0] == rolledDices[2] && rolledDices[3] == rolledDices[4]) || (rolledDices[0] == rolledDices[1] && rolledDices[2] == rolledDices[4])){
            res = 25;
        }
        return res;
    }

    /**
     * Checks for Kniffeliger/Yahtzee (= 5 of a kind) by sorting the array first and then checking if there are five of a kind,
     * because if there are, the first and the last value are the same: {f, f, f, f, f}
     * @param rolledDices are the dices that have been rolled and saved
     * @return returns value of five same dices
     */
    public static int kniffeliger(int[] rolledDices) throws Exception {
        // checks if there are 5 rolled dices
        if(!(rolledDices.length == 5)){
            throw new Exception("There are 5 dices but you handed me more or less.");
        }

        // we do not need to sort rolled dices first because it should be all the same value
        int sum = 0;
        if(rolledDices[0] == rolledDices[4]){
            sum = 50;
        }
        return sum;
    }

    /**
     * Sums up all dices.
     * @param rolledDices are the dices that have been rolled and saved
     * @return return the sum of all dices
     * @throws Exception if there are less than 5 dices saved
     */
    public static int chance(int[] rolledDices) throws Exception{
        // checks if there are 5 rolled dices
        if(!(rolledDices.length == 5)){
            throw new Exception("There are 5 dices but you handed me more or less.");
        }

        // adds dices
        int sum = 0;
        for (int d : rolledDices){
            sum = sum + d;
        }
        return sum;
    }
}
