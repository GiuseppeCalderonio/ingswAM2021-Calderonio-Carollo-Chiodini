
package it.polimi.ingsw.view.cli.graphic;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.view.cli.graphic.utilities.CharStream;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.ForeColor;

public class GraphicalLeaderCard implements CharFigure {
    private final CharStream stream;
    private final LeaderCard leaderCard;
    private final int width;
    private final int height;
    private final BackColor cardColor;



    public GraphicalLeaderCard(CharStream stream, LeaderCard leaderCard, int width, int height){
        this.leaderCard = leaderCard;
        this.stream = stream;
        this.width = width;
        this.height = height;
        this.cardColor = leaderCard.getResource().getColor();
    }

    /**
     *this method returns the width of the GraphicalLeaderCard
     */
    public int getWidth() {
        return width;
    }

    /**
     *this method returns the height of the GraphicalLeaderCard
     */
    public int getHeight() {
        return height;
    }

    /**
     *this method draw the GraphicalLeaderCard in the CharStream at the default position
     */
    @Override
    public void draw() { draw(CharStream.defaultX, CharStream.defaultY); }

    /**
     *this method draw the GraphicalLeaderCard in the CharStream at X,Y position
     *@param relX X position to be considered as X absolute zero when drawing
     *@param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {
        for(int i = 0; i <= width; ++i){
            for(int j = 0; j <= height; ++j){
                if(j==0)
                    stream.addColor(i + relX, j + relY, cardColor);
                else stream.addColor(i + relX, j + relY, BackColor.ANSI_BG_WHITE);
            }
        }
        int midPointCard = (int) (relX + Math.ceil(width * 1.0 / 2.0));
        int midHeight = (int) (relX + Math.ceil(height * 1.0 / 2.0));
        //stream.addChar(leaderCard.identifier(), midPointCard, relY, cardColor); //resource associated
        stream.addString(relX, relY, leaderCard.identifier(), cardColor);
        stream.addString(midPointCard, height + relY, String.valueOf(leaderCard.getVictoryPoints()), ForeColor.ANSI_BLACK, BackColor.ANSI_BG_WHITE); // victory points
        if (leaderCard.isActive())
            stream.addChar('A', relX, height + relY, ForeColor.ANSI_BLACK, BackColor.ANSI_BG_WHITE);
        else stream.addChar('U', relX, height + relY, ForeColor.ANSI_BLACK, BackColor.ANSI_BG_WHITE);
        stream.addString(relX,  relY + 1, leaderCard.getRequirements().identifier(),ForeColor.ANSI_BLACK, BackColor.ANSI_BG_WHITE);
        stream.addColor(relX+4, relY + 1, leaderCard.getRequirements().colors().get(0));
        if (leaderCard.getRequirements().colors().size()==2)
            stream.addColor(relX + 5, relY + 1, leaderCard.getRequirements().colors().get(1));
        if (leaderCard.getRequirements().colors().size()==3) {
            stream.addChar('2',relX+4, relY + 1, ForeColor.ANSI_BLACK, leaderCard.getRequirements().colors().get(0));
            stream.addColor(relX + 5, relY + 1, leaderCard.getRequirements().colors().get(2));
        }


    }
}

