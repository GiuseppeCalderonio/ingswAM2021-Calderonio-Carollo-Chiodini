package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.Status;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.view.thinModelComponents.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface View {

    Command createCommand() throws IOException;

    void showContextAction(Status message);

    default void showCli(){

    };

    void showErrorMessage(Exception e);

    void showInitialisingPhase(List<LeaderCard> leaderCards, int position);

    void showCompleteGame();

    void updateStartGame(DevelopmentCard[][] cardsMarket,
                         Marble[][] marbleMarket,
                         Marble lonelyMarble,
                         SoloToken soloToken,
                         ThinPlayer actualPlayer,
                         List<ThinPlayer> opponents);

    void updateWarehouse(ThinWarehouse warehouse, String nickname);

    void updateStrongbox(CollectionResources strongbox, String nickname);

    void updateMarbleMarket(Marble[][] marbleMarket, Marble lonelyMarble);

    void updateCardsMarket(DevelopmentCard[][] cardsMarket);

    void updateTrack(Map<String, ThinTrack> tracks);

    void updatePosition(int position);

    void updateBufferMarbles(int marbles);

    void updateBufferGainedMarbles(List<Resource> gainedMarbles);

    void updateProductionPower(ThinProductionPower productionPower, String nickname);

    void updateCard(int level, CardColor color, DevelopmentCard card);

    void updateToken(SoloToken token);

    default List<LeaderCard> getAllLeaderCards(){
        return ThinPlayer.createAllLeaderCards();
    };

    void updateLeaderCards(List<ThinLeaderCard> leaderCards, String nickname);

    void updateTrack(ThinTrack track, String nickname);

    void quit();

    void updateTurn(String ownerTurnNickname);

    void showWinner(String winner, int victoryPoints);

}
