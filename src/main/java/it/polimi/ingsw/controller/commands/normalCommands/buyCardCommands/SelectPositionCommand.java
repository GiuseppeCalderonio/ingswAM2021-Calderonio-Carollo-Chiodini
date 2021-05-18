package it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.normalCommands.NormalActionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Game;

import java.util.List;

/**
 * this class represent the select position command.
 * in particular, when a player want to buy a card, after the command
 * buy_card, have to decide the position of the card bought in the dashboard
 */
public class SelectPositionCommand extends NormalActionCommand {

    /**
     * this attribute represent the position of the card to buy, it should be from 1 to 3
     */
    private final int dashboardPosition;

    /**
     * this constructor create the command setting the position in which place the card
     * @param dashboardPosition this is the position in which place the card
     */
    public SelectPositionCommand(int dashboardPosition){
        this.dashboardPosition = dashboardPosition;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.SELECT_POSITION;
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
        return "you can't locate the card in this position";
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
        return "decide how to pay the resources";
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

        Game game = client.getGame();
        DevelopmentCard card = getCard(client.getInterpreter(), game);
        // if card previously selected can't be placed in the space selected
        if (!game.checkPlacement(card, dashboardPosition)) {
            // reset the possible commands
            possibleCommands.clear();
            possibleCommands.addAll(previousPossibleCommands);
            //possibleCommands = previousPossibleCommands;
            return errorMessage();
        }
        // set the dashboard position in which place the card
        client.getInterpreter().setDashboardPosition(dashboardPosition);
        // set the new possible command
        possibleCommands.clear();
        possibleCommands.add(CommandName.SELECT_RESOURCES_FROM_WAREHOUSE);

        return acceptedMessage();
    }

}
