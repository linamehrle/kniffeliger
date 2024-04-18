package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;
import org.apache.logging.log4j.Logger;
import starter.Starter;

/**
 * This class handles the high score list. It has methods to read the current list and update it when a game is done.
 */
public class HighScore {
    static Logger logger = Starter.getLogger();

    /**
     * This method updates the high score list with the new scores once a game is done.
     *
     * @param ranking The score at the end of the game given by the GameManager (not in order!) of the form
     *                "username:score,username:score,..."
     */
    public static void updateHighScore(String ranking) {
        //TODO check if highest score in the game is lower than lowest on the board to make more efficient?
        try {
            //read current high scores
            BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
            String line = reader.readLine();
            reader.close();

            line = line + ranking;

            Map<Integer,String> sortedRanking = new TreeMap<>(); //apparently this is a sorted map

            String[] splitLine = line.split(",");
            for (String score : splitLine) {
                String[] personAndScore = score.split(":");
                sortedRanking.put(Integer.parseInt(personAndScore[1]), personAndScore[0]);
            }

            //create new list to write in highscore.txt
            Object[] mapKeys = sortedRanking.keySet().toArray();
            String newHighScores = "";

            for (int i = mapKeys.length - 1; i >= Math.max(0, mapKeys.length - 10); i--) {
                newHighScores = newHighScores + sortedRanking.get(mapKeys[i]) + ":" + mapKeys[i].toString() + ",";
            }

        } catch (FileNotFoundException e) {
            //creates the file if it does not exist yet
            new File("highscore.txt");
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
        try (BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"))) {
            highScoreList = reader.readLine();
            FileWriter writer = new FileWriter("highscore.txt");
            writer.write("test");
            writer.flush();
            writer.close();
        } catch (FileNotFoundException e) {
            logger.warn(e.getMessage() + ": no file highscore.txt found");
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return highScoreList;
    }


}
