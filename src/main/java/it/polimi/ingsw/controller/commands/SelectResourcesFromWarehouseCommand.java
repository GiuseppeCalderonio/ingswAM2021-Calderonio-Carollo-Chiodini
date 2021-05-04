package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.CommandInterpreter;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Resources.CollectionResources;

import java.util.List;

import static it.polimi.ingsw.controller.ResponseToClient.broadcastCardAction;

public class SelectResourcesFromWarehouseCommand implements Command {

    private final CollectionResources toPayFromWarehouse;

    public SelectResourcesFromWarehouseCommand(CollectionResources toPayFromWarehouse){
        //super("select_resources_from_warehouse");
        this.toPayFromWarehouse = toPayFromWarehouse;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "select_resources_from_warehouse";
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

        possibleCommands.clear();
        possibleCommands.addAll(previousPossibleCommands);
        // if the warehouse resources to buy the card are not compatible with the
        // resources storage state of the player
        if (!game.checkWarehouseResources(card, toPayFromWarehouse))
            return buildResponse("error,you have selected an incorrect number of resources", possibleCommands);

        // buy the card with the resources chosen
        game.buyCard(card.getLevel(),
                card.getColor(),
                client.getInterpreter().getDashboardPosition(),
                toPayFromWarehouse);
        // delete any normal action from the possible commands
        possibleCommands.removeAll(getNormalActions());
        // add end turn to the possible commands
        possibleCommands.add("end_turn");

        // send to every player the new game state
        sendBroadcastCardAction(client.getClients());
        return buildResponse("card bought correctly", possibleCommands);
    }

    private DevelopmentCard getCard(CommandInterpreter interpreter, Game game){
        return game.getSetOfCard().getCard(interpreter.getLevel(), interpreter.getColor());
    }

    private void sendBroadcastCardAction(List<ClientHandler> clients) {
        clients.forEach(client -> client.send(broadcastCardAction(client.getGame(), client.getNickname()) ));
    }
}
