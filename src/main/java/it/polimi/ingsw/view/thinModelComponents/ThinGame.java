package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.SingleGame.SoloToken;

import java.util.List;
import java.util.stream.Collectors;

import static it.polimi.ingsw.network.Client.createLeaderCards;

public class ThinGame {

    private ThinPlayer myself;
    private List<ThinPlayer> opponents;
    private final List<LeaderCard> allLeaderCards = createLeaderCards();
    private DevelopmentCard[][] cardsMarket;
    private Marble[][] marbleMarket;
    private Marble lonelyMarble;
    private SoloToken soloToken;

    public ThinGame(DevelopmentCard[][] cardsMarket,
                    Marble[][] marbleMarket,
                    Marble lonelyMarble,
                    SoloToken soloToken,
                    ThinPlayer actualPlayer,
                    List<ThinPlayer> opponents){
        this.cardsMarket = cardsMarket;
        this.marbleMarket = marbleMarket;
        this.lonelyMarble = lonelyMarble;
        this.soloToken = soloToken;
        this.myself = actualPlayer;
        // this part of code is used to use the constructor that recreate the leader cards from the thin ones
        this.opponents = opponents.stream().
                map(ThinPlayer::new).
                collect(Collectors.toList());
    }

    public void setCardsMarket(DevelopmentCard[][] cardsMarket) {
        this.cardsMarket = cardsMarket;
    }

    public void setLonelyMarble(Marble lonelyMarble) {
        this.lonelyMarble = lonelyMarble;
    }

    public void setMarbleMarket(Marble[][] marbleMarket) {
        this.marbleMarket = marbleMarket;
    }

    public void setMyself(ThinPlayer myself) {
        this.myself = new ThinPlayer(myself);
    }

    public void setOpponents(List<ThinPlayer> opponents) {
        this.opponents = opponents;
    }

    public void setSoloToken(SoloToken solotoken) {
        this.soloToken = solotoken;
    }

    public ThinPlayer getMyself() {
        return myself;
    }

    public List<ThinPlayer> getOpponents() {
        return opponents;
    }

    public DevelopmentCard[][] getCardsMarket() {
        return cardsMarket;
    }

    public void setCard(int level, CardColor color, DevelopmentCard card){
        cardsMarket[2 - (level - 1)][color.getIndex()] = card;
    }

    public List<LeaderCard> getAllLeaderCards() {
        return allLeaderCards;
    }

    public Marble getLonelyMarble() {
        return lonelyMarble;
    }

    public Marble[][] getMarbleMarket() {
        return marbleMarket;
    }

    public SoloToken getSoloToken() {
        return soloToken;
    }
}
