package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.PlayerAndComponents.ProductionPower;

import java.util.ArrayList;
import java.util.List;

public class EndProductionCommand extends ProductionCommand {

    private int endProductionCode;


    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "end_production";
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
        possibleCommands.clear();
        possibleCommands.addAll(previousPossibleCommands);
        //client.getInterpreter().setPossibleCommands(previousPossibleCommands);
        // if the player didn't activate any kind of production
        if (anyProductionGotActivated(client.getGame().getActualPlayer().getPersonalDashboard().getPersonalProductionPower()))
            return buildResponse("you didn't activate any production", possibleCommands);

        // end the production filling the strongbox with all the resources gained and reset the productions
        client.getGame().endProduction();
        // remove all the normal actions from the possible commands
        possibleCommands.removeAll(getNormalActions());
        // add the end turn to the possible commands
        possibleCommands.add("end_turn");
        // set the new possible commands
        return buildResponse("the production is finished", possibleCommands);
    }

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
