package it.polimi.ingsw.controller.commands.normalCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.ShiftResourcesResponse;
import it.polimi.ingsw.model.Game;

import java.util.List;

/**
 * this class represent the shift command.
 * in particular, during the turn, a player can shift the resources into his warehouse
 * in every way that he wants when the shift his limited to the normal shelves,
 * can shift only resources with compatible shelves moving one resource at time
 * when one of the shelves selected is a leader shelf
 */
public class ShiftResourcesCommand extends NormalActionCommand {

    /**
     * this attribute represent the first shelf in which the player want to do the shift
     */
    private final int source;

    /**
     * this attribute represent the second shelf in which the player want to do the shift
     */
    private final int destination;

    /**
     * this constructor crete the object starting from the two shelves to shift
     * @param source this int represent the first shelf to shift
     * @param destination this int represent the second shelf to shift
     */
    public ShiftResourcesCommand(int source, int destination){
        this.source = source;
        this.destination = destination;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "shift_resources";
    }

    /**
     * this method execute the command given in input,
     * returning a message that will be sent to the client.
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     * @param possibleCommands these are the possible commands to eventually modify
     * @param client this is the handler to notify in case of
     *                a internal state change
     * @param previousPossibleCommands these are the possible commands referred to the previous
     *                                 command, that eventually may be modified
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(List<String> possibleCommands, ClientHandler client, List<String> previousPossibleCommands) {
        // if on of the shelves selected doesn't exist
        Game game = client.getGame();
        if (!(game.checkShelfSelected(source) &&
                game.checkShelfSelected(destination)))
            return buildResponse("error, one of the shelf selected does not exist", possibleCommands);
        // if a shift can't be done
        if (!(game.shiftResources(source, destination)))
            return buildResponse("error, you can't do this kind of shift", possibleCommands);
        // send to every player the new game state
        client.sendInBroadcast(new ShiftResourcesResponse(client));
        //sendBroadcastChangePlayerState(client.getClients());
        // send that string to the client
        return buildResponse("ok, shift done correctly", possibleCommands);
    }
}
