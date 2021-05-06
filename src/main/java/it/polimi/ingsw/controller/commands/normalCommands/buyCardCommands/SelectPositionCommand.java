package it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.commands.normalCommands.NormalActionCommand;
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
    public String getCmd() {
        return "select_position";
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
    public ResponseToClient executeCommand(List<String> possibleCommands, ClientHandler client, List<String> previousPossibleCommands) {

        Game game = client.getGame();
        DevelopmentCard card = getCard(client.getInterpreter(), game);
        // if card previously selected can't be placed in the space selected
        if (!game.checkPlacement(card, dashboardPosition)) {
            // reset the possible commands
            possibleCommands.clear();
            possibleCommands.addAll(previousPossibleCommands);
            //possibleCommands = previousPossibleCommands;
            return buildResponse("error, one of these things could be the motivation :" +
                    "1) the position selected is not between 1 and 3" +
                    "2) the position selected does not allow the card placement", possibleCommands);
        }
        // set the dashboard position in which place the card
        client.getInterpreter().setDashboardPosition(dashboardPosition);
        // set the new possible command
        possibleCommands.clear();
        possibleCommands.add("select_resources_from_warehouse");

        return buildResponse("now select the warehouse resources to pay the card", possibleCommands);
    }

}
