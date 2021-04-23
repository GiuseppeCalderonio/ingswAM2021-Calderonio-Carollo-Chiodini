package it.polimi.ingsw.model.Resources;

import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.PurpleMarble;

/**
 * this class represent the servant resource
 */
public class Servant implements Resource {

    /**
     * this method returns a servant type
     * @return a servant type
     */
    @Override
    public ResourceType getType() {
        return ResourceType.SERVANT;
    }

    /**
     * this method convert the resource to his marble associated
     * @return a purple marble
     */
    @Override
    public Marble convertInMarble(){return new PurpleMarble();}

    /**
     * this method verify if the argument toCompare has the same type of the resource that calls the method
     * @param toCompare this is the resource to verify
     * @return true if toCompare is a servant, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return toCompare instanceof Servant;
    }

    /**
     * this method get the string "servant"
     * @return the string "servant"
     */
    @Override
    public String toString() {
        return getType().getName();
    }
}

