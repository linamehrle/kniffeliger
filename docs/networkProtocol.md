This document details the protocol for the client/server interaction.

All commands have to consist of four characters in uppercase followed by a blank space and optionally one or more parameters.

Commands from the client to the server:

| command | parameter                              | functionality                                                              |
|---------|----------------------------------------|----------------------------------------------------------------------------|
| CHAT    | message                                | sends the given message to the server to redistribute to all other clients |
| WHSP    | name of one other client and a message | sends the message to the server to redistribute to the given other client  |
| CHNA    | new username                           | sends the new username to the server, server changes the username          |
| PONG    | time send by the server                | returns a ping to the server with the prev. send time                      |



Commands from the server to the client:

| command | parameter   | functionality                                                                             |
|---------|-------------|-------------------------------------------------------------------------------------------|
| PRNT    | message     | sends the message to one or more clients to print out (in the console, later in the chat) |
| PING    | system time | sends a ping to a client containing the current time                                      |