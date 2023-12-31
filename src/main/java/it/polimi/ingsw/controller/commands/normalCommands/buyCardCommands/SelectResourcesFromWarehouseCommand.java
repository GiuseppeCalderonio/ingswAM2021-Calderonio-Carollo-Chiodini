package it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.normalCommands.NormalActionCommand;
import it.polimi.ingsw.controller.responseToClients.BuyCardActionResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.CollectionResources;

import java.util.List;

/**
 * this class represent the select resources from warehouse command.
 * in particular, once that the player decides the position in which
 * place the card to buy, he have to decide how to pay the card, and
 * this command do this, selecting the resources to pay from the warehouse,
 * inferring the resources to pay from the strongbox
 */
public class SelectResourcesFromWarehouseCommand extends NormalActionCommand {

    /**
     * this attribute represent the resources that the player want to use
     * in order to buy the card
     *
     */
    private final CollectionResources toPayFromWarehouse;

    /**
     * this constructor create the command starting from the resources to pay from the warehouse
     * @param toPayFromWarehouse these are the resources to pay from the warehouse
     */
    public SelectResourcesFromWarehouseCommand(CollectionResources toPayFromWarehouse){
        this.toPayFromWarehouse = toPayFromWarehouse;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.SELECT_RESOURCES_FROM_WAREHOUSE;
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
        return "the resources selected doesn't cover the cost of the card";
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
        return "card bought";
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

        possibleCommands.clear();
        possibleCommands.addAll(previousPossibleCommands);
        // if the warehouse resources to buy the card are not compatible with the
        // resources storage state of the player
        if (!game.checkWarehouseResources(card, toPayFromWarehouse))
            return errorMessage();

        // buy the card with the resources chosen
        game.buyCard(card.getLevel(),
                card.getColor(),
                client.getInterpreter().getDashboardPosition(),
                toPayFromWarehouse);
        // delete any normal action from the possible commands
        possibleCommands.removeAll(getNormalActions());
        // add end turn to the possible commands
        possibleCommands.add(CommandName.END_TURN);
        // send to every player the new game state
        client.sendInBroadcast(new BuyCardActionResponse(client,
                game.getSetOfCard().getCard(card.getLevel(), card.getColor()),
                card.getLevel(),
                card.getColor()));
        return acceptedMessage();
    }
}
