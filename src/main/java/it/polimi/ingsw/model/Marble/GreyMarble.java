package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.Stone;

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
}
