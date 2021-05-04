
package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

public class GraphicalMarbleMarket implements CharFigure{
    private final CharStream stream;
    private static final int width = 15;
    private static final int height = 8;
    private final Marble[][] marketTray;
    private final Marble lonelyMarble;


    public GraphicalMarbleMarket(CharStream stream, Marble[][] marketTray, Marble lonelyMarble) {
        this.stream = stream;
        this.marketTray = marketTray;
        this.lonelyMarble = lonelyMarble;
    }

    /**
     *this method returns the width of the GraphicalMarbleMarket
     */
    public int getWidth() {
        return width;
    }

    /**
     *this method returns the height of the GraphicalMarbleMarket
     */
    public int getHeight() {
        return height;
    }

    /**
     *this method draws the GraphicalMarbleMarket in the CharStream at the default position
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     *this method draws the GraphicalMarketMarble in the CharStream at X,Y position
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {
        //initialize white matrix
        for(int i = 0; i <= width; ++i) {
            for (int j = 0; j <= height; ++j) {
                stream.addColor(i + relX, j + relY, BackColor.ANSI_BG_GREEN);
            }
        }

        // write "marbles market" on the top
        stream.addString(relX, relY, " MARBLES MARKET", ForeColor.ANSI_BRIGHT_CYAN, BackColor.ANSI_BG_GREEN);

        // add marbles to the matrix
        int j, z=0;
        for (int i=2; i<=6; i = i+2) { //rows (y)
            int k=0;
            j=2;
            while (j<=12){
                if (j==2) {
                    stream.addColor(j + relX, i + relY, marketTray[z][k].getBackColor());
                    j++;
                    stream.addColor(j + relX, i + relY, marketTray[z][k].getBackColor());
                    k++;
                }
                else if (j==5) {
                    stream.addColor(j + relX, i + relY, marketTray[z][k].getBackColor());
                    j++;
                    stream.addColor(j + relX, i + relY, marketTray[z][k].getBackColor());
                    k++;
                }
                else if (j==8) {
                    stream.addColor(j + relX, i + relY, marketTray[z][k].getBackColor());
                    j++;
                    stream.addColor(j + relX, i + relY, marketTray[z][k].getBackColor());
                    k++;
                }
                else if (j==11) {
                    stream.addColor(j + relX, i + relY, marketTray[z][k].getBackColor());
                    j++;
                    stream.addColor(j + relX, i + relY, marketTray[z][k].getBackColor());
                    k++;
                }
                j++;
            }
            z++;
        }

        // add lonelyMarble to the matrix
        stream.addColor(width + relX, 1+ relY, lonelyMarble.getBackColor());
        stream.addColor(width+relX-1, 1+ relY, lonelyMarble.getBackColor());

        j=1;
        for (int i=0; i<=13; i++) {
            stream.addColor(i+relX, 1+relY, BackColor.ANSI_BG_WHITE);
            if(j<=6) {
                stream.addColor(relX, j + relY, BackColor.ANSI_BG_WHITE);
                stream.addColor(relX+1, j + relY, BackColor.ANSI_BG_WHITE);
                j++;
            }
        }

        int k=1;
        for (int i=2; i<=11; i = i+3) {
            stream.addChar('^', relX + i, relY + height - 1, ForeColor.ANSI_BRIGHT_YELLOW, BackColor.ANSI_BG_GREEN);
            stream.addString(relX + i, relY + height, String.valueOf(k), ForeColor.ANSI_BLACK, BackColor.ANSI_BG_GREEN);
            k++;
        }

        k=1;
        for (int i=2; i<=6; i = i+2 ) {
            stream.addChar('<', relX + width - 1, relY + i, ForeColor.ANSI_BRIGHT_YELLOW, BackColor.ANSI_BG_GREEN);
            stream.addString(relX + width, relY + i, String.valueOf(k), ForeColor.ANSI_BLACK, BackColor.ANSI_BG_GREEN);
            k++;
        }

    }

}

