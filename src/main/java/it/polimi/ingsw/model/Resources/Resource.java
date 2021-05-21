package it.polimi.ingsw.model.Resources;


import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;

/**
 * this class represents one single resource
 */
public interface Resource{


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

    /**
     * this method return a string that represent the resource
     * @return a string that represent the resource
     */
    String toString();

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method return the color associated to the Resource
     */
    BackColor getColor();

    /**
     * this method get the id associated with the resource,
     * USEFUL ONLY FOR THE GUI
     * @return the ID associated with the resource
     */
    int getId();
}