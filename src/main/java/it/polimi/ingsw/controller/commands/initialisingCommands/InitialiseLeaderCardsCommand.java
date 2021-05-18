package it.polimi.ingsw.controller.commands.initialisingCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;

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
    public CommandName getCmd() {
        return CommandName.INITIALISE_LEADER_CARDS;
    }

    /**
     * this method return a string representing the error message
     * associated with the command
     *
     * @return a string representing the error message
     * associated with the command
     */
    @Override
    public String getErrorMessage() {
        return "initialising failed";
    }

    /**
     * this method return a string representing the confirm message
     * associated with the command
     *
     * @return a string representing the confirm message
     * associated with the command
     */
    @Override
    public String getConfirmMessage() {
        return "leader cards initialised, now initialise your resources";
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
    public ResponseToClient executeCommand(List<CommandName> possibleCommands, ClientHandler client, List<CommandName> previousPossibleCommands){
        // if the index of the cards is not between 1 and 4
        if( (firstCard < 1 || firstCard > 4) ||
                (secondCard < 1 || secondCard > 4))
            return errorMessage();
        // if the indexes are equals
        if( firstCard == secondCard)
            return errorMessage();
        // store the first leader card in a buffer
        client.getInterpreter().setFirstLeaderCard(firstCard);
        // store the second leader card in a buffer
        client.getInterpreter().setSecondLeaderCard(secondCard);
        // remove the actual command from the possible ones
        possibleCommands.remove(CommandName.INITIALISE_LEADER_CARDS);
        // add the next command to the possible ones
        possibleCommands.add(CommandName.INITIALISE_RESOURCES);

        return acceptedMessage();
    }
}
