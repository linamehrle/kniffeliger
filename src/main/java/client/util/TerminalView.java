package client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TerminalView {

    /**
     * Constructor of the TerminalView class.
     */
    public TerminalView() {
        // print welcome message
        String welcomeText = "==============================================================\n" +
                             "===                     Kniffeliger                        ===\n" +
                             "==============================================================\n" +
                             "Welcome to Kniffeliger TestDemo.";
        printText(welcomeText);
    }

    /**
     * This function prints the given text in a new line to the console.
     * @param text the given text which will be printed to the console
     */
    public void printText(String text) {
        System.out.println(text);
    }

    /**
     * This function prints the given text in a new line to the console and wait for an user input.
     * @param text the given text which will be printed to the console
     * @return message entered by user
     */
    public String writePrompt(String text) {
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
