package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.ClientHandler.getHandlers;

public class CommandManager {

    private static final List<CommandManager> commandManagers= new ArrayList<>();
    private final ClientHandler handler;
    private CommandInterpreter commandInterpreter;

    public CommandManager(ClientHandler handler) throws IOException {
        ResponseToClient response = new ResponseToClient();
        if (ClientHandler.getNumberOfPlayers() == null) {
            commandInterpreter = new SetSizeInterpreter();

            response.message = "Welcome to the server! Set the number of players";
            response.possibleCommands = new ArrayList<>(Collections.singletonList("set_players"));
            //handler.send("{ \"message\" : \"Welcome to the server! Set the number of players\", \"possibleCommands\" : "+ new ArrayList<>(Collections.singletonList("set_players")) +"}");
        }else {
            commandInterpreter = new LoginInterpreter();

            response.message = "Welcome to the server! Start with the login";
            response.possibleCommands = new ArrayList<>(Collections.singletonList("login"));
            //handler.send("{ \"message\" : \"Welcome to the server! Start with the login\", \"possibleCommands\" : "+ new ArrayList<>(Collections.singletonList("login")) +"}");
        }
        handler.send(response);
        commandManagers.add(this);
        this.handler = handler;
    }

    public CommandInterpreter getCommandInterpreter() {
        return commandInterpreter;
    }

    public static List<CommandManager> getCommandManagers() {
        return commandManagers;
    }

    public ClientHandler getHandler() {
        return handler;
    }

    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    public void processCommand(Command command, ClientHandler handler) throws IOException {

        ResponseToClient response = commandInterpreter.executeCommand(command, handler);
        handler.send(response);
        List<String> nicknames = ClientHandler.getNicknames();

        // if the number of players got set
        if (response.message.equals("ok, start with the login")) commandInterpreter = new LoginInterpreter();

        // if every player did the login
        if (response.message.equals("ok, wait for other players to join") && nicknames.size() == 
                ClientHandler.getNumberOfPlayers().get()) {
            commandManagers.forEach(commandManager -> commandManager.setCommandInterpreter(new InitialisingInterpreter()));

            // notify all
            ResponseToClient broadcast = new ResponseToClient();
            broadcast.message = "Reached the number of players, start with the initialisation";
            broadcast.possibleCommands = new ArrayList<>(Collections.singletonList("initialise_leaderCards"));
            ClientHandler.sendInBroadcast(broadcast);
            // create the game
            if (ClientHandler.getNumberOfPlayers().get() == 1){
                ClientHandler.setSingleGame(nicknames);
            }
            else {
                ClientHandler.setGame(nicknames);
                nicknames = ClientHandler.getGame().getPlayers().stream().map(RealPlayer::getNickname).collect(Collectors.toList());
                ClientHandler.setNicknames(nicknames);
            }
            sendPositions();
        }
        if (response.message.equals("ok, now wait that everyone initialise his game and the game will start") &&
                commandManagers.stream().map(commandManager -> commandManager.getCommandInterpreter().getPossibleCommands()).
                allMatch(List::isEmpty)){
            commandManagers.forEach(commandManager -> commandManager.setCommandInterpreter(new TurnsInterpreter()));
            sendBroadcastInitialState();
            //commandInterpreter = new TurnsInterpreter();
        }
    }

    private synchronized void sendPositions() throws IOException {
        // sort the sockets with the same order of the nickname of the game
        List<Socket> sockets = new ArrayList<>();
        for (String nickname : ClientHandler.getNicknames()){
            int i = 0;
            for (ClientHandler ignored : getHandlers()) {
                if (getHandlers().get(i).getNickname().equals(nickname)){
                    sockets.add(getHandlers().get(i).getSocket());
                }
                i++;
            }
        }
        ClientHandler.setSockets(sockets);
        // send the position to every player
        int i = 1;
        for (Socket socket : sockets) {
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            ResponseToClient response = new ResponseToClient();
            response.message = "You are the number: " + i;
            response.position = i;
            out.println(handler.getGson().toJson(response, ResponseToClient.class));
            out.flush();
            i++;
        }
        // send the 4 initial leader cards for every player
        for (ClientHandler handler : getHandlers()){
            handler.sendObject( ClientHandler.getGame().findPlayer(handler.getNickname()).getPersonalLeaderCards());
        }
    }

    private synchronized void sendBroadcastInitialState() throws IOException {
        for (ClientHandler handler : getHandlers()){
            RealPlayer player = ClientHandler.getGame().findPlayer(handler.getNickname());
            Marble[][] marketMarble = ClientHandler.getGame().getMarketBoard().getMarketTray();
            Marble lonelyMarble = ClientHandler.getGame().getMarketBoard().getLonelyMarble();
            handler.sendObject(2, player.getPersonalDashboard().getPersonalWarehouse().getShelf(1).getResources());
            handler.sendObject(3, player.getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources());
            handler.sendObject(4, player.getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources());
            handler.sendObject(7, player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources());
            handler.sendObject(ClientHandler.getGame().getSetOfCard().show());
            handler.sendObject(marketMarble);
            handler.sendObject(lonelyMarble);
        }
        ResponseToClient broadcast = new ResponseToClient();
        broadcast.message = "The game start!";
        broadcast.possibleCommands = new ArrayList<>();
        ClientHandler.sendInBroadcast(broadcast);
    }
}
