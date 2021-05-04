package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;

public class CardToken implements SoloToken {

    private final CardColor color;

    public CardToken(CardColor color) {
        this.color = color;
    }

    /**
     * this method deletes two cards of the color in the attribute from the CardsMarket starting from the lowest levels
     * @param inTrackCase contains the game
     * @param inCardsCase contains the CardsMarket
     * @return always false
     */
    @Override
    public boolean action(Game inTrackCase, CardsMarket inCardsCase) {
        if (inCardsCase.checkCard(1,color)) {
            inCardsCase.popCard(1,color);
            inTrackCase.checkEndGame(); //miss column
            if (inCardsCase.checkCard(1,color)) {
                inCardsCase.popCard(1,color);
                inTrackCase.checkEndGame(); //miss column
                return false;
            }
            if (inCardsCase.checkCard(2,color)) {
                inCardsCase.popCard(2,color);
                inTrackCase.checkEndGame(); //miss column
                return false;
            }
            inCardsCase.popCard(3,color);
            inTrackCase.checkEndGame(); //miss column
            return false;
        }
        if(inCardsCase.checkCard(2,color)) {
            inCardsCase.popCard(2,color);
            inTrackCase.checkEndGame(); //miss column
            if(inCardsCase.checkCard(2,color)) {
                inCardsCase.popCard(2,color);
                inTrackCase.checkEndGame(); //miss column
                return false;
            }
            inCardsCase.popCard(3,color);
            inTrackCase.checkEndGame(); //miss column
            return false;
        }
        inCardsCase.popCard(3,color);
        inTrackCase.checkEndGame(); //miss column7
        inCardsCase.popCard(3,color);
        inTrackCase.checkEndGame(); //miss column7
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardToken cardToken = (CardToken) o;
        return color == cardToken.color;
    }

    @Override
    public String toString() {
        return "CardToken{" +
                "color=" + color;
    }

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method returns the color associated to the token
     */
    public BackColor getBackColor() {
        if (color.equals(CardColor.BLUE))
            return BackColor.ANSI_BRIGHT_BG_BLUE;
        else if (color.equals(CardColor.GREEN))
            return BackColor.ANSI_BRIGHT_BG_GREEN;
        else if(color.equals(CardColor.PURPLE))
            return BackColor.ANSI_BG_PURPLE;
        else return BackColor.ANSI_BG_YELLOW;
    }

}
