package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;

import java.util.List;

/**
 * this class represent the command relative to the initialising phase.
 * in particular, when the game start, every player have to decide the
 * leader cards to use for the rest of it
 */
public class InitialiseLeaderCardsCommand implements Command {

    /**
     * this attribute represent the index of the first leader card to discard for the initialisation
     */
    private final int firstCard;
    /**
     * this attribute represent the index of the first leader card to discard for the initialisation
     */
    private final int secondCard;

    /**
     * this constructor create a initialising command starting from the leader cards to discard
     * @param firstCard this is the first card to discard
     * @param secondCard this is the second card to discard
     */
    public InitialiseLeaderCardsCommand(int firstCard, int secondCard){
        this.firstCard = firstCard;
        this.secondCard = secondCard;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "initialise_leaderCards";
    }

    /**
     * this method execute the command given in input,
     * returning a message that will be sent to the client.
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     * @param possibleCommands these are the possible commands to eventually modify
     * @param client this is the handler to notify in case of
     *                a internal state change
     * @param previousPossibleCommands these are the possible commands referred to the previous
     *                                 command, that eventually may be modified
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(List<String> possibleCommands, ClientHandler client, List<String> previousPossibleCommands){
        if (!possibleCommands.contains(getCmd()))
            return buildResponse("this command is not available in this phase of the game", possibleCommands);

        if( (firstCard < 1 || firstCard > 4) ||
                (secondCard < 1 || secondCard > 4))
            return buildResponse("one of the cards index is not between 1 and 4", possibleCommands);

        if(firstCard == secondCard)
            return buildResponse("the two cards indexes are equals", possibleCommands);

        client.getInterpreter().setFirstLeaderCard(firstCard);
        client.getInterpreter().setSecondLeaderCard(secondCard);

        possibleCommands.remove("initialise_leaderCards");
        possibleCommands.add("initialise_resources");

        client.getInterpreter().setPossibleCommands(possibleCommands);

        return buildResponse("ok, choose your resources", possibleCommands);
    }
}
