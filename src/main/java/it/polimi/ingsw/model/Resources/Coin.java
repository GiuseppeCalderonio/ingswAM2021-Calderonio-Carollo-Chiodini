package it.polimi.ingsw.model.Resources;

import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.YellowMarble;

/**
 * this class represent the coin resource
 */
public class Coin implements Resource {

    /**
     * this method returns a coin type
     * @return a coin type
     */
    @Override
    public ResourceType getType() {
        return ResourceType.COIN;
    }

    /**
     * this method convert the resource to his marble associated
     * @return a yellow marble
     */
    @Override
    public Marble convertInMarble(){ return new YellowMarble(); }

    /**
     * this method verify if the argument toCompare has the same type of the resource that calls the method
     * @param toCompare this is the resource to verify
     * @return true if toCompare is a coin, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return toCompare instanceof Coin;
    }
}
