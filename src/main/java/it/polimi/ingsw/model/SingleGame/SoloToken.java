package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;

public interface SoloToken {

    /**
     *this method
     * @return
     */
    boolean action (Game inTrackCase, CardsMarket inCardsCase);
}
