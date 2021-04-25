package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.view.ThinLeaderCard;
import it.polimi.ingsw.view.ThinPlayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.ClientHandler.getGame;
import static it.polimi.ingsw.controller.ClientHandler.getHandlers;

public class CommandManager {

    private static final List<CommandManager> commandManagers= new ArrayList<>();
    private final ClientHandler handler;
    private CommandInterpreter commandInterpreter;

    public CommandManager(ClientHandler handler) throws IOException {
        ResponseToClient response = new ResponseToClient();
        if (ClientHandler.getNumberOfPlayers() == null) {
            commandInterpreter = new SetSizeInterpreter();

            response.message = "Welcome to the server! Set the number of players";
            response.possibleCommands = new ArrayList<>(Collections.singletonList("set_players"));
        }else {
            commandInterpreter = new LoginInterpreter();

            response.message = "Welcome to the server! Start with the login";
            response.possibleCommands = new ArrayList<>(Collections.singletonList("login"));
        }
        handler.send(response);
        commandManagers.add(this);
        this.handler = handler;
    }

    public CommandInterpreter getCommandInterpreter() {
        return commandInterpreter;
    }

    public static List<CommandManager> getCommandManagers() {
        return commandManagers;
    }

    public ClientHandler getHandler() {
        return handler;
    }

    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    public void processCommand(Command command, ClientHandler handler) throws IOException {

        ResponseToClient response = commandInterpreter.executeCommand(command, handler);
        handler.send(response);
        List<String> nicknames = ClientHandler.getNicknames();

        // if the number of players got set
        if (response.message.equals("ok, start with the login"))
            // change the state of the game from "set players" to "login"
            commandInterpreter = new LoginInterpreter();

        // if every player did the login
        if (response.message.contains("login completed successfully") && nicknames.size() ==
                ClientHandler.getNumberOfPlayers().get()) {
            // change the state of the game from "login" to "initialise game"
            commandManagers.forEach(commandManager -> commandManager.setCommandInterpreter(new InitialisingInterpreter()));
            // create a single game
            if (ClientHandler.getNumberOfPlayers().get() == 1){
                ClientHandler.setSingleGame(nicknames);
            }
            // create a normal game
            else {
                ClientHandler.setGame(nicknames);
                nicknames = ClientHandler.getGame().getPlayers().stream().map(RealPlayer::getNickname).collect(Collectors.toList());
                ClientHandler.setNicknames(nicknames);
            }
            // send in broadcast the leader cards and the position to every player
            sendBroadcastInitialising();
        }
        // if every player did correctly the initialisation
        if (response.message.contains("ok, now wait that everyone decides his resources and leader cards, and the game will start") &&
                commandManagers.stream().map(commandManager -> commandManager.getCommandInterpreter().getPossibleCommands()).
                allMatch(List::isEmpty)){
            commandManagers.forEach(commandManager -> commandManager.setCommandInterpreter(new TurnsInterpreter()));
            // send for every player, the market of cards, the market of marbles,
            // the position on the faith track of every player, the warehouse of every player,
            // the strongbox of every player, the leader cards of every player,
            // the production power of every players,
            sendBroadcastStartGame();
        }
    }

    /**
     * this method sort the threads associated with the player
     * in the same order of the nicknames of the game
     */
    private synchronized void sortHandlers(){
        List<ClientHandler> handlers = new ArrayList<>();
        for (String nickname : ClientHandler.getNicknames()){
            int i = 0;
            for (ClientHandler ignored : getHandlers()) {
                if (getHandlers().get(i).getNickname().equals(nickname))
                    handlers.add(getHandlers().get(i));
                i++;
            }
        }
        ClientHandler.setHandlers(handlers); // set the list of sorted handlers
    }

    private synchronized void sendBroadcastInitialising() throws IOException {
        // sort the client handlers
        sortHandlers();
        int i = 1;
        for (ClientHandler handler : getHandlers()) {
            List<LeaderCard> leaderCards = ClientHandler.getGame().findPlayer(handler.getNickname()).getPersonalLeaderCards();
            ResponseToClient message = new ResponseToClient();
            message.message = "the game initialization start! decide 2 different leader cards to discard";
            message.possibleCommands = new ArrayList<>(Collections.singletonList("initialise_leaderCards"));
            message.position = i;
            message.leaderCards = leaderCards.stream().map(LeaderCard::getThin).collect(Collectors.toList());
            message.code = 1;
            message.serialize = true;
            handler.send(message);
            i++;
        }
    }

    private synchronized void sendBroadcastStartGame() throws IOException {
        List<ThinPlayer> players = getGame().getPlayers().stream().map(ThinPlayer::new).collect(Collectors.toList());
        String message;
        List<String> possibleCommands = new ArrayList<>();
        for (ClientHandler handler : getHandlers()){
            ThinPlayer actualPlayer = new ThinPlayer(getGame().findPlayer(handler.getNickname()));
            List<ThinPlayer> opponents = players.stream().filter(thinPlayer -> !actualPlayer.equals(thinPlayer)).collect(Collectors.toList());
            hideLeaderCards(opponents);
            if (handler.getNickname().equals( getGame().getActualPlayer().getNickname())) {
                message = "the game start! Is your turn";
                possibleCommands.add("shift_resources");
                possibleCommands.add("choose_marbles");
                possibleCommands.add("production");
                possibleCommands.add("buy_card");
                possibleCommands.add("leader_action");
            }
            else {
                message = "the game start! Is not your turn, wait...";
            }

            String turnNickname = getGame().getActualPlayer().getNickname();
            ResponseToClient broadcast = new ResponseToClient();
            broadcast.message = message;
            broadcast.possibleCommands = possibleCommands;
            broadcast.cardsMarket = getGame().getSetOfCard().show();
            broadcast.marbleMarket = getGame().getMarketBoard().getMarketTray();
            broadcast.lonelyMarble = getGame().getMarketBoard().getLonelyMarble();
            try {
                broadcast.soloToken = getGame().getSoloTokens().get(getGame().getSoloTokens().size() - 1);
            } catch (IndexOutOfBoundsException | NullPointerException e){ // when there is a singleGame
                broadcast.soloToken = null;
            }
            broadcast.actualPlayer = actualPlayer;
            broadcast.opponents = opponents;
            broadcast.code = 2;
            broadcast.serialize = true;
            handler.send(broadcast);
        }
    }

    private void hideLeaderCards(List<ThinPlayer> players){
        for (ThinPlayer player : players){
            for (ThinLeaderCard card : player.getThinLeaderCards()){
                if (!card.isActive())
                    card.hide();
            }
        }
    }
}
