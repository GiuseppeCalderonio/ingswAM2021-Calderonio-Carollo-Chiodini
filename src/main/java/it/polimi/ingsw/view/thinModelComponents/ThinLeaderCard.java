package it.polimi.ingsw.view.thinModelComponents;

import it.polimi.ingsw.model.Resources.Resource;

/**
 * this method represent the thin leader card.
 * in particular, it is used only to be sent throughout the network,
 * and after that the client will reconstruct them, thanks to
 * a local copy of all the leader cards of the game
 */
public class ThinLeaderCard {

    /**
     * this attribute represent the victory points of the card
     */
    private int victoryPoints;

    /**
     * this attribute indicates if the card is active or not
     */
    private final boolean isActive;

    /**
     * this attribute represent the resource associated with the leader card
     */
    private Resource resource;

    /**
     * this constructor create the leader card starting from
     * the victory points, if the card is active or not, and the resource associated
     * @param isActive this parameter indicates if the card is active or not
     * @param victoryPoints these are the victory points of the card
     * @param resource this is the resource associated with the card
     */
    public ThinLeaderCard(boolean isActive, int victoryPoints, Resource resource) {
        this.isActive = isActive;
        this.victoryPoints = victoryPoints;
        this.resource = resource;
    }

    /**
     * this method get the boolean associated with the activation state of the card
     * @return true if the card is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * this method get the resource associated with the leader card
     * @return the resource associated with the leader card
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * this method get the victory points associated with the leader card
     * @return the victory points associated with the leader card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * this method hide a card.
     * in particular, when a client have to receive the leader card state
     * of a player, if the player didn't activate the card, the client
     * can' see the card, so he will see the covered card.
     * this method cover the card, setting the victory points to 0 and the resource
     * associated with null
     */
    public void hide(){
        victoryPoints = 0;
        resource = null;
    }
}
