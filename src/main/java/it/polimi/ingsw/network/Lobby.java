package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.responseToClients.PingResponse;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.SingleGame.SingleGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * this class represent the lobby.
 * in particular, it is responsible of store all the shared data for every client as
 * the game, the reference to the thread of every other client, the number of players
 * of the game, the nicknames of every player, if the game is started or finished.
 * it also implements runnable to ping players
 */
public class Lobby implements Runnable {

    /**
     * this attribute represent a list of every thread associated with the clients
     * that are playing the same game
     */
    private final List<ClientHandler> clients = new ArrayList<>();

    /**
     * this attribute represent the game of the lobby in common with every player
     */
    private Game game = new Game(Collections.singletonList(""));

    /**
     * this attribute represent a list (ordered once the game starts) of
     * the players nicknames
     */
    private List<String> nicknames = new ArrayList<>();

    /**
     * this attribute represent the number of players of the game
     */
    private final AtomicInteger numberOfPlayers;

    /**
     * this attribute indicates if a game finished.
     * in particular, when one of the methods of the game throws an EndGameException,
     * this attribute will be set to true, and even if the number of clients
     * of the lobby is empty.
     */
    private final AtomicBoolean gameIsFinished = new AtomicBoolean(false);

    /**
     * this attribute indicates if the game started.
     * in particular, when every player complete the login, the game start
     * and this attribute will be true till the end of the game
     */
    private final AtomicBoolean gameIsStarted = new AtomicBoolean(false);

    /**
     * this constructor create a lobby setting the number of players of the game
     * @param numberOfPlayers this is the number of players of the game to set
     */
    public Lobby(int numberOfPlayers){
        this.numberOfPlayers = new AtomicInteger(numberOfPlayers);
    }

    /**
     * this method adds a client to the list of the client associated with the lobby (and the game).
     * this method have to be called when a client want to play a game with the same
     * number of players of the lobby and the game isn't started yet
     * @param client this attribute represent the client to add
     */
    public synchronized void addClient(ClientHandler client){
        clients.add(client);
    }

    /**
     * this method remove a client from the list of clients of the lobby.
     * in particular, this method will be called when a client disconnect during the login phase
     * (before that the game start ), and the lobby will kick him out and continue the login phase,
     * otherwise when the game started and a client quit, the class clientHandler have to call this
     * method for every client of the list of clients
     * @param client this is the client to remove
     */
    public void removeClient(ClientHandler client){
        clients.remove(client);
    }

    /**
     * this method get the game from reference, so if a client have to do any action,
     * have to call this method first
     * @return the reference of the game associated with the lobby
     */
    public Game getGame() {
        return game;
    }

    /**
     * this method create a game.
     * in particular, depending on the number of players, will create a
     * single game or a multi game, setting the nicknames previously chosen
     * and stored as attribute into this class, then sort
     * the list of clients with the same casual order of the game,
     * it finally set to true that the game is started
     */
    public synchronized void createGame() {
        // singlePlayer
        if (nicknames.size() == 1){
            game = new SingleGame(nicknames);
            return;
        }

        // else (multiPlayer)
        game = new Game(nicknames);
        // sort nicknames based on the casual order of the game
        nicknames = game.getPlayers().stream().map(RealPlayer::getNickname).collect(Collectors.toList());
        // sort the clients based on the game nicknames order
        sortClients();
        // start the game
        gameIsStarted.set(true);
    }

    /**
     * this method get the nicknames of the players.
     * in particular, when every player do the login have to send the nickname, that
     * will be stored into a list into the lobby, so this method will return only the
     * nicknames of the players that have done the login
     * @return the nicknames of the players
     */
    public List<String> getNicknames() {
        return nicknames;
    }

    /**
     * this method add a nickname to the list of nicknames.
     * in particular, every client have to send his nickname to complete the login,
     * and, once accepted, the thread associated with the client that own a reference to the lobby
     * will call this method to add the nickname to the list.
     * if the nickname is the last one the server will start the initialisation phase
     * @param nickname this is the nickname to add
     */
    public void addNickname(String nickname){
        nicknames.add(nickname);
    }

    /**
     * this method return the number of players.
     * in particular, the lobby will store an integer containing the number of players
     * decided by the first player that joined
     * @return the number of players of the game
     */
    public int getNumberOfPlayers(){
        return numberOfPlayers.get();
    }

    /**
     * this method get the list of clients.
     * in particular, when a lobby has been created, it store a list of clients,
     * and this method return this
     * @return the list of clients that have joined the lobby
     */
    public List<ClientHandler> getClients() {
        return clients;
    }

    /**
     * this method sort the threads associated with the player
     * in the same order of the nicknames of the game
     */
    private synchronized void sortClients(){
        List<ClientHandler> newClients = new ArrayList<>();
        for (String nickname : nicknames){
            int i = 0;
            for (ClientHandler ignored : this.clients) {
                if (clients.get(i).getNickname().equals(nickname))
                    newClients.add(clients.get(i));
                i++;
            }
        }
        this.clients.clear();
        this.clients.addAll(newClients);// set the list of sorted clients
    }

    /**
     * this method set the state of the game to finished.
     * in particular, when one of the methods of the game throws an EndGameException,
     * this method will be called, and even if the number of clients
     * of the lobby is empty will be called.
     * after that, the waiting room will delete this lobby from the possible ones
     */
    public void setGameFinished(){
        gameIsFinished.set(true);
    }

    /**
     * this method indicates if the game is finished or not.
     * it will be called from the waiting room to delete this lobby
     * from the possible ones when return true
     * @return true if the game is finished, false otherwise
     */
    public boolean isGameFinished(){
        return gameIsFinished.get();
    }

    /**
     * this method indicates if the game is started or not.
     * in particular, when the waiting room receive a new request to join
     * a lobby with the same number of players of this, this method will be called and if
     * it is true, the client will be added
     * @return true if the gme is started (every player did the login), false otherwise
     */
    public boolean isGameStarted() {
        return gameIsStarted.get();
    }

    /**
     * this method ping the clients to understand if a client crashed.
     * in particular, this method works only if the connection fall for
     * reasons of connection break
     * @throws IOException if a network error occurs
     */
    public synchronized void ping() throws  IOException {
            for (ClientHandler client : clients){
                client.send(new PingResponse());
                /*
                if (!client.getSocket().getInetAddress().isReachable(200))
                    throw new IOException("a client crushed");

                 */
            }


    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        while (true){
            try {
                ping();
                if(clients.size() == 0)
                    setGameFinished();
                TimeUnit.SECONDS.sleep(2);
                if (isGameFinished())
                    return;
            } catch ( // possible exceptions handled
                    IOException
                    | InterruptedException
                    e
            ){
                try {
                    clients.get(0).sendBroadcastDisconnection();
                } catch (
                        IOException | IndexOutOfBoundsException ex) {
                    ex.printStackTrace();
                }
                return;
            }
        }
    }
}