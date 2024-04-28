package server.gamelogic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameManagerTest {
    // starter method cannot be tested with unit tests but only with play-through

    /**
     * Dummy player class to test methods. Contains all important fields and methods of a player.
     */
    public class DummyPlayer extends server.Player{
        private ActionDice[] actionDice;

        /**
         * The constructor for the Player class. It starts a new ClientThread per Player.
         */
        public DummyPlayer(String name) {
            super(name);
        }

        /**
         * Get the action dice of player.
         *
         * @return all action dice of player saved in array.
         */
        public ActionDice[] getActionDice() {
            return actionDice;
        }

        /**
         * Access private field username.
         *
         * @return name of user
         */
        public String getUsername() {
            return super.username;
        }

        /**
         * Access private field id.
         *
         * @return id of user
         */
        public int getId() {
            return super.id;
        }

        /**
         * Set a new set of action dice.
         *
         * @param newActionDice new array of action dice a player can use.
         */
        public void setActionDices(ActionDice[] newActionDice) {
            actionDice = newActionDice;
        }

    }

    /**
     * Turns action dice array into String array with names of actions as array
     *
     * @param actionDiceArray array to extract names from
     * @return name of actions as String
     */
    public static String turnActionDiceToString(ActionDice[] actionDiceArray){
        String result = "";
        for (int i = 0; i < actionDiceArray.length; i++){
            if (actionDiceArray[i] != null) {
                result = result + " " + actionDiceArray[i].getActionName();
            }
        }
        return result;
    }


//    // TODO: allDiceSavedTest
//    @Test
//    @DisplayName("Tests if it properly checks if all dice are saved.")
//    void allDiceSavedTest() {
//
//    }

    // TODO: resetDiceTest

    // TODO: rollDiceTest

    // TODO: addActionDiceTest

    @Test
    @DisplayName("Tests the method that deletes an action dice out of an array")
    void deleteActionDiceTest(){
        // action dice for player
        ActionDice[] actionDiceLina = new ActionDice[]{new ActionDice("steal"), new ActionDice("freeze"), new ActionDice("crossOut"), new ActionDice("shift"), new ActionDice("swap")};
        ActionDice[] actionDiceRiccardo = new ActionDice[]{new ActionDice("crossOut"), new ActionDice("shift"), new ActionDice("swap")};

        // players that hold action dice (needed to apply the delete entry method)
        DummyPlayer lina = new DummyPlayer("lina");
        DummyPlayer riccardo = new DummyPlayer("riccardo");
        lina.setActionDices(actionDiceLina);
        riccardo.setActionDices(actionDiceRiccardo);

        // delete action dice
        GameManager gm = new GameManager();
        gm.deleteActionDice(lina, "shift");
        gm.deleteActionDice(riccardo, "crossOut");

        // control action dice to check
        ActionDice[] controlActionDiceLina = new ActionDice[]{new ActionDice("steal"), new ActionDice("freeze"), new ActionDice("crossOut"), new ActionDice("swap")};
        ActionDice[] controlActionDiceRiccardo = new ActionDice[]{new ActionDice("shift"), new ActionDice("swap")};

        assertAll(() -> assertEquals(turnActionDiceToString(controlActionDiceLina), turnActionDiceToString(lina.getActionDice())),
                () -> assertEquals(turnActionDiceToString(controlActionDiceRiccardo), turnActionDiceToString(riccardo.getActionDice()))
        );
    }

    // TODO: rankingTest

    // TODO: returnScoreAsStringTest

}