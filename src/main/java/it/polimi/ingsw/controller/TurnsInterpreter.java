package it.polimi.ingsw.controller;

import java.util.List;

public class TurnsInterpreter implements CommandInterpreter{

    private List<String> possibleCommands;

    /**
     * this method execute the command given in input, returning a code that will
     * be sent to the client associated with the handler
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     *
     * @param command this is the command to execute
     * @param handler this is the handler to notify in case of
     *                a internal state change
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(Command command, ClientHandler handler) {
        return null;
    }

    @Override
    public List<String> getPossibleCommands() {
        return null;
    }
}
