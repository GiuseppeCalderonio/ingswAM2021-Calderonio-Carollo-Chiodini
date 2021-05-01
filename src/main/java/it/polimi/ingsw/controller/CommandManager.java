package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class represent the command manager, it process the commands,
 * and manage the possible commands that a player can do in a phase of the game
 */
public class CommandManager {

    private final List<CommandManager> commandManagers= new ArrayList<>();
    private final ClientHandler client;
    private CommandInterpreter commandInterpreter;

    public CommandManager(ClientHandler client) {
        ResponseToClient response = new ResponseToClient();

        commandInterpreter = new LoginInterpreter();

        response.message = "Welcome to the server! Start with the login";
        response.possibleCommands = new ArrayList<>(Collections.singletonList("login"));
        client.send(response);
        commandManagers.add(this);
        this.client = client;
    }

    public CommandInterpreter getCommandInterpreter() {
        return commandInterpreter;
    }

    public List<CommandManager> getCommandManagers() {
        return commandManagers;
    }

    public ClientHandler getClient() {
        return client;
    }

    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    public void processCommand(Command command) throws EndGameException {

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
                client.getClients().stream().
                        allMatch(client ->  client.getCommandManager().getCommandInterpreter().IsPhaseFinished());
    }

    private boolean initialisingPhaseFinished(){
        return commandInterpreter.getGamePhase().equals(GamePhase.INITIALISING) &&
                client.getClients().stream().
                        allMatch(client -> client.getCommandManager().getCommandInterpreter().IsPhaseFinished());
    }

    private boolean turnFinished(){
        return  commandInterpreter.getGamePhase().equals(GamePhase.TURNS) &&
                commandInterpreter.IsPhaseFinished();

    }

    private void startInitialisingPhase(){
        // change the state of the game from "login" to "initialise game"
        client.getClients().forEach(client1 -> client1.getCommandManager().setCommandInterpreter(new InitialisingInterpreter()));
        // create the game
        client.createGame();
        // send in broadcast the leader cards and the position to every player
        sendBroadcastInitialising();
    }

    private void startTurnsPhase() {
        for (ClientHandler client1 : client.getClients()){
            client1.getCommandManager().setCommandInterpreter(new TurnsInterpreter(client1));
        }

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
            client1.getCommandManager().setCommandInterpreter(new TurnsInterpreter(client1));

            // if the client have to play his turn
            if (client1.isYourTurn()) {
                // create a new response for the client
                ResponseToClient notifyTurn = new ResponseToClient();
                notifyTurn.message = "now is your turn";
                // get the possible commands to send
                notifyTurn.possibleCommands = client1.
                        getCommandManager().
                        getCommandInterpreter().
                        getPossibleCommands();
                // send the message to the client
                client1.send(notifyTurn);
            }
        }
    }



    private synchronized void sendBroadcastInitialising()  {
        int i = 1;
        for (ClientHandler client : client.getClients()) {
            // get the leader cards of the client
            List<LeaderCard> leaderCards = client.getGame().findPlayer(client.getNickname()).getPersonalLeaderCards();
            // create a new response to send to the client
            ResponseToClient message = new ResponseToClient();
            message.message = "the game initialization start! decide 2 different leader cards to discard";
            // set the only possible command
            message.possibleCommands = new ArrayList<>(Collections.singletonList("initialise_leaderCards"));
            // set the position (that works because the clients are sorted with the game casual order)
            message.position = i;
            // send the thin leader cards
            message.leaderCards = leaderCards.stream().map(LeaderCard::getThin).collect(Collectors.toList());
            // set the code to 1
            message.code = 1;
            // set the message for sending an object
            message.serialize = true;
            // send the message
            client.send(message);
            i++;
        }
    }

    private synchronized void sendBroadcastStartGame() {
        String message;
        List<String> possibleCommands = new ArrayList<>();
        ResponseToClient broadcast = new ResponseToClient();
        for (ClientHandler client : client.getClients()){

            if (client.getNickname().equals( client.getGame().getActualPlayer().getNickname())) {
                message = "the game start! Is your turn";
                possibleCommands.add("shift_resources");
                possibleCommands.add("choose_marbles");
                possibleCommands.add("production");
                possibleCommands.add("buy_card");
                possibleCommands.add("leader_action");
            }
            else {
                message = "the game start! Is not your turn, wait...";
                possibleCommands.clear();
            }

            broadcast.message = message;
            broadcast.possibleCommands = possibleCommands;
            client.updateMarbleMarket(broadcast);
            client.updateCardsMarket(broadcast);
            client.updateSoloTokens(broadcast);
            client.updatePlayers(broadcast, client.getNickname());
            broadcast.code = 2;
            broadcast.serialize = true;
            client.send(broadcast);
        }
    }
}
