package it.polimi.ingsw.model;


/**
 * this class represents one single resource
 */
public interface Resource {


    /**
     * this method get the type of the resource
     * @return the type of the resource
     */
    ResourceType getType();

    /**
     * this method convert the resource to the corresponding marble
     *
     */
    Marble convertInMarble();

    /**
     * this method verify if the argument toCompare has the same type of the resource that calls the method
     * @param toCompare this is the resource to verify
     * @return true if the resources have the same type, false if they have not
     */
    @Override
    boolean equals(Object toCompare);
}