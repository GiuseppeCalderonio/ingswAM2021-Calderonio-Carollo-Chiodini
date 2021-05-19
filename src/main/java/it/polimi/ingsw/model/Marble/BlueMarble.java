package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.Shield;
import it.polimi.ingsw.view.utilities.colors.BackColor;

/**
 * this class implements the marble interface and represent the blue marble
 */
public class BlueMarble implements Marble {

    private final MarbleColor color = MarbleColor.BLUE;

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

    @Override
    public String getPng() {
        return "/marbles/biglia_blu.png";
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
    public MarbleColor getColor() {
        return color;
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