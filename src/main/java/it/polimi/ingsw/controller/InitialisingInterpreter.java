package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Resources.CollectionResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InitialisingInterpreter implements CommandInterpreter{

    private final List<String> possibleCommands;

    private int card1, card2;

    public InitialisingInterpreter(){
        possibleCommands = new ArrayList<>(Collections.singletonList("initialise_leaderCards"));

    }


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
        if (!possibleCommands.contains(command.cmd))
            return buildResponse("this command is not available in this phase of the game");

        switch (command.cmd){
            case "initialise_leaderCards":

                if( (command.firstCard < 1 || command.firstCard > 4) ||
                        (command.secondCard < 1 || command.secondCard > 4))
                    return buildResponse("one of the cards index is not between 1 and 4");

                if(command.firstCard == command.secondCard)
                    return buildResponse("the two cards indexes are equals");


                possibleCommands.remove("initialise_leaderCards");
                possibleCommands.add("initialise_resources");
                card1 = command.firstCard;
                card2 = command.secondCard;
                return buildResponse("ok, choose your resources");

            case "initialise_resources":
                CollectionResources toVerify = new CollectionResources();
                toVerify.add(command.firstResource);
                toVerify.add(command.secondResource);
                // if the resources chosen are not compatible with the player position
                if (!ClientHandler.getGame().checkInitialising(handler.getNickname(), toVerify))
                    return buildResponse("you have chosen too much or not anymore resources");

                possibleCommands.remove("initialise_resources");
                ClientHandler.getGame().initialiseGame(handler.getNickname(), toVerify, card1, card2);
                // if the game got created correctly
                return buildResponse("ok, now wait that everyone decides his resources and leader cards, then the game will start");
            // in theory unreachable statement, but it can help if something does not work correctly
            default:
                return buildResponse("this command is not available in this phase of the game");

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
