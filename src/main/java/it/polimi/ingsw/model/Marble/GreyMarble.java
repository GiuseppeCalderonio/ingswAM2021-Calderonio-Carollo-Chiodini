package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.Stone;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;
import javafx.scene.paint.Color;

/**
 * this class implements the marble interface and represent the grey marble
 */
public class GreyMarble implements Marble {

    /**
     * this method return zero faith point
     * @return zero faith point
     */
    @Override
    public int faithPoints() {
        return 0;
    }
    /**
     * this method return a stone resource
     * @return stone resource
     */
    @Override
    public Resource convert() {
        return new Stone();
    }

    /**
     * this method verify if the input is a grey marble
     * @param toCompare this is the marble to compare
     * @return true if toCompare is a grey marble, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return (toCompare instanceof GreyMarble);
    }

    @Override
    public String toString() {
        return "Grey";
    }

    /**
     * this method get the color of the marble
     * @return grey
     */
    public Color getColor() {
        return Color.GREY;
    }

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the BackColor associated to the Marbles
     */
    @Override
    public BackColor getBackColor() {
        return BackColor.ANSI_BRIGHT_BG_BLACK;
    }
}
