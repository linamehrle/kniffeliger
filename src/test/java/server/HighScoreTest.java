package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This is the test class for the class HighScore which handles the high score list
 */
class HighScoreTest {

    private String savedScore;

    /**
     * Used to safe the current high score list in highscore.txt so nothing changes when a test is run
     */
    @BeforeEach
    void safeString() {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
            savedScore = reader.readLine();
            reader.close();
        } catch (Exception e) {
            fail();
        }
    }

    /**
     * This is the test for the function updateHighScore which updates the high score list
     */
    @Test
    void testUpdateHighScore() {
        try {

            //test, if new score is placed correctly in the file
            BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"));
            writer.write("300:Lina,210:Anisja,150:Riccardo,30:Dominique,");
            writer.flush();
            writer.close();

            String newScore1 = "190:Nico,310:Benni,0:Loris,";
            HighScore.updateHighScore(newScore1);
            BufferedReader reader = new BufferedReader(new FileReader("highscore.txt"));
            assertEquals("310:Benni,300:Lina,210:Anisja,190:Nico,150:Riccardo,30:Dominique,0:Loris,", reader.readLine());
            reader.close();

            //test what happens when two people have the same score
            BufferedWriter writer2 = new BufferedWriter(new FileWriter("highscore.txt"));
            writer2.write("300:Lina,210:Anisja,150:Riccardo,30:Dominique,");
            writer2.flush();
            writer2.close();

            String newScore2 = "300:Benni,150:Nico,";
            HighScore.updateHighScore(newScore2);
            BufferedReader reader2 = new BufferedReader(new FileReader("highscore.txt"));
            assertEquals("300:Benni,300:Lina,210:Anisja,150:Nico,150:Riccardo,30:Dominique,", reader2.readLine());
            reader2.close();

            //test if score ends at ten correctly
            BufferedWriter writer3 = new BufferedWriter(new FileWriter("highscore.txt"));
            writer3.write("300:Lina,210:Anisja,150:Riccardo,30:Dominique,");
            writer3.flush();
            writer3.close();

            String newScore3 = "310:Benni,190:Nico,0:Loris,205:Rahel,154:Maria,500:Heiko,10:Raphi,432:Renato,";
            HighScore.updateHighScore(newScore3);
            BufferedReader reader3 = new BufferedReader(new FileReader("highscore.txt"));
            assertEquals("500:Heiko,432:Renato,310:Benni,300:Lina,210:Anisja,205:Rahel,190:Nico,154:Maria,150:Riccardo,30:Dominique,", reader3.readLine());
            reader3.close();

        } catch (IOException e) {
            fail();
        }
    }

    /**
     * Resets highscore.txt to the high score list before the test was run
     */
    @AfterEach
    void reset() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("highscore.txt"));
            writer.write(savedScore);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            fail();
        }
    }

}