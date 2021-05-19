package it.polimi.ingsw.model.Resources;

import it.polimi.ingsw.model.Marble.BlueMarble;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.view.utilities.colors.BackColor;

/**
 * this class represent the shield resource
 */
public class Shield implements Resource {

    /**
     * this constructor create a new shield
     */
    public Shield(){

    }

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

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method return the color associated to the Shield: blue
     */
    public BackColor getColor() {
        return BackColor.ANSI_BG_CYAN;
    }

    /**
     * this method get the png associated with the resource,
     * USEFUL ONLY FOR THE GUI
     *
     * @return the png associated with the resource
     */
    @Override
    public String getPng() {
        return "/punchboard/shield.png";
    }
}
