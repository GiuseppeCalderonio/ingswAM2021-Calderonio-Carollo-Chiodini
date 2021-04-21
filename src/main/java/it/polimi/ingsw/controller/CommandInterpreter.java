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
     * @return a code based on the type of action
     */
    String executeCommand(Command command, EchoServerClientHandler handler);

    default List<String> getPossibleCommands() {
        return null;
    }
}
