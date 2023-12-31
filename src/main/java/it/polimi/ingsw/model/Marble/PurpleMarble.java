package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.Servant;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;
import javafx.scene.paint.Color;

/**
 * this class implements the marble interface and represent the purple marble
 */
public class PurpleMarble implements Marble {

    /**
     * this method return zero faith point
     * @return zero faith point
     */
    @Override
    public int faithPoints() {
        return 0;
    }
    /**
     * this method return a servant resource
     * @return servant resource
     */
    @Override
    public Resource convert() {
        return new Servant();
    }

    /**
     * this method verify if the input is a purple marble
     * @param toCompare this is the marble to compare
     * @return true if toCompare is a purple marble, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return (toCompare instanceof PurpleMarble);
    }

    @Override
    public String toString() {
        return "Purple";
    }

    /**
     * this method get the color of the marble
     *
     * @return purple
     */
    @Override
    public Color getColor() {
        return Color.PURPLE;
    }

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the BackColor associated to the Marbles
     */
    @Override
    public BackColor getBackColor() {
        return BackColor.ANSI_BG_PURPLE;
    }
}
