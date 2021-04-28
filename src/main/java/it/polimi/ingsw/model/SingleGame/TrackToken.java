package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;

public class TrackToken implements SoloToken {

    private final boolean shuffle;
    private final int faithPoints;

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
}