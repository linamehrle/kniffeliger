package client.gui;

import javafx.scene.image.Image;

import java.io.FileNotFoundException;
import java.util.stream.IntStream;


/**
 * Class that contains helper function for GameWindow for loading data (images) and formatting and conversions
 */
public class GameWindowHelper {

    /**
     * Loads dice images to Image array, such that images have only to be loaded once
     * @param imageArray Image array, to which the images are loaded
     */
    public static void loadImagesToArray(Image[] imageArray){
        IntStream.range(0, imageArray.length).forEach(i -> {
            try {
                imageArray[i] = GameWindowHelper.diceImageLoader(i);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * method to load images of dice faces
     * @param diceNumber number of dice face e.g. 1-6
     * @return ImageView object of diceFace
     * @throws FileNotFoundException if file not found
     */
    public static Image diceImageLoader(int diceNumber) throws FileNotFoundException {
        //String.valueOf(GameWindowController.class.getResource("/images/dice-" + diceNumber  + ".png"))
        //FileInputStream fis = new FileInputStream(file);
        String saved = "";
        if (diceNumber > 6){
            saved = "s";
            diceNumber = diceNumber -6;
        }
        return new Image(String.valueOf(GameWindowController.class.getResource("/images/dice-" + saved + diceNumber  + ".png")), 64, 63.2, true, false);
    }


    /**
     * Method to convert array of String containing indices of saved dices to String suitable to send to gamelogic
     * @param diceStashedList Array of String containing indices of dices to save as String
     * @return New string containing the indices of the dice separated by spaces
     */
    public static String diceStashedArrToString(String[] diceStashedList){
        StringBuilder saveMsgString = new StringBuilder();
        for (String elem:diceStashedList){
            if (! elem.isEmpty() ){
                saveMsgString.append(elem).append(" ");
            }
        }
        return saveMsgString.toString();
    }



    /**
     * Method that determines the required separation (tabulators) to align different strings
     * @param title String, on the length of which the required number of tabualators is determined
     * @param baselength Minimal number of tabulators added, e.g. separation added to longest String
     * @return String of containing a number of \t (tabulators), the number depends on the length of the layouted string
     */
    public static String fillWithTabulators(String title, int baselength) {
        String separation = "";
        //Align the scores by adjusting separation
        int titleLength = title.length();
        if ( title.equals("kniffeliger") ){
            separation = "\t".repeat(baselength + 1);
        }else if (titleLength >= 10) {
            separation = "\t".repeat(baselength);
        } else if (titleLength > 8) {
            separation = "\t".repeat(baselength + 1);
        } else if (titleLength > 3) {
            separation = "\t".repeat(baselength + 2);
        } else {
            separation = "\t".repeat(baselength + 3);
        }
        return separation;
    }
}
