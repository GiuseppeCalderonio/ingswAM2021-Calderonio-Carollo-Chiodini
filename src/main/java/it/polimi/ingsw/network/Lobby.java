package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.responseToClients.PingResponse;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.SingleGame.SingleGame;

import java.util.*;
import java.util.stream.Collectors;

/**
 * this class represent the lobby.
 * in particular, it is responsible of store all the shared data for every client as
 * the game, the reference to the thread of every other client, the number of players
 * of the game, the nicknames of every player, if the game is started or finished.
 * it also implements runnable to ping players
 */
public class Lobby {

    /**
     * this attribute represent a list of every thread associated with the clients
     * that are playing the same game
     */
    private List<ClientHandler> clients = new ArrayList<>();

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
    private final int numberOfPlayers;

    /**
     * this attribute indicates if a game finished.
     * in particular, when one of the methods of the game throws an EndGameException,
     * this attribute will be set to true, and even if the number of clients
     * of the lobby is empty.
     */
    private boolean gameIsFinished = false;

    /**
     * this attribute indicates if the game started.
     * in particular, when every player complete the login, the game start
     * and this attribute will be true till the end of the game
     */
    private boolean gameIsStarted = false;

    private Timer timer;

    /**
     * this constructor create a lobby setting the number of players of the game
     * @param numberOfPlayers this is the number of players of the game to set
     */
    public Lobby(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
        // create a thread for the ping
        startTimer();
    }

    /**
     * this method adds a client to the list of the client associated with the lobby (and the game).
     * this method have to be called when a client want to play a game with the same
     * number of players of the lobby and the game isn't started yet
     * @param client this attribute represent the client to add
     * @throws LobbyFinishedException when the attribute gameIsFinished is true
     */
    public void addClient(ClientHandler client) throws LobbyFinishedException{
        if (gameIsFinished){
            throw new LobbyFinishedException();
        }
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
        if (clients.size() == 0)
            timer.cancel();
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
    public void createGame() {
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
        gameIsStarted = true;
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
        return numberOfPlayers;
    }

    /**
     * this method get the list of clients.
     * in particular, when a lobby has been created, it store a list of clients,
     * and this method return this
     * @return the list of clients that have joined the lobby
     */
    public List<ClientHandler> getClients() {
        // this method is synchronized because when the waiting room try to
        // get the number of clients of the match, this list MUST not change,
        // in order to give a right information
        synchronized (this){
            return clients;
        }

    }

    /**
     * this method sort the threads associated with the player
     * in the same order of the nicknames of the game
     */
    private void sortClients(){
        List<ClientHandler> newClients = new ArrayList<>();
        for (String nickname : nicknames){
            //int i = 0;
            for (ClientHandler client : this.clients) {
                if (client.getNickname().equals(nickname))
                    newClients.add(client);
                //i++;
            }
        }
        clients = newClients;
    }

    /**
     * this method set the state of the game to finished.
     * in particular, when one of the methods of the game throws an EndGameException,
     * this method will be called, and even if the number of clients
     * of the lobby is empty will be called.
     * after that, the waiting room will delete this lobby from the possible ones
     */
    public void setGameFinished(){
        gameIsFinished = true;
    }

    /**
     * this method indicates if the game is finished or not.
     * it will be called from the waiting room to delete this lobby
     * from the possible ones when return true
     * @return true if the game is finished, false otherwise
     */
    public boolean isGameFinished(){
        return gameIsFinished;
    }

    /**
     * this method indicates if the game is started or not.
     * in particular, when the waiting room receive a new request to join
     * a lobby with the same number of players of this, this method will be called and if
     * it is true, the client will be added
     * @return true if the gme is started (every player did the login), false otherwise
     */
    public boolean isGameStarted() {
        return gameIsStarted;
    }

    /**
     * this method ping the clients to understand if a client crashed.
     * in particular, it send a broadcast ping message, that should be followed with a ping response
     * in order to stimulate the in.readLine and reset the timeout of the socket
     */
    public void ping() {
            clients.forEach(client -> client.send(new PingResponse()));
    }

    /**
     * this method start the timer that ping every client to handle disconnections
     */
    public void startTimer() {

        TimerTask ping = new TimerTask() {
            @Override
            public void run() {
                ping();
            }
        };

        timer = new Timer("Timer");
        long delay = 1000L;
        timer.schedule(ping, delay, 1000);
    }
}