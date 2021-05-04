package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.CollectionResources;

import java.util.List;

public class NormalProductionCommand extends ProductionCommand{

    private final int position;

    private final CollectionResources toPayFromWarehouse;

    public NormalProductionCommand(int position, CollectionResources toPayFromWarehouse){
        //super("normal_production");
        this.position = position;
        this.toPayFromWarehouse = toPayFromWarehouse;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "normal_production";
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
        if (!game.checkProduction(position))
            return buildResponse("you selected a position that doesn't exist," +
                    " or you already selected this production before, choose another one or end the production", possibleCommands);

        if (!game.checkActivateProduction(position, toPayFromWarehouse))
            return buildResponse("error from check", possibleCommands);
        // activate the production
        game.activateProduction(position, toPayFromWarehouse);
        // filter the possible production after the internal state change
        clearProductions(game, possibleCommands);
        // send to every player the new game state
        sendBroadcastChangePlayerState(client.getClients()); // code 3
        return buildResponse(position + "° normal production activated correctly, now choose another one or end the production", possibleCommands);
    }
}
