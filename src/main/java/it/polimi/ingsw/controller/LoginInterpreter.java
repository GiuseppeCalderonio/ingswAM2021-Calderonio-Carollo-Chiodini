package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.network.ClientHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * this class represent the login interpreter.
 * in particular, when every player have to start the login,
 * this class will be created and the method execute command will be called
 * from the commandManager
 */
public class LoginInterpreter implements CommandInterpreter {

    /**
     * this attribute represent the possible commands
     */
    final List<CommandName> possibleCommands = new ArrayList<>(Collections.singletonList(CommandName.LOGIN));
    
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
        if (command.getCmd().equals(CommandName.QUIT))
            throw new QuitException();
        if (!possibleCommands.contains(command.getCmd()))
            return new ResponseToClient(Status.REFUSED);

        return command.executeCommand(possibleCommands, client, new ArrayList<>() );
    }

    /**
     * this method get the possible command for a player
     * according with the rules of the game
     * @return the possible command for a player according with the rules of the game
     */
    public List<CommandName> getPossibleCommands() {
        return possibleCommands;
    }

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
