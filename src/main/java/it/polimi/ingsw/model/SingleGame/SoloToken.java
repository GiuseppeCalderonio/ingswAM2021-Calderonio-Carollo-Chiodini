package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;

/**
 * this class represent the solo token of the single game.
 * this interface is implemented by the classes CardToken and CardToken
 */
public interface SoloToken {

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

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method get the png associated with the token
     * @return the png associated with the token
     */
    String getPng();
}
