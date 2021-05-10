package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

@SuppressWarnings("SameReturnValue")
public class GraphicalBasicProduction implements CharFigure{

    private final CharStream stream;


    public GraphicalBasicProduction (CharStream stream){
        this.stream = stream;
    }

    /**
     *this method returns the width of the GraphicalBasicProduction
     */
    public int getWidth() {return 7;}

    /**
     *this method returns the height of the GraphicalBasicProduction
     */
    public int getHeight() {return 2;}

    /**
     * This method should be implemented by classes that will eventually use CharStream to draw themselves on it.
     */
    @Override
    public void draw() {
        draw(0, 0);
    }

    /**
     * This method should be implemented by classes that will eventually use CharStream to draw themselves on it given relative positions on the stream.
     *
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {
        for(int i = 0; i <= 7; ++i) {
            for (int j = 0; j <= 2; ++j) {
                stream.addColor(relX+i , relY+j , BackColor.ANSI_BG_BLACK);
            }
        }

        stream.addString(relX, relY , "(?)", ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_BLACK);
        stream.addString(relX, relY+2 , "(?)", ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_BLACK);
        stream.addString(relX+3, relY+1 , "}", ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_BLACK);
        stream.addString(relX+5, relY+1 , "(?)", ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_BLACK);
    }
}
