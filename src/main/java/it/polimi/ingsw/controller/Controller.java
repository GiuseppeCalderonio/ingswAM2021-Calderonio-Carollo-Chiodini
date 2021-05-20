package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.InitialisingResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.StartGameResponse;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.network.ClientHandler;

/**
 * this class represent the command manager, it process the commands,
 * and manage the possible commands that a player can do in a phase of the game
 */
public class Controller {

    /**
     * this attribute represent the client handler associated with
     * this object, and that actually create this object
     */
    private final ClientHandler client;

    /**
     * this attribute represent the command interpreter associated with
     * tis object (and with the client).
     * in particular, the command interpreter is used to read and
     * execute command, checking if a command is right or not, store
     * the buffers to manage multiple operations, store
     * the list of possible commands, send the broadcast messages if necessary
     */
    private CommandInterpreter commandInterpreter;

    /**
     * this constructor create the object starting from the client handler
     * associated with it.
     * in particular, it create the commandInterpreter of login dynamic type,
     * it set the reference of the client passed as attribute and it send the login
     * message
     * @param client this is the client handler associated with the object to set
     * @see ClientHandler
     */
    public Controller(ClientHandler client) {

        commandInterpreter = new LoginInterpreter();
        this.client = client;

    }


    /**
     * this method get the command interpreter associated with this object
     * @return the command interpreter associated with this object
     * @see CommandInterpreter
     */
    public CommandInterpreter getCommandInterpreter() {
        return commandInterpreter;
    }

    /**
     * this method set the command interpreter associated with this object
     * @param commandInterpreter this is the command interpreter to set
     * @see CommandInterpreter
     */
    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    /**
     * this method process a command sent from the client.
     * in particular, it execute the command using the command interpreter method executeCommand(..),
     * send the response to the client returned from the method executeCommand, and verify
     * the state of every player, changing it if necessary.
     * in this case,  "the state of every player" means the phase of the game, that can be
     * (LOGIN, INITIALISING, TURNS @see GamePhase enum), and: (1) when every player is in the login phase
     * and everyone complete it, the method evolve the FSA of the match sending a
     * broadcast message to every player with all the 4 leader cards casually set from the game
     * and the position of every player based on the inkwell, (2) when every player is in the
     * initialising phase and everyone complete it, the method evolve the FSA of the match
     * sending a broadcast message with every game component and the possible command based
     * on the turn of the player, (3) when a player finish his turn, the method evolve the FSA of
     * the match sending to the player the have to play his turn the notification with the message
     * and the possible command ; if the game is a single one, send the new cards market
     * and the new token in order to notify the change of the action token
     * @param command this is the command to execute
     * @throws EndGameException if the conditions to finish a game are met
     * @throws QuitException if the player sent a quit command
     */
    public void processCommand(Command command) throws EndGameException, QuitException {

        // if the command is a pong
        if (command.getCmd().equals(CommandName.PONG))
            return; // do nothing

        // execute the command with the command interpreter
        ResponseToClient response = commandInterpreter.executeCommand(command, client);
        // send the response created based on the command executed from the command interpreter
        client.send(response);
        // update the state of the controller after this change
        updateState();

    }

    private void updateState(){
        // if every player did the login, so if everyone is in a login phase, there are enough players in the lobby, and everyone decided his nickname
        if (loginPhaseFinished())
            startInitialisingPhase(); //change the state of the game from LOGIN to INITIALISE

        // if every player did correctly the initialisation
        if (initialisingPhaseFinished())
            startTurnsPhase(); //change the state of the game from INITIALISE to TURNS

        if (turnFinished()) {
            changeTurn();
        }
    }

    /**
     * this private method indicates if the login phase is finished.
     * in particular, when every player is in the login phase, and
     * the size of the nicknames is equals to the number of players of
     * the match, and the method isPhaseFinished returns true, the method
     * return true, false otherwise
     * @return true if the login phase is finished for every player, false otherwise
     */
    private boolean loginPhaseFinished(){
        return commandInterpreter.getGamePhase().equals(GamePhase.LOGIN) &&
                client.getNumberOfPlayers() == client.getNicknames().size() &&
                isPhaseFinished();
    }

