package it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands;

import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.commands.normalCommands.NormalActionCommand;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.TwoLeaderWhiteMarblesResponse;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.RedMarble;
import it.polimi.ingsw.model.Marble.WhiteMarble;

import java.util.List;
import java.util.stream.Collectors;

/**
 * this class represent the choose marbles command.
 * in particular, it extend the class normal action because this action
 * can be done only once during the turn.
 * when the player want to gain resources from the market, he have to
 * decide the index of row or column, this command do it
 */
public class ChooseMarblesCommand extends NormalActionCommand {

    /**
     * this attribute represent the dimension of the market, it can be: [row, column]
     */
    private final String dimension;

    /**
     * this attribute represent the index of the matrix to take
     */
    private final int index;

    /**
     * this constructor create the command setting the dimension and the index in which
     * gain the marbles into the market
     * @param dimension this is the dimension in which gain the marbles into the market
     * @param index this is the index in which gain the marbles into the market
     */
    public ChooseMarblesCommand(String dimension, int index){
        this.dimension = dimension;
        this.index = index;
    }


    /**
     * this method get the cmd associated with the command
     *
     * @return the cmd associated with the command
     */
    @Override
    public CommandName getCmd() {
        return CommandName.CHOOSE_MARBLES;
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
        return "you can't select this resources";
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
        return "ok, now decide how to place the resources in the warehouse, or do a shift, or decide how to convert white marbles";
    }

    /**
     * this method execute the command given in input,
     * returning a message that will be sent to the client.
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     * @param possibleCommands these are the possible commands to eventually modify
     * @param client this is the handler to notify in case of
     *                a internal state change
     * @param previousPossibleCommands these are the possible commands referred to the previous
     *                                 command, that eventually may be modified
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(List<CommandName> possibleCommands, ClientHandler client, List<CommandName> previousPossibleCommands) {
        Game game = client.getGame();
        List<Marble> marbles = client.getInterpreter().getMarbles();
        // if the parameters are not correct
        if (!dimension.equals("row") && !dimension.equals("column"))
            return errorMessage();
        // if the indexes are not correct
        if (!checkDimension())
            return errorMessage();

        // select the marbles from the market and shift it depending on the input
        marbles = shiftMarket(game, marbles);
        // set the marbles to the buffer of the command interpreter for the specific case in which the player have 2 leader cards white marbles conversion
        client.getInterpreter().setMarbles(marbles);

        // store to a local variable all the white marbles
        List<Marble> whiteMarbles = marbles.stream().filter(marble -> marble.equals(new WhiteMarble())).collect(Collectors.toList());
        // if the player selected a list of marbles without any white marble, so the marbles are all to insert in warehouse
        if (whiteMarbles.size() == 0)
            return buildResponseToInsertInWarehouse(client.getInterpreter(), game, marbles);

        // from here is sure that the marbles selected contains almost a white marble

        // if the player does not own any leader card
        if (game.getActualPlayer().getLeaderWhiteMarbles().isEmpty())
            return caseNoLeaderCards(marbles, game, client, possibleCommands);

         // if the actual player has a leader card of type white marble activated
        else if (game.getActualPlayer().getLeaderWhiteMarbles().size() == 1)
            return caseOneLeaderCard(game, marbles, client);

         // if the actual player has two leader cards of type white marble activated
        else
            return caseTwoLeaderCards(possibleCommands, previousPossibleCommands, whiteMarbles.size());
    }

    /**
     * this method verify if the command contains the right parameters
     * for the selection of marbles on marble market
     * in particular, if the dimension selected is the column
     * and the index is not between 1 and 4 return false,
     * if the dimension selected is the row and the index
     * is not between 1 and 3 return false, return true otherwise
     *
     * @return true if the parameters are correct, false otherwise
     */
    private boolean checkDimension() {
        if (index < 1)
            return false;
        if (dimension.equals("row"))
            if (index > 3)
                return false;
        if (dimension.equals("column"))
            return index <= 4;
        return true;
    }

