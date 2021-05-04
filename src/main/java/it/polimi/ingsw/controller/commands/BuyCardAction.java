package it.polimi.ingsw.controller.commands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.Game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BuyCardAction implements Command {

    private final CardColor color;

    private final int level;

    public BuyCardAction(CardColor color, int level){
        //super("buy_card");
        this.color = color;
        this.level = level;
    }

    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public String getCmd() {
        return "buy_card";
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

        // if the client can't buy the specified card
        if (!game.checkBuyCard(level, color))
            return buildResponse("error, one of these things could be the motivation :" +
                    "(1) you have selected a level not between 1 and 3, " +
                    "(2) you have selected a color that doesn't exist, " +
                    "(3) you have selected an empty deck of cards, " +
                    "(4) you can't buy the card because you can't afford it, " +
                    "(5) you can't place the card selected into the dashboard", possibleCommands);
        // set the color of the card into a buffer
        client.getCommandManager().getCommandInterpreter().setColor(color);
        //color = command.color;
        // set the level of the card into a buffer
        client.getCommandManager().getCommandInterpreter().setLevel(level);
        //level = command.level;

        // set the new possible commands
        previousPossibleCommands.clear();
        previousPossibleCommands.addAll(possibleCommands);
        possibleCommands.clear();
        possibleCommands.add("select_position");
        return buildResponse("deck selected is available, now decide where you want to place the card in your dashboard", possibleCommands);
    }
}
