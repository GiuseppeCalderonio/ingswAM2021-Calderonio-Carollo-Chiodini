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
        if (!command.cmd.equals("set_players")) {
            return buildResponse("this command is not available in this phase of the game");
            //response.message = "this command is not available in this phase of the game";
            //response.possibleCommands = new ArrayList<String>(Collections.singletonList("set_players"));
            //return response;
        }
            //return "{ \"message\" : \"this command is not available in this phase of the game\", \"possibleCommands\" : " +  + "}";
        if (command.size < 1 || command.size > 4){
            return buildResponse("the size is not between 1 and 4");
            //response.message = "the size is not between 1 and 4";
            //response.possibleCommands = new ArrayList<String>(Collections.singletonList("set_players"));
            //return response;
        }

        handler.setNumberOfPlayers(new AtomicInteger(command.size));
        possibleCommands.remove("set_players");
        possibleCommands.add("login");

        return buildResponse("ok, start with the login");
        //response.message = "ok, start with the login";
        //response.possibleCommands = new ArrayList<String>(Collections.singletonList("login"));
        //return response;
            //return "{ \"message\" : \"the size is not between 1 and 4\", \"possibleCommands\" : " + new ArrayList<String>(Collections.singletonList("set_players")) + "}";

        //return "{ \"message\" : \"ok, start with the login\", \"possibleCommands\" : " + new ArrayList<String>(Collections.singletonList("login")) + "}";
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
