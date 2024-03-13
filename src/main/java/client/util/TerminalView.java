package client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalView {
    /**
     * This function prints the given text in a new line to the console.
     * @param text the given text which will be printed to the console
     */
    public static void printText(String text) {
        System.out.println(text);
    }

    /**
     * This function prints the given text in a new line to the console and wait for an user input.
     * @param text the given text which will be printed to the console
     * @return message entered by user
     */
    public static String writePrompt(String text) {
        // create reader
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        printText(text);

        try {
            // read and return input
            String input = reader.readLine();

            //TODO: create a BufferedReader as attribute to the class and then close on quit somewhere
            reader.close();

            // return input
            return input;
        } catch (IOException e) {
            printText("An error occurred.");
            throw new RuntimeException(e);
        }
    }
}
