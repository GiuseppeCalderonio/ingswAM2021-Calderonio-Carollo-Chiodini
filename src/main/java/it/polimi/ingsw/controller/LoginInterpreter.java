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
        if (!possibleCommands.contains(command.cmd))
            return buildResponse("this command is not available in this phase of the game");

        if (command.nickname.equals(""))
            return buildResponse("the nickname can't be the empty string");

         if (ClientHandler.getNicknames().contains(command.nickname))
            return buildResponse("nickname selected is already taken");

        handler.addNickname(command.nickname);
        handler.setNickname(command.nickname);
        possibleCommands.remove("login");

        if (ClientHandler.getNumberOfPlayers().get() == 1){
            return buildResponseIgnoringCommands();
        }


        return buildResponse("login completed successfully, wait for other players to join");
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

    private ResponseToClient buildResponseIgnoringCommands(){
        ResponseToClient response = new ResponseToClient();
        response.message = "login completed successfully";
        response.ignorePossibleCommands = true;
        return response;
    }
}
