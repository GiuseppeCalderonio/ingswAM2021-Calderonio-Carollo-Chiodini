package it.polimi.ingsw.model.LeaderCard;

import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.Resource;

/**
 * this method represent the leader card that add a new discount
 */
public class NewDiscount extends LeaderCard {


    /**
     * this constructor create a leader card from requirements,
     * victory points and resource
     *
     * @param requirements  these are the requirements to set
     * @param victoryPoints these ate the victory points to set
     * @param resource      this is the resource to set
     */
    public NewDiscount(LeaderCardRequirements requirements, int victoryPoints, Resource resource) {
        super(requirements, victoryPoints, resource);
    }

    /**
     * this method add a new resource to use as discount
     * when the player in input buy a card,but only if he
     * has all the requirements of the leader card
     * @param toChange this is the player that activate the card
     * @return true if the player in input meet the requirements,
     *         false otherwise, or if the card selected is already active
     */
    @Override
    public boolean activateCard(RealPlayer toChange) {
        if(!this.getRequirements().containsRequirements(toChange)) return false;
        if (this.isActive()) return false;
        toChange.addDiscount(getResource());
        setActive();
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "\n" +
                "New discount : " + getResource()
                ;
    }
}
