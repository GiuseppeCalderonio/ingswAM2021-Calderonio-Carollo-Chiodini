package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.SingleGame.SoloToken;

import java.util.*;
import java.util.stream.Collectors;

/**
 * this class represent the thin game
 */
public class ThinGame {

    /**
     * this attribute represent the player associated with the host of the object
     */
    private final ThinPlayer myself;

    /**
     * this attribute represent a list of opponents of the game
     */
    private final List<ThinPlayer> opponents;

    /**
     * this attribute represent the upper layer of the cards market
     */
    private DevelopmentCard[][] cardsMarket;

    /**
     * this attribute represent the marble market
     */
    private Marble[][] marbleMarket;

    /**
     * this attribute represent the lonely marble
     */
    private Marble lonelyMarble;

    /**
     * this attribute represent the solo token, it is null if the game is not a single one
     */
    private SoloToken soloToken;

    /**
     * this constructor create the object starting from all the necessary attributes
     * @param cardsMarket this is the cards market to set
     * @param marbleMarket this is the marble market to set
     * @param lonelyMarble this is the lonely marble to set
     * @param soloToken this is the solo token to set
     * @param actualPlayer this is the actual player to set
     * @param opponents these are the opponents to set
     */
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

    /**
     * this method set the cards market
     * @param cardsMarket this is the cards market to set
     */
    public void setCardsMarket(DevelopmentCard[][] cardsMarket) {
        this.cardsMarket = cardsMarket;
    }

    /**
     * this method set the lonely marble
     * @param lonelyMarble this is the lonely marble to set
     */
    public void setLonelyMarble(Marble lonelyMarble) {
        this.lonelyMarble = lonelyMarble;
    }

    /**
     * this method set the marble market to set
     * @param marbleMarket this is the marble market to set
     */
    public void setMarbleMarket(Marble[][] marbleMarket) {
        this.marbleMarket = marbleMarket;
    }

    /**
     * this method set the solo token
     * @param soloToken this is the solo token to set
     */
    public void setSoloToken(SoloToken soloToken) {
        this.soloToken = soloToken;
    }

    /**
     * this method get the host owner player
     * @return the host owner player
     */
    public ThinPlayer getMyself() {
        return myself;
    }

    /**
     * this method get the list of opponents of the game
     * @return the list of opponents of the game
     */
    public List<ThinPlayer> getOpponents() {
        return opponents;
    }

    /**
     * this method get the cards market
     * @return the cards market
     */
    public DevelopmentCard[][] getCardsMarket() {
        return cardsMarket;
    }

    /**
     * this method set the card specified on the input in the
     * level or color specified on the input.
     * if the card is null, the method works the same setting a null value
     * @param level this is the level of the card to set
     * @param color this is the color of the card to set
     * @param card this is the card to set
     */
    public void setCard(int level, CardColor color, DevelopmentCard card){
        cardsMarket[2 - (level - 1)][color.getIndex()] = card;
    }

    /**
     * this method get the lonely marble
     * @return the lonely marble
     */
    public Marble getLonelyMarble() {
        return lonelyMarble;
    }

    /**
     * this method get the marble market
     * @return the marble market
     */
    public Marble[][] getMarbleMarket() {
        return marbleMarket;
    }

    /**
     * this method get the solo token
     * @return the solo token
     */
    public SoloToken getSoloToken() {
        return soloToken;
    }

    /**
     * this method get the player by nickname.
     * return null if any player match the nickname given in input
     * @param nickname this is the nickname of the player to get
     * @return the player with the nickname given in input, null if there in any match
     */
    public ThinPlayer getPlayer(String nickname){
        if (myself.getNickname().equals(nickname))
            return myself;
        for (ThinPlayer opponent : opponents){
            if (opponent.getNickname().equals(nickname))
                return opponent;
        }
        return null;
    }

    /**
     * this method update the tracks
     * @param tracks this is a map representing a track as a value associated with
     *               the player with the nickname as key
     */
    public void updateTracks(Map<String, ThinTrack> tracks){

        List<ThinPlayer> players = new LinkedList<>(opponents);
        players.add(myself);

        players.forEach(player -> player.setTrack(tracks.get(player.getNickname())));
    }
}
