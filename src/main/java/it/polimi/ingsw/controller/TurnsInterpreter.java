package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.*;

public class TurnsInterpreter implements CommandInterpreter {

    private List<String> possibleCommands = new ArrayList<>();
    private List<String> previousPossibleCommands = new ArrayList<>();
    private List<Marble> marbles;
    private CollectionResources marblesConverted;
    private List<Resource> resourcesSet;
    private CardColor color;
    private int level;
    private int dashboardPosition;


    public TurnsInterpreter(ClientHandler client) {
        if (client.isYourTurn() ){
            possibleCommands = new ArrayList<>(
                    Arrays.asList("shift_resources",
                            "choose_marbles",
                            "production",
                            "buy_card",
                            "leader_action"));

            previousPossibleCommands = new ArrayList<>(
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
     * @param client this is the handler to notify in case of
     *                a internal state change
     * @return the response to send to the client\s
     */
    @Override
    public ResponseToClient executeCommand(Command command, ClientHandler client) throws EndGameException {
        if (command.getCmd().equals("quit"))
            throw new QuitException();
        // if is not your turn
        if (!client.isYourTurn())
            return buildResponse("is not your turn!");
        // if the command is not a possible command in this phase of game
        if (!possibleCommands.contains(command.getCmd()))
            return buildResponse("this command is not available in this phase of the game");
        // execute the command based on the dynamic type of it
        return command.executeCommand(possibleCommands, client, previousPossibleCommands);
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
        return new ResponseToClient(message, possibleCommands);
    }

    /**
     * this method set the possible commands to the value passed as parameter
     *
     * @param possibleCommands this is the new list to set
     */

    @Override
    public void setPossibleCommands(List<String> possibleCommands) {
        this.possibleCommands = possibleCommands;
    }

    /**
     * this method get the marbles that the player got in the market during the choose_marbles action.
     * in particular, return the right list when the interface is implemented by the class TurnsInterpreter,
     * return null otherwise
     *
     * @return the marbles that the player got in the market
     */
    @Override
    public List<Marble> getMarbles() {
        return marbles;
    }

    /**
     * this method set the marbles that the player got in the market during the choose_marbles action.
     * in particular, set the right list when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param marbles these are the marbles to set
     */
    @Override
    public void setMarbles(List<Marble> marbles) {
        this.marbles = marbles;
    }

    /**
     * this method get the collectionResources associated with the marbles chosen
     * from the market that have being converted into resources
     * in particular, get the right resources when the interface is implemented by the class TurnsInterpreter,
     * null otherwise
     *
     * @return the collectionResources associated with the marbles chosen
     * from the market that have being converted into resources
     */
    @Override
    public CollectionResources getMarblesConverted() {
        return marblesConverted;
    }

    /**
     * this method set the collectionResources associated with the marbles chosen
     * from the market that have being converted into resources
     * in particular, set the right resources when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param marblesConverted this is the collectionResources associated with the marbles chosen
     *                         from the market that have being converted into resources
     *                         to set
     */
    @Override
    public void setMarblesConverted(CollectionResources marblesConverted) {
        this.marblesConverted = marblesConverted;
    }

    /**
     * this method set the list of resources without duplicates associated with
     * the collection of resources associated with the marbles converted
     * in particular, set the right resources when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param resourceSet this is the list of resources without duplicates associated with
     *                    * the collection of resources associated with the marbles converted
     */
    @Override
    public void setResourceSet(List<Resource> resourceSet) {
        this.resourcesSet = resourceSet;
    }

    /**
     * this method get the list of resources without duplicates associated with
     * the collection of resources associated with the marbles converted
     * in particular, get the right resources when the interface is implemented by the class TurnsInterpreter,
     * return null otherwise
     *
     * @return the list of resources without duplicates associated with
     * the collection of resources associated with the marbles converted
     */
    @Override
    public List<Resource> getResourcesSet() {
        return resourcesSet;
    }

    /**
     * this method get the color of the card that the player want to buy
     * during the buy_card normal action
     * in particular, get the right level when the interface is implemented by the class TurnsInterpreter,
     * return null otherwise
     *
     * @return the color of the card that the player want to buy
     */
    @Override
    public CardColor getColor() {
        return color;
    }

    /**
     * this method set the color of the card that the player want to buy
     * during the buy_card normal action
     * in particular, set the right color when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param color this is the color of the card that the player want to buy
     */
    @Override
    public void setColor(CardColor color) {
        this.color = color;
    }

    /**
     * this method get the level of the card that the player want to buy
     * during the buy_card normal action
     * in particular, get the right level when the interface is implemented by the class TurnsInterpreter,
     * return 0 otherwise
     *
     * @return the level of the card that the player want to buy
     */
    @Override
    public int getLevel() {
        return level;
    }

    /**
     * this method set the level of the card that the player want to buy
     * during the buy_card normal action
     * in particular, set the right level when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param level this is the level of the card that the player want to buy
     */
    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * this method get the position in which the player want to place the card
     * that want to buy during the buy_card normal action
     * in particular, get the right position when the interface is implemented by the class TurnsInterpreter,
     * return 0 otherwise
     *
     * @return the position of the card that the player want to buy
     */
    @Override
    public int getDashboardPosition() {
        return dashboardPosition;
    }

    /**
     * this method set the position in which the player want to place the card
     * that want to buy during the buy_card normal action
     * in particular, set the right position when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param dashboardPosition this is the position of the card that the player want to buy
     */
    @Override
    public void setDashboardPosition(int dashboardPosition) {
        this.dashboardPosition = dashboardPosition;
    }
}