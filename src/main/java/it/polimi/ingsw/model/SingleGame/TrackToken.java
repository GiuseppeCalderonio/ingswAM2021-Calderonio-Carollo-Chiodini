package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;

public class TrackToken implements SoloToken {

    private boolean shuffle;
    private int faithPoints;

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
}