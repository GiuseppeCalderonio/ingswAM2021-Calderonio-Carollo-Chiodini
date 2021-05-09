package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.view.utilities.CharStream;

public class GraphicalDevelopmentCardBelow implements CharFigure{

    private CharStream stream;
    private final DevelopmentCard developmentCard;
    private static final int width = 6;
    private static final int height = 0;


    public GraphicalDevelopmentCardBelow(CharStream stream, DevelopmentCard developmentCard) {
        this.stream = stream;
        this.developmentCard = developmentCard;
    }

    /**
     *this method returns the width of the GraphicalDevelopmentCardBelow
     */
    public int getWidth() {
        return width;
    }

    /**
     *this method returns the height of the GraphicalDevelopmentCardBelow
     */
    public int getHeight() {
        return height;
    }

    /**
     *
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     *
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {

        int midPointWidth = (int) (relX + Math.ceil(width * 1.0 / 2.0));

        //initialize the card a the respective color
        for(int i = 0; i <= width; ++i)
            for(int j = 0; j <= height; ++j)
                stream.addColor(i + relX, j + relY, developmentCard.getBackColor());

        //add the victory points to the card
        stream.addString(midPointWidth, height + relY, String.valueOf(developmentCard.getVictoryPoints()), developmentCard.getBackColor()); // victory points

    }
}
