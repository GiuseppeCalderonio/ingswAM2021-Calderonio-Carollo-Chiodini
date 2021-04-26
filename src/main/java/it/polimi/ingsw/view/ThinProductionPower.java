package it.polimi.ingsw.view;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;

public class ThinProductionPower {

    private DevelopmentCard card = null;
    private int deckVictoryPoints = 0;

    public ThinProductionPower(DevelopmentCard card, int deckVictoryPoints) {
        this.card = card;
        this.deckVictoryPoints = deckVictoryPoints;
    }

    public int getDeckVictoryPoints() {
        return deckVictoryPoints;
    }

    public void setDeckVictoryPoints(int deckVictoryPoints) {
        this.deckVictoryPoints = deckVictoryPoints;
    }

    public DevelopmentCard getCard() {
        return card;
    }

    public void setCard(DevelopmentCard card) {
        this.card = card;
    }
}