    /**
     * this private method indicates if the initialising phase is finished.
     * in particular, when every player is in the initialising phase,
     * and the method isPhaseFinished returns true, the method
     * return true, false otherwise
     * @return true if the initialising phase is finished for every player, false otherwise
     */
    private boolean initialisingPhaseFinished(){
        return commandInterpreter.getGamePhase().equals(GamePhase.INITIALISING) &&
                isPhaseFinished();
    }

    /**
     * this private method indicates if a turn is finished.
     * in particular, when the phase of the game is a turns phase,
     * and the method isPhaseFinished returns true, the method
     * return true, false otherwise
     * @return true if the turn is finished for the current player, false otherwise
     */
    private boolean turnFinished(){
        return  commandInterpreter.getGamePhase().equals(GamePhase.TURNS) &&
                isPhaseFinished();

    }

    /**
     * this method indicates if a game phase is finished.
     * in particular, a game phase finish when every command interpreter of every
     * client of the lobby return false when calling the method IsPhaseFinished()
     * @return true if the phase of the game is finished, false otherwise
     * @see CommandInterpreter
     */
    private boolean isPhaseFinished(){
        return client.getClients().stream().
                allMatch(client -> client.
                        getInterpreter().
                        IsPhaseFinished());
    }

    /**
     * this method start the initialising phase for every client.
     * in particular, set the command interpreter of every client
     * to a new initialising interpreter, create the game, and
     * send in broadcast the message that shows the 4 initial casual
     * leaderCards for every client
     */
    private void startInitialisingPhase(){
        // change the state of the game from "login" to "initialise game"
        client.getClients().forEach(client -> client.
                setCommandInterpreter(new InitialisingInterpreter()));
        // create the game
        client.createGame();
        // send in broadcast the leader cards and the position to every player
        sendBroadcastInitialising();
    }

    /**
     * this method start the turns phase.
     * in particular, set the command interpreter of every client
     * to a new turns interpreter, and send the broadcast message
     * for every client with every component of the game,
     * such as the cards market, the marble market, the faith track,
     * and the state of every player, eventually the solo token
     */
    private void startTurnsPhase() {
        client.getClients().forEach(client -> client
                .setCommandInterpreter(new TurnsInterpreter(client)));

        // send for every player, the market of cards, the market of marbles,
        // the position on the faith track of every player, the warehouse of every player,
        // the strongbox of every player, the leader cards of every player,
        // the production power of every players,
        sendBroadcastStartGame();
    }

    /**
     * this method change the turn of the game.
     * in particular, set the command interpreter of every client
     * to a new turns interpreter, and send a personalized message to
     * the client that owns the turn, with his possible commands, and
     * notifying him that now he have the inkwell
     */
    private void changeTurn() {
        // for all clients of the game
        for (ClientHandler client1 : client.getClients()) {
            // set a new turns interpreter
            client1.setCommandInterpreter(new TurnsInterpreter(client1));

            // if the client have to play his turn
            if (client1.isYourTurn()) {
                // send the message to the client
                client1.send(new ResponseToClient(Status.YOUR_TURN));
            }
        }
    }


    /**
     * this method send the broadcast initialising message.
     * in particular, it send an initialising response to client object,
     * with the position of every client based on the casual order of the game,
     * and the 4 initial and casual leaderCards
     */
    private void sendBroadcastInitialising()  {
        int i = 1;
        for (ClientHandler client : client.getClients()) {
            client.send(new InitialisingResponse(client, i));
            i++;
        }
    }

    /**
     * this method send for every player the start game broadcast message.
     * in particular, it is a personalized message containing
     * the state of the game just created, and the possible commands
     * for a player, that will be an empty list if the player doesn't own
     * the inkwell, will be the list of all the possible commands otherwise
     */
    private void sendBroadcastStartGame() {

        client.getClients().
                forEach(client -> client.
                        send(new StartGameResponse(client)));
    }


}
