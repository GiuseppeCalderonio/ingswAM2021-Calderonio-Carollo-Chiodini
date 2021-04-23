package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.SingleGame.SingleGame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandManager {

    private List<String> possibleCommands;
    private static List<CommandManager> commandManagers= new ArrayList<>();
    private EchoServerClientHandler handler;
    private CommandInterpreter commandInterpreter;

    public CommandManager(EchoServerClientHandler handler) throws IOException {
        possibleCommands = new ArrayList<>();
        if (handler.getNumberOfPlayers() == null) {
            possibleCommands.add("set_players");
            commandInterpreter = new SetSizeInterpreter();
            handler.send("Poosible coomands :[set_players]");
        }else {
            possibleCommands.add("login");
            commandInterpreter = new LoginInterpreter();
            handler.send("Poosible coomands :[login]");
        }
        commandManagers.add(this);
        this.handler = handler;
    }

    public CommandInterpreter getCommandInterpreter() {
        return commandInterpreter;
    }

    public static List<CommandManager> getCommandManagers() {
        return commandManagers;
    }

    public EchoServerClientHandler getHandler() {
        return handler;
    }

    public List<String> getPossibleCommands() {
        return possibleCommands;
    }

    public void setCommandInterpreter(CommandInterpreter commandInterpreter) {
        this.commandInterpreter = commandInterpreter;
    }

    public String processCommand(Command command, EchoServerClientHandler handler) throws IOException {

        String code = commandInterpreter.executeCommand(command, handler);
        String sendToClient;
        List<String> nicknames = handler.getNicknames();
        ResponseToClient response = new ResponseToClient();
        Game game = handler.getGame();
        // if the number of players got set
        if (code.contains("ok, start with the login")) commandInterpreter = new LoginInterpreter();
        // if every player did the login

        if (code.contains("ok, wait for other players to join") && nicknames.size() == handler.getNumberOfPlayers().get()) {
            commandManagers.forEach(commandManager -> commandManager.setCommandInterpreter(new InitialisingInterpreter()));
            //commandInterpreter = new InitialisingInterpreter();
            handler.notifySomething(
                    "Reached the number of players, start with the initialisation" +  ", Possible commands: [initialise_leaderCards]");
            // notify all
            if (handler.getNumberOfPlayers().get() == 1){
                handler.setSingleGame(nicknames);
            }
            else {
                handler.setGame(nicknames);
                nicknames = handler.getGame().getPlayers().stream().map(RealPlayer::getNickname).collect(Collectors.toList());
            }
        }
        if (code.contains("ok, wait for other players to decide and the game will start")){
            commandInterpreter = new TurnsInterpreter();
        }
        return code;
    }
}
