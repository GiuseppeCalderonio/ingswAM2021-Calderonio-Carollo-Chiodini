package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.SingleGame.SingleGame;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * this class represent the lobby
 */
public class Lobby {

    private List<ClientHandler> clients = new ArrayList<>();
    private Game game = new Game(Collections.singletonList(""));
    private List<String> nicknames = new ArrayList<>();
    private final AtomicInteger numberOfPlayers;
    private final AtomicBoolean gameIsFinished = new AtomicBoolean(false);
    private final AtomicBoolean gameIsStarted = new AtomicBoolean(false);

    public Lobby(int numberOfPlayers){
        this.numberOfPlayers = new AtomicInteger(numberOfPlayers);
    }


    public synchronized void addClient(ClientHandler client){
        clients.add(client);
    }

    public void removeClient(ClientHandler client){
        clients.remove(client);
    }

    public Game getGame() {
        return game;
    }

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
        gameIsStarted.set(true);
    }

    public List<String> getNicknames() {
        return nicknames;
    }

    public void addNickname(String nickname){
        nicknames.add(nickname);
    }

    public int getNumberOfPlayers(){
        return numberOfPlayers.get();
    }

    public List<ClientHandler> getClients() {
        return clients;
    }

    public void setClients(List<ClientHandler> clients) {
        this.clients = clients;
    }

    /**
     * this method sort the threads associated with the player
     * in the same order of the nicknames of the game
     */
    private synchronized void sortClients(){
        List<ClientHandler> newClients = new ArrayList<>();
        for (String nickname : nicknames){
            int i = 0;
            for (ClientHandler client : this.clients) {
                if (client.getClients().get(i).getNickname().equals(nickname))
                    newClients.add(client.getClients().get(i));
                i++;
            }
        }
        this.clients = newClients; // set the list of sorted clients
    }

    public void setGameFinished(){
        gameIsFinished.set(true);
    }

    public boolean isGameFinished(){
        return gameIsFinished.get();
    }

    public boolean isGameStarted() {
        return gameIsStarted.get();
    }

    public synchronized void ping() throws IOException {
        for (ClientHandler client : clients){
            if (!client.getSocket().getInetAddress().isReachable(200))
                throw new IOException("client not reachable");
        }
    }
}