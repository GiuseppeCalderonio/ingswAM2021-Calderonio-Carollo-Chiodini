package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.commands.CommandName;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.List;

/**
 * this class is used to execute commands, and create the response to the clients
 */
public interface CommandInterpreter {

    /**
     * this method execute the command given in input, returning a code that will
     * be sent to the client associated with the handler
     * the method will change the internal model state
     * depending on the dynamic type of the commandInterpreter
     * @param command this is the command to execute
     * @param client this is the handler to notify in case of
     *                a internal state change
     * @return the response to send to the client\s
     */
    ResponseToClient executeCommand(Command command, ClientHandler client);

    /**
     * this method get the possible command for a player
     * according with the rules of the game
     * @return the possible command for a player according with the rules of the game
     */
    List<CommandName> getPossibleCommands();

    /**
     * this method return a boolean that represent if a phase of the game is finished,
     * and the FSA of the game can evolve
     * @return true if the phase of the game is finished, false otherwise
     */
    default boolean IsPhaseFinished(){
        return getPossibleCommands().isEmpty();
    }

    /**
     * this method return the enum associated with the phase of the game
     * for loginInterpreter, it returns LOGIN, for initialisingInterpreter, it returns INITIALISING,
     * for turnsInterpreter, it returns TURNS
     * @return the enum associated with the phase of the game
     */
    GamePhase getGamePhase();

    /**
     * this method get the index relative to the first card that the player want to discard during
     * the initialising game phase.
     * in particular, return the right index when the interface is implemented by the class InitialisingInterpreter,
     * return 0 otherwise
     * @return the index relative to the first card that the player want to discard
     */
    default int getFirstLeaderCard(){
        return 0;
    }

    /**
     * this method set the index relative to the first card that the player want to discard during
     * the initialising game phase.
     * in particular, set the right index when the interface is implemented by the class InitialisingInterpreter,
     * do nothing otherwise
     *
     * @param firstCard this is the index relative to the first card that the player want to discard
     */
    default void setFirstLeaderCard(int firstCard){ }
    
    
    /**
     * this method get the index relative to the second card that the player want to discard during
     * the initialising game phase.
     * in particular, return the right index when the interface is implemented by the class InitialisingInterpreter,
     * return 0 otherwise
     * @return the index relative to the second card that the player want to discard
     */
    default int getSecondLeaderCard(){
        return 0;
    }

    /**
     * this method set the index relative to the second card that the player want to discard during
     * the initialising game phase.
     * in particular, set the right index when the interface is implemented by the class InitialisingInterpreter,
     * do nothing otherwise
     *
     * @param secondCard this is the index relative to the second card that the player want to discard
     */
    default void setSecondLeaderCard(int secondCard){ }

    /**
     * this method get the marbles that the player got in the market during the choose_marbles action.
     * in particular, return the right list when the interface is implemented by the class TurnsInterpreter,
     * return null otherwise
     *
     * @return the marbles that the player got in the market
     */
    default List<Marble> getMarbles(){
        return null;
    }

    /**
     * this method set the marbles that the player got in the market during the choose_marbles action.
     * in particular, set the right list when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param marbles these are the marbles to set
     */
    default void setMarbles(List<Marble> marbles){ }

    /**
     * this method get the collectionResources associated with the marbles chosen
     * from the market that have being converted into resources
     * in particular, get the right resources when the interface is implemented by the class TurnsInterpreter,
     * null otherwise
     *
     * @return the collectionResources associated with the marbles chosen
     *         from the market that have being converted into resources
     */
    default CollectionResources getMarblesConverted(){
        return null;
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
    default void setMarblesConverted(CollectionResources marblesConverted){ }

    /**
     * this method get the list of resources without duplicates associated with
     * the collection of resources associated with the marbles converted
     * in particular, get the right resources when the interface is implemented by the class TurnsInterpreter,
     * return null otherwise
     *
     * @return the list of resources without duplicates associated with
     *         the collection of resources associated with the marbles converted
     */
    default List<Resource> getResourcesSet(){
        return null;
    }

    /**
     * this method set the list of resources without duplicates associated with
     * the collection of resources associated with the marbles converted
     * in particular, set the right resources when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param resourceSet this is the list of resources without duplicates associated with
     *      * the collection of resources associated with the marbles converted
     */
    default void setResourceSet(List<Resource> resourceSet){ }

    /**
     * this method get the level of the card that the player want to buy
     * during the buy_card normal action
     * in particular, get the right level when the interface is implemented by the class TurnsInterpreter,
     * return 0 otherwise
     *
     * @return the level of the card that the player want to buy
     */
    default int getLevel(){ return 0; }

    /**
     * this method set the level of the card that the player want to buy
     * during the buy_card normal action
     * in particular, set the right level when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param level this is the level of the card that the player want to buy
     */
    default void setLevel(int level){ }

    /**
     * this method get the color of the card that the player want to buy
     * during the buy_card normal action
     * in particular, get the right level when the interface is implemented by the class TurnsInterpreter,
     * return null otherwise
     *
     * @return the color of the card that the player want to buy
     */
    default CardColor getColor(){ return null; }

    /**
     * this method set the color of the card that the player want to buy
     * during the buy_card normal action
     * in particular, set the right color when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param color this is the color of the card that the player want to buy
     */
    default void setColor(CardColor color){
    }

    /**
     * this method get the position in which the player want to place the card
     * that want to buy during the buy_card normal action
     * in particular, get the right position when the interface is implemented by the class TurnsInterpreter,
     * return 0 otherwise
     *
     * @return the position of the card that the player want to buy
     */
    default int getDashboardPosition(){ return 0; }

    /**
     * this method set the position in which the player want to place the card
     * that want to buy during the buy_card normal action
     * in particular, set the right position when the interface is implemented by the class TurnsInterpreter,
     * do nothing otherwise
     *
     * @param dashboardPosition this is the position of the card that the player want to buy
     */
    default void setDashboardPosition(int dashboardPosition){ }
}
