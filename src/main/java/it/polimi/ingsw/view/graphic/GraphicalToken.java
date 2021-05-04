package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.SingleGame.CardToken;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

public class GraphicalToken implements CharFigure {

    private CharStream stream;
    private final SoloToken soloToken;
    private final int width;
    private final int height;

    public GraphicalToken(CharStream stream, SoloToken soloToken, int width, int height) {
        this.stream = stream;
        this.soloToken = soloToken;
        this.width = width;
        this.height = height;
    }

    /**
     *this method returns the width of the GraphicalToken
     */
    public int getWidth() {
        return width;
    }

    /**
     *this method returns the height of the GraphicalToken
     */
    public int getHeight() {
        return height;
    }

    /**
     *this method draws the GraphicalToken in the CharStream at the default position
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     *this method draws the GraphicalToken in the CharStream at X,Y position
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {

        for(int i = 0; i <= width; ++i){
            for(int j = 0; j <= height; ++j){
                stream.addColor(i + relX, j + relY, BackColor.ANSI_BG_WHITE);
            }

            int midPointCard = (int) (Math.ceil(width * 1.0 / 2.0));
            int midPointHeight = (int) (Math.ceil(height * 1.0 / 2.0));
            String title = "TOKEN";
            int midPointName = title.length() / 2;

            // write "token" on the top
            stream.addString(relX +midPointCard - midPointName, relY, title, ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_WHITE);


            if (soloToken instanceof CardToken) {
                stream.addString(relX  , relY + midPointHeight, "-2", ForeColor.ANSI_BRIGHT_RED, BackColor.ANSI_BG_WHITE);
                stream.addColor(relX+2,relY + midPointHeight, soloToken.getBackColor());
                stream.addColor(relX+3,relY + midPointHeight, soloToken.getBackColor());
            } else {
                if (soloToken.getShuffle()) {
                    stream.addString(relX +1  , relY + midPointHeight, "+1", ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_BLACK);
                    stream.addChar('S',relX, relY+ height, ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_YELLOW);
                } else {
                    stream.addString(relX +1  , relY + midPointHeight, "+2", ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BG_BLACK);


                }


                stream.addColor(relX+3,relY + midPointHeight,BackColor.ANSI_BG_BLACK);
                stream.addColor(relX+2,relY + midPointHeight -1,BackColor.ANSI_BG_BLACK);
                stream.addColor(relX+2,relY + midPointHeight +1,BackColor.ANSI_BG_BLACK);
            }
        }
    }


}
