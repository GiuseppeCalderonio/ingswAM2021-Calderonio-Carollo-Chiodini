package it.polimi.ingsw.controller.commands.normalCommands;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.CommandInterpreter;
import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.MarbleActionResponse;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.WhiteMarblesConversionResponse;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Marble.Marble;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this abstract class represent the normal action of a player.
 * in particular, it will be extended from all the classes that represent
 * a command executable only once during the turn [choose marbles, buy card, production],
 * it contains all the methods to send in broadcast some game changes
 */
public abstract class NormalActionCommand implements Command {

    /**
     * this method get a list containing all the normal actions of the game
     * @return a list containing all the normal actions of the game
     */
    public List<String> getNormalActions(){
        return new ArrayList<>(
                Arrays.asList("choose_marbles",
                        "production",
                        "buy_card"));
    }

    /**
     * this method send for each client the changes to the game
     * when a player do a choose_marbles action: the marble market, and the new state of every player
     * @param client this is the client that notify everyone of the change
     */
    protected void sendBroadcastMarbleAction(ClientHandler client) {
        // send in broadcast the new game state
        client.sendInBroadcast(new MarbleActionResponse(client));
    }

    /**
     * this method send to the client that chose the choose_marbles action
     * a response containing the resources gained from the selection.
     * in particular, it convert the marbles passed in input into the collectionResources
     * correspondent and store it into a buffer, then create the set of resources
     * starting from the collection gained
     * it also set into a buffer all the resources gained from the selection
     * and change the possible commands to [insert_in_warehouse] (the name of the method)
     *
     * @param interpreter this is the command interpreter containing all the buffers
     * @param game this is the game in which the method have to call the convert
     * @param marbles these are the marbles to convert
     *
     * @return the response to send to the client
     */
    protected ResponseToClient buildResponseToInsertInWarehouse(CommandInterpreter interpreter,
                                                                      Game game,
                                                                      List<Marble> marbles){
        interpreter.getPossibleCommands().clear();
        interpreter.getPossibleCommands().add("insert_in_warehouse");
        interpreter.setMarblesConverted(game.convert(marbles));
        interpreter.setResourceSet(new ArrayList<>(new HashSet<>(interpreter.getMarblesConverted().asList())));
        //interpreter.setResourceSet(interpreter.getMarblesConverted().asList().stream().distinct().collect(Collectors.toList()));
        return new WhiteMarblesConversionResponse(interpreter.getMarblesConverted());
    }

    /**
     * this method get the card from the cards market with the level and the color
     * that are stored in the interpreter passed as parameter.
     * it requires that the card exist and the level and the color have been previously set
     * @param interpreter this is the interpreter that contains the level and the color to get
     * @param game this is the game in which get the cards market
     * @return the card selected
     */
    protected DevelopmentCard getCard(CommandInterpreter interpreter, Game game){
        return game.getSetOfCard().getCard(interpreter.getLevel(), interpreter.getColor());
    }
}
