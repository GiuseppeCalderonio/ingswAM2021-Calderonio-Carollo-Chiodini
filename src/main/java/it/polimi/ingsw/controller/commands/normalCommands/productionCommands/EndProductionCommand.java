package it.polimi.ingsw.controller.commands.normalCommands.productionCommands;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.EndProductionResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.PlayerAndComponents.ProductionPower;

import java.util.ArrayList;
import java.util.List;

/**
 * this class represent the end production command.
 * in particular, this command has 3 possible behaviors:
 * (1) if the client didn't activate any production restore the possible commands (rollback);
 * (2) if the client activated one or more productions, it fill the strongbox with the buffer,
 * and reset every production, so that the next turn tem will be available
 */
public class EndProductionCommand extends ProductionCommand {

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.END_PRODUCTION;
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
        return "production ended";
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
        return "error, you can't end the production";
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
        possibleCommands.clear();
        possibleCommands.addAll(previousPossibleCommands);
        //client.getInterpreter().setPossibleCommands(previousPossibleCommands);
        // if the player didn't activate any kind of production
        if (anyProductionGotActivated(client.getGame().getActualPlayer().getPersonalDashboard().getPersonalProductionPower()))
            return acceptedMessage();

        // end the production filling the strongbox with all the resources gained and reset the productions
        client.getGame().endProduction();
        // send the end production broadcast for view the new strongbox
        client.sendInBroadcast(new EndProductionResponse(client));
        // remove all the normal actions from the possible commands
        possibleCommands.removeAll(getNormalActions());
        // add the end turn to the possible commands
        possibleCommands.add(CommandName.END_TURN);
        // set the new possible commands
        return acceptedMessage();
    }

    /**
     * this method verify if any production has been activated.
     * in particular, it get all the productions from the production power
     * and store into a list of Booleans all the values of them, then return true
     * if and only if all the elements of this list are false
     * @param productionPower this is the production power to check
     * @return true if any production have been activated, false otherwise
     */
    private boolean anyProductionGotActivated(ProductionPower productionPower){
        // create a new arraylist of booleans
        List<Boolean> activatedProductions = new ArrayList<>();
        // add to it the value of the basic production
        activatedProductions.add(productionPower.isBasicProductionActivated());
        // add to it the value of the normal productions
        for (int i = 1; i <= 3; i++) {
            activatedProductions.add(productionPower.isProductionActivated(i));
        }
        // add to it the value of the leader productions
        for (int i = 1; i <= 2; i++) {
            activatedProductions.add(productionPower.isLeaderProductionActivated(i));
        }

        // return true when any production got activated
        return activatedProductions.stream().noneMatch(production -> production);

    }
}
