import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import server.Server;

import java.util.Arrays;

/**
 * This class is the starter class for our game "Kniffeliger". It is designed to contain the main method to decide given
 * the main parameters to start either the client or the server.
 */
public class Starter {
    public static Logger logger;

    /**
     * The main method.
     *
     * @param args In args we have that args[0] should be either "client" or "server".<br>
     *             The subsequent parameters are then
     *             <ul>
     *                  <li>client &lt;hostadress&gt;:&lt;port&gt; [&lt;username&gt;] {logger}</li>
     *                  <li>server &lt;port&gt; {logger}</li>
     *             </ul>
     *             For the optional parameter logger (default: TRACE_FILE_LOGGER) there are several opportunities:
     *             <ul>
     *                  <li>DEBUG_CONSOLE_LOGGER: level is DEBUG and output is only on console.</li>
     *                  <li>DEBUG_CONSOLE_AND_FILE_LOGGER: level is DEBUG and output is on console and in log file.</li>
     *                  <li>TRACE_FILE_LOGGER: level is TRACE and output is in log file.</li>
     *                  <li>TRACE_CONSOLE_AND_FILE_LOGGER: level is TRACE and output is on console and in log file.</li>
     *             </ul>
     */
    public static void main(String[] args) {
        try {
            // fetch logger parameter if available
            String loggingParam = args[args.length - 1];
            boolean loggerSpecified = false;

            if (loggingParam.contains("{") && loggingParam.contains("}")) {
                loggerSpecified = true;

                // set logger
                logger = LogManager.getLogger(loggingParam.substring(1, loggingParam.length() - 1));
            } else if (loggingParam.contains("{") || loggingParam.contains("}")) {
                throw new Exception("Invalid logger parameter.");
            } else {
                logger = LogManager.getLogger("TRACE_FILE_LOGGER");
            }

            logger.info("Start parameter were: " + Arrays.toString(args));

            // evaluate what to start
            String toStart = args[0];
            if (toStart.equalsIgnoreCase("client")) {
                startClient(args, loggerSpecified);
            } else if (toStart.equalsIgnoreCase("server")) {
                startServer(args);
            } else {
                throw new Exception("Neither client nor server was entered.");
            }
        } catch (
                Exception e) {
            // print the correct syntax if an exception was thrown
            String errMsg = """
                    ERROR: Please consider the following syntax:
                          client <hostadress>:<port> [<yourUsername>] {logger}
                          server <port> {logger}
                    Please try again with the correct syntax.""";

            System.out.println(e.getMessage());
            System.out.println(errMsg);
        }
    }

    /**
     * This method start the client and is called by the main method if "client" was entered.
     *
     * @param args            The input data the user has provided to start the jar. Look at main for the correct syntax.
     * @param loggerSpecified Set true if the logger was specified
     */
    private static void startClient(String[] args, boolean loggerSpecified) throws Exception {
        logger.trace("startClient()");

        // get host address and port
        String[] firstInput = args[1].split(":");

        // assure syntax
        if (firstInput.length != 2) {
            throw new Exception("Incorrect syntax in " + args[1]);
        }

        String hostAddress = firstInput[0];
        int port = Integer.parseInt(firstInput[1]);

        // set default username
        String username = "default";

        // allow additional syntax
        if (args.length != 2 && !loggerSpecified) {
            // get username
            int startIdx = args[2].indexOf('[') + 1;
            int endIdx = args[2].indexOf(']');

            // assure user entered something
            if (startIdx >= endIdx || startIdx <= 0) {
                throw new Exception("Incorrect syntax in username " + args[2] +
                        ".\nPlease enter a nonempty username with correct syntax.");
            }

            username = args[2].substring(startIdx, endIdx);
        }

        // start client
        client.GameManager client = new client.GameManager(hostAddress, port, username);
    }

    /**
     * This method start the client and is called by the main method if "server" was entered.
     *
     * @param args The input data the user has provided to start the jar. Look at main for the correct syntax.
     */
    private static void startServer(String[] args) throws Exception {
        logger.trace("startServer()");

        // get port
        int port = Integer.parseInt(args[1]);

        // start server
        server.Server server = new Server(port);
    }
}
