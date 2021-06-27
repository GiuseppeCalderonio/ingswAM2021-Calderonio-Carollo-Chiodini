package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.SingleGame.SoloToken;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * this class represent the thin model.
 * in particular, contains all the attributes that are used to manage the game from the client
 */
public class ThinModel implements Updatable{

    /**
     * this attribute represent the thin game
     */
    private ThinGame game;

    /**
     * this attribute represent the position of the client that own the object
     */
    private int position = 0;

    /**
     * this attribute represent the buffer of resources gained from a selection of the marble market
     */
    private List<Resource> gainedFromMarbleMarket;

    /**
     * this attribute represent a buffer of the amount of the white marbles
     * selected, used only when the client own 2 active leader cards white marble conversion
     */
    private int marbles;

    /**
     * this method get the buffer of resources gained from the marble market after a selection
     * @return the buffer of resources gained from the marble market after a selection
     */
    public List<Resource> getGainedFromMarbleMarket() {
        return gainedFromMarbleMarket;
    }

    /**
     * this method set the buffer of resources gained from the marble market after a selection
     * @param gainedFromMarbleMarket this is the buffer of resources gained from the marble
     *                              market after a selection to set
     */
    public void setGainedFromMarbleMarket(List<Resource> gainedFromMarbleMarket) {
        this.gainedFromMarbleMarket = gainedFromMarbleMarket;
    }

    /**
     * this method get the thin game
     * @return the thin game
     */
    public ThinGame getGame() {
        return game;
    }

    /**
     * this method set the thin game
     * @param game this is te thin game to set
     */
    public void setGame(ThinGame game) {
        this.game = game;
    }

    /**
     * this method get the amount of white marbles selected after a selection in the
     * specific case in which the player owns 2 active leader white marbles conversion
     * @return the amount of white marbles selected after a selection in the
     *         specific case in which the player owns 2 active leader white marbles conversion
     */
    public int getMarbles() {
        return marbles;
    }

    /**
     * this method set the amount of white marbles selected after a selection in the
     * specific case in which the player owns 2 active leader white marbles conversion
     * @param marbles this is the integer representing the amount of
     *               white marbles selected after a selection in the
     *                specific case in which the player owns 2 active
     *                leader white marbles conversion to set
     */
    public void setMarbles(int marbles) {
        this.marbles = marbles;
    }

    /**
     * this method get the position of the player (based on the inkwell)
     * @return the position of the player (based on the inkwell)
     */
    public int getPosition() {
        return position;
    }

    /**
     * this method set the position of the player (based on the inkwell)
     * @param position this is the position of the player (based on the inkwell) to set
     */
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public void updateStartGame(DevelopmentCard[][] cardsMarket, Marble[][] marbleMarket, Marble lonelyMarble, SoloToken soloToken, ThinPlayer actualPlayer, List<ThinPlayer> opponents) {
        setGame(new ThinGame(cardsMarket, marbleMarket, lonelyMarble, soloToken, actualPlayer, opponents));
    }

    @Override
    public void updateWarehouse(ThinWarehouse warehouse, String nickname) {
        getGame().getPlayer(nickname).setWarehouse(warehouse);
    }

    @Override
    public void updateStrongbox(CollectionResources strongbox, String nickname) {
        getGame().getPlayer(nickname).setStrongbox(strongbox);
    }

    @Override
    public void updateMarbleMarket(Marble[][] marbleMarket, Marble lonelyMarble) {
        getGame().setMarbleMarket(marbleMarket);
        getGame().setLonelyMarble(lonelyMarble);
    }

    @Override
    public void updateCardsMarket(DevelopmentCard[][] cardsMarket) {
        getGame().setCardsMarket(cardsMarket);
    }

    @Override
    public void updateTrack(Map< String, ThinTrack > tracks) {
        getGame().updateTracks(tracks);
    }

    @Override
    public void updatePosition(int position) {
        setPosition(position);
    }

    @Override
    public void updateBufferMarbles(int marbles) {
        setMarbles(marbles);
    }

    @Override
    public void updateBufferGainedMarbles(List<Resource> gainedMarbles) {
        setGainedFromMarbleMarket(gainedMarbles);
    }

    @Override
    public void updateProductionPower(ThinProductionPower productionPower, String nickname) {
        getGame().getPlayer(nickname).setProductionPower(productionPower);
    }

    @Override
    public void updateCard(int level, CardColor color, DevelopmentCard card) {
        getGame().setCard(level, color, card);
    }

    @Override
    public void updateToken(SoloToken token) {
        getGame().setSoloToken(token);
    }

    @Override
    public void updateLeaderCards(List<ThinLeaderCard> leaderCards, String nickname) {

        if (!nickname.equals(getGame().getMyself().getNickname()))
            leaderCards.stream().filter(card -> !card.isActive()).forEach(ThinLeaderCard::hide);

        getGame().getPlayer(nickname).
                setLeaderCards(leaderCards.stream().map(ThinPlayer::recreate).collect(Collectors.toList()));
    }

    @Override
    public void updateTrack(ThinTrack track, String nickname) {
        getGame().getPlayer(nickname).setTrack(track);
    }

}
