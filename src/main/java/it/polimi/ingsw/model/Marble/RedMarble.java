package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.view.utilities.colors.BackColor;

/**
 * this class implements the marble interface and represent the red marble
 */
public class RedMarble implements Marble {

    private final MarbleColor color = MarbleColor.RED;

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
    public MarbleColor getColor() {
        return color;
    }
}

