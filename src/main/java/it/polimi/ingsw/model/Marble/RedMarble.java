package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;
import javafx.scene.paint.Color;

/**
 * this class implements the marble interface and represent the red marble
 */
public class RedMarble implements Marble {

    /**
     * this method return one faith point
     * @return one faith point
     */
    @Override
    public int faithPoints() {
        return 1;
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
     * this method verify if the input is a red marble
     * @param toCompare this is the marble to compare
     * @return true if toCompare is a red marble, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return (toCompare instanceof RedMarble);
    }

    @Override
    public String toString() {
        return "Red";
    }

    /**
     * this method get the color of the marble
     *
     * @return red
     */
    @Override
    public Color getColor() {
        return Color.RED;
    }

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the BackColor associated to the Marbles
     */
    @Override
    public BackColor getBackColor() {
        return BackColor.ANSI_BG_RED;
    }

}

