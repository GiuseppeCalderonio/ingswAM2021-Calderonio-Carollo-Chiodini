package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.SingleGame.SingleGame;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EchoServerClientHandler implements Runnable {
    private final Socket socket;
    private static List<Socket> sockets = new ArrayList<>();
    private boolean end = true;
    private static Game game;
    private static Integer numberOfPlayers = null;
    private String nickname;
    private static List<String> nicknames = new ArrayList<>();
    private CommandInterpreter commandInterpreter;
    private static List<CommandInterpreter> interpreters = new ArrayList<>();

    public EchoServerClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {


        Scanner in = null;
        try {
            in = new Scanner(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        PrintWriter out = null;
        try {
            out = new PrintWriter(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (game != null) {
            assert out != null;
            out.print("there is already a running game, ");
            out.println("exit");
            out.flush();
            end = true;
        } else if (numberOfPlayers != null && numberOfPlayers < 0) {
            assert out != null;
            out.print("wait for the player to initialise the game, try to reconnect again, ");
            out.println("exit");
            out.flush();
            end = true;
        } else {
            assert out != null;
            out.println("Benvenuto nel gioco");
            out.flush();
        }
        sockets.add(socket);
        commandInterpreter = new SetSizeInterpreter();
        interpreters.add(commandInterpreter);
        while (end) {

            if (numberOfPlayers == null) {
                numberOfPlayers = -1;
                commandInterpreter = new SetSizeInterpreter();
            } else if (nickname == null) {

                commandInterpreter = new LoginInterpreter();
            }

            assert in != null;

            String line = in.nextLine();
            Gson gson = new Gson();
            Command command = gson.fromJson(line, Command.class);

            if (command.cmd.equals("quit")) {
                break;
            }

            String code = commandInterpreter.executeCommand(command, this);
            // if the number of players got set
            if (code.equals("ok, start with the login")) commandInterpreter = new LoginInterpreter();
            // if every player did the login
            if (code.equals("ok, wait for other players to join")) {
                commandInterpreter = new InitialisingInterpreter();
                // notify all
                if (numberOfPlayers == 1){
                    game = new SingleGame(nicknames);
                }
                else {
                    game = new Game(nicknames);
                    nicknames = game.getPlayers().stream().map(RealPlayer::getNickname).collect(Collectors.toList());
                }
            }
            if (code.equals("ok, wait for other players to decide and the game will start")){
                commandInterpreter = new TurnsInterpreter();
            }

            out.println("Code : " + code);
            out.flush();

        }

        assert in != null;
        in.close();
        out.close();
        try {
            notifyEnd();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Eccugi");
        if (nickname != null) {
            synchronized (nicknames) {
                nicknames.remove(nickname);
            }
        }
    }

    public synchronized Game getGame() {
        return game;
    }

    public synchronized void setGame(Game game) {
        EchoServerClientHandler.game = game;
    }

    public synchronized Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public synchronized void setNumberOfPlayers(Integer numberOfPlayers) {
        EchoServerClientHandler.numberOfPlayers = numberOfPlayers;
    }

    public synchronized void addNickname(String nickname){
        nicknames.add(nickname);
    }

    public synchronized List<String> getNicknames() {
        return nicknames;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    private void notifyEnd() throws IOException {
        PrintWriter out;
        sockets = sockets.stream().filter(Objects::nonNull).collect(Collectors.toList());
        for (Socket s : sockets){
            out = new PrintWriter(s.getOutputStream());
            out.println("qualcuno si è disconnesso, exit");
            out.flush();
            s.close();
        }
        sockets = new ArrayList<>();
    }

    private void ping(Socket s) throws IOException {
        PrintWriter out = new PrintWriter(s.getOutputStream());
        out.println("ping");
        out.flush();
        Scanner in = new Scanner(s.getInputStream());
        String line = in.nextLine();
        if (!line.equals("pong"))
            notifyEnd();
    }
}


