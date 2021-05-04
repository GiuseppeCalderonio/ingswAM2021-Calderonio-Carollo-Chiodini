package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.List;

public class LeaderProductionCommand extends ProductionCommand{

    private final int position;

    private final boolean fromWarehouse;

    private final Resource output;

    public LeaderProductionCommand(int position, boolean fromWarehouse, Resource output){
        //super("leader_production");
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
    public String getCmd() {
        return "leader_production";
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
    public ResponseToClient executeCommand(List<String> possibleCommands, ClientHandler client, List<String> previousPossibleCommands) {

        Game game = client.getGame();

        // if the production can't be activated
        if (!game.checkProduction(position + 3))
            return buildResponse("error,you don't have enough resources or the production selected doesn't exist," +
                    " or you already selected this production before, choose another one or end the production", possibleCommands);
        if (!game.checkActivateLeaderProduction(position, fromWarehouse))
            return buildResponse("error,you have selected an incorrect number of resources", possibleCommands);
        // activate the production
        game.activateLeaderProduction(position, output, fromWarehouse);
        // filter the possible production after the internal state change
        clearProductions(game, possibleCommands);
        // send to every player the new game state
        sendBroadcastChangePlayerState(client.getClients());
        return buildResponse(position + "° leader production activated correctly, now choose another one or end the production", possibleCommands);
    }
}
