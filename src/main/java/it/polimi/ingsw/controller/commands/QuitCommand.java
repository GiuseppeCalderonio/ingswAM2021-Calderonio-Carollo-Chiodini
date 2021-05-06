package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.QuitException;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.List;

/**
 * this class represent a quit command.
 * in particular, when a player want to quit the game, he send this object to the server,
 * the connection with the client will be closed,
 * and the game will finish closing all the connections with the clients
 * that are playing the game if there is one running,
 * nothing happen when there isn't a game running
 */
public class QuitCommand implements Command{


    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "quit";
    }

    /**
     * this method execute the command given in input,
     * returning a message that will be sent to the client.
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     *
     * @param possibleCommands         these are the possible commands to eventually modify
     * @param client                   this is the handler to notify in case of
     *                                 a internal state change
     * @param previousPossibleCommands these are the possible commands referred to the previous
     *                                 command, that eventually may be modified
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(List<String> possibleCommands, ClientHandler client, List<String> previousPossibleCommands) throws EndGameException, QuitException {
        return null;
    }
}
