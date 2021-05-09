package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.List;

/**
 * this interface represent a command to do for the evolution of the game.
 * in particular, when a player have to modify something, it create this object as static type
 * (the dynamic one will be different depending on the function) and send it thought the network.
 * the server will receive and process it, eventually modifying the game state
 */
public interface Command {

    /**
     * this method get the cmd associated with the command
     * @return the cmd associated with the command
     */
    String getCmd();

    /**
     * this method execute the command given in input,
     * returning a message that will be sent to the client.
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     * @param possibleCommands these are the possible commands to eventually modify
     * @param client this is the handler to notify in case of
     *                a internal state change
     * @param previousPossibleCommands these are the possible commands referred to the previous
     *                                 command, that eventually may be modified
     * @return the response to send to the client\s
     */
    ResponseToClient executeCommand(List<String> possibleCommands, ClientHandler client, List<String> previousPossibleCommands) throws EndGameException;

    /**
     * this method build an object ResponseToClient to send to the client.
     * in particular, it create it starting from a string message,
     * and a list of available possible commands in a specific phase of the game
     * @param message this is the message to send
     * @param possibleCommands these are the possible commands
     * @return the object ResponseToClient built with the input parameters
     */
    default ResponseToClient buildResponse(String message, List<String> possibleCommands){
        return new ResponseToClient(message, possibleCommands);
    }

    /**
     * this method build an object ResponseToClient to send to the client.
     * in particular, it create it starting from a string message,
     * and set the attribute ignorePossibleCommands = true
     * @param message these are the possible commands
     * @return the object ResponseToClient built with the input parameters
     */
    default ResponseToClient buildResponseIgnoringCommands(String message){
        return new ResponseToClient(message);
    }

    /**
     * this method get the number of players that the client sent
     * the method is used only in the subClass SetSizeCommand, otherwise it returns always 0
     * @return the number of players that the client sent
     */
    default int getNumberOfPlayers() {
        return 0;
    }
}
