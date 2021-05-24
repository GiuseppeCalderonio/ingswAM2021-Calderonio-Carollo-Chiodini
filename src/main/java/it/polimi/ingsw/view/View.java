package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.view.thinModelComponents.ThinModel;

import java.util.List;

public interface View {

    void showContextAction(Status message);

    default void showCli(){

    };

    void showErrorMessage(Exception e);

    void showInitialisingPhase(List<LeaderCard> leaderCards, int position);

    void showCompleteGame();

    void quit();

    void updateTurn(String ownerTurnNickname);

    void showWinner(String winner, int victoryPoints);

    ThinModel getModel();

}
