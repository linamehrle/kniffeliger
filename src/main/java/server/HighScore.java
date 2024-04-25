package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
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

        try {

            BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
            String line = reader.readLine();
            reader.close();

            line = line + ranking;

            String[] splitLine = line.split(",");

            Comparator<String> comparator = new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    Integer number1 = Integer.parseInt(o1.substring(0, o1.indexOf(":")));
                    Integer number2 = Integer.parseInt(o2.substring(0, o2.indexOf(":")));

                    if (number1 == number2) {
                        return 0;
                    } else if (number1 < number2) {
                        return 1;
                    } else {
                        return -1;
                    }

                }
            };

            Arrays.sort(splitLine, comparator);

            String newRanking = "";
            for (int i = 0; i < Math.min(10, splitLine.length); i++) {
                newRanking = newRanking + splitLine[i] + ",";
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"));
            writer.write(newRanking);
            writer.flush();
            writer.close();

        } catch (FileNotFoundException e) {
            //creates the file if it does not exist yet
            File highScore = new File("highscore.txt");
            logger.info("new file highscore.txt created");
            writeInitialHighScore(highScore);
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
        } catch (FileNotFoundException e) {
            //creates the file if it does not exist yet
            File highScore = new File("highscore.txt");
            logger.info("new file highscore.txt created");
            writeInitialHighScore(highScore);
            return "353:Benni,";
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
        return highScoreList;
    }

    /**
     * Used to wirte an initial high score to the list (because Benni will always be the mvp <3)
     * @param file
     */
    private static void writeInitialHighScore(File file) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write("353:Benni,");
            writer.close();
        } catch (IOException e) {
            logger.warn(e.getMessage());
        }
    }

}
