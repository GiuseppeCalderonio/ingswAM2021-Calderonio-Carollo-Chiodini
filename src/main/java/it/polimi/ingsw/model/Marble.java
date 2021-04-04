package it.polimi.ingsw.model;

/**
 * this interface represent the marble
 */
public interface Marble {
    /**
     * this method return the faith points associated with the marble
     * @return the faith points associated with the marble
     */
    int faithPoints();
    /**
     * this method return the resource associated with the marble
     * @return the resource associated with the marble
     */
    Resource convert();


    boolean equals(Object toCompare);

}