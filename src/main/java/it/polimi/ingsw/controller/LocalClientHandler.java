package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SingleGame.SingleGame;
import it.polimi.ingsw.network.Client;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this class represent the local client handler.
 * in particular, this class extends the ClientHandler class because
 * it is used only in case of local match.
 * it redefine some methods to simulate the network exchange of message
 * as some methods call, as the send, the createGame, the
 * sendInBroadcast, the readMessage, ecc..
 */
public class LocalClientHandler extends ClientHandler{

    /**
     * this attribute represent the client to modify when necessary with the
     * method updateClient(client) of the class ResponseToClient
     * @see ResponseToClient
     */
    private final Client view;

    /**
     * this attribute represent the game of the local match.
     * in particular, this will always be a single game, because of
     * the nature of the class, that is used only in local to handle
     * single games
     */
    private Game game;

    /**
     * this constructor create the local client handler starting from the
     * reference of the client to interact with calling method that simulates
     * the network message exchange
     * @param view this is the client to set
     */
    public LocalClientHandler(Client view) {
        super(null, null, null, null, null);
        this.view = view;
        setCommandManager(new CommandManager(this));
    }

    /**
     * this method is used only by the subclass localClientHandler.
     * it is used to read the message starting from the command passed in input, and then will be
     * called the method processCommand on it
     * @param command this is the command to process
     */
    public void readMessage(Command command) {
        try {
            getCommandManager().processCommand(command);
        } catch (EndGameException e){
            send(new ResponseToClient("The game finish, the winner is" + e.getMessage()));
        } catch (QuitException e){
            send(new ResponseToClient(e.getMessage()));
        }
    }

    /**
     * this method send a message to the client associated.
     * in particular, in this class simply calls the method updateClient(view)
     * in order to change his state
     *
     * @param message this is the message to send
     * @see ResponseToClient
     */
    @Override
    public synchronized void send(ResponseToClient message) {
        message.updateClient(view);
    }

    /**
     * this method notify every player of the disconnection of the game
     * for any possible reason, and close the connection.
     * in this subclass just call the method send with an error message to the client
     */
    @Override
    public synchronized void sendBroadcastDisconnection() {
        send(new ResponseToClient("The game finish..."));
    }

    /**
     * this method send in broadcast the message in input, it simulate
     * the method notify for the pattern notify-observer.
     * in this subclass just use the method send redefined in this class
     * with the message in input
     *
     * @param message this is the message to send in broadcast
     */
    @Override
    public synchronized void sendInBroadcast(ResponseToClient message) {
        send(message);
    }

    /**
     * this method get a list of String representing the nicknames of every player that joined the lobby.
     * in this class, it create a singleton list containing the nickname of the local client
     *
     * @return the nicknames of every player that joined the lobby
     */
    @Override
    public synchronized List<String> getNicknames() {
        return new ArrayList<>(Collections.singletonList(getNickname()));
    }

    /**
     * this method add a nickname to the list of nicknames associated with the lobby.
     * in this subclass, the method do nothing
     *
     * @param nickname this is the nickname to add
     */
    @Override
    public synchronized void addNickname(String nickname) {

    }

    /**
     * this method get the game associated with the lobby joined from the client.
     * in this subclass, get the reference of the single game stored as attribute
     * of the subclass
     *
     * @return the game associated with the lobby joined from the client
     */
    @Override
    public synchronized Game getGame() {
        return game;
    }

    /**
     * this method create the game to play for the lobby joined by the client.
     * in particular, this method if the lobby is composed only by one player,
     * will create a single game, a normal one otherwise.
     * in this subclass create a single game with the unique nickname of the local client
     */
    @Override
    public synchronized void createGame() {
        game = new SingleGame(getNicknames());
    }

    /**
     * this method get the list of Client Handlers associated with every client that
     * joined the lobby.
     * it is mainly used to send broadcast messages.
     * in this subclass this method return a singleton list
     * with this object
     *
     * @return the list of client handlers associated with every client that
     * joined the lobby
     */
    @Override
    public List<ClientHandler> getClients() {
        return new ArrayList<>(Collections.singletonList(this));
    }

    /**
     * this method get the number of players of the lobby joined by the client.
     * in particular, when a client want to start a game, and selecting the number of
     * players desired, if there isn't space in any lobby, the waiting room creates one and
     * store there the number of player, that this method get.
     * in this subclass, it simply return 1
     *
     * @return the number of players of the lobby joined by the client
     */
    @Override
    public synchronized int getNumberOfPlayers() {
        return 1;
    }

    /**
     * this method indicates if if the turn of the client or not.
     * if the match is composed only by one player, it should return true always.
     * in this subclass, it always return true
     *
     * @return true if id the turn of the client, false otherwise
     */
    @Override
    protected boolean isYourTurn() {
        return true;
    }

    /**
     * this method set the attribute ,relative to indicates if continue the
     * match for this client or not, to true.
     * in this subclass, the method do nothing
     */
    @Override
    public void setPlayFalse() {

    }

    /**
     * this method get the socket associated with the client.
     * in this subclass, the method returns null
     *
     * @return the socket associated with the client
     */
    @Override
    public Socket getSocket() {
        return null;
    }
}
