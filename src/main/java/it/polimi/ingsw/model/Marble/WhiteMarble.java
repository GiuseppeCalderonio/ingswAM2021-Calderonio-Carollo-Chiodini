package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;
import javafx.scene.paint.Color;

/**
 * this class implements the marble interface and represent the white marble
 */
public class WhiteMarble implements Marble{

    /**
     * this method return zero faith point
     * @return zero faith point
     */
    @Override
    public int faithPoints() {

        return 0;
    }
    /**
     * this method return a null resource
     * @return null
     */
    @Override
    public Resource convert() {

        return null;
    }

    /**
     * this method verify if the input is a white marble
     * @param toCompare this is the marble to compare
     * @return true if toCompare is a white marble, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return (toCompare instanceof WhiteMarble);
    }

    @Override
    public String toString() {
        return "White";
    }

    /**
     * this method get the color of the marble
     *
     * @return white
     */
    @Override
    public Color getColor() {
        return Color.WHITE;
    }

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method returns the BackColor associated to the Marbles
     */
    @Override
    public BackColor getBackColor() {
        return BackColor.ANSI_BRIGHT_BG_WHITE;
    }

}