package it.polimi.ingsw.view.graphic;

import it.polimi.ingsw.model.Resources.*;
import it.polimi.ingsw.view.utilities.CharStream;
import it.polimi.ingsw.view.utilities.colors.ForeColor;
import it.polimi.ingsw.view.utilities.colors.BackColor;

public class GraphicalStrongbox implements CharFigure {
    private final CharStream stream;
    private CollectionResources strongboxResources = new CollectionResources();
    private final int width = 16;
    private final int height = 3;

    public GraphicalStrongbox(CharStream stream, CollectionResources strongboxResources){
        this.stream = stream;
        this.strongboxResources = strongboxResources;
    }

    /**
     *this method returns the width of the GraphicalStrongbox
     */
    public int getWidth() {
        return width;
    }

    /**
     *this method returns the height of the GraphicalStrongbox
     */
    public int getHeight() {
        return height;
    }

    /**
     *this method draw the GraphicalStrongbox in the CharStream at the default position
     */
    @Override
    public void draw() {
        draw(CharStream.defaultX, CharStream.defaultY);
    }

    /**
     *this method draw the GraphicalLeaderCard in the CharStream at X,Y position
     *@param relX X position to be considered as X absolute zero when drawing
     *@param relY Y position to be considered as Y absolute zero when drawing
     */
    @Override
    public void draw(int relX, int relY) {
        //these offset are used to shift correctly all strongbox
        int offsetX = 1;
        int offsetY = 1;
        for (int i = 0; i <= width; ++i) {
            for (int j = 0; j <= height; ++j) {
                //this is the base of strongbox
                stream.addColor(i + relX, j + relY, BackColor.ANSI_BRIGHT_BG_WHITE);
            }
        }
        //here there are the back color of resources

        if (strongboxResources.contains(new Coin())) {
            stream.addColor(relX + offsetX, relY + 2 * offsetY, BackColor.ANSI_BG_YELLOW);
            stream.addColor(relX + 2 * offsetX, relY + 2 * offsetY, BackColor.ANSI_BG_YELLOW);
            stream.addColor(relX + 3 * offsetX, relY + 2 * offsetY, BackColor.ANSI_BG_YELLOW);
            stream.addString(relX + offsetX,relY + 2 * offsetY,"x" ,ForeColor.ANSI_BRIGHT_WHITE , BackColor.ANSI_BG_YELLOW);
            stream.addString(relX + 2 * offsetX,relY + 2 * offsetY, Integer.toString(strongboxResources.getMaps().stream().filter(map -> map.getResource().equals(new Coin())).mapToInt(MapResources::getCardinality).sum()), ForeColor.ANSI_BRIGHT_WHITE , BackColor.ANSI_BG_YELLOW);
        }

        if (strongboxResources.contains(new Shield())) {
            stream.addColor(relX + 5 * offsetX, relY + 2 * offsetY, BackColor.ANSI_BG_BLUE);
            stream.addColor(relX + 6 * offsetX, relY + 2 * offsetY, BackColor.ANSI_BG_BLUE);
            stream.addColor(relX + 7 * offsetX, relY + 2 * offsetY, BackColor.ANSI_BG_BLUE);
            stream.addString(relX + 5 * offsetX,relY + 2 * offsetY,"x" ,ForeColor.ANSI_BRIGHT_WHITE , BackColor.ANSI_BG_BLUE);
            stream.addString(relX + 6 * offsetX,relY + 2 * offsetY, Integer.toString(strongboxResources.getMaps().stream().filter(map -> map.getResource().equals(new Shield())).mapToInt(MapResources::getCardinality).sum()), ForeColor.ANSI_BRIGHT_WHITE , BackColor.ANSI_BG_BLUE);
        }

        if (strongboxResources.contains(new Servant())) {
            stream.addColor(relX + 9 * offsetX, relY + 2 * offsetY, BackColor.ANSI_BG_PURPLE);
            stream.addColor(relX + 10 * offsetX, relY + 2 * offsetY, BackColor.ANSI_BG_PURPLE);
            stream.addColor(relX + 11 * offsetX, relY + 2 * offsetY, BackColor.ANSI_BG_PURPLE);
            stream.addString(relX + 9 * offsetX,relY + 2 * offsetY,"x" ,ForeColor.ANSI_BRIGHT_WHITE , BackColor.ANSI_BG_PURPLE);
            stream.addString(relX + 10 * offsetX,relY + 2 * offsetY, Integer.toString(strongboxResources.getMaps().stream().filter(map -> map.getResource().equals(new Servant())).mapToInt(MapResources::getCardinality).sum()), ForeColor.ANSI_BRIGHT_WHITE , BackColor.ANSI_BG_PURPLE);
        }

        if (strongboxResources.contains(new Stone())){
            stream.addColor(relX + 13 * offsetX, relY + 2 * offsetY, BackColor.ANSI_GREY);
            stream.addColor(relX + 14 * offsetX, relY + 2 * offsetY, BackColor.ANSI_GREY);
            stream.addColor(relX + 15 * offsetX, relY + 2 * offsetY, BackColor.ANSI_GREY);
            stream.addString(relX + 13 * offsetX,relY + 2 * offsetY,"x" ,ForeColor.ANSI_BRIGHT_WHITE , BackColor.ANSI_GREY);
            stream.addString(relX + 14 * offsetX,relY + 2 * offsetY, Integer.toString(strongboxResources.getMaps().stream().filter(map -> map.getResource().equals(new Stone())).mapToInt(MapResources::getCardinality).sum()), ForeColor.ANSI_BRIGHT_WHITE , BackColor.ANSI_GREY);
        }
        //this is the title of strongbox
        stream.addString(relX+4*offsetX, relY,"STRONGBOX",ForeColor.ANSI_RED);
    }
}

