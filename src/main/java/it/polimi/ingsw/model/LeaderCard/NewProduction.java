package it.polimi.ingsw.model.LeaderCard;

import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.Resource;

/**
 * this method represent the leader card that add a new power production
 */
public class NewProduction extends LeaderCard {


    /**
     * this constructor create a leader card from requirements,
     * victory points and resource
     *
     * @param requirements  these are the requirements to set
     * @param victoryPoints these ate the victory points to set
     * @param resource      this is the resource to set
     */
    public NewProduction(LeaderCardRequirements requirements, int victoryPoints, Resource resource) {
        super(requirements, victoryPoints, resource);
    }

    public NewProduction(LeaderCardRequirements requirements, int victoryPoints, Resource resource, String png) {
        super(requirements, victoryPoints, resource, png);
    }

    /**
     * this method add a new power production at the player in input when he
     * has all the requirements of the leader card
     * @param toChange this is the player that activate the card
     * @return true if the player in input meet the requirements,
     *         false otherwise, or if the card selected is already active
     */
    @Override
    public boolean activateCard(RealPlayer toChange) {
        if(!(getRequirements().containsRequirements(toChange))) return false;
        if (this.isActive()) return false;
        toChange.addLeaderProduction(getResource());
        setActive();
        return true;
    }

    @Override
    public String toString() {
        return super.toString()  +
                "New Production : " + getResource() + "\n"
                ;
    }

    /**
     *USEFUL ONLY FOR CLIENTS
     * this method returns the String that identifies the LeaderCard
     */
    public String identifier() {
        return "PROD";
    }
}
