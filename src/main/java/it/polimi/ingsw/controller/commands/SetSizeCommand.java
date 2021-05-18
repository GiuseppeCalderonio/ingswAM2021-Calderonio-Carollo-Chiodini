package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.List;

/**
 * this class represent the set size command.
 * in particular, this is the first command: every player, once the connection
 * is established, send this command to the server in order to decide the number of players
 * of the game that he want to play
 */
public class SetSizeCommand implements Command {

    /**
     * this attribute represent the number of players of the game that the client want to play
     */
    private final int numberOfPlayers;

    /**
     * this constructor create the object setting the number of players desired
     * @param numberOfPlayers this is the integer representing the number of players to set
     */
    public SetSizeCommand(int numberOfPlayers){
        this.numberOfPlayers = numberOfPlayers;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.SET_SIZE;
    }

    /**
     * this method return a string representing the error message
     * associated with the command
     *
     * @return a string representing the error message
     * associated with the command
     */
    @Override
    public String getErrorMessage() {
        return "size setting failed";
    }

    /**
     * this method return a string representing the confirm message
     * associated with the command
     *
     * @return a string representing the confirm message
     * associated with the command
     */
    @Override
    public String getConfirmMessage() {
        return "size set correctly, start with the login";
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
    public ResponseToClient executeCommand(List<CommandName> possibleCommands, ClientHandler client, List<CommandName> previousPossibleCommands) throws EndGameException {
        return null;
    }


    /**
     * this method get the number of players that the client sent
     * the method is used only in the subClass SetSizeCommand, otherwise it returns always 0
     *
     * @return the number of players that the client sent
     */
    @Override
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }
}
