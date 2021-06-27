package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.SingleGame.SoloToken;

import java.util.List;
import java.util.Map;

/**
 * this interface is used to update the thin model of the client
 */
public interface Updatable {

    /**
     * this method create the initial state of the thin model.
     * in particular, once that every player did the initialization, this method
     * set all the attributes to represent a complete game
     * @param cardsMarket this is the cards market to set
     * @param marbleMarket this is the marble market to set
     * @param lonelyMarble this is the lonely marble to set
     * @param soloToken this is the solo token to set
     * @param actualPlayer this is the actual player to set
     * @param opponents these are the opponents to set
     */
    void updateStartGame(DevelopmentCard[][] cardsMarket,
                         Marble[][] marbleMarket,
                         Marble lonelyMarble,
                         SoloToken soloToken,
                         ThinPlayer actualPlayer,
                         List<ThinPlayer> opponents);

    /**
     * this method update the warehouse of the player
     * specified by the nickname passed as input
     * @param warehouse this is the thin warehouse to update
     * @param nickname this is the nickname of the player to update
     */
    void updateWarehouse(ThinWarehouse warehouse, String nickname);

    /**
     * this method update the strongbox of the player
     * specified by the nickname passed as input
     * @param strongbox this is the thin strongbox to update
     * @param nickname this is the nickname of the player to update
     */
    void updateStrongbox(CollectionResources strongbox, String nickname);

    /**
     * this method update the marble market
     * @param marbleMarket this is the matrix of marble to update
     * @param lonelyMarble this is the lonely marble to update
     */
    void updateMarbleMarket(Marble[][] marbleMarket, Marble lonelyMarble);

    /**
     * this method update the cards market
     * @param cardsMarket this is the matrix of cards to update
     */
    void updateCardsMarket(DevelopmentCard[][] cardsMarket);

    /**
     * this method update the track of every player
     * @param tracks this is a map representing the nickname of the player
     *               and his new thin track to update
     */
    void updateTrack(Map<String, ThinTrack> tracks);

    /**
     * this method update the position of the client that receive the message
     * @param position this is the position to set
     */
    void updatePosition(int position);

    /**
     * this method update the buffer marble containing the number of white marbles
     * @param marbles this is the integer representing the number of white marbles to be converted to update
     */
    void updateBufferMarbles(int marbles);

    /**
     * this method update the list of resources associated with the marbles selected from
     * a selection on the marble market and converted
     * @param gainedMarbles these are the resources gained from the marble market to update
     */
    void updateBufferGainedMarbles(List<Resource> gainedMarbles);

    /**
     * this method update the thin production power of the player
     * specified by the nickname passed as input
     * @param productionPower this is the thin production power to update
     * @param nickname this is the nickname of the player to update
     */
    void updateProductionPower(ThinProductionPower productionPower, String nickname);

    /**
     * this method update the cards market, substituting the card with the level
     * and color specified by the inputs with the card in input, that can be also null,
     * that means that that deck of cards is empty
     * @param level this is the level of the card to substitute
     * @param color this is the color of the card to substitute
     * @param card this is the card to substitute
     */
    void updateCard(int level, CardColor color, DevelopmentCard card);

    /**
     * this method update the solo token, if it is null do anything
     * @param token this is the token to update
     */
    void updateToken(SoloToken token);

    /**
     * this method get all the leader cards of the game with their ids
     * @return all the leader cards of the game with their ids
     */
    default List<LeaderCard> getAllLeaderCards(){
        return ThinPlayer.createAllLeaderCards();
    };

    /**
     * this method update the leader cards of the player
     * specified by the nickname passed as input
     * @param leaderCards these are the leader cards to update
     * @param nickname this is the nickname of the player to update
     */
    void updateLeaderCards(List<ThinLeaderCard> leaderCards, String nickname);

    /**
     * this method update the thin track of the player
     * specified by the nickname passed as input
     * @param track this is the thin track to update
     * @param nickname this is the nickname of the player to update
     */
    void updateTrack(ThinTrack track, String nickname);
}
