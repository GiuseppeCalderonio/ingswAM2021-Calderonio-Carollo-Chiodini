package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SetSizeInterpreter implements CommandInterpreter{

    List<String> possibleCommands = new ArrayList<>(Collections.singletonList("set_players"));

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
    @Override
    public ResponseToClient executeCommand(Command command, ClientHandler handler) {
        if (!possibleCommands.contains(command.cmd))
            return buildResponse("this command is not available in this phase of the game");

        if (command.numberOfPlayers < 1 || command.numberOfPlayers > 4)
            return buildResponse("the size is not between 1 and 4");

        handler.setNumberOfPlayers(new AtomicInteger(command.numberOfPlayers));
        possibleCommands.remove("set_players");
        possibleCommands.add("login");

        return buildResponse("ok, start with the login");
    }

    public List<String> getPossibleCommands() {
        return possibleCommands;
    }
    
    private ResponseToClient buildResponse(String message){
        ResponseToClient response = new ResponseToClient();
        response.message = message;
        response.possibleCommands = possibleCommands;
        return response;
    }
}