package it.polimi.ingsw.controller.commands.normalCommands.buyCardCommands;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.normalCommands.NormalActionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.Game;

import java.util.List;

/**
 * this class represent the buy card command.
 * in particular, it extend the class normal action command because this command
 * can be done only once during the turn
 */
public class BuyCardAction extends NormalActionCommand {

    /**
     * this attribute represent the color of the card to buy
     */
    private final CardColor color;

    /**
     * this attribute represent the level of the card to buy
     */
    private final int level;

    /**
     * this constructor create the command starting from the color and the level of the card to buy
     * @param color this is the color of the card to buy
     * @param level this is the level of the card to buy
     */
    public BuyCardAction(CardColor color, int level){
        this.color = color;
        this.level = level;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.BUY_CARD;
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
        return "you can' buy this card";
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
        return "select the position in which locate the card";
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

        // if the client can't buy the specified card
        if (!game.checkBuyCard(level, color))
            return errorMessage();
        // set the color of the card into a buffer
        client.getInterpreter().setColor(color);
        //color = command.color;
        // set the level of the card into a buffer
        client.getInterpreter().setLevel(level);
        //level = command.level;

        // set the new possible commands
        previousPossibleCommands.clear();
        previousPossibleCommands.addAll(possibleCommands);
        possibleCommands.clear();
        possibleCommands.add(CommandName.SELECT_POSITION);
        return acceptedMessage();
    }
}
