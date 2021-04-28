package it.polimi.ingsw.model.Resources;

import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.YellowMarble;
import it.polimi.ingsw.view.utilities.colors.BackColor;

/**
 * this class represent the coin resource
 */
public class Coin implements Resource {

    ResourceType type = ResourceType.COIN;

    public Coin(){

    }

    public Coin(ResourceType type){
        this.type = type;
    }

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

    /**
     * this method get the string "coin"
     * @return the string "coin"
     */
    @Override
    public String toString() {
        return getType().getName();
    }

    /**
     * USEFUL ONLY FOR CLIENTS
     * this method return the color associated to the Coin: yellow
     */
    public BackColor getColor() {
        return BackColor.ANSI_BG_YELLOW;
    }
}
