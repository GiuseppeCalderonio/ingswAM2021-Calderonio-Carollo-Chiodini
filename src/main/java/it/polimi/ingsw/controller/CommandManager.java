package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.InitialisingResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.StartGameResponse;
import it.polimi.ingsw.model.EndGameException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this class represent the command manager, it process the commands,
 * and manage the possible commands that a player can do in a phase of the game
 */
public class CommandManager {

    private final ClientHandler client;

    private CommandInterpreter commandInterpreter;

    public CommandManager(ClientHandler client) {

        commandInterpreter = new LoginInterpreter();
        this.client = client;

        client.send(buildLoginResponse());
    }

    public CommandInterpreter getCommandInterpreter() {
        return commandInterpreter;
    }

    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    public synchronized void processCommand(Command command) throws EndGameException {

        ResponseToClient response = commandInterpreter.executeCommand(command, client);
        client.send(response);

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

    private boolean loginPhaseFinished(){
        return commandInterpreter.getGamePhase().equals(GamePhase.LOGIN) &&
                client.getNumberOfPlayers() == client.getNicknames().size() &&
                isPhaseFinished();
    }

    private boolean initialisingPhaseFinished(){
        return commandInterpreter.getGamePhase().equals(GamePhase.INITIALISING) &&
                isPhaseFinished();
    }

    private boolean turnFinished(){
        return  commandInterpreter.getGamePhase().equals(GamePhase.TURNS) &&
                isPhaseFinished();

    }

    private boolean isPhaseFinished(){
        return client.getClients().stream().
                allMatch(client -> client.
                        getInterpreter().
                        IsPhaseFinished());
    }

    private void startInitialisingPhase(){
        // change the state of the game from "login" to "initialise game"
        client.getClients().forEach(client -> client.
                setCommandInterpreter(new InitialisingInterpreter()));
        // create the game
        client.createGame();
        // send in broadcast the leader cards and the position to every player
        sendBroadcastInitialising();
    }

    private void startTurnsPhase() {
        client.getClients().forEach(client -> client
                .setCommandInterpreter(new TurnsInterpreter(client)));

        // send for every player, the market of cards, the market of marbles,
        // the position on the faith track of every player, the warehouse of every player,
        // the strongbox of every player, the leader cards of every player,
        // the production power of every players,
        sendBroadcastStartGame();
    }

    private void changeTurn() {
        // for all clients of the game
        for (ClientHandler client1 : client.getClients()) {
            // set a new turns interpreter
            client1.setCommandInterpreter(new TurnsInterpreter(client1));

            // if the client have to play his turn
            if (client1.isYourTurn()) {
                String message = "now is your turn";
                // get the possible commands to send
                List<String> possibleCommands = client1.
                        getInterpreter().
                        getPossibleCommands();
                // send the message to the client
                client1.send(new ResponseToClient(message, possibleCommands));
            }
        }
    }


    private ResponseToClient buildLoginResponse(){
        return new ResponseToClient("Welcome to the server! Start with the login",
                new ArrayList<>(Collections.singletonList("login")));
    }


    // this is a personalized message, shouldn't be removed
    private synchronized void sendBroadcastInitialising()  {
        int i = 1;
        for (ClientHandler client : client.getClients()) {
            client.send(new InitialisingResponse(client, i));
            i++;
        }
    }

    // this shouldn't change
    private synchronized void sendBroadcastStartGame() {

        client.getClients().
                forEach(client -> client.
                        send(new StartGameResponse(
                                client,
                                getMessage(client),
                                getPossibleCommands(client)
                        )));

        //client.sendInBroadcast(new StartGameResponse(client, getMessage(), getPossibleCommands(client)));

    }

    private String getMessage(ClientHandler client){
        if (client.isYourTurn() )
            return "the game start! Is your turn";
        return "the game start! Is not your turn, wait...";
    }

    private List<String> getPossibleCommands(ClientHandler client){
        List<String> possibleCommands = new ArrayList<>();
        if (client.isYourTurn() ) {
            possibleCommands.add("shift_resources");
            possibleCommands.add("choose_marbles");
            possibleCommands.add("production");
            possibleCommands.add("buy_card");
            possibleCommands.add("leader_action");
            return possibleCommands;
        }

        return possibleCommands;
    }
}
