package server.gamelogic;

import application.server.gamelogic.ActionDice;
import application.server.gamelogic.GameManager;
import application.server.gamelogic.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class GameManagerTest {

    //TODO: DO ALL THE TESTING
    //TODO: reset dice testing
    //TODO: check for action dice testing
    //TODO: test delete action dice

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
    @DisplayName("Tests the method that deletes an action dice out of an array")
    void deleteActionDiceTest(){
        // action dice for player
        ActionDice[] actionDiceLina = new ActionDice[]{new ActionDice("steal"), new ActionDice("freeze"), new ActionDice("crossOut"), new ActionDice("shift"), new ActionDice("swap")};
        ActionDice[] actionDiceRiccardo = new ActionDice[]{new ActionDice("crossOut"), new ActionDice("shift"), new ActionDice("swap")};

        // players that hold action dice (needed to apply the delete entry method)
        Player lina = new Player("lina", 007);
        Player riccardo = new Player("riccardo", 001);
        lina.setActionDices(actionDiceLina);
        riccardo.setActionDices(actionDiceRiccardo);

        // delete action dice
        GameManager.deleteActionDice(lina, "shift");
        GameManager.deleteActionDice(riccardo, "crossOut");

        // control action dice to check
        ActionDice[] controlActionDiceLina = new ActionDice[]{new ActionDice("steal"), new ActionDice("freeze"), new ActionDice("crossOut"), new ActionDice("swap")};
        ActionDice[] controlActionDiceRiccardo = new ActionDice[]{new ActionDice("shift"), new ActionDice("swap")};



        assertAll(() -> assertEquals(turnActionDiceToString(controlActionDiceLina), turnActionDiceToString(lina.getActionDice())),
                () -> assertEquals(turnActionDiceToString(controlActionDiceRiccardo), turnActionDiceToString(riccardo.getActionDice()))
        );

    }

}