## Network Protocol

This document details the protocol for the client/server interaction.

All internal commands have to consist of four characters in uppercase followed by a blank space and optionally one or 
more parameters. The commands as well as the needed message and the intended functionality are detailed in the tables below.<br>
On the client side, the user can input commands via the terminal. The column "terminal input" shows which commands can 
be used in which context. On the server side, no commands can be input in the terminal.

### Commands from the client to the server:

| command | parameter                              | functionality                                                              | terminal input                               |
|---------|----------------------------------------|----------------------------------------------------------------------------|----------------------------------------------|
| CHAT    | message                                | sends the given message to the server to redistribute to all other clients | \chat message                                |         
| WHSP    | name of one other client and a message | sends the message to the server to redistribute to the given other client  | \whisper username message                    |            
| CHNA    | new username                           | sends the new username to the server, server changes the username          | \changeUsername newUsername                  |         
| PONG    | time send by the server                | returns a ping send by the server to the server                            | there is no terminal input for this command  |         
 | PING    | current system time                    | sends a ping to the server to check for connection losses                  | there is no terminal input for this command  |
| QUIT    | no parameter                           | used to disconnect the client                                              | \quit                                        |
| LOLI    | no parameter                           | requests a list of all lobbies and players in lobbies from the server      | \showLobbies                                 | 
| CRLO    | name of the lobby                      | creates a new lobby with the given name                                    | \newLobby name                               |
| ENLO    | name of the lobby                      | the player enters the lobby with the given name                            | \enterLobby name                             |
| LELO    | name of the lobby                      | the player leaves the lobby with the given name                            | \leaveLobby name                             |
| LOCH    | message                                | used to send a chat message only to players in the same lobby              | \lobbyChat message                           |
| STRG    | no parameter                           | used to start a game in a lobby                                            | \start                                       |
| PLLI    | no parameter                           | requests a list of all players that are connected to the server            | \showPlayers                                 |
| HGSC    | no parameter                           | requests the current high score list from the server                       | \showHighScores                              |
| ROLL    | no parameter                           | communicates that a player wants to roll the dice                          | there is no terminal input for this command  |
| ENTY    | entry name                             | gives the chosen entry to the game logic to enter the dice                 | there is no terminal input for this command  |
| STEA    | username of a player and entry name    | used by the player to steal an entry from another player                   | there is no terminal input for this command  |
| FRZE    | username of a player and entry name    | used by the player to freeze an entry of another player                    | there is no terminal input for this command  |
| COUT    | username of a player and entry name    | used by the player to cross out an entry of another player                 | there is no terminal input for this command  |
| SHFT    | no parameter                           | used by the player to shift the entry sheet by one person                  | there is no terminal input for this command  |
| SWAP    | username of a player                   | used by the player to swap their sheet with another player                 | there is no terminal input for this command  |
| ENDT    | no parameter                           | used to indicate that a player ends their round                            | there is no terminal input for this command  |
| SAVE    | numbers of the dice                    | used to save dice the player does not want to re-roll                      | there is no terminal input for this command  |
| LOPL    | no parameter                           | requests the list of players in the same lobby as the asking player        | there is no terminal input for this command  |

### Commands from the server to the client:

