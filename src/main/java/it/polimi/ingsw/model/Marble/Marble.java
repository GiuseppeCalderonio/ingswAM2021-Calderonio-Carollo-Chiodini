package it.polimi.ingsw.model.Marble;

import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.view.utilities.colors.BackColor;

/**
 * this interface represent the marble
 */
public interface Marble{
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

    String toString();

    /**
     * this method get the color of the marble
     * @return the enum with the color of the marble
     */
    MarbleColor getColor();

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method returns the BackColor associated to the Marble
     */
    public BackColor getBackColor();

}
