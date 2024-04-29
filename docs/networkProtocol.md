## Network Protocol

This document details the protocol for the client/server interaction.

All internal commands have to consist of four characters in uppercase followed by a blank space and optionally one or 
more parameters. The commands as well as the needed message and the intended functionality are detailed in the tables below.<br>
On the client side, the user can input commands via the terminal. The column "terminal input" shows which commands can 
be used in which context. On the server side, no commands can be input in the terminal.

### Commands from the client to the server:

| command | parameter                              | functionality                                                              | terminal input                                |
|---------|----------------------------------------|----------------------------------------------------------------------------|-----------------------------------------------|
| CHAT    | message                                | sends the given message to the server to redistribute to all other clients | \chat message                                 |         
| WHSP    | name of one other client and a message | sends the message to the server to redistribute to the given other client  | \whisper username message                     |            
| CHNA    | new username                           | sends the new username to the server, server changes the username          | \changeUsername newUsername                   |         
| PONG    | time send by the server                | returns a ping send by the server to the server                            | there is no terminal input for this command   |         
 | PING    | current system time                    | sends a ping to the server to check for connection losses                  | there is no terminal input for this command   |
| QUIT    | no parameter                           | used to disconnect the client                                              | \quit                                         |
| LOLI    | no parameter                           | requests a list of all lobbies and players in lobbies from the server      | \showLobbies                                  | 
| CRLO    | name of the lobby                      | creates a new lobby with the given name                                    | \newLobby name                                |
| ENLO    | name of the lobby                      | the player enters the lobby with the given name                            | \enterLobby name                              |
| LELO    | name of the lobby                      | the player leaves the lobby with the given name                            | \leaveLobby name                              |
| LOCH    | message                                | used to send a chat message only to players in the same lobby              | \lobbyChat message                            |
| STRG    | no parameter                           | used to start a game in a lobby                                            | \start                                        |
| PLLI    | no parameter                           | requests a list of all players that are connected to the server            | \showPlayers                                  |
| HGSC    | no parameter                           | requests the current high score list from the server                       | \showHighScores                               |
| ROLL    | no parameter                           | communicates that a player wants to roll the dice                          | there is no terminal input for this command   |
| ENTY    | entry name                             | gives the chosen entry to the game logic to enter the dice                 | there is no terminal input for this command   |
| STEA    | username of a player and entry name    | used by the player to steal an entry from another player                   | there is no terminal input for this command   |
| FRZE    | username of a player and entry name    | used by the player to freeze an entry of another player                    | there is no terminal input for this command   |
| COUT    | username of a player and entry name    | used by the player to cross out an entry of another player                 | there is no terminal input for this command   |
| SHFT    | no parameter                           | used by the player to shift the entry sheet by one person                  | there is no terminal input for this command   |
| SWAP    | username of a player                   | used by the player to swap their sheet with another player                 | there is no terminal input for this command   |
| ENDT    | no parameter                           | used to indicate that a player ends their round                            | there is no terminal input for this command   |
| SAVE    | numbers of the dice                    | used to save dice the player does not want to re-roll                      | there is no terminal input for this command   |
| LOPL    | no parameter                           | requests the list of players in the same lobby as the asking player        | there is no terminal input for this command   |
| GAME    | game action                            | used to send information related to an ongoing game to the server          | \gameAction gameAction                        |
| RUSR    | no parameter                           | requests the own username                                                  | -                                             |
| STRT    | no parameter                           | used to start a game in a lobby                                            | \start                                        |

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
| GAME    | information from the game  | prints game related information for the clients to see                                                                           |
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
| ENTY    | entry name and score       | informs the client about a new entry made and about the score to update the gui                                                  |
| ACTN    | list of actions            | informs the client about an update of actions they have                                                                          |
| RANK    | rank of the players        | returns the final rank at the end of the game to display the winner                                                              |
| LOPL    | player list                | returns a list of players in a given lobby                                                                                       |
| ALDI    | rolled dice                | returns the rolled dice to tab 2 of the gui (similar to ROLL)                                                                    |
| ALES    | entry name and score       | informs the client about a new entry made and its score to update tab 2 the gui (similar to ENTY)                                |
| INES    | no paramter                | initializes the entry sheets in tab 2 of the gui (passive sheets of other players)                                               |
| TUSR    | transmits username         | transmits the username                                                                                                           |

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
of existing lobbies. If the player then enters "\enterLobby exampleName" / "\leaveLobby exampleName", the client sends
"ENLO exampleName" / "LELO exampleName" to the server which enters/takes the player out of the lobby and again informs the 
clients about this change with the same commands.
<br> Everytime the lobby status changes, e.g. if the lobby is full or a game is started, this is communicated to the clients 
via "LOST lobbyname (status)". The clients then update the lobby status in the lobby list in the lobby gui window.

