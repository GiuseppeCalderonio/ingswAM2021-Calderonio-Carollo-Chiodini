package it.polimi.ingsw.controller.commands.normalCommands.productionCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.commands.normalCommands.NormalActionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * this class represent the production command.
 * in particular, it extend the class normal action because
 * this command can be done only during the turn.
 * the productions can be: [basic_production, normal_production, leader_production, end_production],
 * according to the game rules
 */
public class ProductionCommand extends NormalActionCommand {

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "production";
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

        // store the possible commands to not lose information about the state of the turn
        previousPossibleCommands.clear();
        previousPossibleCommands.addAll(possibleCommands);

        // set the possible commands to the productions
        possibleCommands.clear();
        possibleCommands.addAll(getProductions());

        // clear production filter only the production that could be activated, returning true if are none

        // if the client can't activate any kind of production
        if (clearProductions(client.getGame(), possibleCommands)){
            // reset the previous possible commands
            possibleCommands.clear();
            possibleCommands.addAll(previousPossibleCommands);
            return buildResponse("you can't activate any production, choose another action",
                    possibleCommands);
        }

        return buildResponse("write the production type that you want activate",
                client.getInterpreter().getPossibleCommands());
    }

    /**
     * this method filter only the productions that can be activated according with
     * the method game.checkProduction(int position), eventually returning true when
     * any production can be activated
     * @param game this is the game
     * @param possibleCommands these are the possible commands in this phase of game
     * @return true if any production can be activated, false otherwise
     */
    protected boolean clearProductions(Game game, List<String> possibleCommands){
        if (!game.checkProduction(0))
            possibleCommands.remove("basic_production");

        if (!game.checkProduction(1) && !game.checkProduction(2) && !game.checkProduction(3))
            possibleCommands.remove("normal_production");

        if (!game.checkProduction(4) && !game.checkProduction(5))
            possibleCommands.remove("leader_production");
        // if the possible command is only end_production
        return possibleCommands.equals(new ArrayList<>(Collections.singletonList("end_production")));
    }

    /**
     * this method get the possible productions, counting the end production
     * @return the possible productions
     */
    protected List<String> getProductions(){
        return new ArrayList<>(
                Arrays.asList("basic_production",
                        "normal_production" ,
                        "leader_production" ,
                        "end_production"));
    }
}
