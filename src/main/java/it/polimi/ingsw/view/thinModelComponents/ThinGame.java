package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.SingleGame.SoloToken;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ThinGame {

    private ThinPlayer myself;
    private List<ThinPlayer> opponents;
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
        this.myself = new ThinPlayer(actualPlayer);
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

    public Marble getLonelyMarble() {
        return lonelyMarble;
    }

    public Marble[][] getMarbleMarket() {
        return marbleMarket;
    }

    public SoloToken getSoloToken() {
        return soloToken;
    }

    public ThinPlayer getPlayer(String nickname){
        if (myself.getNickname().equals(nickname))
            return myself;
        for (ThinPlayer opponent : opponents){
            if (opponent.getNickname().equals(nickname))
                return opponent;
        }
        return null;
    }

    public void updateTracks(Map<String, ThinTrack> tracks){

        List<ThinPlayer> players = new LinkedList<>(opponents);
        players.add(myself);

        players.forEach(player -> player.setTrack(tracks.get(player.getNickname())));
    }
}
