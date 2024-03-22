## Network Protocol

This document details the protocol for the client/server interaction.

All internal commands have to consist of four characters in uppercase followed by a blank space and optionally one or 
more parameters. The commands as well as the needed message and the intended functionality are detailed in the tables below.<br>
On the client side, the user can input commands via the terminal. The column "terminal input" shows which commands can 
be used in which context. On the server side, no commands can be input in the terminal.

### Commands from the client to the server:

| command | parameter                              | functionality                                                              | terminal input                              |
|---------|----------------------------------------|----------------------------------------------------------------------------|---------------------------------------------|
| CHAT    | message                                | sends the given message to the server to redistribute to all other clients | \chat message                               |         
| WHSP    | name of one other client and a message | sends the message to the server to redistribute to the given other client  | \whisper username message                   |            
| CHNA    | new username                           | sends the new username to the server, server changes the username          | \changeUsername newUsername                 |         
| PONG    | time send by the server                | returns a ping send by the server to the server                            | there is no terminal input for this command |         
 | PING    | current system time                    | sends a ping to the server to check for connection losses                  | there is no terminal input for this command |
| QUIT    | no parameter                           | used to disconnect the client                                              | \quit                                       |


### Commands from the server to the client:

| command | parameter               | functionality                                                                                                                    |
|---------|-------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| BRCT    | message                 | used to send messages from the server to multiple client, the message will be print out in the client console as Alfred: message |
| PING    | current system time     | sends a ping to a client containing the current time                                                                             |
| PONG    | time send by the client | returns a ping send by the client to the client                                                                                  |
| CHNA    | new username            | confirms a changed username to the client, all connected clients are informed about this change via a printout in the terminal   |
| QUIT    | no parameter            | confirms a client disconnect to the client                                                                                       |
| CHAT    | message                 | distributes a chat message to the connected clients, message will be print out as username: message                              |


### Examples:

Case Ping/Pong: <br>
Every two seconds, client/server send a string with PING and the current system time to server/client via a Ping class, 
e.g. "PING 1711116082041".
The other side recognizes the command "PING" by splitting the string. Via a switch case the string "PONG 1711116082041" 
is sent back.
client/server then again split the string and again in a switch case give the time back to the Ping class which compares 
it with the now current time to check for timeouts.

Case Chat: <br>
The user Anisja (for example) inputs the string "\chat hi" into the console. The command \chat is then recognized and 
the string "CHAT hi" send to the server. Again in the switch case the message is sent to the communications class which
then sends the string "CHAT Anisja: hi" to all other clients. They in turn print out "Anisja: hi" in the console.

Case Whisper: <br>
The user Anisja inputs the string "\whisper Lina hi" into the console. Analogously to above, then the string "WHSP Lina hi"
is send to the server. The server splits the string, checks if a client with username Lina exists and either sends 
"BRCT There is no player with this name" to Anisja or "CHAT Anisja: hi" to Lina. The clients in turn print out either
"Alfred: There is no player with his name" or "Anisja: hi" to the console.

Case Change Username: <br>
The user Anisja wants to change their name to Lina and inputs "\changeUsername Lina" into the console. The client then
sends "CHNA Lina" to the server, which forwards the string "Lina" to the method for changing the username in the Player
class. The method checks whether the name already exists (in which case a "_number" is added to the end) and then sends
"CHNA Lina" (or "CHNA Lina_number") back to the client and "BRCT Player Anisja changed their name to Lina" to all other clients. 
The messages then are print out in the console again as "Your new username is Lina" or "Player Anisja changed their name to Lina"
respectively.

Case Quit: <br>
The user Anisja wants to disconnect from the game and enters "\quit" in the console. The client then sends "QUIT" to the server
which then sends "QUIT goodbye client" back to the client, closes all necessary object for this client, removes
the client from the player list and informs other clients via
"BRCT Player Anisja has disconnected". The client receives this string, prints out "Goodbye" in the terminal, closes all 
necessary objects and terminates.

Case Broadcast: <br>
This command is only used by the server to communicate necessary information to all necessary clients, e.g. when other clients
change their usernames or connect or disconnect to/from the server. Also see cases above for concrete examples.