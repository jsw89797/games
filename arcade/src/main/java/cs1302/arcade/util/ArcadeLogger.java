package cs1302.arcade.util; //Package statement

/**
 * A collection of logging methods used in the ArcadeApp.
 * Capable of logging errors, info, and debug messages, which
 * can be toggled.
 * @author Team Muskyteers
 * @version 4/29/19
 */
public class ArcadeLogger {

    /**
     * This boolean determines whether to display debug messages.
     */
    public static final boolean DEBUG = false;

    // Style Reset Code
    public static final String ANSI_RESET = "\u001B[0m";
    // Color Codes
    private static final String ANSI_BLACK = "\u001B[30m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN = "\u001B[36m";
    private static final String ANSI_WHITE = "\u001B[37m";

    /**
     * Logs an info message to the console. Always displayed.
     * @param msg the message to display
     */
    public static void logInfo(String msg) {
        System.out.println(ANSI_CYAN + "(info) cs1302-arcade > " + ANSI_RESET + msg);
    }

    /**
     * Logs a debug message to the console.
     * Only logs when {@code ArcadeLogger.DEBUG} is true
     * @param msg the message to display
     */
    public static void logDebug(String msg) {
        if (DEBUG) {
            System.out.println(ANSI_GREEN + "(debug) cs1302-arcade > " + ANSI_RESET + msg);
        }
    }

    /**
     * Logs an error message to the console. Always displayed.
     * @param err the error message to display
     */
    public static void logError(String err) {
        System.out.println(ANSI_YELLOW + "(error!) cs1302-arcade > " + ANSI_RESET + err);
    }

}