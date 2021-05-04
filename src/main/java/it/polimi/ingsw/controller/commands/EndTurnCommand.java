package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.List;

import static it.polimi.ingsw.controller.ResponseToClient.endSingleGame;

public class EndTurnCommand implements Command {

    private int endTurnCode;

    /*public EndTurnCommand(){
        super("end_turn");
    }

     */

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "end_turn";
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
        // end the turn
        client.getGame().endTurn();
        // remove all the possible commands
        possibleCommands.clear();
        // if the client is playing a single player match
        if (client.getNumberOfPlayers() == 1){
            // send to the client the new game state
            sendEndSingleGame(client.getClients());
            return buildSoloResponse();
        }
        return buildResponse("turn finished, is not your turn now", possibleCommands);
    }

    private ResponseToClient buildSoloResponse() {
        return new ResponseToClient("Lorenzo did his action!");
    }

    private void sendEndSingleGame(List<ClientHandler> clients) {
        clients.forEach(client -> client.send(endSingleGame(client.getGame(), client.getNickname())));
    }
}
