package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.ThinLeaderCard;
import it.polimi.ingsw.view.ThinPlayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable{
    private final Socket socket;
    private String nickname = "";
    private final Gson gson;
    private CommandManager commandManager;
    private final Lobby lobby;
    private final PrintWriter out;
    private final Scanner in;
    private final AtomicBoolean play = new AtomicBoolean(true);

    public ClientHandler(Socket socket, Lobby lobby, PrintWriter out, Scanner in, Gson gson) {

        this.socket = socket;
        this.lobby = lobby;
        // create the gson parser
        this.gson = gson;
        this.in = in;
        this.out = out;
    }

    public void start() {
        try {
            System.out.println("New connection with " + socket);
            // create a thread to ping players
            Thread t = new Thread(this);
            t.start();

            // create a new command manager
            commandManager = new CommandManager(this);


            while (play.get()) {
                try {
                    play.set(readMessage());
                } catch (Exception e){
                    System.err.println("A generic error occurs :" + e.getMessage());
                    play.set(false);
                }
            }

            // closing stream and sockets, and eventually restart a new game kicking off every player
            in.close();
            out.close();
            socket.close();
            lobby.removeClient(this);
            lobby.getNicknames().remove(nickname);
            System.out.println("Connection closed with " + socket);

            if (!commandManager.getCommandInterpreter().getGamePhase().equals(GamePhase.LOGIN)) // if the game isn't in the login phase
                sendBroadcastDisconnection();

        } catch(IOException e) {
            System.err.println("Error with IOException");
            System.err.println(e.getMessage());
        } catch (Exception e){
            System.err.println("Fatal error");
            System.out.println(e.getMessage());
        }
    }

    public synchronized Game getGame() {
        return lobby.getGame();
    }

    public synchronized void createGame (){
        lobby.createGame();
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public synchronized int getNumberOfPlayers () {
        return lobby.getNumberOfPlayers();
    }


    public synchronized void addNickname (String nickname){
        lobby.addNickname(nickname);
    }

    public synchronized List<String> getNicknames () {

        return lobby.getNicknames();
    }

    public String getNickname () {
        return nickname;
    }

    public void setNickname (String nickname){ this.nickname = nickname; }

    public List<ClientHandler> getClients() {
        return lobby.getClients();
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isYourTurn(){
        return lobby.getGame().isYourTurn(nickname);
    }



    /**
     * this method notify every player of the disconnection of the game
     * for any possible reason, and close the connection
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    private synchronized void sendBroadcastDisconnection () throws IOException {
        List<ClientHandler> clients = getClients().stream().filter(Objects::nonNull).collect(Collectors.toList());
        for (ClientHandler client : clients) {
            ResponseToClient response = new ResponseToClient();
            response.message = "Someone left the game, everyone will be kicked out";
            client.send(response);
            client.socket.getOutputStream().close();
            client.socket.getInputStream().close();
            client.socket.close();
        }
        getClients().clear();
        lobby.setGameFinished();

    }

    public synchronized void sendInBroadcast (ResponseToClient message) {
        for (ClientHandler handler : getClients()) {
            handler.send(message);
        }
    }

    /**
     * this method send a message to the client associated
     * @param message this is the message to send
     */
    public synchronized void send(ResponseToClient message){
        out.println(gson.toJson(message, ResponseToClient.class));
        out.flush();
    }

    public void updatePlayers(ResponseToClient response , String nickname){
        // create the thin single player relative to the player that is playing the turn
        response.actualPlayer = new ThinPlayer(getGame().findPlayer(nickname));
        // if the game is a single game
        if (getNumberOfPlayers() == 1){
            ThinPlayer lorenzo = new ThinPlayer(getGame().getLorenzoIlMagnifico());
            response.opponents = new ArrayList<>(Collections.singletonList(lorenzo));
        } // if the game is not a single game
        else{
            response.opponents = getGame().getPlayers().stream().
                    filter(player -> !getGame().findPlayer(nickname).equals(player)).
                    map(ThinPlayer::new).
                    collect(Collectors.toList());
            hideLeaderCards(response.opponents);
        }
        response.serialize = true;
    }

    private ResponseToClient buildResponse(String message){
        ResponseToClient response = new ResponseToClient();
        response.message = message;
        response.possibleCommands = commandManager.getCommandInterpreter().getPossibleCommands();
        return response;
    }

    private ResponseToClient buildEndGameResponse(){
        ResponseToClient response = buildResponse("The game finish, the winner is" + lobby.getGame().getWinner());
        response.ignorePossibleCommands = true;
        return response;
    }

    /**
     * this method hide the leader cards of a thin player, in fact the cards
     * of a player different from the owner, when another player did not activate
     * a leader card, should not be visible
     * @param players these are the player with the leader cards to hide
     */
    private void hideLeaderCards(List<ThinPlayer> players){
        for (ThinPlayer player : players){
            for (ThinLeaderCard card : player.getThinLeaderCards()){
                if (!card.isActive())
                    card.hide();
            }
        }
    }

    public void updateMarbleMarket(ResponseToClient response){
        response.marbleMarket = getGame().getMarketBoard().getMarketTray();
        response.lonelyMarble = getGame().getMarketBoard().getLonelyMarble();
        response.serialize = true;
    }

    public void updateCardsMarket(ResponseToClient response){
        response.cardsMarket = getGame().getSetOfCard().show();
        response.serialize = true;
    }

    public void updateSoloTokens(ResponseToClient response){
        try {
            response.soloToken = getGame().getSoloTokens().get(getGame().getSoloTokens().size() - 1);
        } catch (IndexOutOfBoundsException | NullPointerException e){ // when there isn't a singleGame
            response.soloToken = null;
        }
        response.serialize = true;
    }

    private boolean readMessage() {
        String line;
        // read from the input (eventually throws NoSuchElementException)
        try {
            line = in.nextLine();
        } catch (NoSuchElementException e){
            return false;
        }


        Command command;
        try {
            // translate the string to a command
            command = gson.fromJson(line, Command.class); // convert the message in a processable command
            // when the command is not in a json format
        }catch (JsonSyntaxException e) { // the string received is not in gson format
            send(buildResponse("you have to insert a json string format"));
            return true;
        }
        try {
            // quit the game if a player wat to exit
            if (command.cmd.equals("quit"))
                return false;
            // process the command and send the message or the messages to the players
            commandManager.processCommand(command);

            return true;
            // if one of the parameters of the command does not respect the preconditions
        }catch (NullPointerException | IndexOutOfBoundsException e){ // the command sent is not correct

            send(buildResponse("Something gone wrong, you've probably chosen wrong inputs "));
            return true;
        // the condition of ending a game are met
        }catch (EndGameException e){

            sendInBroadcast(buildEndGameResponse());

            lobby.setGameFinished();

            return true;
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
                lobby.ping();
                TimeUnit.SECONDS.sleep(2);
                if (lobby.isGameFinished())
                    return;
            } catch (IOException
                    | InterruptedException e
            ){
                play.set(false);
                return;
            }
        }
    }
}
