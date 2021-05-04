package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
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
        //super("initialise_resources");
        this.initialisingResources = initialisingResources;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "initialise_resources";
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
        Game game = client.getGame();
        int firstCard = client.getInterpreter().getFirstLeaderCard();
        int secondCard = client.getInterpreter().getSecondLeaderCard();
        // if the resources chosen are not compatible with the player position
        if (!game.checkInitialising(client.getNickname(), initialisingResources))
            return buildResponse("you have chosen too much or not anymore resources", possibleCommands);

        possibleCommands.remove("initialise_resources");
        client.getInterpreter().setPossibleCommands(possibleCommands);
        game.initialiseGame(client.getNickname(), initialisingResources, firstCard ,secondCard );
        // if the game got created correctly
        return buildResponse("ok, now wait that everyone decides his resources and leader cards, then the game will start", possibleCommands);
    }
}
