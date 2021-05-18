package it.polimi.ingsw.network;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.controller.QuitException;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.Status;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import static it.polimi.ingsw.controller.gsonManager.PersonalGsonBuilder.createPersonalGsonBuilder;

/**
 * this class represent the waiting room, in which every client can choose
 * the number of player of the game that he want to play
 */
public class WaitingRoom implements Runnable, NetworkUser<ResponseToClient, Command>{

    /**
     * this attribute represent the socket associated with the player
     */
    private final Socket socket;

    /**
     * this attribute represent the gson parser
     */
    private final Gson gson;

    /**
     * this is a list representing the lobbies in which are stored the game.
     * every player, after choosing the number of players of the game,
     * will login into a lobby with the number selected if existing,
     * or he will create it
     */
    private final static List<Lobby> lobbies = new ArrayList<>();

    private PrintWriter out;

    private Scanner in;


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
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            return;
        }
        // this is the int that represent the number of player of the game, that the client will decide
        int numberOfPlayers;


        try { // let the client decide the number of players of the game
            numberOfPlayers = decideNumberOfPlayers();
        } catch (QuitException e){ // if he want to quit
            return;
        } catch (Exception e){
            e.printStackTrace();
            return;
        }


        // create a new variable to start the game for the client
        ClientHandler newClient;
        // search the lobby to play
        synchronized (lobbies){
            // filter only the lobbies that aren't finished a game
            // here is synchronized because two threads can try to remove the same lobby
            clearLobbies();
            // here is synchronized because two threads could enter into a lobby even if there isn't enough space
            newClient = searchLobby(numberOfPlayers);
        }

        // start the match for the client
        newClient.start();

    }


    /**
     * this method filter only the lobbies with a game in progress.
     * in particular, check all the lobbies that have false the attribute
     * isGameFinished
     */
    private void clearLobbies(){
        // remove if the game is finished
        lobbies.removeIf(Lobby::isGameFinished);
    }

    /**
     * this method search a lobby for the client.
     * in particular, when already exist a game with the same number of players chosen
     * and there is already space in the lobby, the method adds the client to the lobby,
     * create a new lobby and add the client to it
     *
     * @param numberOfPlayers these are the number of players of the game to search/create
     * @return the ClientHandler already added to the lobby
     */
    private ClientHandler searchLobby(int numberOfPlayers) {
        // for all lobbies
        for (Lobby lobby : lobbies){
            try {
                // if the client can enter into the lobby
                if (joinLobby(lobby, numberOfPlayers))
                    // return the new client adding him to the lobby
                    return createClientHandler(lobby);
                // if a client disconnect, is the last client of the lobby, and another client try to
                // enter into the lobby while the attribute gameIsFinished is true
            } catch (LobbyFinishedException ignored) { }

        }
        // create a new lobby
        Lobby lobby = new Lobby(numberOfPlayers);
        // add it to the lobbies static list
        lobbies.add(lobby);
        // create a client handler and return it
        return createClientHandler(lobby);
    }

    /**
     * this method verifies if the player can join a lobby.
     * in particular, it verifies: (1) the number of players of the lobby is the
     * same of the number of players in input, (2) the game isn't started yet, (3)
     * the number of players in the lobby is less than the number of players
     * that can join the lobby
     * @param lobby this is the lobby to verify
     * @param numberOfPlayers these are the number of players chosen from the client
     * @return true if the player can join the lobby, false otherwise
     */
    private boolean joinLobby(Lobby lobby, int numberOfPlayers) {
        return lobby.getNumberOfPlayers() == numberOfPlayers && // if the lobby has the same number of players chosen from the client
                !lobby.isGameStarted() && //if the lobby isn't already started
                lobby.getClients().size() < lobby.getNumberOfPlayers(); // if the lobby isn't full of players
    }

    /**
     * this method initialise a client and adds it to the lobby in input
     * @param lobby this is the lobby in which add the client
     * @return the client handler object associated with the client
     * @throws LobbyFinishedException when the method try to add a client to a lobby, but
     *         another client set the attribute gameIsFinished to true
     */
    private ClientHandler createClientHandler( Lobby lobby) throws LobbyFinishedException {
        // create a new object client handler
        ClientHandler newClient = new ClientHandler(socket, lobby, out, in, gson);
        // add the client just created to the lobby assigned
        // this action is synchronized to the specified lobby because can happen that a
        // client, while disconnecting himself, set the attribute gameIsFinished to true, and
        // this code, if not synchronized, can join the lobby and wait infinite time,
        synchronized (lobby){
            lobby.addClient(newClient);
        }

        return newClient;
    }

    /**
     * this method is used to decide the number of players of the game that
     * the client associated with the thread want to play.
     * in particular, it communicates with the client until he send
     * the object SetSizeCommand.class with json, and the attribute numberOfPlayers
     * is between 1 and 4.
     * the method throws a QuitException when the client send a QuitCommand.class
     * or when the connection is closed
     *
     * @return the number of players of the game that the client associated with
     *         the thread want to play
     *
     * @throws QuitException when the client send a QuitCommand.class or when the connection is closed
     */
    private int decideNumberOfPlayers() throws QuitException{
        int numberOfPlayers;
        do {
            try {
                // convert the message in a processable command
                Command command = gson.fromJson(in.nextLine(), Command.class);
                // set the number of players of the game
                numberOfPlayers = command.getNumberOfPlayers();

                if (command.getCmd().equals(CommandName.PONG))
                    throw new QuitException();

                // if the number of players are between 1 and 4
                if (!(numberOfPlayers < 1 || numberOfPlayers > 4))
                    break;
                //else
                // notify the player of his error
                send( buildResponse(Status.REFUSED));
                // if the player didn't send a number
            }catch (NumberFormatException e){
                send( buildResponse(Status.REFUSED));
            }
            // if the string sent from the client isn't in a gson format
            catch (JsonSyntaxException | NullPointerException e){
                send( buildResponse(Status.QUIT));
                // if the player disconnect
            } catch (NoSuchElementException e){
                throw new QuitException();
            } catch (Exception e){
                send( buildResponse(Status.ERROR));
            }
        }while (true);

        return numberOfPlayers;
    }

    /**
     * this method send a message to the client in the gson format, as a ResponseToClient object
     * @param response this is the response that must be send
     */
    @Override
    public void send( ResponseToClient response){
        // parse the object ResponseToClient to a string
        String toSend = gson.toJson(response, ResponseToClient.class);
        // send the string to the print writer
        out.println(toSend);
        out.flush();
    }

    /**
     * this method receive a message from the network
     * @return the message received
     */
    @Override
    public Command receiveMessage() {
        return gson.fromJson(in.nextLine(), Command.class);
    }

    /**
     * this method create an object ResponseToClient starting from a string.
     * in particular, it assign to the attribute response.message the string message
     * and to the attribute response.possibleCommands the list [se_players],
     * that is the only action that the client can do in this phase of the game
     * @param message this is the message to send
     * @return the response built starting from the message
     */
    private ResponseToClient buildResponse(Status message){
        return  new ResponseToClient(message);
    }

}
