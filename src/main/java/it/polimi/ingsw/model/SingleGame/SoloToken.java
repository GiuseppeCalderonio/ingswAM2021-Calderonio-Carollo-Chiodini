package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.utilities.colors.BackColor;

import java.io.Serializable;

public interface SoloToken extends Serializable {

    /**
     *this method invoked by SingleGame and implemented by CardToken and TrackToken defines the behaviour of a token
     * when it is drawn by the deck of token
     * @return true if the deck of token needs to be shuffled false otherwise
     */
    boolean action (Game inTrackCase, CardsMarket inCardsCase);

    boolean equals(Object o);

    String toString();

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the color associated to the token if it is a CardToken
     */
    default BackColor getBackColor() {
        return null;
    }

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns true only if the token requires to shuffle the deck
     */
    default boolean getShuffle() {
        return false;
    }
}
