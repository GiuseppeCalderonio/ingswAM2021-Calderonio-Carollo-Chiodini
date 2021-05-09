
package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.BackColor;
import it.polimi.ingsw.view.utilities.colors.ForeColor;

public class GraphicalProduction implements CharFigure{
    private final CharStream stream;
    private final CollectionResources inputResources;
    private final CollectionResources outputResources;
    private final int width;
    private final int height;
    private final int faithPoints;


    public GraphicalProduction(CharStream stream, CollectionResources inputResources, CollectionResources outputResources, int faithPoints, int height, int width) {
        this.stream = stream;
        this.inputResources = inputResources;
        this.outputResources = outputResources;
        this.faithPoints = faithPoints;
        this.height = height;
        this.width = width;
    }

    /**
     *this method returns the width of the GraphicalProduction
     */
    public int getWidth() {
        return width;
    }

    /**
     *this method returns the height of the GraphicalProduction
     */
    public int getHeight() {
        return height;
    }

    /**
     *this method draws the GraphicalProduction in the CharStream at the default position
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     *this method draws the GraphicalProduction in the CharStream at X,Y position
     * @param relX X position to be considered as X absolute zero when drawing
     * @param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {


        int i = 0;
        stream.addString(relX+i, relY, String.valueOf(inputResources.getMaps().get(0).getCardinality()), ForeColor.ANSI_BLACK, inputResources.getMaps().get(0).getResource().getColor());
        i++;
        if(inputResources.getMaps().size()==2) {
            stream.addString(relX + i, relY, String.valueOf(inputResources.getMaps().get(1).getCardinality()), ForeColor.ANSI_BLACK, inputResources.getMaps().get(1).getResource().getColor());
            i++;
        }
        stream.addChar('-', relX+i, relY, ForeColor.ANSI_BRIGHT_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        i++;
        stream.addChar('>', relX+i, relY, ForeColor.ANSI_BRIGHT_BLACK, BackColor.ANSI_BRIGHT_BG_WHITE);
        i++;
        if (!outputResources.getMaps().isEmpty()) {
            stream.addString(relX+i, relY, String.valueOf(outputResources.getMaps().get(0).getCardinality()), ForeColor.ANSI_BLACK, outputResources.getMaps().get(0).getResource().getColor());
            i++;
            if(outputResources.getMaps().size()>=2) {
                stream.addString(relX + i, relY, String.valueOf(outputResources.getMaps().get(1).getCardinality()), ForeColor.ANSI_BLACK, outputResources.getMaps().get(1).getResource().getColor());
                i++;
            }
            if(outputResources.getMaps().size()==3) {
                stream.addString(relX + i, relY, String.valueOf(outputResources.getMaps().get(2).getCardinality()), ForeColor.ANSI_BLACK, outputResources.getMaps().get(2).getResource().getColor());
                i++;
            }
        }
        stream.addString(relX+i, relY, String.valueOf(faithPoints), ForeColor.ANSI_BLACK, BackColor.ANSI_BG_RED);
    }

}

