package server;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This class handles the high score list. It has methods to read the current list and update it when a game is done.
 */
public class HighScore {

    static Logger logger = Starter.getLogger();

    private static BufferedReader reader;

    /**
     * This method updates the high score list with the new scores once a game is done.
     *
     * @param ranking The score at the end of the game given by the GameManager
     */
    public static void updateHighScore(String ranking) {
        //TODO handle input string to get different player rankings
        try {
            reader = new BufferedReader(new FileReader("highscore.txt"));
            String line = reader.readLine();

            //TODO update the list and rewrite the file, only go up to ten?

        } catch (FileNotFoundException e) {
            //TODO create file if it does not exist
            logger.info("new file highscore.txt created");
        } catch (Exception e) {
            logger.warn(e.getMessage());
        }
    }

    /**
     * This method reads the high score list from the txt file and returns it as a string
     *
     * @return a String containing the current high score list in the form of
     * "username:score,username:score,.."
     */
    public static String getHighScoreList() {
        String highScoreList = "";
        try {
            reader = new BufferedReader(new FileReader("highscore.txt"));
            highScoreList = reader.readLine();
        } catch (FileNotFoundException e) {
            logger.warn(e.getMessage() + " : no file highscore.txt found");
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return highScoreList;
    }
}
