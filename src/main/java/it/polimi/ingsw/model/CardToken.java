package it.polimi.ingsw.model;

public class CardToken implements SoloToken{

    private CardColor color;

    public CardToken(CardColor color) {
        this.color = color;
    }

    /**
     * @param inTrackCase
     * @param inCardsCase
     * @return
     */
    @Override
    public boolean action(Game inTrackCase, CardsMarket inCardsCase) {
        if(inCardsCase.popCard(1,color)!=null) {
                if (inCardsCase.popCard(1, color) != null)
                    return false;
                if (inCardsCase.popCard(2, color) != null)
                    return false;
                inCardsCase.popCard(3, color);
                return  false;
            }
        if(inCardsCase.popCard(2,color)!=null) {
            if (inCardsCase.popCard(2, color) != null)
                return false;
            inCardsCase.popCard(3, color);
            return false;
        }
        inCardsCase.popCard(3,color);
        if(inCardsCase.popCard(3,color) != null)
            return false;
        inTrackCase.checkEndGame();
        return false;
    }

}