Case Lists: <br>
The player can request a list of all connected players as well as a list with all existing lobbies and players in those lobbies
with the commands "\showLobbies" and "\showPlayers". The client then sends the request to the server with the commands
"LOLI" and "PLLI". The server gets the lists as a String from the ListManager class (for example "Lina,Anisja,Riccardo,Dominique,"
for a list of Players, analogously for the lobbies) and sends "PLLI Lina,Anisja,Riccardo,Dominique," back to the client which 
prints the list to the terminal after formatting the string in the Print class.
<br> The client can also request a list with all players in the same lobby as the player. This happens via the command "LOPL"
everytime a player enters a lobby so the game window controller has an updated list of all players that are part of the game.

Case in game: <br>
Once a player is in a lobby with at least one other player, they can start a game in this lobby. For this the player inputs "\start"
to the console or presses the start button in the gui. The client then send the request to the server using "STRG". 
The server receives this command and calls the starter() function in the GameManager class, which handles the game.
<br> At the beginning of each turn, the GameManager class informs the players about the current round and whos turn it is.
This happens via e.g. "STRT 3 Lina" to indicate that it is Lina's turn in round three.
<br> A usual play move goes like this: the player first rolls the dice using the roll button. This is send to the server
via the command "ROLL", the server gives the information to the gameManager to handle and then returns the rolled dice, e.g.
"ROLL 1 4 5 3 5". The player can roll again or save one or multiple dice. This is handled via "SAVE diceNumbers". nce all dice are saved
or the player has rolled three times, the player can choose an entry to insert the dice. The choice is send to the server via
the command "ENTY entryName". The server confirms this via the same command and the gui then updates the entry sheet list.
Once the player wants to end their turn, they press the "end turn" button and the server send "ENDT" to the server to communicate
that the next phase of the game is started.
<br> At the end of the game, the server send the final ranking to the players. This for example looks like "RANK 350:Benni,200:Anisja"
if Benni scored 350 points and Anisja scored 200 points. The client then opens a pop-up window to display the winner of the game.

Case action dice: <br>
Throughout the game, players can gain action dices that grant them special play moves. Everytime a player gets a new action dice 
or uses one, the server sends the whole list of current action dices to the player via "ACTN actionName actionName ...". The 
gui then updates the counter and enables/disables the action dice buttons accordingly. If a player has a certain action dice and 
wants to use it, they press the button and choose a victim and an entry if necessary. In the cases of the steal, freeze and cross out
actions, this is communicated to the server via "STEA/FRZE/COUT username entryName". In the case of the swap action via 
"SWAP username" and in the case of the shift action via "SHFT". The server handles the action and confirms this using the same 
commands. The gui then updates the information.

Case high score: <br>
Everytime a player pushes the high score button in the lobby window or the game window of the gui, the command "HGSC" is
send to the server. The server then returns the current list (which is saved in the file highscore.txt) back to the player 
via "HGSC points:username,points:username,...". The gui then opens the high score pop-up window and displays the list.
