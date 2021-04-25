package it.polimi.ingsw.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginInterpreter implements CommandInterpreter {

    List<String> possibleCommands = new ArrayList<>(Collections.singletonList("login"));
    
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
        if (!command.cmd.equals("login")){
            return buildResponse("this command is not available in this phase of the game");
            //response.message = "this command is not available in this phase of the game";
            //response.possibleCommands = new ArrayList<String>(Collections.singletonList("login"));
            //return response;
        }
            //return "{ \"message\" : \"this command is not available in this phase of the game\", \"possibleCommands\" : " + new ArrayList<String>(Collections.singletonList("login")) + "}";
        if (command.nickname.equals("")){
            return buildResponse("the nickname can't be the empty string");
            //response.message = "the nickname can't be the empty string";
            //response.possibleCommands = new ArrayList<String>(Collections.singletonList("login"));
            //return response;
        }
            //return "{ \"message\" : \"the nickname can't be the empty string\", \"possibleCommands\" : " + new ArrayList<String>(Collections.singletonList("login")) + "}";
        if (ClientHandler.getNicknames().contains(command.nickname)){
            return buildResponse("nickname selected is already taken");
            //response.message = "nickname selected is already taken";
            //response.possibleCommands = new ArrayList<String>(Collections.singletonList("login"));
            //return response;
        }
            //return "{ \"message\" : \"nickname selected is already taken\", \"possibleCommands\" : " + new ArrayList<String>(Collections.singletonList("login")) + "}";
        handler.addNickname(command.nickname);
        handler.setNickname(command.nickname);
        possibleCommands.remove("login");

        return buildResponse("ok, wait for other players to join");
        //response.message = "ok, wait for other players to join";
        //response.possibleCommands = new ArrayList<String>();
        //return response;
        //return "{ \"message\" : \"ok, wait for other players to join\" , \"possibleCommands\" : " + new ArrayList<String>() + "}";

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
