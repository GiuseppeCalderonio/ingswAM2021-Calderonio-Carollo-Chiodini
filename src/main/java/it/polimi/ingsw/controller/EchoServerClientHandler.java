package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.ResourceType;
import it.polimi.ingsw.model.SingleGame.SingleGame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class EchoServerClientHandler implements Runnable {
    private final Socket socket;
    private static List<Socket> sockets = new ArrayList<>();
    private static Game game;
    private static AtomicInteger numberOfPlayers = null;
    private String nickname = new String("");
    private static List<String> nicknames = new ArrayList<>();
    private CommandManager commandManager;

    public EchoServerClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            Scanner in = new Scanner(socket.getInputStream());
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            if (kickIfGameExist(out, in) || kickIfPlayersToDefine(out, in)){
                if (sockets.isEmpty()) // if any socket is connected to the server
                    resetValues();
                return;
            }
            //kickIfPlayersToDefine(out, in);
            //kickIfGameExist(out, in);
            sockets.add(socket);
            System.out.println("New connection with " + socket);
            send("Welcome to the server");
            commandManager = new CommandManager(this);
            if (numberOfPlayers == null)
                numberOfPlayers = new AtomicInteger(-1);
            GsonBuilder gsonBuilder = new GsonBuilder();
            Gson gson = gsonBuilder.create();
            Command command = new Command();
            // Leggo e scrivo nella connessione finche' non ricevo "quit"
            while (true) {
                String line = null;
                try {
                    try {
                        //GsonBuilder gsonBuilder = new GsonBuilder();
                        //Gson gson = gsonBuilder.create();
                        line = in.nextLine();
                        command = gson.fromJson(line, Command.class);
                        if (command.cmd.equals("quit"))
                            break;
                        String code = commandManager.processCommand(command, this);

                        send(code);


                    } catch (JsonSyntaxException e) {
                        System.err.println("Problem...");
                        break;
                    } catch (Exception e){
                        System.err.println(e.getMessage());
                        break;
                    }

                } catch (NoSuchElementException e){
                    System.out.println("Error with: " + socket);
                    break;
                }

                //System.out.println(" Error with: " + s);
                //in = new Scanner(socket.getInputStream());
            }
            // Chiudo gli stream e il socket
            in.close();
            out.close();
            socket.close();
            sockets.remove(socket);
            nicknames.remove(nickname);
            System.out.println("Connection closed with " + socket);
            System.out.println("Connection to close: " + sockets);

            if (game != null) // if the game isn't in the login phase
                notifyEnd();
            if (sockets.isEmpty()) // if any socket is connected to the server
                resetValues();

            System.out.println("Connection remaining: " + sockets);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }


    public synchronized Game getGame () {
        return game;
    }

    public synchronized void setGame (List<String> nicknames){
        EchoServerClientHandler.game = new Game(nicknames);
    }

    public synchronized void setSingleGame (List<String> nicknames){
        EchoServerClientHandler.game = new SingleGame(nicknames);
    }


    public synchronized AtomicInteger getNumberOfPlayers () {
        return numberOfPlayers;
    }

    public synchronized void setNumberOfPlayers (AtomicInteger numberOfPlayers){
        EchoServerClientHandler.numberOfPlayers = numberOfPlayers;
    }

    public synchronized void addNickname (String nickname){
        nicknames.add(nickname);
    }

    public synchronized List<String> getNicknames () {
        return nicknames;
    }

    public String getNickname () {
        return nickname;
    }

    public void setNickname (String nickname){

        this.nickname = nickname;
    }



    /**
     * this method notify every player of the disconnection of the game
     * for any possible reason, and close the connection
     * @throws IOException Signals that an I/O exception of some sort has occurred
     */
    private void notifyEnd () throws IOException {
        sockets = sockets.stream().filter(Objects::nonNull).collect(Collectors.toList());
        PrintWriter out;
        for (Socket s : sockets) {
            out = new PrintWriter(s.getOutputStream());
            out.println("Someone left the game, everyone will be kicked out");
            out.flush();
            s.getOutputStream().close();
            s.getInputStream().close();
            s.close();
        }
        sockets.clear();
    }

    private void resetValues(){
        game = null;
        numberOfPlayers = null;
        nicknames = new ArrayList<>();
    }

    public void notifySomething (String message) throws IOException {
        PrintWriter out;// = new PrintWriter(s.getOutputStream());
        for (Socket s : sockets) {
            out = new PrintWriter(s.getOutputStream());
            out.println(message);
            out.flush();
        }

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
            out.println("Spiacente, il numero di giocatori è ancora da definire");
            out.flush();
            in.close();
            out.close();
            socket.close();
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

        out.println("Spiacente, esiste già un gioco in corso, riporvare in seguito");
        out.flush();
        in.close();
        out.close();
        socket.close();
        return true;
    }

    public void send(String message) throws IOException {
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        out.println(message);
        out.flush();
    }
}
