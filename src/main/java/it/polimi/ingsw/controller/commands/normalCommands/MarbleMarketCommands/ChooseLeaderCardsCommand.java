package it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.normalCommands.NormalActionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.WhiteMarble;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class represent the choose leader cards command.
 * in particular, when a player choose some marbles from the market
 * and own 2 leader cards of type white marble conversion, he have to
 * decide for each white marble gained how to convert it; this command do this
 */
public class ChooseLeaderCardsCommand extends NormalActionCommand {

    /**
     * this attribute represent an array of integers that represents for each
     * white marbles the index of the leader card to use in order to convert the white marble
     */
    private final int[] indexes;

    /**
     * this constructor create the command starting from the indexes of the leader cards
     * @param indexes these are the indexes of the leader cards
     */
    public ChooseLeaderCardsCommand(int[] indexes){
        this.indexes = indexes;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.CHOOSE_LEADER_CARDS;
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
        return "you can't use this leader card";
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
        return "ok, now decide how to place the resources in the warehouse";
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
    public ResponseToClient executeCommand(List<CommandName> possibleCommands, ClientHandler client, List<CommandName> previousPossibleCommands) {
        List<Marble> marbles = client.getInterpreter().getMarbles();
        Game game = client.getGame();
        // create a list with the white marbles contained into the marbles previously selected
        List<Marble> whiteMarbles = marbles.stream().filter(marble -> marble.equals(new WhiteMarble())).collect(Collectors.toList());
        // if the white marbles length don't match the indexes length
        if (whiteMarbles.size() != indexes.length)
            return errorMessage();
        // if one of the indexes selected is not between 1 and 2
        if (!Arrays.stream(indexes).allMatch(index -> index == 1 || index == 2))
            return errorMessage();

        for (int i = 0; i < whiteMarbles.size(); i++) {
            game.changeWhiteMarble(marbles, indexes[i]);
        }
        // change the state and send to the player that he have to select the marbles converted in his warehouse
        return buildResponseToInsertInWarehouse(client.getInterpreter(), game, marbles);
    }


}
