package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.network.ClientHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * this class represent the turns interpreter.
 * in particular, when every player finish the initialisation, this
 * object will be created from the command manager for every client,
 * and the method execute command will be called.
 * this class is used also as buffer
 */
public class TurnsInterpreter implements CommandInterpreter {

    /**
     * this attribute represent the possible commands in a specific phase of game
     */
    private List<CommandName> possibleCommands = new ArrayList<>();

    /**
     * this attribute represent the previous possible commands in a specific phase of game
     */
    private List<CommandName> previousPossibleCommands = new ArrayList<>();

    /**
     * this attribute represent a buffer that store the marbles when a player
     * select them from the marble market
     */
    private List<Marble> marbles;

    /**
     * this attribute represent a buffer of the resources gained from the selection
     * of marbles from the marble market
     */
    private CollectionResources marblesConverted;

    /**
     * thia attribute represent a set of resources stored as a list that need to
     * be stored in order to remember the resources gained from the shift, taken
     * singular, and with a specific order, so that every client will send a list
     * of integers representing the shelves ordered in which the player want to
     * place the resources in the warehouse for each resource
     */
    private List<Resource> resourcesSet;

    /**
     * this attribute is a buffer of the color of the card that the player want to buy
     */
    private CardColor color;

    /**
     * this attribute is a buffer of the level of the card that the player want to buy
     */
    private int level;

    /**
     * this attribute is a buffer of the dashboard position of the card that the player want
     * to place into the dashboard
     */
    private int dashboardPosition;

    /**
     * this constructor create the turns interpreter setting the possible commands
     * depending on the turn
     * @param client this is the client associated with the interpreter
     */
    public TurnsInterpreter(ClientHandler client) {
        if (client.isYourTurn() ){
            possibleCommands = new ArrayList<>(
                    Arrays.asList(CommandName.SHIFT_RESOURCES,
                            CommandName.CHOOSE_MARBLES,
                            CommandName.PRODUCTION,
                            CommandName.BUY_CARD,
                            CommandName.LEADER_ACTION));

            previousPossibleCommands = new ArrayList<>(possibleCommands);
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
    public ResponseToClient executeCommand(Command command, ClientHandler client) throws EndGameException, QuitException{
        if (command.getCmd().equals(CommandName.QUIT))
            throw new QuitException();
        // if is not your turn
        if (!client.isYourTurn())
            return new ResponseToClient(Status.WRONG_TURN);
        // if the command is not a possible command in this phase of game
        if (!possibleCommands.contains(command.getCmd()))
            return new ResponseToClient(Status.REFUSED);
        // execute the command based on the dynamic type of it
        return command.executeCommand(possibleCommands, client, previousPossibleCommands);
    }

    /**
     * this method get the possible command for a player
     * according with the rules of the game
     * @return the possible command for a player according with the rules of the game
     */
    @Override
    public List<CommandName> getPossibleCommands() {

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