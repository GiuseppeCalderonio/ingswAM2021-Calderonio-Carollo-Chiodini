package it.polimi.ingsw.model;

/**
 * this class represent the leader card
 */
public abstract class LeaderCard {

    /**
     * this attribute represent the requirements to activate the card
     */
    private final LeaderCardRequirements requirements;

    /**
     * this attribute represent the victory points of the card
     */
    private final int victoryPoints;

    /**
     * this attribute specify if the card have been activated by a player
     */
    private boolean isActive;

    /**
     * this attribute specify the resource associated with the leader card
     */
    private final Resource resource;

    /**
     * this constructor create a leader card from requirements,
     * victory points and resource
     * @param requirements these are the requirements to set
     * @param victoryPoints these ate the victory points to set
     * @param resource this is the resource to set
     */
    public LeaderCard(LeaderCardRequirements requirements, int victoryPoints, Resource resource){
        this.requirements = requirements;
        this.victoryPoints = victoryPoints;
        this.resource = resource;
        this.isActive = false;
    }

    /**
     * this method verify if the card is active or not
     * @return true if the card is active, false otherwise
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * this method get the requirements of the card
     * @return the requirements of the card
     */
    public LeaderCardRequirements getRequirements() {
        return requirements;
    }

    /**
     * this method get the resource associated with tle leader card
     * @return the resource associated with tle leader card
     */
    public Resource getResource() {
        return resource;
    }

    /**
     * this method get the victory points of the leader card
     * @return the victory points of the leader card
     */
    public int getVictoryPoints() {
        return victoryPoints;
    }

    /**
     * this method set the attribute isActive to true
     * it is called only in the method activeCard
     */
    public void setActive(){
        this.isActive = true;
    }

    /**
     * this method activate the card if the player in input
     * meets the requirements, do nothing otherwise
     * this method is overrided
     * @param toChange this is the player that activate the card
     * @return true if the card got activated, false otherwise
     */
    public boolean activateCard(RealPlayer toChange){
        return false;
    }

    /**
     * this method verify if two leader cards are equals
     * in particular, if they have the same requirements,
     * victory points, and resource associated
     * @param o this is the card to verify, it must be casted
     * @return true if the card are equals, false otherwise
     */
    public boolean equals(Object o){
        if (o == this) return true;
        if (!(o instanceof LeaderCard)) return false;
        LeaderCard toVerify = (LeaderCard) o;
        return (victoryPoints == toVerify.victoryPoints &&
                resource.equals(toVerify.resource));
    }
}
