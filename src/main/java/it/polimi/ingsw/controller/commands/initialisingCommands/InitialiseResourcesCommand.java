package it.polimi.ingsw.controller.commands.initialisingCommands;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.CollectionResources;

import java.util.List;

/**
 * this class represent the initialising resources command.
 * in particular, during the phase of the initialisation, every player,
 * depending on the inkwell position, will get some resources before starting the game
 */
public class InitialiseResourcesCommand implements Command {

    /**
     * this is the collection of resources that represent the resources to get during this phase of the game
     */
    private final CollectionResources initialisingResources;

    /**
     * this constructor create the object starting from the collection of resources to set and eventually get
     * @param initialisingResources these are the resources chosen from the player to get in the initialising phase
     */
    public InitialiseResourcesCommand(CollectionResources initialisingResources){
        this.initialisingResources = initialisingResources;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.INITIALISE_RESOURCES;
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
        return "initialising of resources failed";
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
        return "resources initialised correctly, if is your turn start, wait otherwise";
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
        // get the game
        Game game = client.getGame();
        // get the first leader card from the buffer
        int firstCard = client.getInterpreter().getFirstLeaderCard();
        // get the second leader card from the buffer
        int secondCard = client.getInterpreter().getSecondLeaderCard();
        // if the resources chosen are not compatible with the player position
        if (!game.checkInitialising(client.getNickname(), initialisingResources))
            return errorMessage();
        // remove from the possible commands the actual one
        possibleCommands.remove(CommandName.INITIALISE_RESOURCES);
        // initialise the player assigning him the resources and discarding the leader cards chosen
        game.initialiseGame(client.getNickname(), initialisingResources, firstCard ,secondCard );
        // if the game got created correctly
        return acceptedMessage();
    }
}
