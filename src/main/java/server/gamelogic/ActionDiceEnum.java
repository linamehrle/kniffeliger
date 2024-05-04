package server.gamelogic;

/**
 * This is the list containing all possible action dice in the game
 */
public enum ActionDiceEnum {

    /**
     * Enables a player to steal an already filled entry from another player if they did not fill their entry yet
     */
    STEAL,

    /**
     * Enables a player to freeze an empty entry of another player, that player cannot fill this entry in their next turn
     */
    FREEZE,

    /**
     * [TODO]
     */
    CROSSOUT,
    SHIFT,
    SWAP
}
