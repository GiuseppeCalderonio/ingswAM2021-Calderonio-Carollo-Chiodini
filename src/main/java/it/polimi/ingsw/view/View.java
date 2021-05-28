package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;

import java.util.List;

/**
 * this interface represent the gui, with the
 * methods that have to be used for manage it
 */
public interface View {

    /**
     * this method show the action associated with a specific
     * state of the game, as a suggestion or an error message
     * @param message this is the message to show
     */
    void showContextAction(Status message);

    /**
     * this method is used only in the cli version of the gui,
     * it print all the game state.in particular, it is used
     * after some changes
     */
    default void showCli(){ }

    /**
     * this method show the error message whenever is present
     * @param e this is the exception trowed
     */
    void showErrorMessage(Exception e);

    /**
     * this method is used to show the initialising phase.
     * in particular, after that every player do the login, he have
     * to select the leader cards and the initial resources
     * @param leaderCards these are the initial leader cards
     * @param position this is the position of the player, used
     *                 to manage the choice of the resources
     */
    void showInitialisingPhase(List<LeaderCard> leaderCards, int position);

    /**
     * this method show the game after that every player complete the initialization phase
     */
    void showCompleteGame();

    /**
     * this method is used to quit the game after
     */
    void quit();

    /**
     * this method is used to change the turn, after that a player finish it.
     * in particular, if is the turn of the player specified by the nickname,
     * the view will notify the player, do nothing otherwise (or eventually show the
     * nickname of the player that own the turn)
     * @param ownerTurnNickname this is the nickname of the player that now own the turn
     */
    void updateTurn(String ownerTurnNickname);

    /**
     * this method show the winner of the game.
     * @param winner this is the nickname of the winner
     * @param victoryPoints these are the victory points of the winner
     */
    void showWinner(String winner, int victoryPoints);

    /**
     * this method get the thin model associated with the game
     * @return the thin model associated with the game
     */
    ThinModel getModel();

}
