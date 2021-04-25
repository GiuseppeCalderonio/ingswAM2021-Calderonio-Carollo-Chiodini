package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Resources.CollectionResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitialisingInterpreter implements CommandInterpreter{

    private final List<String> possibleCommands;

    private int card1, card2;
    private CollectionResources toInitialise;

    public InitialisingInterpreter(){
        possibleCommands = new ArrayList<>(Collections.singletonList("initialise_leaderCards"));

    }

    //return "{ \"message\" : \"\", \"possibleCommands\" : " + possibleCommands + "}";

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
        ResponseToClient response = new ResponseToClient();
        if (!possibleCommands.contains(command.cmd)){
            return buildResponse("this command is not available in this phase of the game");
            //response.message = "this command is not available in this phase of the game";
            //response.possibleCommands = possibleCommands;
            //return response;
        }
            //return "{ \"message\" : \"this command is not available in this phase of the game\", \"possibleCommands\" : " + possibleCommands + "}";
        switch (command.cmd){
            case "initialise_leaderCards":
                if( (command.firstCard < 1 || command.firstCard > 4) ||
                        (command.secondCard < 1 || command.secondCard > 4)){
                    return buildResponse("one of the cards index is not between 1 and 4");
                    //response.message = "one of the cards index is not between 1 and 4";
                    //response.possibleCommands = possibleCommands;
                    //return response;
                }
                    //return "{ \"message\" : \"one of the cards index is not between 1 and 4\", \"possibleCommands\" : " + possibleCommands + "}";
                if(command.firstCard == command.secondCard){
                    return buildResponse("the two cards indexes are equals");
                    //response.message = "the two cards indexes are equals";
                    //response.possibleCommands = possibleCommands;
                    //return response;
                }
                    //return "{ \"message\" : \"the two cards indexes are equals\", \"possibleCommands\" : " + possibleCommands + "}";
                possibleCommands.remove("initialise_leaderCards");
                possibleCommands.add("initialise_resources");
                card1 = command.firstCard;
                card2 = command.secondCard;
                return buildResponse("ok, choose your resources");
                //response.message = "ok, choose your resources";
                //response.possibleCommands = possibleCommands;
                //return response;
                //return "{ \"message\" : \"ok, choose your resources\", \"possibleCommands\" : " + possibleCommands + "}";

            case "initialise_resources":
                CollectionResources toVerify = new CollectionResources();
                toVerify.add(command.firstResource);
                toVerify.add(command.secondResource);
                if (!ClientHandler.getGame().checkInitialising(handler.getNickname(), toVerify))
                    return buildResponse("you have chosen too much or not anymore resources");
                    //return "{ \"message\" : \"you have chosen too much or not anymore resources\", \"possibleCommands\" : " + possibleCommands + "}";
                possibleCommands.remove("initialise_resources");
                ClientHandler.getGame().initialiseGame(handler.getNickname(), toVerify, card1, card2);
                return buildResponse("ok, now wait that everyone initialise his game and the game will start");
                //return "{ \"message\" : \"ok, now wait that everyone initialise his game and the game will start\", \"possibleCommands\" : " + possibleCommands + "}";

            default:
                return buildResponse("this command is not available in this phase of the game");
                //return "{ \"message\" : \"this command is not available in this phase of the game\", \"possibleCommands\" : " + possibleCommands + "}";

        }
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
