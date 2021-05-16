package it.polimi.ingsw.controller.commands.normalCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.EndTurnSingleGameResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.EndGameException;

import java.util.List;

/**
 * this class represent the command end turn.
 * when the client want to finish his turn, this command will be received
 * and will be called the method endTurn in the game.
 * eventually the method executeCommand could throw the EndGameException
 */
public class EndTurnCommand extends NormalActionCommand {

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.END_TURN;
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
        return "you can't end the turn now";
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
        return "turn finished, is not your turn anymore";
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
    public ResponseToClient executeCommand(List<CommandName> possibleCommands, ClientHandler client, List<CommandName> previousPossibleCommands) throws EndGameException {
        // end the turn
        client.getGame().endTurn();
        // remove all the possible commands
        possibleCommands.clear();
        // if the client is playing a single player match
        if (client.getNumberOfPlayers() == 1){
            // send to the client the new game state
            sendEndSingleGame(client);
        }
        // return the response
        return acceptedMessage();
    }

    /**
     * this method send to the client that is playing the game the new
     * state of the game, caused by the action of the SoloTokens
     * @param client this is the (unique) client to notify
     */
    private void sendEndSingleGame(ClientHandler client) {
        // send the new game state
        client.send(new EndTurnSingleGameResponse(client));
    }
}
