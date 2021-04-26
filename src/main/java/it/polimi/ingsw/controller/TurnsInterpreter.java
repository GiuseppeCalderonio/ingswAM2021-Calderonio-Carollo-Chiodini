package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.RedMarble;
import it.polimi.ingsw.model.Marble.WhiteMarble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.ResourceType;
import it.polimi.ingsw.view.ThinPlayer;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static it.polimi.ingsw.controller.ClientHandler.*;
import static it.polimi.ingsw.controller.CommandManager.updateMarbleMarket;
import static it.polimi.ingsw.controller.CommandManager.updatePlayers;

public class TurnsInterpreter implements CommandInterpreter {

    private List<String> possibleCommands = new ArrayList<>();
    private List<String> previousPossibleCommands = new ArrayList<>();
    private List<Marble> marbles;
    private CollectionResources marblesConverted;
    private List<Resource> resourcesSet;
    private  static List<String> normalActions = new ArrayList<>(
            Arrays.asList("choose_marbles",
                    "production",
                    "buy_card"));

    public TurnsInterpreter(ClientHandler handler) {
        if (getGame().getActualPlayer().getNickname().equals(handler.getNickname())){
            possibleCommands = new ArrayList<>(
                    Arrays.asList("shift_resources",
                            "choose_marbles",
                            "production",
                            "buy_card",
                            "leader_action"));
        }
    }

    /**
     * this method execute the command given in input, returning a code that will
     * be sent to the client associated with the handler
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     *
     * @param command this is the command to execute
     * @param handler this is the handler to notify in case of
     *                a internal state change
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(Command command, ClientHandler handler) throws IOException, EndGameException {
        if (!getGame().getActualPlayer().getNickname().equals(handler.getNickname()))
            return buildResponse("is not your turn!");

        if (!possibleCommands.contains(command.cmd))
            return buildResponse("this command is not available in this phase of the game");

        switch (command.cmd) {

            case "shift_resources":

                if (!(getGame().checkShelfSelected(command.source) &&
                        getGame().checkShelfSelected(command.destination)))
                    return buildResponse("error, one of the shelf selected does not exist");

                if (!(getGame().shiftResources(command.source, command.destination)))
                    return buildResponse("error, you can't do this kind of shift");

                for (ClientHandler handler1 : getHandlers()){
                    ResponseToClient response = new ResponseToClient();
                    response.ignorePossibleCommands = true;
                    updatePlayers(response, handler1.getNickname());
                    response.code = 3;
                    handler1.send(response);
                }
                return buildResponse("ok, shift done correctly");



            case "choose_marbles":

                // if the parameters are not correct
                if (!command.dimension.equals("row") && !command.dimension.equals("column"))
                    return buildResponse("the dimension chosen is not correct, it should be \"row\" or \"column\" ");
                // if the indexes are not correct
                if (!checkDimension(command))
                    return buildResponse("the index is not correct");

                // select the marbles from the market and shift it depending on the input
                shiftMarket(command);

                // store to a local variable all the white marbles
                List<Marble> whiteMarbles = marbles.stream().filter(marble -> marble.equals(new WhiteMarble())).collect(Collectors.toList());
                // if the player selected a list of marbles without any white marble, so the marbles are all to insert in warehouse
                if (whiteMarbles.size() == 0)
                    return buildResponseToInsertInWarehouse();

                // from here is sure that the marbles selected contains almost a white marble

                // if the player does not own any leader card
                if (ClientHandler.getGame().getActualPlayer().getLeaderWhiteMarbles().isEmpty()) {

                    // if the marbles selected converted to resources do not contains any resource (4 white marbles or 3 white and 1 red marbles)
                    if (marbles.stream().
                            allMatch(marble -> marble.equals(new WhiteMarble()) || marble.equals(new RedMarble()))){
                        // set the state of the game
                        marblesConverted = ClientHandler.getGame().convert(marbles);
                        possibleCommands.removeAll(normalActions);
                        possibleCommands.add("end_turn");
                        sendBroadcastMarbleAction();
                        return buildResponse("action completed, any resource got added");
                    }
                    else // if the marbles selected converted to resources contain a resource
                        return buildResponseToInsertInWarehouse();


                } // if the actual player has a leader card of type white marble activated
                else if (ClientHandler.getGame().getActualPlayer().getLeaderWhiteMarbles().size() == 1){
                    while (ClientHandler.getGame().changeWhiteMarble(marbles, 1));
                    return buildResponseToInsertInWarehouse();
                } // if the actual player has two leader cards
                else {
                    previousPossibleCommands = new ArrayList<>(possibleCommands);
                    possibleCommands = new ArrayList<>(Collections.singletonList("choose_leaderCards"));
                    ResponseToClient response = buildResponse("you own 2 white marble leader cards , choose for each white marble how to convert it");
                    response.marbles = marbles.size();
                    return response;
                }
            case "choose_leaderCard":
                // create a list with the white marbles contained into the marbles previously selected
                whiteMarbles = marbles.stream().filter(marble -> marble.equals(new WhiteMarble())).collect(Collectors.toList());
                // if the white marbles length don't match the indexes length
                if (whiteMarbles.size() != command.indexes.length)
                    return buildResponse("you have selected too much or not anymore leader cards");
                // if one of the indexes selected is not between 1 and 2
                if (!Arrays.stream(command.indexes).allMatch(index -> index == 1 || index == 2))
                    return buildResponse("one of the leader card selected does not exist");

                for (int i = 0; i < whiteMarbles.size(); i++) {
                    ClientHandler.getGame().changeWhiteMarble(marbles, command.indexes[i]);
                }
                // change the state and send to the player that he have to select the marbles converted in his warehouse
                return buildResponseToInsertInWarehouseLeader();

            case "insert_in_warehouse":
                // if one of the shelves selected does not exist
                if (!Arrays.stream(command.shelves).allMatch(shelf -> ClientHandler.getGame().checkShelfSelected(shelf)))
                    return buildResponse("one of the shelf selected does not exist");
                if (resourcesSet.size() != command.shelves.length)
                    return buildResponse("you have selected too much or not anymore shelves");
                // insert in warehouse all the resources selected in all the shelves selected
                for (int i = 0; i < resourcesSet.size(); i++) {
                    ClientHandler.getGame().insertInWarehouse(command.shelves[i], resourcesSet.get(i), marblesConverted);
                }

                // restore the previous command and delete from them all the normal actions
                possibleCommands = previousPossibleCommands;
                possibleCommands.removeAll(normalActions);
                possibleCommands.add("end_turn");
                sendBroadcastMarbleAction();
                return buildResponse("action completed, resources got added");
            case "buy_card":
            case "select position":
            case "select_resources_from_warehouse":
            case "production":
            case "basic_production":
            case "normal_production":
            case "leader_production":
            case "end_production":
            case "leader_action":
            case "leader_action_activate":
            case "leader_action_discard":
            case "end_turn":
                getGame().endTurn();
                possibleCommands.clear();
                return buildResponse("turn finished, is not your turn now");

        }

        return null;
    }

    @Override
    public List<String> getPossibleCommands() {

        return possibleCommands;
    }

    private ResponseToClient buildResponse(String message) {
        ResponseToClient response = new ResponseToClient();
        response.message = message;
        response.possibleCommands = possibleCommands;
        return response;
    }

    /**
     * this method verify if the command contains the right parameters
     * for the selection of marbles on marble market
     * in particular, if the dimension selected is the column
     * and the index is not between 1 and 4 return false,
     * if the dimension selected is the row and the index
     * is not between 1 and 3 return false, return true otherwise
     *
     * @param command this is the command that contains the parameters to check
     * @return true if the parameters are correct, false otherwise
     */
    private boolean checkDimension(Command command) {
        if (command.index < 1)
            return false;
        if (command.dimension.equals("row"))
            if (command.index > 3)
                return false;
        if (command.dimension.equals("column"))
            return command.index <= 4;
        return true;
    }

