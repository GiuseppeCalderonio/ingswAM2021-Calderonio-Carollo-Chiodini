package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;

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
        this.gson = gson;
        this.in = in;
        this.out = out;
    }

    public void start() {

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
        try {
            in.close();
            out.close();
            socket.close();
            lobby.removeClient(this);
            lobby.getNicknames().remove(nickname);
            System.out.println("Connection closed with " + socket);
            // if the game isn't in the login phase
            if (!commandManager.getCommandInterpreter().getGamePhase().equals(GamePhase.LOGIN))
                sendBroadcastDisconnection();

        } catch(IOException e) {
            System.err.println("A client disconnected");
        } catch (Exception e){
            System.err.println("Fatal error");
            System.out.println(e.getMessage());
        }
    }

    public void setPlayFalse(){
        play.set(false);
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

    private List<String> getPossibleCommands(){
        return getCommandManager().getCommandInterpreter().getPossibleCommands();
    }

    public CommandInterpreter getInterpreter(){
        return getCommandManager().getCommandInterpreter();
    }



    /**
     * this method notify every player of the disconnection of the game
     * for any possible reason, and close the connection
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    private synchronized void sendBroadcastDisconnection () throws IOException {
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
            send(new ResponseToClient("you have to insert a json string format", getPossibleCommands()));
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
                try {
                    sendBroadcastDisconnection();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                return;
            }
        }
    }
}