package it.polimi.ingsw.controller.commands.normalCommands.productionCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.ProductionResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.CollectionResources;

import java.util.List;

/**
 * this class represent the normal production command.
 * in particular, if the player own a card, it can activate its
 * production power, paying some resources from the warehouse,
 * and the other resources (cost input - warehouse) from the strongbox;
 * this command do it
 */
public class NormalProductionCommand extends ProductionCommand {

    /**
     * this attribute represent the position of the card to activate
     */
    private final int position;

    /**
     * this attribute represent the resources to pay from the warehouse
     */
    private final CollectionResources toPayFromWarehouse;

    /**
     * this constructor create the command setting the position of the card to activate and
     * the resources to pay from warehouse in order to activate the card
     * @param position this is the position of the card to activate
     * @param toPayFromWarehouse these are the resources to pay from warehouse in order to activate the card
     */
    public NormalProductionCommand(int position, CollectionResources toPayFromWarehouse){
        this.position = position;
        this.toPayFromWarehouse = toPayFromWarehouse;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.NORMAL_PRODUCTION;
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
        if (!game.checkProduction(position))
            return errorMessage();

        if (!game.checkActivateProduction(position, toPayFromWarehouse))
            return errorMessage();
        // activate the production
        game.activateProduction(position, toPayFromWarehouse);
        // filter the possible production after the internal state change
        clearProductions(game, possibleCommands);
        // send to every player the new game state
        client.sendInBroadcast(new ProductionResponse(client));
        //sendBroadcastChangePlayerState(client.getClients()); // code 3
        return acceptedMessage();
    }
}
