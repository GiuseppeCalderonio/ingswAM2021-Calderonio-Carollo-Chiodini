package it.polimi.ingsw.model;

/**
 * this method represent the leader card that add a new shelf
 */
public class NewShelf extends LeaderCard{


    /**
     * this constructor create a leader card from requirements,
     * victory points and resource
     *
     * @param requirements  these are the requirements to set
     * @param victoryPoints these ate the victory points to set
     * @param resource      this is the resource to set
     */
    public NewShelf(LeaderCardRequirements requirements, int victoryPoints, Resource resource) {
        super(requirements, victoryPoints, resource);
    }

    /**
     * this method add a new shelf at the player in input when he
     * has all the requirements of the leader card
     * @param toChange this is the player that activate the card
     * @return true if the player in input meet the requirements,
     *         false otherwise
     */
    @Override
    public boolean activateCard(RealPlayer toChange) {
        if(!(getRequirements().containsRequirements(toChange))) return false;
        toChange.addLeaderShelf(getResource());
        setActive();
        return true;
    }
}
