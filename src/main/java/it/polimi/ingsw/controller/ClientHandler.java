package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.controller.responseToClients.WinnerResponse;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.Lobby;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

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
    private boolean play = true;

    /**
     * this constructor create the client handler starting from his socket, the
     * lobby joined with his game, the print writer to send messages with the socket, the scanner to
     * receive messages from the socket, the gson to deserialize and serialize messages
     * @param socket this is the socket associated with the client
     * @param lobby this is the lobby joined from the client
     * @param out this is the print writer associated with the socket
     * @param in this is the scanner associated with the socket
     * @param gson this is the gson to deserialize and serialize messages
     */
    public ClientHandler(Socket socket, Lobby lobby, PrintWriter out, Scanner in, Gson gson) {

        this.socket = socket;
        this.lobby = lobby;
        this.gson = gson;
        this.in = in;
        this.out = out;
    }

    /**
     * this method start the client handler.
     * in particular, when the waiting room find the lobby for a client, after adding him,
     * call this method that is the main one of this class, because it handle
     * disconnections, it read message, it show the methods to send messages, send broadcasts,
     * send broadcast disconnections , so that the controller can use them to manage the match.
     * this method use a while true until the match finish for any reason, and wait to read messages
     * and process them using the helper method read message
     */
    public void start() {

        System.out.println("New connection with " + socket);

        // set the timeout of the socket for handle remote disconnections
        try {
                socket.setSoTimeout(3000);

        } catch (SocketException e){
            e.printStackTrace();
            return;
        }

        // create a new command manager
        commandManager = new CommandManager(this);

        while (play) {
                play = readMessage();
        }

        synchronized (lobby){
            // closing stream and sockets, and eventually restart a new game kicking off every player
            try {
                sendBroadcastDisconnection();
            } catch(IOException e) {
                System.err.println("A client disconnected, message " + e.getMessage() + " method: start in ClientHandler, socket" + socket);
                e.printStackTrace();
            } catch (Exception e){
                System.err.println("Fatal error message " + e.getMessage() + " method: start in ClientHandler, socket" + socket);
                System.out.println(e.getMessage());
            }

            if (lobby.getClients().isEmpty())
                lobby.setGameFinished();
        }

    }

    /**
     * this method get the game associated with the lobby joined from the client
     * @return the game associated with the lobby joined from the client
     */
    public Game getGame() {
        return lobby.getGame();
    }

    /**
     * this method create the game to play for the lobby joined by the client.
     * in particular, this method if the lobby is composed only by one player,
     * will create a single game, a normal one otherwise
     */
    public void createGame (){
        lobby.createGame();
    }

    /**
     * this method get the number of players of the lobby joined by the client.
     * in particular, when a client want to start a game, and selecting the number of
     * players desired, if there isn't space in any lobby, the waiting room creates one and
     * store there the number of player, that this method get
     * @return the number of players of the lobby joined by the client
     */
    public int getNumberOfPlayers () {
        return lobby.getNumberOfPlayers();
    }

    /**
     * this method add a nickname to the list of nicknames associated with the lobby
     * @param nickname this is the nickname to add
     */
    public void addNickname (String nickname){
        lobby.addNickname(nickname);
    }

    /**
     * this method get a list of String representing the nicknames of every player that joined the lobby
     * @return the nicknames of every player that joined the lobby
     */
    public List<String> getNicknames () {

        return lobby.getNicknames();
    }

    /**
     * this method get the nickname of the client chosen during the login phase
     * @return the nickname of the client chosen during the login phase, "" if the
     *         client haven't selected his nickname yet
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * this method set the nickname of the client, it is used during the login phase
     * @param nickname this is the nickname to set
     */
    public void setNickname (String nickname){ this.nickname = nickname; }

    /**
     * this method get the list of Client Handlers associated with every client that
     * joined the lobby.
     * it is mainly used to send broadcast messages
     * @return the list of client handlers associated with every client that
     *         joined the lobby
     */
    public List<ClientHandler> getClients() {
        return lobby.getClients();
    }

    /**
     * this method get the socket associated with the client
     * @return the socket associated with the client
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * this method indicates if if the turn of the client or not.
     * if the match is composed only by one player, it should return true always
     * @return true if id the turn of the client, false otherwise
     */
    protected boolean isYourTurn(){
        return lobby.getGame().isYourTurn(nickname);
    }



    /**
     * this method get the command interpreter of the client.
     * in particular, the command interpreter is used to read and
     * execute command, checking if a command is right or not, store
     * the buffers to manage multiple operations, store
     * the list of possible commands, send the broadcast messages if necessary
     * @return the command interpreter associated with the client
     */
    public CommandInterpreter getInterpreter(){
        return commandManager.getCommandInterpreter();
    }

    /**
     * this method set the command interpreter of the client.
     * in particular, the command interpreter is used to read and
     * execute command, checking if a command is right or not, store
     * the buffers to manage multiple operations, store
     * the list of possible commands, send the broadcast messages if necessary
     * @param interpreter this is the command interpreter to set
     */
    protected void setCommandInterpreter(CommandInterpreter interpreter){
        commandManager.setCommandInterpreter(interpreter);
    }

    /**
     * this class get the command manager associated with the client.
     * in particular, the command manager is used to manage the phases of the game
     * (LOGIN, INITIALISE, TURNS see GamePhase) , to send broadcast during the change of a phase,
     * to send the messages processed from the command interpreter
     * @return the command manager associated with the client
     * @see GamePhase
     */
    protected CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * this class set the command manager associated with the client.
     * in particular, the command manager is used to manage the phases of the game
     * (LOGIN, INITIALISE, TURNS see GamePhase) , to send broadcast during the change of a phase,
     * to send the messages processed from the command interpreter
     *
     * @param commandManager this is the command manager to set
     * @see GamePhase
     */
    public void setCommandManager(CommandManager commandManager) {
        this.commandManager = commandManager;
    }


    /**
     * this method notify every player of the disconnection of the game
     * for any possible reason, and close the connection
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    public void sendBroadcastDisconnection () throws IOException {

        removeClient();

        if (!lobby.isGameStarted()){
            return;
        }

        for (ClientHandler client : getClients()) {
            client.socket.close();
        }

        getClients().clear();
        lobby.setGameFinished();

    }

    private void removeClient() throws IOException {
        in.close();
        out.close();
        socket.close();
        lobby.removeClient(this);
        lobby.getNicknames().remove(nickname);
        System.out.println("Connection closed with " + socket);
    }

    /**
     * this method send in broadcast the message in input, it simulate
     * the method notify for the pattern notify-observer
     * @param message this is the message to send in broadcast
     */
    public void sendInBroadcast (ResponseToClient message) {
        getClients().forEach(client -> client.send(message));
    }

    /**
     * this method send a message to the client associated.
     * if the class is of dynamic type LocalClientHandler,
     * the method just update the client with the method
     * message.updateClient(client).
     * if the parsing of the message fails, the method send
     * a broadcast disconnection (this thing should never happen)
     * @param message this is the message to send
     * @see LocalClientHandler
     */
    public synchronized void send(ResponseToClient message){
        try {
            out.println(gson.toJson(message, ResponseToClient.class));
            out.flush();
        } catch (JsonParseException e){
            System.err.println("Error json parser exception: " + e.getMessage() + ", in method send, class ClientHandler");
            e.printStackTrace();
            play = false;
        }

    }

    /**
     * this method read the message sent from the client.
     * in particular,it use the nextLine(), and if the client disconnect (and the game isn' in the login phase),
     * the method will throw a NoSuchElementException, and will be called a broadcast disconnection,
     * if the client send a quit message (and the game isn' in the login phase),
     * will be called a broadcast disconnection, if the client send a wrong json message,
     * will be sent to the client an error message, if the method of the command manager
     * processCommand throws an IndexOutOfBoundException or a NullPointerException, the method
     * send an error message to the client, if the method processCommand throws an
     * EndGameException will be notified in broadcast the name of the winner and the lobby will be
     * set to gameFinished, in order to be filtered by the waiting room.
     * otherwise , the command will be processed and executed
     * @return false if the client have to leave the lobby for any reason, true otherwise
     */
    public boolean readMessage() {
        String line;
        // read from the input (eventually throws NoSuchElementException)
        try {

            line = in.nextLine();

        } catch (NoSuchElementException | IllegalStateException e){ //when a client disconnected
             return false;
        }
        Command command;
        try {
            // translate the string to a command
            command = gson.fromJson(line, Command.class); // convert the message in a processable command
            // when the command is not in a json format
        }catch (JsonParseException e) { // the string received is not in gson format
            System.err.println(e.getMessage());
            send(new ResponseToClient(Status.ERROR));
            return true;
        }

        synchronized (lobby){
            return readMessage(command);
        }
    }

    /**
     * this method is an helper method of the readMessage method.
     * in particular, it is synchronized in the lobby, and calls the method
     * processCommand of the commandManager, handling all the possible exceptions of the game.
     *
     * in the subclass localClientHandler it is used to read the message starting from the command passed in input, and then will be
     * called the method processCommand on it
     * @param command this is the command to process
     * @see LocalClientHandler
     */
    public boolean readMessage(Command command){

        try {
            // process the command and send the message or the messages to the players
            commandManager.processCommand(command);
            return true;
            // if one of the parameters of the command does not respect the preconditions
        }catch (NullPointerException | IndexOutOfBoundsException e){
            System.err.println(e.getMessage());
            e.printStackTrace();
            send(new ResponseToClient(Status.ERROR));
            return true;
            // the condition of ending a game are met
        }catch (EndGameException e){
            // send in broadcast the name of the winner and the personal victory points gained
            sendInBroadcast(new WinnerResponse(e.getMessage(), getGame().findPlayer(nickname).getVictoryPoints()));
            // set the lobby to finished
            lobby.setGameFinished();
            return true;
        } catch (QuitException e){ // quit the game if a player wat to exit
            // exit from the loop
            return false;
        }
    }
}