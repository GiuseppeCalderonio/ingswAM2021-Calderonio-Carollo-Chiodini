package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;

public class CardToken implements SoloToken {

    private CardColor color;

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
        if(inCardsCase.popCard(1,color)!=null) {
                if (inCardsCase.popCard(1, color) != null)
                    return false;
                if (inCardsCase.popCard(2, color) != null)
                    return false;
                if (inCardsCase.popCard(3, color)!=null)
                    return  false;
                inTrackCase.checkEndGame(); //miss column
            }
        if(inCardsCase.popCard(2,color)!=null) {
            if (inCardsCase.popCard(2, color) != null)
                return false;
            if (inCardsCase.popCard(3, color)!=null)
                return false;
            inTrackCase.checkEndGame(); //miss column
        }
        inCardsCase.popCard(3,color);
        if(inCardsCase.popCard(3,color) != null)
            return false;
        inTrackCase.checkEndGame();
        return false;
    }

}
