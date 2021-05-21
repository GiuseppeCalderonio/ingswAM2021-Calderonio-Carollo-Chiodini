package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.Shield;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;
import javafx.scene.paint.Color;

/**
 * this class implements the marble interface and represent the blue marble
 */
public class BlueMarble implements Marble {

    /**
     * this method return zero faith point
     * @return zero faith point
     */
    @Override
    public int faithPoints() {
        return 0;
    }
    /**
     * this method return a shield resource
     * @return shield resource
     */
    @Override
    public Resource convert() {
        return new Shield();
    }

    /**
     * this method verify if the input is a blue marble
     * @param toCompare this is the marble to compare
     * @return true if toCompare is a blue marble, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return (toCompare instanceof BlueMarble);
    }

    @Override
    public String toString() {
        return "Blue";
    }

    /**
     * this method get the color of the marble
     * @return blue
     */
    public Color getColor() {
        return Color.BLUE;
    }

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the BackColor associated to the Marble
     */
    @Override
    public BackColor getBackColor() {
        return BackColor.ANSI_BG_BLUE;
    }
}