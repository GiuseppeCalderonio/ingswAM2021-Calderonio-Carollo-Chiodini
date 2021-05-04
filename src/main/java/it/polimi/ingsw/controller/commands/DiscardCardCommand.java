package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.List;

public class DiscardCardCommand extends LeaderCommand{

    private final int toDiscard;

    public DiscardCardCommand(int toDiscard){
        this.toDiscard = toDiscard;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "discard_card";
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

        possibleCommands.clear();
        possibleCommands.addAll(previousPossibleCommands);

        // if the card selected doesn't exist
        if (!client.getGame().checkLeaderCard(toDiscard))
            return buildResponse("the leader cards selected does not exist", possibleCommands);

        // if the card can't be discarded
        if (!client.getGame().discardLeaderCard(toDiscard))
            return buildResponse("the leader card is already active and you can't discard it", possibleCommands);

        // remove leader actions from possible commands
        possibleCommands.remove("leader_action");
        // send to every player the new game state
        sendBroadcastChangePlayerState(client.getClients());
        return buildResponse("leader card discarded", possibleCommands);
    }
}
