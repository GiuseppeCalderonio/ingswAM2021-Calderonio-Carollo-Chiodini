package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

public class GraphicalCardsMarket implements CharFigure{

    private CharStream stream;
    private DevelopmentCard[][] cardsMarket;
    private static final int width = 32;
    private static final int height = 15;


    public GraphicalCardsMarket(CharStream stream, DevelopmentCard[][] cardsMarket) {
        this.stream = stream;
        this.cardsMarket = cardsMarket;
    }

    /**
     *this method returns the width of the GraphicalCardsMarket
     */
    public int getWidth() {
        return width;
    }

    /**
     *this method returns the height of the GraphicalCardsMarket
     */
    public int getHeight() {
        return height;
    }

    /**
     *this method draws the GraphicalCardsMarket in the CharStream at the default position
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     *this method draws the GraphicalCardsMarket in the CharStream at X,Y position
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {
        for(int i = 0; i <= width; ++i) {
            for (int j = 0; j <= height; ++j) {
                stream.addColor(i + relX, j + relY, BackColor.ANSI_BRIGHT_BG_WHITE);
            }

            String title = "CARDS MARKET";
            int midPointCard = (int) (Math.ceil(width * 1.0 / 2.0));
            int midPointName = title.length() / 2;
            // write "cards market" on the top
            stream.addString(relX + midPointCard - midPointName, relY, title, ForeColor.ANSI_BRIGHT_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);


            int y=0;
            for (int x=1; x<=31; x++) {  //column
                for (int j = 1; j <= 14; j++) { //row
                    int k=0;
                    GraphicalDevelopmentCard graphicalDevelopmentCard = new GraphicalDevelopmentCard(stream, cardsMarket[k][y], 6, 3);
                    graphicalDevelopmentCard.draw(relX + x, relY + j);
                    //k++;
                    j = j + 4;
                }
                x = x +7;
                y++;
            }
        }
    }
}


