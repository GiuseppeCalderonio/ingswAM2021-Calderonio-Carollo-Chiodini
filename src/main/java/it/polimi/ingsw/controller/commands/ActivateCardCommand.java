package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.List;

public class ActivateCardCommand extends LeaderCommand{

    private final int toActivate;


    public ActivateCardCommand(int toActivate){
        //super("activate_card");
        this.toActivate = toActivate;
    }



    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "activate_card";
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
        // if the card doesn't exist
        if (!client.getGame().checkLeaderCard(toActivate))
            return buildResponse("the leader cards selected does not exist", possibleCommands);

        // if the card can't be activated
        if (!client.getGame().activateLeaderCard(toActivate))
            return buildResponse("the leader card is already active or you don't meet the requirements to activate it", possibleCommands);

        // reset the possible commands
        possibleCommands.remove("leader_action");
        sendBroadcastChangePlayerState(client.getClients());
        return buildResponse("leader card activated", possibleCommands);
    }
}
