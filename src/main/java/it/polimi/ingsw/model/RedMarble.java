package it.polimi.ingsw.model;

/**
 * this class implements the marble interface and represent the red marble
 */
public class RedMarble implements Marble {
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
}
