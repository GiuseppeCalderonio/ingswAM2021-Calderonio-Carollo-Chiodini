package it.polimi.ingsw.controller.commands.normalCommands.productionCommands;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.ProductionResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.List;

/**
 * this command represent the basic production command.
 * in particular, it can be done only once during the turn, and
 * the player decide 2 resources in input to pay, and get one resource
 * in output to add in the strongbox
 */
public class BasicProductionCommand extends ProductionCommand {

    /**
     * this attribute represent the collection of resources to pay from warehouse
     */
    private final CollectionResources toPayFromWarehouse;

    /**
     * this attribute represent the collection of resources to pay from strongbox
     */
    private final CollectionResources toPayFromStrongbox;

    /**
     * this attribute represent the resource to gain in output
     */
    private final Resource output;

    /**
     * this command create the class setting the resources to pay from the strongbox,
     * the resources to pay from the warehouse, and the resource to gain as output
     * @param toPayFromWarehouse these are the resources to pay from the strongbox
     * @param toPayFromStrongbox these are the resources to pay from the warehouse
     * @param output this is the resource to gain as output
     */
    public BasicProductionCommand(CollectionResources toPayFromWarehouse, CollectionResources toPayFromStrongbox, Resource output){
        this.toPayFromStrongbox = toPayFromStrongbox;
        this.toPayFromWarehouse = toPayFromWarehouse;
        this.output = output;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.BASIC_PRODUCTION;
    }

    /**
     * this method execute the command given in input,
     * returning a message that will be sent to the client.
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     *
     * @param possibleCommands         these are the possible commands to eventually modify
     * @param client                   this is the handler to notify in case of
     *                                 a internal state change
     * @param previousPossibleCommands these are the possible commands referred to the previous
     *                                 command, that eventually may be modified
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(List<CommandName> possibleCommands, ClientHandler client, List<CommandName> previousPossibleCommands) {

        Game game = client.getGame();

        // if the production can't be activated
        if (!game.checkProduction(0))
            return errorMessage();
        if (!game.checkActivateBasicProduction(toPayFromWarehouse, toPayFromStrongbox, output))
            return errorMessage();
        // activate the basic production
        game.activateBasicProduction(toPayFromWarehouse, toPayFromStrongbox, output);
        // filter the possible production after the internal state change
        clearProductions(game, possibleCommands);
        // remove the basic production from the possible commands
        possibleCommands.remove(CommandName.BASIC_PRODUCTION);
        // send to every player the new game state
        client.sendInBroadcast(new ProductionResponse(client));
        //sendBroadcastChangePlayerState(client.getClients());

        return acceptedMessage();
    }


}
