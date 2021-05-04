package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.CommandInterpreter;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SelectPositionCommand implements Command {

    private final int dashboardPosition;

    public SelectPositionCommand(int dashboardPosition){
        //super("select_position");
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
        DevelopmentCard card = getCard(client.getCommandManager().getCommandInterpreter(), game);
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

    private DevelopmentCard getCard(CommandInterpreter interpreter, Game game){
        return game.getSetOfCard().getCard(interpreter.getLevel(), interpreter.getColor());
    }
}
