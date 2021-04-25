package it.polimi.ingsw.controller;

import java.util.List;

public interface CommandInterpreter {

    /**
     * this method execute the command given in input, returning a code that will
     * be sent to the client associated with the handler
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     * @param command this is the command to execute
     * @param handler this is the handler to notify in case of
     *                a internal state change
     * @return the response to send to the client\s
     */
    ResponseToClient executeCommand(Command command, ClientHandler handler);

    /**
     * this method get the possible command for a player
     * according with the rules of the game
     * @return the possible command for a player according with the rules of the game
     */
    List<String> getPossibleCommands();
}
