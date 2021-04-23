package it.polimi.ingsw.model.Resources;

import it.polimi.ingsw.model.Marble.BlueMarble;
import it.polimi.ingsw.model.Marble.Marble;

/**
 * this class represent the shield resource
 */

public class Shield implements Resource {

    /**
     * this method returns a shield type
     * @return a shield type
     */
    @Override
    public ResourceType getType() {
        return ResourceType.SHIELD;
    }
    /**
     * this method convert the resource to his marble associated
     * @return a blue marble
     */
    @Override
    public Marble convertInMarble(){ return new BlueMarble(); }

    /**
     * this method verify if the argument toCompare has the same type of the resource that calls the method
     * @param toCompare this is the resource to verify
     * @return true if toCompare is a shield, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return toCompare instanceof Shield;
    }

    /**
     * this method get the string "shield"
     * @return the string "shield"
     */
    @Override
    public String toString() {
        return getType().getName();
    }
}