    /**
     * this method calls the method selectRow or selectColumn depending on the input
     * and set the attribute marbles with the return of the methods
     */
    private void shiftMarket(Command command) {
        if (command.dimension.equals("row"))
            marbles = ClientHandler.getGame().selectRow(command.index);
        if (command.dimension.equals("column"))
            marbles = ClientHandler.getGame().selectColumn(command.index);
    }


    private void sendBroadcastMarbleAction() throws IOException {
        for (ClientHandler handler : getHandlers()) {
            ResponseToClient response = new ResponseToClient();
            updatePlayers(response, handler.getNickname());
            updateMarbleMarket(response);
            response.code = 5;
            response.ignorePossibleCommands = true;
            handler.send(response);
        }
        /*
        addWarehouse(response);
        addMarbleMarket(response);
        addTrack(response);
        response.code = 5;
        ClientHandler.sendInBroadcast(response);

         */
    }

    private ResponseToClient buildResponseToInsertInWarehouse(){

        previousPossibleCommands = new ArrayList<>(possibleCommands);
        possibleCommands = new ArrayList<>(Collections.singletonList("insert_in_warehouse"));
        marblesConverted = ClientHandler.getGame().convert(marbles);
        ResponseToClient response = buildResponse("you gained " + marblesConverted + ", decide how to place them into the warehouse");
        response.serialize = true;
        response.code = 4;
        response.resourcesSet = new ArrayList<Resource>(
                new HashSet<>(marblesConverted.asList()));
        resourcesSet = response.resourcesSet;
        return response;
    }

    private ResponseToClient buildResponseToInsertInWarehouseLeader(){
        possibleCommands = new ArrayList<>(Collections.singletonList("insert_in_warehouse"));
        marblesConverted = ClientHandler.getGame().convert(marbles);
        ResponseToClient response = buildResponse("you gained " + marblesConverted + ", decide how to place them into the warehouse");
        response.serialize = true;
        response.code = 4;
        response.resourcesSet = new ArrayList<Resource>(
                new HashSet<>(marblesConverted.asList()));
        resourcesSet = response.resourcesSet;
        return response;
    }

}

