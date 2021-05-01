package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.RedMarble;
import it.polimi.ingsw.model.Marble.WhiteMarble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.*;
import java.util.stream.Collectors;

public class TurnsInterpreter implements CommandInterpreter {

    private final ClientHandler handler;
    private final Game game;

    private List<String> possibleCommands = new ArrayList<>();
    private List<String> previousPossibleCommands = new ArrayList<>();
    private List<Marble> marbles;
    private CollectionResources marblesConverted;
    private List<Resource> resourcesSet;
    private final List<String> normalActions = new ArrayList<>(
            Arrays.asList("choose_marbles",
                    "production",
                    "buy_card"));
    private final List<String> productions = new ArrayList<>(
            Arrays.asList("basic_production",
                    "normal_production" ,
                    "leader_production" ,
                    "end_production"));
    private final List<String> leaderActions = new ArrayList<>(
            Arrays.asList("activate_card",
                    "discard_card"));
    private CardColor color;
    private int level;
    private DevelopmentCard card;
    private int dashboardPosition;
    private Boolean[] normalProductionsActivated = {false, false, false};
    private Boolean[] leaderProductionsActivated = {false, false};
    private boolean basicProductionActivated = false;


    public TurnsInterpreter(ClientHandler handler) {
        this.handler = handler;
        this.game = handler.getGame();
        if (game.getActualPlayer().getNickname().equals(handler.getNickname())){
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
    public ResponseToClient executeCommand(Command command, ClientHandler handler) throws EndGameException {
        // if is not your turn
        if (!getGame().isYourTurn(handler.getNickname()))
            return buildResponse("is not your turn!");
        // if the command is not a possible command in this phase of game
        if (!possibleCommands.contains(command.cmd))
            return buildResponse("this command is not available in this phase of the game");

        switch (command.cmd) {

            case "shift_resources":
                // if on of the shelves selected doesn't exist
                if (!(getGame().checkShelfSelected(command.source) &&
                        getGame().checkShelfSelected(command.destination)))
                    return buildResponse("error, one of the shelf selected does not exist");
                // if a shift can't be done
                if (!(getGame().shiftResources(command.source, command.destination)))
                    return buildResponse("error, you can't do this kind of shift");
                // send to every player the new game state
                sendBroadcastChangePlayerState();
                // send that string to the client
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
                if (getGame().getActualPlayer().getLeaderWhiteMarbles().isEmpty()) {

                    // if the marbles selected converted to resources do not contains any resource (4 white marbles or 3 white and 1 red marbles)
                    if (marbles.stream().
                            allMatch(marble -> marble.equals(new WhiteMarble()) || marble.equals(new RedMarble()))){
                        // set the state of the game
                        marblesConverted = getGame().convert(marbles);
                        possibleCommands.removeAll(normalActions);
                        possibleCommands.add("end_turn");
                        sendBroadcastMarbleAction();
                        return buildResponse("action completed, any resource got added");
                    }
                    else // if the marbles selected converted to resources contain a resource
                        return buildResponseToInsertInWarehouse();


                } // if the actual player has a leader card of type white marble activated
                else if (getGame().getActualPlayer().getLeaderWhiteMarbles().size() == 1){
                    while (getGame().changeWhiteMarble(marbles, 1));
                    return buildResponseToInsertInWarehouse();
                } // if the actual player has two leader cards of type white marble activated
                else {
                    previousPossibleCommands = new ArrayList<>(possibleCommands);
                    possibleCommands = new ArrayList<>(Collections.singletonList("choose_leaderCards"));
                    ResponseToClient response = buildResponse("you own 2 white marble leader cards , choose for each white marble how to convert it");
                    response.marbles = marbles.size();
                    return response;
                }
            case "choose_leaderCards":
                // create a list with the white marbles contained into the marbles previously selected
                whiteMarbles = marbles.stream().filter(marble -> marble.equals(new WhiteMarble())).collect(Collectors.toList());
                // if the white marbles length don't match the indexes length
                if (whiteMarbles.size() != command.indexes.length)
                    return buildResponse("you have selected too much or not anymore leader cards");
                // if one of the indexes selected is not between 1 and 2
                if (!Arrays.stream(command.indexes).allMatch(index -> index == 1 || index == 2))
                    return buildResponse("one of the leader card selected does not exist");

                for (int i = 0; i < whiteMarbles.size(); i++) {
                    getGame().changeWhiteMarble(marbles, command.indexes[i]);
                }
                // change the state and send to the player that he have to select the marbles converted in his warehouse
                return buildResponseToInsertInWarehouseLeader();

            case "insert_in_warehouse":
                // if one of the shelves selected does not exist
                if (!Arrays.stream(command.shelves).allMatch(shelf -> getGame().checkShelfSelected(shelf)))
                    return buildResponse("one of the shelf selected does not exist");
                if (resourcesSet.size() != command.shelves.length)
                    return buildResponse("you have selected too much or not anymore shelves");
                // insert in warehouse all the resources selected in all the shelves selected
                for (int i = 0; i < resourcesSet.size(); i++) {
                    getGame().insertInWarehouse(command.shelves[i], resourcesSet.get(i), marblesConverted);
                }

                // restore the previous command and delete from them all the normal actions
                possibleCommands = previousPossibleCommands;
                possibleCommands.removeAll(normalActions);
                possibleCommands.add("end_turn");
                // send to every player the new game state
                sendBroadcastMarbleAction();
                return buildResponse("action completed, resources got added");
            case "buy_card":
                // if the client can't buy the specified card
                if (!getGame().checkBuyCard(command.level, command.color))
                    return buildResponse("error, one of these things could be the motivation :" +
                            "(1) you have selected a level not between 1 and 3, " +
                            "(2) you have selected a color that doesn't exist, " +
                            "(3) you have selected an empty deck of cards, " +
                            "(4) you can't buy the card because you can't afford it, " +
                            "(5) you can't place the card selected into the dashboard");
                // set the color of the card into a buffer
                color = command.color;
                // set the level of the card into a buffer
                level = command.level;
                // store the card selected into a buffer
                card = getGame().getSetOfCard().getCard(level, color);
                // set the new possible commands
                previousPossibleCommands = new ArrayList<>(possibleCommands);
                possibleCommands = new ArrayList<>(Collections.singletonList("select_position"));
                return buildResponse("deck selected is available, now decide where you want to place the card in your dashboard");

            case "select_position":
                // if card previously selected can't be placed in the space selected
                if (!getGame().checkPlacement(card, command.dashboardPosition)) {
                    // reset the possible commands
                    possibleCommands = previousPossibleCommands;
                    return buildResponse("error, one of these things could be the motivation :" +
                            "1) the position selected is not between 1 and 3" +
                            "2) the position selected does not allow the card placement");
                }
                // set the dashboard position in which place the card
                dashboardPosition = command.dashboardPosition;
                possibleCommands = new ArrayList<>(Collections.singletonList("select_resources_from_warehouse"));
                return buildResponse("now select the warehouse resources to pay the card");

            case "select_resources_from_warehouse":
                possibleCommands = previousPossibleCommands;
                // if the warehouse resources to buy the card are not compatible with the
                // resources storage state of the player
                if (!getGame().checkWarehouseResources(card, command.toPayFromWarehouse))
                    return buildResponse("error,you have selected an incorrect number of resources");

                // buy the card with the resources chosen
                getGame().buyCard(level, color, dashboardPosition ,command.toPayFromWarehouse);
                // delete any normal action from the possible commands
                possibleCommands.removeAll(normalActions);
                // add end turn to the possible commands
                possibleCommands.add("end_turn");
                // send to every player the new game state
                sendBroadcastCardAction();
                return buildResponse("card bought correctly");

            case "production":

                previousPossibleCommands = new ArrayList<>(possibleCommands);
                // set the possible commands to the productions
                possibleCommands = productions;
                // clear production filter only the production that could be activated, returning true if are none
                // if the client can't activate any kind of production
                if (clearProductions()){
                    possibleCommands = previousPossibleCommands;
                    return buildResponse("you can't activate any production, choose another action");
                }

                return buildResponse("write the production type that you want activate");

            case "basic_production":
                // if the production can't be activated
                if (!getGame().checkProduction(0))
                    return buildResponse("you selected a position that doesn't exist, or you selected a wrong amount of resources");
                if (!getGame().checkActivateBasicProduction(command.toPayFromWarehouse, command.toPayFromStrongbox, command.output))
                    return buildResponse("error, one of these things could be the motivation :" +
                            "1) you haven't chosen the right amount of resources (you have to choose 2 resources in input and 1 in output)" +
                            "2) you don't own the chosen resources in your storage");
                // activate the basic production
                getGame().activateBasicProduction(command.toPayFromWarehouse, command.toPayFromStrongbox, command.output);
                // filter the possible production after the internal state change
                clearProductions();
                // remove the basic production from the possible commands
                possibleCommands.remove("basic_production");
                // set to true the activation of the basic production
                basicProductionActivated = true;
                // send to every player the new game state
                sendBroadcastChangePlayerState();
                return buildResponse("basic production activated correctly, now choose another one or end the production");
            case "normal_production":
                // if the production can't be activated
                if (!getGame().checkProduction(command.position))
                    return buildResponse("you selected a position that doesn't exist");
                if (normalProductionsActivated[command.position - 1])
                    return buildResponse("you already selected this production before, choose another one or end the production");
                if (!getGame().checkActivateProduction(command.position, command.toPayFromWarehouse))
                    return buildResponse("error from check");
                // activate the production
                getGame().activateProduction(command.position, command.toPayFromWarehouse);
                // filter the possible production after the internal state change
                clearProductions();
                // set to true the activation of the normal production eventually removing it from the possible commands
                checkActivatedNormalProductions(command.position);
                // send to every player the new game state
                sendBroadcastChangePlayerState(); // code 3
                return buildResponse(command.position + "° normal production activated correctly, now choose another one or end the production");

            case "leader_production":
                // if the production can't be activated
                if (!getGame().checkProduction(command.position + 3))
                    return buildResponse("error,you don't have enough resources or the production selected doesn't exist");
                if (leaderProductionsActivated[command.position - 1])
                    return buildResponse("you already selected this production before, choose another one or end the production");
                if (!getGame().checkActivateLeaderProduction(command.position, command.fromWarehouse))
                    return buildResponse("error,you have selected an incorrect number of resources");
                // activate the production
                getGame().activateLeaderProduction(command.position, command.output, command.fromWarehouse);
                // filter the possible production after the internal state change
                clearProductions();
                // set to true the activation of the leader production eventually removing it from the possible commands
                checkActivatedNormalProductions(command.position);
                // send to every player the new game state
                sendBroadcastChangePlayerState();
                return buildResponse(command.position + "° leader production activated correctly, now choose another one or end the production");
            case "end_production":
                // end the production filling the strongbox with all the resources gained
                getGame().endProduction();
                // if the player didn't activate any kind of production
                if (anyProductionGotActivated()){
                    possibleCommands = previousPossibleCommands;
                    return buildResponse("you didn't activate any production");
                }
                // reset the possible commands
                possibleCommands = previousPossibleCommands;
                // remove all the normal actions from the possible commands
                possibleCommands.removeAll(normalActions);
                // add the end turn to the possible commands
                possibleCommands.add("end_turn");
                return buildResponse("the production is finished");

            case "leader_action":
                previousPossibleCommands = new ArrayList<>(possibleCommands);
                // set the possible commands to the leader actions
                possibleCommands = leaderActions;
                return buildResponse("choose the leader action to do");

            case "activate_card":
                // if the card doesn't exist
                if (!getGame().checkLeaderCard(command.toActivate)){
                    // reset the previous possible commands
                    possibleCommands = previousPossibleCommands;
                    return buildResponse("the leader cards selected does not exist");
                }
                // if the card can't be activated
                if (!getGame().activateLeaderCard(command.toActivate)){
                    // reset the possible commands
                    possibleCommands = previousPossibleCommands;
                    return buildResponse("the leader card is already active or you don't meet the requirements to activate it");
                }
                // reset the possible commands
                possibleCommands = previousPossibleCommands;
                // remove leader actions from possible commands
                possibleCommands.remove("leader_action");
                // send to every player the new game state
                sendBroadcastChangePlayerState();
                return buildResponse("leader card activated");

            case "discard_card":
                // if the card selected doesn't exist
                if (!getGame().checkLeaderCard(command.toDiscard)){
                    possibleCommands = previousPossibleCommands;
                    return buildResponse("the leader cards selected does not exist");
                }
                // if the card can't be discarded
                if (!getGame().discardLeaderCard(command.toDiscard)){
                    possibleCommands = previousPossibleCommands;
                    return buildResponse("the leader card is already active and you can't discard it");
                }
                // reset the previous possible commands
                possibleCommands = previousPossibleCommands;
                // remove leader actions from possible commands
                possibleCommands.remove("leader_action");
                // send to every player the new game state
                sendBroadcastChangePlayerState();
                return buildResponse("leader card discarded");

            case "end_turn":
                // end the turn
                getGame().endTurn();
                // remove all the possible commands
                possibleCommands.clear();
                // if the client is playing a single player match
                if (handler.getNumberOfPlayers() == 1){
                    // send to the client the new game state
                    sendEndSingleGame();
                    return buildSoloResponse();
                }
                return buildResponse("turn finished, is not your turn now");
        }
        return buildResponse("The command was in the list but not found from the possible commands, is probably server fault");
    }

    @Override
    public List<String> getPossibleCommands() {

        return possibleCommands;
    }

    /**
     * this method return the enum associated with the phase of the game
     * for loginInterpreter, it returns LOGIN, for initialisingInterpreter, it returns INITIALISING,
     * for turnsInterpreter, it returns TURNS
     *
     * @return the enum associated with the phase of the game
     */
    @Override
    public GamePhase getGamePhase() {
        return GamePhase.TURNS;
    }

    private ResponseToClient buildResponse(String message) {
        ResponseToClient response = new ResponseToClient();
        response.message = message;
        response.possibleCommands = possibleCommands;
        return response;
    }

    private ResponseToClient buildSoloResponse() {
        ResponseToClient response = new ResponseToClient();
        response.message = "Lorenzo did his action!";
        response.ignorePossibleCommands = true;
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
            marbles = getGame().selectRow(command.index);
        if (command.dimension.equals("column"))
            marbles = getGame().selectColumn(command.index);
    }


    private void sendBroadcastMarbleAction() {
        for (ClientHandler client : handler.getClients()) {
            ResponseToClient response = new ResponseToClient();
            client.updatePlayers(response, client.getNickname());
            client.updateMarbleMarket(response);
            response.code = 5;
            response.ignorePossibleCommands = true;
            client.send(response);
        }
    }

    private void sendBroadcastCardAction() {
        for (ClientHandler client : handler.getClients()) {
            ResponseToClient response = new ResponseToClient();
            client.updatePlayers(response, client.getNickname());
            client.updateCardsMarket(response);
            response.code = 6;
            response.ignorePossibleCommands = true;
            client.send(response);
        }
    }

    private void sendBroadcastChangePlayerState() {
        for (ClientHandler client : handler.getClients()) {
            ResponseToClient response = new ResponseToClient();
            client.updatePlayers(response, client.getNickname());
            response.code = 3;
            response.ignorePossibleCommands = true;
            client.send(response);
        }
    }


    private ResponseToClient buildResponseToInsertInWarehouse(){

        previousPossibleCommands = new ArrayList<>(possibleCommands);
        return buildResponseToInsertInWarehouseLeader();
    }

    private ResponseToClient buildResponseToInsertInWarehouseLeader(){
        possibleCommands = new ArrayList<>(Collections.singletonList("insert_in_warehouse"));
        marblesConverted = getGame().convert(marbles);
        ResponseToClient response = buildResponse("you gained " + marblesConverted + ", decide how to place them into the warehouse");
        response.serialize = true;
        response.code = 4;
        response.resourcesSet = new ArrayList<>(
                new HashSet<>(marblesConverted.asList()));
        resourcesSet = response.resourcesSet;
        return response;
    }

    private void checkActivatedNormalProductions(int toDeactivate){
        normalProductionsActivated[toDeactivate - 1] = true;
        if (Arrays.stream(normalProductionsActivated).allMatch(production -> production))
            possibleCommands.remove("normal_production");
    }

    private void checkActivateLeaderProductions(int toDeactivate){
        normalProductionsActivated[toDeactivate - 1] = true;
        if (Arrays.stream(leaderProductionsActivated).allMatch(production -> production))
            possibleCommands.remove("leader_production");
    }

    private boolean clearProductions(){
        if (!getGame().checkProduction(0))
            possibleCommands.remove("basic_production");

        if (!getGame().checkProduction(1) && !getGame().checkProduction(2) && !getGame().checkProduction(3))
                possibleCommands.remove("normal_production");

        if (!getGame().checkProduction(4) || !getGame().checkProduction(5))
            possibleCommands.remove("leader_production");

        return possibleCommands.size() == 1;
    }

    private  boolean anyProductionGotActivated(){
        // create a new arraylist of booleans
        List<Boolean> activatedProductions = new ArrayList<>();
        // add to it the value of the basic production
        activatedProductions.add(basicProductionActivated);
        // add to it the value of the normal productions
        activatedProductions.addAll(Arrays.asList(normalProductionsActivated));
        // add to it the value of the leader productions
        activatedProductions.addAll(Arrays.asList(leaderProductionsActivated));
        // reset all the values
        basicProductionActivated = false;
        normalProductionsActivated = new Boolean[]{false, false, false};
        leaderProductionsActivated = new Boolean[]{false, false};
        // return true when any production got activated
        return activatedProductions.stream().noneMatch(production -> production);

    }

    private void sendEndSingleGame() {
        for (ClientHandler client : handler.getClients()) {
            ResponseToClient response = new ResponseToClient();
            client.updatePlayers(response, client.getNickname());
            client.updateCardsMarket(response);
            client.updateSoloTokens(response);
            response.ignorePossibleCommands = true;
            response.code = 7;
            response.serialize = true;
            client.send(response);
        }
    }

    private Game getGame(){
        return game;
    }
}