package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Coin;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.view.utilities.colors.BackColor;

/**
 * this class implements the marble interface and represent the yellow marble
 */
public class YellowMarble implements Marble{

    private final MarbleColor color = MarbleColor.YELLOW;

    /**
     * this method return zero faith point
     * @return zero faith point
     */
    @Override
    public int faithPoints() {
        return 0;
    }
    /**
     * this method return a coin resource
     * @return coin resource
     */
    @Override
    public Resource convert() {
        return new Coin();
    }

    /**
     * this method verify if the input is a yellow marble
     * @param toCompare this is the marble to compare
     * @return true if toCompare is a yellow marble, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return (toCompare instanceof YellowMarble);
    }

    @Override
    public String toString() {
        return "Yellow";
    }

    /**
     * this method get the color of the marble
     *
     * @return yellow
     */
    @Override
    public MarbleColor getColor() {
        return color;
    }
    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the BackColor associated to the Marbles
     */
    @Override
    public BackColor getBackColor() {
        return BackColor.ANSI_BG_YELLOW;
    }
}