    /**
     * this method calls the method selectRow or selectColumn depending on the input
     * and set the attribute marbles with the return of the methods
     */
    private List<Marble> shiftMarket(Game game, List<Marble> marbles) {
        if (dimension.equals("row"))
            marbles = game.selectRow(index);
        if (dimension.equals("column"))
            marbles = game.selectColumn(index);

        return marbles;
    }

    /**
     * this method handle the case in which a player have no leader cards white marbles conversion
     * active.
     * in particular, if the row/column selected doesn't contain any marble different from red marble
     * and white marble, it do the action without changing anything and removing the normal
     * commands from the possible ones and sending the new marble market,
     * else it prepare the new response to send in order to make the client choose the
     * shelves in which collect the resources chosen.
     * it also change the state of the buffers whenever it is necessary
     * @param marbles this is the list of marbles selected to convert
     * @param game this is the game in which do the conversion
     * @param client this is the client from which get the buffers to change
     * @param possibleCommands these are the possible commands to change
     * @return the response to send to the client/s
     */
    private ResponseToClient caseNoLeaderCards(List<Marble> marbles, Game game, ClientHandler client, List<CommandName> possibleCommands){
        // if the marbles selected converted to resources do not contains any resource (4 white marbles or 3 white and 1 red marbles)
        if (marbles.stream().
                allMatch(marble -> marble.equals(new WhiteMarble()) || marble.equals(new RedMarble()))){
            // set the state of the game
            client.getInterpreter().setMarblesConverted(game.convert(marbles));
            // remove all the normal actions fro the possible commands
            possibleCommands.removeAll(getNormalActions());
            // add the possibility to end the turn
            possibleCommands.add(CommandName.END_TURN);
            sendBroadcastMarbleAction(client);
            return acceptedMessage();
        }
        else // if the marbles selected converted to resources contain a resource
            return buildResponseToInsertInWarehouse(client.getInterpreter(), game, marbles);
    }

    /**
     * this method handle the case in which a player have one leader card white marbles conversion
     * active.
     * in particular, it change every white marble of the list passed in input and
     * it prepare the new response to send in order to make the client choose the
     * shelves in which collect the resources chosen
     * @param game ths is the game in which change the white marbles
     * @param marbles these are the marbles selected from the column/row
     * @param client this is the client from which get the buffers to change
     * @return the response to send to the client
     */
    private ResponseToClient caseOneLeaderCard(Game game, List<Marble> marbles, ClientHandler client){
        //noinspection StatementWithEmptyBody
        while (game.changeWhiteMarble(marbles, 1));
        return buildResponseToInsertInWarehouse(client.getInterpreter(), game, marbles);
    }

    /**
     * this method handle the case in which a player have one leader card white marbles conversion
     * active.
     * in particular, store the possible commands into the previous ones, set the
     * possible commands into the singleton list "choose_leaderCards", and return the
     * response that allows the client to select how to convert every white marble chosen
     * into a resource
     * @param possibleCommands this is the list of possible commands to set
     * @param previousPossibleCommands this is the list of previous possible commands in which
     *                                 store the possible commands before setting them
     * @param whiteMarbles this is the number of white marbles chosen
     * @return the response that allows the player to select how to convert every white marble chosen
     *         into a resource
     */
    private ResponseToClient caseTwoLeaderCards(List<CommandName> possibleCommands,
                                                List<CommandName> previousPossibleCommands,
                                                int whiteMarbles){
        // set the previous possible commands
        previousPossibleCommands.clear();
        previousPossibleCommands.addAll(possibleCommands);
        // set the possible commands
        possibleCommands.clear();
        possibleCommands.add(CommandName.CHOOSE_LEADER_CARDS);
        // set the marbles selected from the player

        return new TwoLeaderWhiteMarblesResponse(whiteMarbles);
    }
}
