package it.polimi.ingsw.view.cli.graphic;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.view.cli.graphic.utilities.CharStream;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.ForeColor;

import java.util.List;

public class GraphicalInitializingLeaderCard implements CharFigure{
    private final CharStream stream;
    private final List<LeaderCard> leaderCards;
    private static final int width = 90;
    private static final int height = 30;

    public GraphicalInitializingLeaderCard(CharStream stream, List<LeaderCard> leaderCards) {
        this.stream = stream;
        this.leaderCards = leaderCards;
    }

    /**
     *this method draws the GraphicalInitializingLeaderCard in the CharStream at the default position
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     * this method draws the GraphicalInitializingLeaderCard in the CharStream at X,Y position
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {
        //heading
        stream.addString(relX+5, relY+3, "You can hold only two Leader Cards. Choose leaderCards that you " +
                "want to discard writing their number located below them", ForeColor.ANSI_BRIGHT_WHITE, BackColor.ANSI_BRIGHT_BG_BLACK);

        int x=5;
        int y=10;
        for(int i=0; i<=3; i++) {
            GraphicalLeaderCard graphicalLeaderCard = new GraphicalLeaderCard(stream, leaderCards.get(i),5,2);
            graphicalLeaderCard.draw(relX+x,relY+y);
            stream.addString(relX+x+2, relY+y+4, String.valueOf(i+1), ForeColor.ANSI_RED, BackColor.ANSI_BRIGHT_BG_BLACK);
            x=x+40;
        }
        y++;
        y++;

        //caption
        stream.addString(relX+5, relY+y+5, "a Leader Card is a special card that later in the game you can choose to activate o discard,",ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+6,"if you choose to discard it you obtain a faith point otherwise to activate it to benefit from his power you need to respect a requirement that is pointed out in the second line of the card:", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+7,"'res' indicates that you must have at least five resources of the type specified by the color; ",ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+8, "'lev' indicates that you must have a card of level 2 of the specified color",ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+9,  "'col' indicates that you must have at least one card of each color specified",ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+10,  "then once activated depending on the Leader Card type, pointed out in the first line of the card, you can benefit of different advantages:",ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+11, "the type SHELF indicates that you can add a new shelf of capacity 3 at the warehouse that can contain resources of the type specified by the color of the first line",ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+12, "the type DISC indicates that you can buy a Development Card paying with a discount equal to the indicated resource by the color of the first line of the card",ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+13,"the type PROD indicates that you can use a new Production with input the resource indicated by the color of the first line and output a faith point and a resource of your choice",ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+14,"the type MARBL indicates that every time you pick a white marble from the Marbles Market you can choose to convert it with the marble of the color of first line of the card",ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
        stream.addString(relX+5,relY+y+15,"at the bottom of the card are pointed out how many victory points is it worth", ForeColor.ANSI_BLACK, BackColor.ANSI_BRIGHT_BG_BLACK);
    }

}
