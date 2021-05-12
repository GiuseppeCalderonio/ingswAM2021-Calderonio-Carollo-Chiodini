package it.polimi.ingsw.controller.commands.leaderCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * this class represent a leader action command.
 * in particular, when a player want to activate or discard a leader card,
 * have to do this command first; it can be done only once during the turn
 */
public class LeaderCommand implements Command {

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "leader_action";
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
    public ResponseToClient executeCommand(List<String> possibleCommands, ClientHandler client, List<String> previousPossibleCommands) throws EndGameException {
        // store the previous possible commands
        previousPossibleCommands.clear();
        previousPossibleCommands.addAll(possibleCommands);
        // set the possible commands to the leader actions
        possibleCommands.clear();
        possibleCommands.addAll(getLeaderActions());
        // return the response
        return buildResponse("choose the leader action to do", client.getInterpreter().getPossibleCommands());
    }

    /**
     * this method get the possible leader actions: [activate_card, discard_card]
     * @return the possible leader actions
     */
    protected List<String> getLeaderActions(){
        return new ArrayList<>(
                Arrays.asList("activate_card",
                        "discard_card"));
    }
}
