package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InitialisingInterpreter implements CommandInterpreter{

    private final List<String> possibleCommands;
    private int card1, card2;
    private CollectionResources toInitialise;

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
     * @return a code based on the type of action
     */
    @Override
    public String executeCommand(Command command, EchoServerClientHandler handler) {
        if (!possibleCommands.contains(command.cmd)) return "this command is not available in this phase of the game";
        switch (command.cmd){
            case "initialise_leaderCards":
                if( (command.firstCard < 1 || command.firstCard > 4) ||
                        (command.secondCard < 1 || command.secondCard > 4))
                    return "one of the cards index is not between 1 and 4";
                if(command.firstCard == command.secondCard)
                    return "the two cards indexes are equals";
                possibleCommands.remove("initialise_leaderCards");
                possibleCommands.add("initialise_resources");
                card1 = command.firstCard;
                card2 = command.secondCard;
                return "ok, choose your resources";

            case "initialise_resources":
                CollectionResources toVerify = new CollectionResources();
                if (command.firstResource != null)
                    toVerify.add(command.firstResource);
                if (command.secondResource != null)
                    toVerify.add(command.secondResource);
                if (!verifyCompatibility(handler.getNicknames().indexOf(handler.getNickname()), toVerify))
                    return "you have chosen too much or not anymore resources";
                possibleCommands.remove("initialise_resources");
                handler.getGame().initialiseGame(handler.getNickname(), toVerify, card1, card2);
                return "ok, wait for other players to decide and the game will start";

            default:
                return "this command is not available in this phase of the game";

        }
    }

    //position is from 0 to 3
    private boolean verifyCompatibility(int position, CollectionResources toVerify){
        if (position == 0 && toVerify.getSize() == 0) return true;
        if (position == 1 && toVerify.getSize() == 1) return true;
        if (position == 2 && toVerify.getSize() == 1) return true;
        return position == 3 && toVerify.getSize() == 2;
    }

    public List<String> getPossibleCommands() {
        return possibleCommands;
    }
}
