package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;

import java.io.Serializable;

public interface SoloToken extends Serializable {

    /**
     *this method invoked by SingleGame and implemented by CardToken and TrackToken defines the behaviour of a token
     * when it is drawn by the deck of token
     * @return true if the deck of token needs to be shuffled false otherwise
     */
    boolean action (Game inTrackCase, CardsMarket inCardsCase);

    boolean equals(Object o);
}
