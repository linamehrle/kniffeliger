import client.util.TerminalView;
import server.Server;

/**
 * This class is the starter class for our game "Kniffeliger". It is designed to contain the main method to decide given
 * the main parameters to start either the client or the server.
 */
public class Starter {
    /**
     * The main method.
     * @param args In args we have that args[0] should be either "client" or "server". The subsequent parameters are then
     *             for client &lt;hostadress&gt;:&lt;port&gt; [&lt;username&gt;] and for server &lt;port&gt;.
     */
    public static void main(String[] args) {
        try {
            // evaluate what to start
            String toStart = args[0];
            if(toStart.equalsIgnoreCase("client")) {
                startClient(args);
            } else if(toStart.equalsIgnoreCase("server")) {
                startServer(args);
            } else {
                throw new Exception("Neither client nor server was entered.");
            }

        } catch(Exception e) {
            // print the correct syntax if an exception was thrown
            String errMsg = """
                    ERROR: Please consider the following syntax:
                          client <hostadress>:<port> [<yourUsername>]
                          server <port>
                    Please try again with the correct syntax.""";


            TerminalView.printlnText(e.getMessage());
            TerminalView.printlnText(errMsg);
        }
    }

    /**
     * This method start the client and is called by the main method if "client" was entered.
     * @param args The input data the user has provided to start the jar. Look at main for the correct syntax.
     */
    private static void startClient(String[] args) throws Exception {
        // get host address and port
        String[] firstInput = args[1].split(":");

        // assure syntax
        if(firstInput.length != 2) {
            throw new Exception("Incorrect syntax in " + args[1]);
        }

        String hostAddress = firstInput[0];
        int port = Integer.parseInt(firstInput[1]);

        // set default username
        String username = "default";

        // allow additional syntax
        if(args.length != 2) {
            // get username
            int startIdx = args[2].indexOf('[') + 1;
            int endIdx = args[2].indexOf(']');

            // assure user entered something
            if(startIdx >= endIdx || startIdx <= 0) {
                throw new Exception("Incorrect syntax in username " + args[2] +
                        ".\nPlease enter a nonempty username with correct syntax.");
            }

            username = args[2].substring(startIdx, endIdx);
        }

        // start client
        client.manager.GameManager client = new client.manager.GameManager(hostAddress, port, username);
    }

    /**
     * This method start the client and is called by the main method if "server" was entered.
     * @param args The input data the user has provided to start the jar. Look at main for the correct syntax.
     */
    private static void startServer(String[] args) throws Exception {
        // get port
        int port = Integer.parseInt(args[1]);

        // start server
        server.Server server = new Server(port);
    }
}
