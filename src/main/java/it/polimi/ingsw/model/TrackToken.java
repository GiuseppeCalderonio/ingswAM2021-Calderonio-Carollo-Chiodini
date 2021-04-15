package it.polimi.ingsw.model;

public class TrackToken implements SoloToken{

    private boolean shuffle;
    private int faithPoints;

    public TrackToken(int faithPoints, boolean shuffle) {
        this.faithPoints = faithPoints;
        this.shuffle = shuffle;
    }

    /**
     *
     * @param inTrackCase
     * @param inCardsCase
     * @return
     */
    @Override
    public boolean action(Game inTrackCase, CardsMarket inCardsCase) {
        inTrackCase.addFaithPointsExceptTo(inTrackCase.getActualPlayer(), faithPoints);
        return shuffle;
    }
}