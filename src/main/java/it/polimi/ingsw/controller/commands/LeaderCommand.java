package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.List;

public class LeaderCommand implements Command {

    private int leaderCommandCode;



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

        return buildResponse("choose the leader action to do", client.getInterpreter().getPossibleCommands());
    }
}
