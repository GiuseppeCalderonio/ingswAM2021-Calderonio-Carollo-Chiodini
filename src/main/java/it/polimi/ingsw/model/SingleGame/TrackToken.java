package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;

/**
 * thi class represent the track token
 */
public class TrackToken implements SoloToken {

    /**
     * this attribute indicates if, when invoking the action method,
     * will cause a shuffle to the stack of tokens
     */
    private final boolean shuffle;

    /**
     * this attribute represent the faith points that lorenzo gain when
     * , at the end of the turn, the method action is called on this object
     */
    private final int faithPoints;

    /**
     * this constructor create the track token setting the faith points and if
     * the token will cause a shuffle on the stack
     * @param faithPoints these are the faith points to set
     * @param shuffle this is the boolean shuffle to set
     */
    public TrackToken(int faithPoints, boolean shuffle) {
        this.faithPoints = faithPoints;
        this.shuffle = shuffle;
    }

    /**
     *this method add faithPoints in the number as in the attribute to Lorenzo il magnifico
     * @param inTrackCase contains the game
     * @param inCardsCase contains the CardsMarket
     * @return true if shuffle is true false otherwise
     */
    @Override
    public boolean action(Game inTrackCase, CardsMarket inCardsCase) {
        inTrackCase.addFaithPointsExceptTo(inTrackCase.getActualPlayer(), faithPoints);
        return shuffle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrackToken that = (TrackToken) o;
        return shuffle == that.shuffle && faithPoints == that.faithPoints;
    }

    @Override
    public String toString() {
        return "TrackToken:" +
                "shuffle= " + shuffle +
                ", faithPoints= " + faithPoints ;
    }

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method returns true only if the token requires to shuffle the deck
     */
    public boolean getShuffle(){
        return shuffle;
    }
}