package it.polimi.ingsw.model.Resources;

import it.polimi.ingsw.model.Marble.GreyMarble;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.view.cli.graphic.utilities.colors.BackColor;

/**
 * this class represent the stone resource
 */

public class Stone implements Resource {

    /**
     * this constructor create a new stone
     */
    public Stone(){

    }

    /**
     * this method returns a stone type
     * @return a stone type
     */
    @Override
    public ResourceType getType() {
        return ResourceType.STONE;
    }
    /**
     * this method convert the resource to his marble associated
     * @return a grey marble
     */
    @Override
    public Marble convertInMarble(){return new GreyMarble();}

    /**
     * this method verify if the argument toCompare has the same type of the resource that calls the method
     * @param toCompare this is the resource to verify
     * @return true if toCompare is a stone, false if it isn't
     */
    @Override
    public boolean equals(Object toCompare) {
        return toCompare instanceof Stone;
    }

    /**
     * this method get the string "stone"
     * @return the string "stone"
     */
    @Override
    public String toString() {
        return getType().getName();
    }

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method return the color associated to the Stone: grey
     */
    public BackColor getColor() {
        return BackColor.ANSI_BRIGHT_BG_BLACK;
    }

    /**
     * this method get the png associated with the resource,
     * USEFUL ONLY FOR THE GUI
     *
     * @return the png associated with the resource
     */
    @Override
    public int getId() {
        return 4;
    }
}
