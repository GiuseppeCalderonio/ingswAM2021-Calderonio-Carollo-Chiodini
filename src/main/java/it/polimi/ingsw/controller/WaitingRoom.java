package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

import static it.polimi.ingsw.controller.gsonManager.PersonalGsonBuilder.createPersonalGsonBuilder;

/**
 * this class represent the waiting room, in which every client can choose
 * the number of player of the game that he want to play
 */
public class WaitingRoom implements Runnable{

    /**
     * this is the socket associated with the player
     */
    private final Socket socket;

    private final Gson gson;

    /**
     * this is a list representing the lobbies in which are stored the game.
     * every player, after choosing the number of players of the game,
     * will login into a lobby with the number selected if existing,
     * or he will create it
     */
    private final static List<Lobby> lobbies = new ArrayList<>();


    /**
     * this method create a waiting room for a player storing the socket associated
     * @param socket this is the socket associated with the client
     */
    public WaitingRoom(Socket socket){
        // assign the socket
        this.socket = socket;
        // create the gsonBuilder
        this.gson = createPersonalGsonBuilder();
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
        Scanner in;
        PrintWriter out;
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            return;
        }
        // this is the int that represent the number of player of the game, that the client will decide
        int numberOfPlayers;
        // filter only the lobbies that aren't finished a game
        clearLobbies();
        // send the welcome message to the player
        sendWelcomeMessage(out);

        do {
            try {
                // convert the message in a processable command
                Command command = gson.fromJson(in.nextLine(), Command.class);
                // set the number of players of the game
                numberOfPlayers = command.numberOfPlayers;
            // if the number of players are between 1 and 4
            if (!(numberOfPlayers < 1 || numberOfPlayers > 4))
                break;
            //else
            // notify the player of his error
            send(out , buildResponse("Insert a number between 1 and 4 "));
            // if the player didn't send a number
            }catch (NumberFormatException e){
                send(out, buildResponse("Insert a valid number"));
            }
            // if the string sent from the client isn't in a gson format
            catch (JsonSyntaxException e){
                send(out, buildResponse("Insert a json string please"));
            // if the player disconnect
            }catch (NoSuchElementException e){
                return;
            }
        }while (true);
        // create a new variable to start the game for the client
        ClientHandler newClient;
        try {
            // search the lobby to play
            newClient = searchLobby(numberOfPlayers, out, in);
            // start the match for the client
            newClient.start();
        } catch (IOException ignored) { }

    }

    /**
     * this method search a lobby for the client.
     * in particular, when already exist a game with the same number of players chosen
     * and there is already space in the lobby, the method adds the client to the lobby,
     * create a new lobby and add the client to it
     *
     * @param numberOfPlayers these are the number of players of the game to search/create
     * @param out this is the printWriter associated with the socket
     * @param in this is the scanner associated with the socket
     * @return the ClientHandler already added to the lobby
     * @throws IOException if occurs a network error
     */
    private ClientHandler searchLobby(int numberOfPlayers, PrintWriter out, Scanner in)throws IOException {
        // for all lobbies
        for (Lobby lobby : lobbies){
            try {
                // if the lobby has the same number of players chosen from the client
                if (lobby.getNumberOfPlayers() == numberOfPlayers){
                    // and if the lobby isn't already started
                    if (!lobby.isGameStarted()){
                        // create a client handler and return it
                        return createClientHandler(out, in, lobby);
                    }
                }
            }catch (NullPointerException | IndexOutOfBoundsException ignored){}
        }
        // create a new lobby
        Lobby lobby = new Lobby(numberOfPlayers);
        // add it to the lobbies static list
        lobbies.add(lobby);
        // create a client handler and return it
        return createClientHandler(out, in, lobby);
    }

    /**
     * this method filter only the lobbies with a game in progress.
     * in particular, check all the lobbies that have false the attribute
     * isGameFinished
     */
    private synchronized void clearLobbies(){
        // remove if the game is finished
        lobbies.removeIf(Lobby::isGameFinished);
    }

    /**
     * this method send the welcome message to the player.
     * in particular, it indicates to the client that he have to decide the number of players
     * of the game that he want to play
     * @param out this is the printWriter associated with the socket
     */
    private void sendWelcomeMessage(PrintWriter out){
        // create a new response
        ResponseToClient response = new ResponseToClient();
        response.message = "Welcome to the server, decide the number of players[1,2,3,4]";
        response.possibleCommands = new ArrayList<>(Collections.singletonList("set_players"));
        // send the message to the client
        send(out, response);
    }

    /**
     * this method send a message to the client in the gson format, as a ResponseToClient object
     * @param out this is the printWriter associated with the socket
     * @param response this is the response that must be send
     */
    private void send(PrintWriter out, ResponseToClient response){
        // parse the object ResponseToClient to a string
        String toSend = gson.toJson(response, ResponseToClient.class);
        // send the string to the print writer
        out.println(toSend);
        out.flush();
    }

    /**
     * this method create an object ResponseToClient starting from a string.
     * in particular, it assign to the attribute response.message the string message
     * and to the attribute response.possibleCommands the list [se_players],
     * that is the only action that the client can do in this phase of the game
     * @param message this is the message to send
     * @return the response built starting from the message
     */
    private ResponseToClient buildResponse(String message){
        // create a new object ResponseToClient
        ResponseToClient response = new ResponseToClient();
        // assign the message
        response.message = message;
        // assign the only possible command compatible with this phase of game
        response.possibleCommands = new ArrayList<>(Collections.singletonList("set_players"));
        return response;
    }

    /**
     * this method initialise a client and adds it to the lobby in input
     * @param out this is the printWriter associated with the socket
     * @param in this is the scanner associated with the socket
     * @param lobby this is the lobby in which add the client
     * @return the client handler object associated with the client
     */
    private ClientHandler createClientHandler(PrintWriter out, Scanner in, Lobby lobby) {
        // create a new object client handler
        ClientHandler newClient = new ClientHandler(socket, lobby, out, in, gson);
        // add the client just created to the lobby assigned
        lobby.addClient(newClient);
        return newClient;
    }
}
