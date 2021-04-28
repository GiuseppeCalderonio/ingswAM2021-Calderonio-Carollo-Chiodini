package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.SingleGame.CardToken;
import it.polimi.ingsw.model.SingleGame.SingleGame;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.model.SingleGame.TrackToken;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class ClientHandler implements Runnable {
    private static List<ClientHandler> handlers = new ArrayList<>();
    private final Socket socket;
    private static List<Socket> sockets = new ArrayList<>();
    private static Game game;
    private static AtomicInteger numberOfPlayers = null;
    private String nickname = "";
    private static List<String> nicknames = new ArrayList<>();
    private Gson gson;
    private CommandManager commandManager;

    public ClientHandler(Socket socket) {

        this.socket = socket;
        /*

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.
                        of(SoloToken.class, "type").
                        registerSubtype(TrackToken.class, "trackToken").
                        registerSubtype(CardToken.class, "cardToken"));
        builder.registerTypeAdapter(Resource.class, new ResourceInterfaceAdapter());
        builder.registerTypeAdapter(Marble.class, new MarbleInterfaceAdapter());
        gson = builder.create();

         */
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            if (kickIfGameExist(out, in) || kickIfPlayersToDefine(out, in)  || kickIfLobbyFullOfClient(out, in) ){
                if (sockets.isEmpty()) // if any socket is connected to the server
                    resetValues();
                return;
            }
            sockets.add(socket);
            handlers.add(this);
            System.out.println("New connection with " + socket);
            gson = createGson();
            commandManager = new CommandManager(this);
            if (numberOfPlayers == null)
                numberOfPlayers = new AtomicInteger(-1);


            while (true) {
                String line;
                try {
                    try {
                        line = in.nextLine(); // read the message sent from the client
                        Command command = gson.fromJson(line, Command.class); // convert the message in a processable command

                        if (command.cmd.equals("quit")) // quit the game if a player wat to exit
                            break;

                        commandManager.processCommand(command, this); // process the command and send the message or the messages to the players
                    } catch (JsonSyntaxException e) {
                        System.err.println("Problem...");
                        break;
                    } catch (Exception e){
                        System.err.println("Generic error");
                        System.err.println(e.getMessage());
                        break;
                    }
                } catch (NoSuchElementException e){ // a player is not connected anymore
                    System.out.println("Error with: " + socket);
                    break;
                }catch (EndGameException e){
                    ResponseToClient response = new ResponseToClient();
                    // multiPlayer
                    if (numberOfPlayers.get() != 1){

                        int maxVictoryPoints = getGame().getPlayers().stream().
                                mapToInt(RealPlayer::getVictoryPoints).
                                max().orElse(0);
                        response.message = "The game finish, the winner is" + getGame().getPlayers().
                                stream().filter(player -> player.getVictoryPoints() == maxVictoryPoints).
                                collect(Collectors.toList());
                        response.ignorePossibleCommands = true;

                    }else { // singlePlayer
                        if (game.getActualPlayer().getPersonalDashboard().getPersonalProductionPower().getNumOfCards() >= 7 ||
                                (game.getActualPlayer().getPersonalTrack().getPopeFavorTiles()[2].getActive()) ) {
                            response.message = "You won! victory points gained :"+ game.getActualPlayer().getVictoryPoints();
                        }
                        else{
                            response.message = "You lost... victory points gained :"+ game.getActualPlayer().getVictoryPoints();
                        }
                        // return to the player the winner
                    }
                    response.ignorePossibleCommands = true;
                    sendInBroadcast(response);
                    CommandManager.getCommandManagers().forEach(commandManager1 -> commandManager1.getCommandInterpreter().getPossibleCommands().clear());
                }
            }
            // closing stream and sockets, and eventually restart a new game kicking off every player
            in.close();
            out.close();
            socket.close();
            sockets.remove(socket);
            handlers.remove(this);
            nicknames.remove(nickname);
            CommandManager.getCommandManagers().remove(commandManager);
            System.out.println("Connection closed with " + socket);
            System.out.println("Connection to close: " + sockets);

            if (game != null) // if the game isn't in the login phase
                notifyEnd();
            if (sockets.isEmpty()) // if any socket is connected to the server
                resetValues();

            System.out.println("Connection remaining: " + sockets);
        } catch (IOException e) {
            System.err.println("Error with IOException");
            System.err.println(e.getMessage());
        }
    }

    public static synchronized Game getGame() {
        return game;
    }

    public static synchronized void setGame (List<String> nicknames){
        ClientHandler.game = new Game(nicknames);
    }

    public static synchronized void setSingleGame (List<String> nicknames){
        ClientHandler.game = new SingleGame(nicknames);
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public static synchronized AtomicInteger getNumberOfPlayers () {
        return numberOfPlayers;
    }

    public synchronized void setNumberOfPlayers (AtomicInteger numberOfPlayers){
        ClientHandler.numberOfPlayers = numberOfPlayers;
    }

    public synchronized void addNickname (String nickname){
        nicknames.add(nickname);
    }

    public static synchronized List<String> getNicknames () {
        return nicknames;
    }

    public String getNickname () {
        return nickname;
    }

    public void setNickname (String nickname){ this.nickname = nickname; }

    public static void setNicknames(List<String> nicknames) {
        ClientHandler.nicknames = nicknames;
    }

    public static List<ClientHandler> getHandlers() {
        return handlers;
    }

    public Socket getSocket() {
        return socket;
    }

    public static void setHandlers(List<ClientHandler> handlers) {
        ClientHandler.handlers = handlers;
    }

    /**
     * this method notify every player of the disconnection of the game
     * for any possible reason, and close the connection
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    private void notifyEnd () throws IOException {
        sockets = sockets.stream().filter(Objects::nonNull).collect(Collectors.toList());
        for (ClientHandler handler : getHandlers()) {
            ResponseToClient response = new ResponseToClient();
            response.message = "Someone left the game, everyone will be kicked out";
            handler.send(response);
            handler.socket.getOutputStream().close();
            handler.socket.getInputStream().close();
            handler.socket.close();
        }
        sockets.clear();
    }

    private synchronized void resetValues(){
        game = null;
        numberOfPlayers = null;
        nicknames.clear();
        handlers.clear();
        CommandManager.getCommandManagers().clear();
    }

    public static void sendInBroadcast (ResponseToClient message) throws IOException {
        for (ClientHandler handler : handlers) {
            handler.send(message);
        }
    }

    private boolean kickIfLobbyFullOfClient(PrintWriter out, Scanner in) throws IOException {
        if (numberOfPlayers == null) return false;
        if (numberOfPlayers.get() == handlers.size()){
            ResponseToClient response = new ResponseToClient();
            response.message = "Sorry, the lobby is full of people";
            send(response);
            in.close();
            out.close();
            socket.close();
            handlers.remove(this);
            return true;
        }
        return false;

    }

    /**
     * this private method close the connection if a player have
     * to decide the total number of players
     * @param out this is the PrintWriter associated with the socket
     * @param in this is the Scanner associated with the socket
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    private boolean kickIfPlayersToDefine(PrintWriter out, Scanner in) throws IOException {
        if (numberOfPlayers == null) return false;
        if (numberOfPlayers.get() < 0) {
            ResponseToClient response = new ResponseToClient();
            response.message = "Sorry, the number of players is yet to define";
            send(response);
            in.close();
            out.close();
            socket.close();
            handlers.remove(this);
            return true;
        }
        return false;
    }

    /**
     * this private method close the connection if a game
     * already exist
     * @param out this is the PrintWriter associated with the socket
     * @param in this is the Scanner associated with the socket
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    private boolean kickIfGameExist(PrintWriter out, Scanner in) throws IOException {
        if (game == null) return false;
        ResponseToClient response = new ResponseToClient();
        response.message = "Sorry, a game is already running on the server, try later";
        send(response);
        in.close();
        out.close();
        socket.close();
        handlers.remove(this);
        return true;
    }

    /**
     * this method send a message to the client associated
     * @param message this is the message to send
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void send(ResponseToClient message) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println(gson.toJson(message, ResponseToClient.class));
        out.flush();
    }

    private Gson createGson(){
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.
                        of(SoloToken.class, "type").
                        registerSubtype(TrackToken.class, "trackToken").
                        registerSubtype(CardToken.class, "cardToken"));
        builder.registerTypeAdapter(Resource.class, new ResourceInterfaceAdapter());
        builder.registerTypeAdapter(Marble.class, new MarbleInterfaceAdapter());
        return builder.create();
    }
}
