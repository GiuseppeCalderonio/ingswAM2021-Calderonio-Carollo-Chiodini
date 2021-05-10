package it.polimi.ingsw.controller.commands.normalCommands.MarbleMarketCommands;

import it.polimi.ingsw.controller.ClientHandler;
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
    public String getCmd() {
        return "choose_marbles";
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
    public ResponseToClient executeCommand(List<String> possibleCommands, ClientHandler client, List<String> previousPossibleCommands) {
        Game game = client.getGame();
        List<Marble> marbles = client.getInterpreter().getMarbles();
        // if the parameters are not correct
        if (!dimension.equals("row") && !dimension.equals("column"))
            return buildResponse("the dimension chosen is not correct, it should be \"row\" or \"column\" ", possibleCommands);
        // if the indexes are not correct
        if (!checkDimension())
            return buildResponse("the index is not correct", possibleCommands);

        // select the marbles from the market and shift it depending on the input
        marbles = shiftMarket(game, marbles);

        // store to a local variable all the white marbles
        List<Marble> whiteMarbles = marbles.stream().filter(marble -> marble.equals(new WhiteMarble())).collect(Collectors.toList());
        // if the player selected a list of marbles without any white marble, so the marbles are all to insert in warehouse
        if (whiteMarbles.size() == 0)
            return buildResponseToInsertInWarehouse(possibleCommands,
                    client.getInterpreter(),
                    game,
                    marbles);

        // from here is sure that the marbles selected contains almost a white marble

        // if the player does not own any leader card
        if (game.getActualPlayer().getLeaderWhiteMarbles().isEmpty()) {

            // if the marbles selected converted to resources do not contains any resource (4 white marbles or 3 white and 1 red marbles)
            if (marbles.stream().
                    allMatch(marble -> marble.equals(new WhiteMarble()) || marble.equals(new RedMarble()))){
                // set the state of the game
                client.getInterpreter().setMarblesConverted(game.convert(marbles));
                //marblesConverted = game.convert(marbles);
                possibleCommands.removeAll(getNormalActions());
                possibleCommands.add("end_turn");
                sendBroadcastMarbleAction(client);
                return buildResponse("action completed, any resource got added", possibleCommands);
            }
            else // if the marbles selected converted to resources contain a resource
                return buildResponseToInsertInWarehouse(possibleCommands,
                        client.getInterpreter(),
                        game,
                        marbles);


        } // if the actual player has a leader card of type white marble activated
        else if (game.getActualPlayer().getLeaderWhiteMarbles().size() == 1){
            //noinspection StatementWithEmptyBody
            while (game.changeWhiteMarble(marbles, 1));
            return buildResponseToInsertInWarehouse(possibleCommands,
                    client.getInterpreter(),
                    game,
                    marbles);
        } // if the actual player has two leader cards of type white marble activated
        else {
            previousPossibleCommands.clear();
            previousPossibleCommands.addAll(possibleCommands);
            possibleCommands.clear();
            possibleCommands.add("choose_leaderCards");

            return new TwoLeaderWhiteMarblesResponse(marbles.size());

        }
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

    /*
     * this method create a response to client to send to the client.
     * in particular, it is called if and only if the player own 2 white marble leader cards,
     * the method send the message, the possibleCommands :[choose_leaderCards], and
     * the int representing the number of white marbles selected, that have to ve converted
     * in resources with the command choose_leaderCards
     * @param possibleCommands these are the possible commands for the player
     * @param marblesSize this is the number of white marbles to convert in resources
     * @return the response to send to the client
     */


}
