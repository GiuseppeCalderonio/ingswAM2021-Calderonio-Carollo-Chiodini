package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.Shield;

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
}