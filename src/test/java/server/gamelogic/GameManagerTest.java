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

    @Test
    @DisplayName("Tests if it properly checks if all dice are saved.")
    void allDiceSavedTest() {
        // random index from 0 to 4 of a dice that will not be saved
        int randomIndex = (int) (Math.random() * 4);

        // generate a game manager, so we can use entry validation method
        GameManager gm = new GameManager();
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        Dice d3 = new Dice();
        Dice d4 = new Dice();
        Dice d5 = new Dice();
        Dice[] notAllSavedDice = new Dice[]{d1, d2, d3, d4, d5};
        // saves all dice except for the one with randomIndex
        for (int i = 0; i < 5; i++) {
            if (i != randomIndex) {
                notAllSavedDice[i].saveDice();
            }
        }

        Dice d11 = new Dice();
        Dice d21 = new Dice();
        Dice d31 = new Dice();
        Dice d41 = new Dice();
        Dice d51 = new Dice();
        Dice[] allSavedDice = new Dice[]{d11, d21, d31, d41, d51};
        // saves all dice
        for (int i = 0; i < 5; i++) {
            allSavedDice[i].saveDice();
        }

        // test
        assertAll(() -> assertFalse(GameManager.allDiceSaved(notAllSavedDice)),
                () -> assertFalse(notAllSavedDice[randomIndex].getSavingStatus()),
                () -> assertTrue(GameManager.allDiceSaved(allSavedDice))
        );

    }

    @Test
    @DisplayName("Checks if dice are properly reset, so savingStatus is false and numberOfRolls and diceValue are 0.")
    void resetDiceTest() {
        // generate a game manager, so we can use entry validation method
        GameManager gm = new GameManager();
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        Dice d3 = new Dice();
        Dice d4 = new Dice();
        Dice d5 = new Dice();
        Dice[] allDice = new Dice[]{d1, d2, d3, d4, d5};
        gm.rollDice(allDice);
        // saves all dice except for the one with randomIndex
        int[] valuesArray = Dice.getAsIntArray(allDice);

        for (int i = 0; i < 5; i++) {
            Dice d = allDice[i];
            int randomRollValue = valuesArray[i];
            d.saveDice();
            assertAll(() -> assertTrue(d.getSavingStatus()),
                    () -> assertEquals(1, d.getNumberOfRolls()),
                    () -> assertEquals(randomRollValue, d.getDiceValue())
            );
        }

        // implementation of reset dice copied to test it with junit
        for (Dice dice : allDice) {
            dice.resetDice();
        }

        for (int i = 0; i < 5; i++) {
            Dice d = allDice[i];
            assertAll(() -> assertFalse(d.getSavingStatus()),
                    () -> assertEquals(0, d.getNumberOfRolls()),
                    () -> assertEquals(0, d.getDiceValue())
            );
        }
    }

    @Test
    @DisplayName("Checks if dice are rolled correctly.")
    void rolledDiceTest() {
        GameManager gm = new GameManager();
        Dice d1 = new Dice();
        Dice d2 = new Dice();
        Dice d3 = new Dice();
        Dice d4 = new Dice();
        Dice d5 = new Dice();
        Dice[] allDice = new Dice[]{d1, d2, d3, d4, d5};
        gm.rollDice(allDice);

        // TODO: finish test

    }


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