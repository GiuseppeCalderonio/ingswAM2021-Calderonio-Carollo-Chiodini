package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.SingleGame.SingleGame;
import it.polimi.ingsw.network.Client;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocalClientHandler extends ClientHandler{

    private Client view;
    private Game game;

    /**
     *
     * @param view
     */
    public LocalClientHandler(Client view) {
        super(null, null, null, null, null);
        this.view = view;
        setCommandManager(new CommandManager(this));
    }

    /**
     * @return
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
     * this method send a message to the client associated
     *
     * @param message this is the message to send
     */
    @Override
    public synchronized void send(ResponseToClient message) {
        message.updateClient(view);
    }

    /**
     * this method notify every player of the disconnection of the game
     * for any possible reason, and close the connection
     *
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    @Override
    public synchronized void sendBroadcastDisconnection() throws IOException {
        send(new ResponseToClient("Someone left the game, everyone will be kicked out"));
    }

    /**
     * this method send in broadcast the message in input, it simulate
     * the method notify for the pattern notify-observer
     *
     * @param message this is the message to send in broadcast
     */
    @Override
    public synchronized void sendInBroadcast(ResponseToClient message) {
        send(message);
    }

    /**
     * @return
     */
    @Override
    public synchronized List<String> getNicknames() {
        return new ArrayList<>(Collections.singletonList(getNickname()));
    }

    /**
     * @param nickname
     */
    @Override
    public synchronized void addNickname(String nickname) {

    }

    /**
     * @return
     */
    @Override
    public synchronized Game getGame() {
        return game;
    }

    /**
     *
     */
    @Override
    public synchronized void createGame() {
        game = new SingleGame(getNicknames());
    }

    /**
     * @return
     */
    @Override
    protected List<ClientHandler> getClients() {
        return new ArrayList<>(Collections.singletonList(this));
    }

    /**
     * @return
     */
    @Override
    public synchronized int getNumberOfPlayers() {
        return 1;
    }

    /**
     * @return
     */
    @Override
    protected boolean isYourTurn() {
        return true;
    }

    /**
     *
     */
    @Override
    public void setPlayFalse() {
    }

    /**
     * @return
     */
    @Override
    public Socket getSocket() {
        return null;
    }


}