| command | parameter                  | functionality                                                                                                                    |
|---------|----------------------------|----------------------------------------------------------------------------------------------------------------------------------|
| BRCT    | message                    | used to send messages from the server to multiple client, the message will be print out in the client console as Alfred: message |
| PING    | current system time        | sends a ping to a client containing the current time                                                                             |
| PONG    | time send by the client    | returns a ping send by the client to the client                                                                                  |
| CHNA    | new username               | confirms a changed username to the client, all connected clients are informed about this change via a printout in the terminal   |
| QUIT    | no parameter               | confirms a client disconnect to the client                                                                                       |
| CHAT    | message                    | distributes a chat message to the connected clients, message will be print out as username: message                              |
| LOLI    | the list of all lobbies    | return the list of all lobbies to the client to print out                                                                        |
| CRLO    | lobby name                 | returns the created lobby to the client to update the list in the gui and confirm creation                                       |
| ENLO    | lobby name and player name | update the gui                                                                                                                   |
| LELO    | lobby name and player name | update the gui                                                                                                                   |
| PLLI    | list of all players        | returns the list of all connected players to the client to print put                                                             |
| HGSC    | current high score list    | returns the current high score list to the client                                                                                |
| ROLL    | rolled dice                | returns the rolled dice to the player                                                                                            |
| LOST    | lobby and status           | informs the clients about a change in lobby status to update the gui                                                             |
| STRT    | round number and username  | informs the players about a new round and whos turn it is                                                                        |
| STEA    | username and entry name    | informs the client about a stolen entry to update the gui                                                                        |
| FRZE    | username and entry name    | informs the client about a frozen entry to update the gui                                                                        |
| COUT    | username and entry name    | informs the client about a crossed out entry to update the gui                                                                   |
| SHFT    | no parameter               | informs the client about a shift in entry sheet to update the gui                                                                |
| SWAP    | username                   | informs the client about a swap in entry sheets to update the gui                                                                |
| ENTY    | entry name and points      | informs the client about a new entry made and about the points to update the gui                                                 |
| ACTN    | list of actions            | informs the client about an update of actions they have                                                                          |
| RANK    | rank of the players        | returns the final rank at the end of the game to display the winner                                                              |
| LOPL    | player list                | returns a list of players in a given lobby                                                                                       |

### Examples:

Case Ping/Pong: <br>
Every two seconds, client/server send a string with PING and the current system time to server/client via a Ping class, 
e.g. "PING 1711116082041".
The other side recognizes the command "PING" by splitting the string. Via a switch case the string "PONG 1711116082041" 
is sent back.
client/server then again split the string and again in a switch case give the time back to the Ping class which compares 
it with the now current time to check for timeouts.

Case Chat and Lobby Chat: <br>
The user Anisja (for example) inputs the string "\chat hi" into the console. The command \chat is then recognized and 
the string "CHAT hi" send to the server. Again in the switch case the message is sent to the communications class which
then sends the string "CHAT Anisja: hi" to all other clients. They in turn print out "Anisja: hi" in the console.
Analogously if Anisja had put "\lobbyChat hi", the server gets "LOCH hi" and only sends the message to
all the players in the lobby again with the command "CHAT".

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

Case Lobbies: <br>
Tha commands "CRLO", "ENLO" and "LELO" are used to handle creating and entering/leaving lobbies. The Player for example inputs
"\newLobby exampleName" into the console, the client then sends "CRLO exampleName" to the server, which creates a new lobby
and informs all other clients with the same command about this change. The lobby window in the gui then updates the list
of existing lobbies. If the player then enters "\enterLobby exampleName" / "\leaveLobby examoleName", the client sends
"ENLO exampleName" / "LELO exampleName" to the server which enters/takes the player out of the lobby and again informs the 
clients about this change with the same commands.

Case Lists: <br>
The player can request a list of all connected players as well as a list with all existing lobbies and players in those lobbies
with the commands "\showLobbies" and "\showPlayers". The client then sends the request to the server with the commands
"LOLI" and "PLLI". The server gets the lists as a String from the ListManager class (for example "Lina,Anisja,Riccardo,Dominique,"
for a list of Players, analogously for the lobbies) and sends "PLLI Lina,Anisja,Riccardo,Dominique," back to the client which 
prints the list to the terminal after formatting the string in the Print class.

Case start: <br>
Once a player is in a lobby with at most one other player, they can start a game in this lobby. For this the player inputs "\start"
to the console. The client then send the request to the server using "STRT". The server receives this command and calls the starter() function
in the GameManager class, which handles the game.

Case game: <br>
Once the starter() function is called to start the game, the method sends all necessary information (like which round it is, 
whos turn it is etc.) to the players in the lobby using "GAME information about the game". This is then printed out in the console
of the players.
The player for example receives the messages "Game: It is your turn!" and then "Game: Do you want to steal an entry or do 
you want to roll the dice? Answer 'want to steal' or 'want to roll'". The player can then enter "\gameAction want to roll"
to indicate that they want to roll the dice. The client then sends "GAME want to roll" to the server which sends the String
"want to roll" to the GameManager to handle in the game.

//TODO update the examples