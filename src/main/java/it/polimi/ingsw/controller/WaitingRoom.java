package it.polimi.ingsw.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class WaitingRoom implements Runnable{

    private Socket socket;
    private final static List<List<ClientHandler>> lobbies = new ArrayList<>();

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

    public WaitingRoom(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        Scanner in;
        PrintWriter out;
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
        }catch (IOException e){
            e.printStackTrace();
            return;
        }
        int numberOfPlayers = 0;

        ResponseToClient response = new ResponseToClient();

        response.message = "Welcome to the server, decide the number of players[1,2,3,4]";
        response.possibleCommands = new ArrayList<>();

        out.println("Welcome to the server, decide the number of players[1,2,3,4], possible commands: [set_players, quit]");
        out.flush();

        do {
            try {
                numberOfPlayers = Integer.parseInt(in.nextLine());
            }catch (NullPointerException e){

            }



        }while (numberOfPlayers < 1 || numberOfPlayers > 4);
    }


    private void searchLobby(Socket socket){
        synchronized (lobbies){
            for (List<ClientHandler> lobby : lobbies){

            }
        }
    }
}
