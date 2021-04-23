package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;

/**
 * this class implements the marble interface and represent the white marble
 */
public class WhiteMarble implements Marble {
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
}