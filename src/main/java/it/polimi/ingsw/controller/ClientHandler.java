package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.Lobby;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * this class represent the client handler.
 * in particular, when a client decide the number of players in the
 * waiting room, it will be associated with a lobby and a client handler
 * contained into it; this class is relative to handle clients, to send messages and
 * also to communicate with the controller
 */
public class ClientHandler {

    /**
     * this attribute represent the socket associated with the controller
     */
    private final Socket socket;

    /**
     * this attribute represent the nickname of the player associated with the client
     */
    private String nickname = "";

    /**
     * this attribute represent the gson parser to receive and send messages
     */
    private final Gson gson;

    /**
     * this attribute represent the command manager.
     * in particular, this attribute is the first real
     * layer of the controller that get messages and respond with messages
     */
    private CommandManager commandManager;

    /**
     * this attribute represent the lobby associated with the client.
     * in particular, every client have to be into a lobby
     * that contains all the client of the game, the game reference,
     * the number of players, if the game is started or finished
     */
    private final Lobby lobby;

    /**
     * this attribute represent the printWriter from which send messages
     * with the client
     */
    private final PrintWriter out;

    /**
     * this attribute represent the scanner from which receive messages
     * with the client
     */
    private final Scanner in;

    /**
     * this attribute indicates if the game for this specific client
     * have to continue the game
     */
    private final AtomicBoolean play = new AtomicBoolean(true);

    /**
     *
     * @param socket
     * @param lobby
     * @param out
     * @param in
     * @param gson
     */
    public ClientHandler(Socket socket, Lobby lobby, PrintWriter out, Scanner in, Gson gson) {

        this.socket = socket;
        this.lobby = lobby;
        this.gson = gson;
        this.in = in;
        this.out = out;
    }

    /**
     *
     */
    public void start() {

        System.out.println("New connection with " + socket);

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
        try {
            in.close();
            out.close();
            socket.close();
            lobby.removeClient(this);
            lobby.getNicknames().remove(nickname);
            System.out.println("Connection closed with " + socket);
            // if the game isn't in the login phase
            if (!getInterpreter().getGamePhase().equals(GamePhase.LOGIN))
                sendBroadcastDisconnection();

        } catch(IOException e) {
            System.err.println("A client disconnected");
        } catch (Exception e){
            System.err.println("Fatal error");
            System.out.println(e.getMessage());
        }
    }

    /**
     *
     */
    public void setPlayFalse(){
        play.set(false);
    }

    /**
     *
     * @return
     */
    public synchronized Game getGame() {
        return lobby.getGame();
    }

    /**
     *
     */
    public synchronized void createGame (){
        lobby.createGame();
    }

    /**
     *
     * @return
     */
    public synchronized int getNumberOfPlayers () {
        return lobby.getNumberOfPlayers();
    }

    /**
     *
     * @param nickname
     */
    public synchronized void addNickname (String nickname){
        lobby.addNickname(nickname);
    }

    /**
     *
     * @return
     */
    public synchronized List<String> getNicknames () {

        return lobby.getNicknames();
    }

    /**
     *
     * @return
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @param nickname
     */
    public void setNickname (String nickname){ this.nickname = nickname; }

    /**
     *
     * @return
     */
    protected List<ClientHandler> getClients() {
        return lobby.getClients();
    }

    /**
     *
     * @return
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     *
     * @return
     */
    protected boolean isYourTurn(){
        return lobby.getGame().isYourTurn(nickname);
    }

    /**
     *
     * @return
     */
    private List<String> getPossibleCommands(){
        return commandManager.getCommandInterpreter().getPossibleCommands();
    }

    /**
     *
     * @return
     */
    public CommandInterpreter getInterpreter(){
        return commandManager.getCommandInterpreter();
    }

    /**
     *
     * @param interpreter
     */
    protected void setCommandInterpreter(CommandInterpreter interpreter){
        commandManager.setCommandInterpreter(interpreter);
    }

    protected CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     *
     * @param commandManager
     */
    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }





    /**
     * this method notify every player of the disconnection of the game
     * for any possible reason, and close the connection
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public synchronized void sendBroadcastDisconnection () throws IOException {
        List<ClientHandler> clients = getClients().stream().filter(Objects::nonNull).collect(Collectors.toList());
        for (ClientHandler client : clients) {
            client.setPlayFalse();
            ResponseToClient response = new ResponseToClient("Someone left the game, everyone will be kicked out");
            client.send(response);
            client.socket.getOutputStream().close();
            client.socket.getInputStream().close();
            client.socket.close();
        }
        getClients().clear();
        lobby.setGameFinished();

    }

    /**
     * this method send in broadcast the message in input, it simulate
     * the method notify for the pattern notify-observer
     * @param message this is the message to send in broadcast
     */
    public synchronized void sendInBroadcast (ResponseToClient message) {
        getClients().forEach(client -> client.send(message));
    }

    /**
     * this method send a message to the client associated
     * @param message this is the message to send
     */
    public synchronized void send(ResponseToClient message){
        out.println(gson.toJson(message, ResponseToClient.class));
        out.flush();
    }

    /**
     *
     * @return
     */
    public boolean readMessage() {
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
        }catch (JsonParseException e) { // the string received is not in gson format

            send(new ResponseToClient("you have to insert a correct json string format", getPossibleCommands()));
            return true;
        }
        try {

            // process the command and send the message or the messages to the players
            commandManager.processCommand(command);

            return true;

            // if one of the parameters of the command does not respect the preconditions
        }catch (NullPointerException | IndexOutOfBoundsException e){

            send(new ResponseToClient("Something gone wrong, you've probably chosen wrong inputs ", getPossibleCommands()));

            return true;
        // the condition of ending a game are met
        }catch (EndGameException e){

            sendInBroadcast(new ResponseToClient("The game finish, the winner is" + e.getMessage()));

            lobby.setGameFinished();

            return true;

        } catch (QuitException e){ // quit the game if a player wat to exit

            return false;
        }
    }

    /**
     * this method is used only by the subclass localClientHandler
     * @param command
     * @return
     */
    public void readMessage(Command command){
    }
}