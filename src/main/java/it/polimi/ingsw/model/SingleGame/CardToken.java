package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.view.utilities.colors.BackColor;

/**
 * this class represent the card token
 */
public class CardToken implements SoloToken {

    /**
     * this attribute represent the color associated with the token.
     * in particular, during a single game, when the player end his turn,
     * if the token action is done from this object, will delete
     * from the cards market 2 cards of this color
     */
    private final CardColor color;

    /**
     * this attribute represent the png associated to each cardToken
     */
    private String png;

    /**
     * this constructor create the card token starting from the color; is used for cli
     * @param color this is the color to set
     */
    public CardToken(CardColor color) {this.color = color;}

    /**
     * this constructor is the same of cli constructor but has one more attribute; the png.
     * This constructor is used for gui.
     * @param color this attribute is the same as that of the other constructor
     * @param png this attribute contains the image linked to the token
     */
    public CardToken(CardColor color , String png) {
        this.color = color;
        String pngNameConstant = "/punchboard/cerchio"; //must be 1/2/3/4
        this.png = pngNameConstant + png + ".png";
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

    /**
     * this method is a development card png getter
     * @return a string tha symbolize the png image
     */
    public String getPng() {
        return png;
    }
}
