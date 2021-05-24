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

    private ThinGame game;
    private int position = 0;
    private List<Resource> gainedFromMarbleMarket;
    private int marbles;

    public List<Resource> getGainedFromMarbleMarket() {
        return gainedFromMarbleMarket;
    }

    public void setGainedFromMarbleMarket(List<Resource> gainedFromMarbleMarket) {
        this.gainedFromMarbleMarket = gainedFromMarbleMarket;
    }

    public ThinGame getGame() {
        return game;
    }

    public void setGame(ThinGame game) {
        this.game = game;
    }

    public int getMarbles() {
        return marbles;
    }

    public void setMarbles(int marbles) {
        this.marbles = marbles;
    }

    public int getPosition() {
        return position;
    }

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
