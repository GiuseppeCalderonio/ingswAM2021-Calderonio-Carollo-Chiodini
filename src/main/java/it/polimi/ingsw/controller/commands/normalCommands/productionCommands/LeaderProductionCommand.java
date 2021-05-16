package it.polimi.ingsw.controller.commands.normalCommands.productionCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.ProductionResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.List;

/**
 * this class represent the leader production command.
 * in particular, if a client activate a leader production,
 * he have to decide what production from the two possibles,
 * and how to pay the resources [warehouse/strongbox],
 * finally it adds a faith point to the player; this command do it
 */
public class LeaderProductionCommand extends ProductionCommand {

    /**
     * this attribute represent the leader production to activate [1/2]
     */
    private final int position;

    /**
     * this attribute represent if the player want to pay the input cost
     * of the production from the warehouse
     */
    private final boolean fromWarehouse;

    /**
     * this attribute represent the resource to gain as output
     */
    private final Resource output;

    /**
     * this constructor create the command setting the position of the leader production,
     * if pay the input cost from the warehouse, and the output resource to gain
     * @param position this is the position of the leader production
     * @param fromWarehouse this represent if pay the input cost from the warehouse
     * @param output this is the output resource to gain
     */
    public LeaderProductionCommand(int position, boolean fromWarehouse, Resource output){
        this.position = position;
        this.fromWarehouse = fromWarehouse;
        this.output = output;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.LEADER_PRODUCTION;
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
        if (!game.checkProduction(position + 3))
            return errorMessage();
        if (!game.checkActivateLeaderProduction(position, fromWarehouse))
            return errorMessage();
        // activate the production
        game.activateLeaderProduction(position, output, fromWarehouse);
        // filter the possible production after the internal state change
        clearProductions(game, possibleCommands);
        // send to every player the new game state
        client.sendInBroadcast(new ProductionResponse(client));

        return acceptedMessage();
    }
}
