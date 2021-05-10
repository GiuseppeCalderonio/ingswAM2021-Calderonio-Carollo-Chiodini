package it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.commands.normalCommands.NormalActionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.Arrays;
import java.util.List;

/**
 * this class represent the insert in warehouse command.
 * in particular, when a player do the choose_marbles action,
 * he have also to decide how to place the resources gained in the warehouse,
 * this command do it
 */
public class InsertInWarehouseCommand extends NormalActionCommand {

    /**
     * this attribute represent the indexes of the shelves in which the player want to
     * place the resources gained from the selection.
     * in particular, will be stored in the command interpreter buffer
     * the set of resources, and for the first resource,all of them will be
     * placed in the shelves[1] shelf, and so on....
     */
    private final int[] shelves;

    /**
     * this constructor create the command setting the shelves in which place the resources gained
     * @param shelves these are the shelves in which place the resources gained
     */
    public InsertInWarehouseCommand(int[] shelves){
        this.shelves = shelves;
    }


    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "insert_in_warehouse";
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
        List<Resource> resourcesSet = client.getInterpreter().getResourcesSet();
        CollectionResources marblesConverted = client.getInterpreter().getMarblesConverted();
        // if one of the shelves selected does not exist
        if (!Arrays.stream(shelves).allMatch(game::checkShelfSelected))
            return buildResponse("one of the shelf selected does not exist", possibleCommands);
        if (resourcesSet.size() != shelves.length)
            return buildResponse("you have selected too much or not anymore shelves", possibleCommands);
        // insert in warehouse all the resources selected in all the shelves selected
        for (int i = 0; i < resourcesSet.size(); i++) {
            game.insertInWarehouse(shelves[i], resourcesSet.get(i), marblesConverted);
        }

        // restore the previous command and delete from them all the normal actions

        possibleCommands.clear();
        possibleCommands.addAll(previousPossibleCommands);
        possibleCommands.removeAll(getNormalActions());
        possibleCommands.add("end_turn");

        // send to every player the new game state
        sendBroadcastMarbleAction(client);
        return buildResponse("action completed, resources got added",
                client.getInterpreter().getPossibleCommands());
    }
}
