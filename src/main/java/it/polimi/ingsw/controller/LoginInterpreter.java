package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginInterpreter implements CommandInterpreter {

    final List<String> possibleCommands = new ArrayList<>(Collections.singletonList("login"));
    
    /**
     * this method execute the command given in input, returning a code that will
     * be sent to the client associated with the handler
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     * @param command this is the command to execute
     * @param client this is the client to notify in case of
     *                a internal state change
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(Command command, ClientHandler client) {
        if (command.getCmd().equals("quit"))
            throw new QuitException();
        if (!possibleCommands.contains(command.getCmd()))
            return new ResponseToClient("this command is not available in this phase of the game",
            possibleCommands);

        return command.executeCommand(possibleCommands, client, new ArrayList<>() );
    }

    /**
     * this method get the possible command for a player
     * according with the rules of the game
     * @return the possible command for a player according with the rules of the game
     */
    public List<String> getPossibleCommands() {
        return possibleCommands;
    }

    /*
      this method set the possible commands to the value passed as parameter

      @param possibleCommands this is the new list to set
     */

    /**
     * this method return the enum associated with the phase of the game
     * for loginInterpreter, it returns LOGIN, for initialisingInterpreter, it returns INITIALISING,
     * for turnsInterpreter, it returns TURNS
     *
     * @return the enum associated with the phase of the game
     */
    @Override
    public GamePhase getGamePhase() {
        return GamePhase.LOGIN;
    }
}
