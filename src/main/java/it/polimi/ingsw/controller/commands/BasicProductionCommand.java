package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.List;

public class BasicProductionCommand extends ProductionCommand{

    private final CollectionResources toPayFromWarehouse;

    private final CollectionResources toPayFromStrongbox;

    private final Resource output;


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
    public String getCmd() {
        return "basic_production";
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
        if (!game.checkProduction(0))
            return buildResponse("you selected a position that doesn't exist, or you selected a wrong amount of resources", possibleCommands);
        if (!game.checkActivateBasicProduction(toPayFromWarehouse, toPayFromStrongbox, output))
            return buildResponse("error, one of these things could be the motivation :" +
                    "1) you haven't chosen the right amount of resources (you have to choose 2 resources in input and 1 in output)" +
                    "2) you don't own the chosen resources in your storage", possibleCommands);
        // activate the basic production
        game.activateBasicProduction(toPayFromWarehouse, toPayFromStrongbox, output);
        // filter the possible production after the internal state change
        clearProductions(game, possibleCommands);
        // remove the basic production from the possible commands
        possibleCommands.remove("basic_production");
        // send to every player the new game state
        sendBroadcastChangePlayerState(client.getClients());

        return buildResponse("basic production activated correctly, now choose another one or end the production", possibleCommands);
    }


}
