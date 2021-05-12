package it.polimi.ingsw.controller.commands.leaderCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.responseToClients.LeaderActionResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.List;

/**
 * this class represent the discard card command.
 * in particular, when a player want to discard a leader card, will
 * send this command and the server will discard it if the card isn't already active
 * according to the model's state, and eventually adding a faith point to the player
 */
public class DiscardCardCommand extends LeaderCommand {

    /**
     * this attribute represent the index of the leader card to discard
     */
    private final int toDiscard;

    /**
     * this constructor create the object setting the index of the card to eventually discard
     * @param toDiscard this is the index of the card to eventually discard
     */
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
        // reset the previous possible commands
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
        client.sendInBroadcast(new LeaderActionResponse(client));
        // return the response
        return buildResponse("leader card discarded", possibleCommands);
    }
}
