package client.gui;

import javafx.scene.image.Image;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import starter.Starter;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;


/**
 * Class that contains helper function for GameWindow for loading data (images) and formatting and conversions
 */
public class GameWindowHelper {

    /* #################################################################################################################

    Image loading and processing methods

     ################################################################################################################ */

    /**
     * Loads dice images to Image array, such that images have only to be loaded once
     * @param imageArray Image array, to which the images are loaded
     */
    public static void loadImagesToArray(Image[] imageArray, String imageType){
        IntStream.range(0, imageArray.length).forEach(i -> {
            try {
                switch (imageType){
                    case "number" -> imageArray[i] = GameWindowHelper.numberImageLoader(i);
                    default -> imageArray[i] = GameWindowHelper.diceImageLoader(i);
                }

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

    public static Image numberImageLoader(int diceNumber) throws FileNotFoundException {
        String saved = "";
        if (diceNumber > 6){
            saved = "s";
            diceNumber = diceNumber -6;
        }
        return new Image(String.valueOf(GameWindowController.class.getResource("/images/number-" + saved + diceNumber  + ".png")), 64, 63.2, true, false);
    }



    /**
     * Method to convert array of String containing indices of saved dices to String suitable to send to gamelogic
     * @param diceStashedList Array of String containing indices of dices to save as String
     * @return New string containing the indices of the dice separated by spaces
     */
    public static String diceStashedArrToString(String[] diceStashedList){
        Starter.getLogger().trace("Dices to save are " + Arrays.toString(diceStashedList));

        StringBuilder saveMsgString = new StringBuilder();
        for (String elem:diceStashedList){
            if (! elem.isEmpty() ){
                saveMsgString.append(elem).append(" ");
            }
        }
        return saveMsgString.toString();
    }

    /* #################################################################################################################

    Sound loading and processing methods

     ################################################################################################################ */

    /**
     * Loads audio file to MediaPlayer object
     * File is buffered for playback (not entire file loaded into memory)
     * Adequate method for large files with more playback controls and low demands on latency
     * @param fileName
     * Name of file including suffix (e.g. music.mp3), file must be in resources/audio/ folder
     * @return
     * Audio file transformed into MediaPlayer object
     * @throws FileNotFoundException
     * if file not found
     */
    public static MediaPlayer loadMedia(String fileName) throws FileNotFoundException {
        Media audio = new Media(Objects.requireNonNull(GameWindowController.class.getResource("/audio/" + fileName)).toExternalForm());
        return new MediaPlayer(audio);
    }


    /**
     * Loads audio file to AudioClip object
     * Entire file is loaded into memory
     * Adequate for small files that require low latency with in turn little control over playback, especially sound effects
     * @param fileName
     * Name of file including suffix (e.g. music.mp3), file must be in resources/audio/ folder
     * @return
     * @throws FileNotFoundException
     * if no such file in resources/audio/ folder
     */
    public static AudioClip loadSoundEffect(String fileName) throws FileNotFoundException {
        return new AudioClip(Objects.requireNonNull(GameWindowController.class.getResource("/audio/" + fileName)).toExternalForm());
    }


    /* #################################################################################################################

    Conversion and parsing methods

     ################################################################################################################ */


    /**
     * Splits String first at spaces and then at colons
     * @param input
     * String to split: expected format "username key1:value1 key2:value2 etc."
     * @return
     * ArrayList containing String arrays of key value pairs (except for first element which is String[1]
     */
    public static ArrayList<String[]> spaceColonToListSplit (String input) {
        ArrayList<String[]> spaceColonSplittedList = new ArrayList<>();
        String[] spaceSplit = input.split(" ");
        spaceColonSplittedList.add(new String[]{spaceSplit[0]});
        for (int i=1; i < spaceSplit.length; i++){
            spaceColonSplittedList.add(spaceSplit[i].split(":"));
        }
        return spaceColonSplittedList;
    }



    /**
     * Method that determines the required separation (tabulators) to align different strings
     * @param title String, on the length of which the required number of tabualators is determined
     * @param baselength Minimal number of tabulators added, e.g. separation added to longest String
     * @return String of containing a number of \t (tabulators), the number depends on the length of the layouted string
     */
    public static String fillWithTabulators(String title, int baselength) {
        String separation;
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
