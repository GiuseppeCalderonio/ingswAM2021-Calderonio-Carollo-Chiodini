package it.polimi.ingsw.model.PlayerAndComponents;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.FaithPointsManager.TrackManager;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

/**
 * this class extends player and represent a real player
 * with all the functionalities
 */
public class RealPlayer extends Player {

    /**
     * this attribute represent the nickname
     */
    private final String nickname;

    /**
     * this attribute represent the dashboard associated with
     * the player
     */
    private Dashboard personalDashboard;

    /**
     * this attribute represent the personal leader cards
     * as a list
     */
    private final List<LeaderCard> personalLeaderCards;

    /**
     * this attribute represent the eventual leader card
     * with the marbles that should substitute the white marbles
     * when buying resources from market
     */
    private final List<Marble> leaderWhiteMarbles;


    /**
     * this constructor create a player giving him his nickname
     * and a deck of 4 leader cards, of which 2 of them
     * will be discarded before the game start
     * @param nickname this is the nickname to set
     * @param personalLeaderCards these are the initial leader cards owned
     */
    public RealPlayer(String nickname, List<LeaderCard> personalLeaderCards) {
        super();
        this.nickname = nickname;
        this.personalDashboard = new Dashboard();
        this.personalLeaderCards = personalLeaderCards;
        this.leaderWhiteMarbles = new ArrayList<>();
    }

    /**
     * this method get the nickname
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * this method get the personal dashboard
     * @return the personal dashboard
     */
    public Dashboard getPersonalDashboard() {
        return personalDashboard;
    }

    /**
     * this method get the personal leader cards
     * @return the personal leader cards
     */
    public List<LeaderCard> getPersonalLeaderCards() {
        return personalLeaderCards;
    }

    /**
     * this method get the marble at position index
     * @param index this is the position of the marble to get
     * @return the marble at position index
     */
    public Marble getLeaderWhiteMarble(int index) {
        return leaderWhiteMarbles.get(index - 1);
    }


    /**
     * this method get the leader white marbles
     *
     * @return the leader white marbles
     */
    public List<Marble> getLeaderWhiteMarbles() { return leaderWhiteMarbles; }

    /**
     * this method get the personal track
     *
     * @return the personal track
     */
    @Override
    public TrackManager getPersonalTrack() {
        return super.getPersonalTrack();
    }

    /**
     * this method verify if a player is in condition to
     * handle a vatican report relative to the
     * index-th pope favour tile
     * in particular, if the position of the player is higher or equals than the
     * end position of the tile
     *
     * @param index this is index relative to the pope favour tile to check
     * @return true if the player is in condition to handle a vatican report,
     * false otherwise
     */
    @Override
    public boolean checkVaticanReport(int index) {
        return super.checkVaticanReport(index);
    }

    /**
     * this method adds faith points to the player
     * increasing his position on the track
     *
     * @param toAdd these are the faith points to add
     */
    @Override
    public void addFaithPoints(int toAdd) {
        super.addFaithPoints(toAdd);
    }

    /**
     * this method set the pope favor tile index-th
     * if the player position is higher or equals than
     * the end position of the tile
     *
     * @param index this is index relative to the pope favour tile to eventually set
     */
    @Override
    public void vaticanReport(int index) {
        super.vaticanReport(index);
    }

    /**
     * this method get the total resources of the player
     * @return the total resources of the player
     */
    public CollectionResources getTotalResources(){
        return personalDashboard.getTotalResources();
    }


    /**
     * this method get the total discounted resources of the player
     * @return the total discounted resources of the player
     */
    public CollectionResources getTotalDiscountedResources(){ return personalDashboard.getTotalDiscountResource(); }

    /**
     * this method is used to shift resources between two shelves,
     * @param source contains the number associate to a shelf. this is the shelf where i take the resource
     * @param destination contains the number associate to a shelf. this is the shelf where i put the resource contained
     *                    in source shelf
     * @return the number of discarded resources
     */
    public int shiftResources(int source, int destination){
        return personalDashboard.shiftShelfResources(source, destination);
    }

    /**
     * this method insert a resource in warehouse
     * @param toAdd this is the resource that a player want to insert in warehouse
     * @param numShelf this is the number of warehouse shelf
     * @return the number of rejected resources
     */
    public int addResourcesToWarehouse(CollectionResources toAdd, int numShelf){
        return personalDashboard.addResourcesToWarehouse(toAdd, numShelf);
    }

    /**
     * this method is used to locate a card in one of the three position in dashboard.
     * @param position is =1 or =2 or =3; is the position where the card is locate in dashboard
     * @param toPlace contains the developmentCard that the player wants to insert in dashboard
     * @return true if the card has been correctly inserted, else false
     */
    public boolean locateDevelopmentCard (DevelopmentCard toPlace , int position) {
        return personalDashboard.placeDevelopmentCard(toPlace , position);
    }

    /**
     * this method buy the a development card removing resources from the dashboard
     * in particular, it removes the resources in the input from the strongbox
     * and from warehouse
     * if the player has a discount of resources, the resources
     * are already discounted by the game
     * @param toPayFromWarehouse these are the resources to pay from warehouse
     * @param toPayFromStrongbox these are the resources to pay from strongbox
     * @return true if this operation is a success else false
     */
    public boolean buyDevelopmentCard(CollectionResources toPayFromWarehouse, CollectionResources toPayFromStrongbox){
       if (!personalDashboard.removeFromWarehouse(toPayFromWarehouse)) return false;
        return personalDashboard.removeFromStrongbox(toPayFromStrongbox);
    }

    /**
     * this method activate the production, in particular
     * it removes from warehouse and from strongbox the resources given in input
     * and add to the buffer toPlaceInBuffer
     * @param toPayFromWarehouse these are the input resources to remove from warehouse
     * @param toPayFromStrongbox these are the input resources to remove from strongbox
     * @param toPlaceInBuffer these are the output resources to add in the buffer
     * @return true if this operation is a success else false
     */
    public boolean activateProduction(CollectionResources toPayFromWarehouse, CollectionResources toPayFromStrongbox, CollectionResources toPlaceInBuffer){
        if(!(personalDashboard.getPersonalStrongbox().getStrongboxResources().containsAll(toPayFromStrongbox) &&
                personalDashboard.getPersonalWarehouse().getTotalResources().containsAll(toPayFromWarehouse))) return false;
        personalDashboard.removeFromWarehouse(toPayFromWarehouse);
        personalDashboard.removeFromStrongbox(toPayFromStrongbox);
        personalDashboard.addToBuffer(toPlaceInBuffer);
        return true;
    }

    /**
     * this method insert in strongbox all resources contained in buffer
     */
    public void fillStrongboxWithBuffer(){
        personalDashboard.fillStrongboxWithBuffer();
    }

    /**
     * this method activate the leader card specified by the
     * parameter (it can be the first one or the second one)
     * the method verify if the player have all the requirements to
     * activate the card, and in case activating it,
     * if the card selected is already active does not activate it
     * and return false
     * @param toActivate this is the index of the owned leader card to activate
     * @return true if the card got activated correctly, false otherwise
     */
    public boolean activateLeaderCard(int toActivate){
        return personalLeaderCards.get(toActivate - 1).activateCard(this);
    }

    /**
     * this method discard the leader card specified by the
     * parameter (it can be the first one or the second one)
     * if the card has already been activated, it does not discard it
     * if not, the method return true
     * @param toDiscard this is the index of the owned leader card to discard
     *                  it should be from 1 to personalLeaderCards.getSize()
     * @return true if the card got discarded correctly, false otherwise
     */
    public boolean discardLeaderCard(int toDiscard){
        if(personalLeaderCards.get(toDiscard - 1).isActive()) return false;
        personalLeaderCards.remove(toDiscard - 1);
        return true;
    }

    /**
     * this method is called when the player activate a new white marble leader card and
     * add a new marble that can be used to substitute
     * a white marble when buying resources from market
     * @param toAdd this is the resource to convert in marble to add
     */
    public void addLeaderWhiteMarble(Resource toAdd){
        leaderWhiteMarbles.add(toAdd.convertInMarble());
    }

    /**
     * this method is called when the player activate a new discount leader card, if
     * it is the first time, it create a new dashboard adding the resource toAdd
     * if it is the second time, it just add the resource to the discounted ones
     * @param toAdd this is the resource discounted associated with the leader card
     */
    public void addDiscount(Resource toAdd){
        if(!(personalDashboard instanceof DiscountDashboard))
            personalDashboard = new DiscountDashboard(toAdd, personalDashboard);
        else personalDashboard.addDiscount(toAdd);
    }

    /**
     * this method is called when the player activate a shelf leader card,
     * and it create a new leader shelf
     * @param toAdd this is the resource of the leader shelf associated with the leader card
     */
    public void addLeaderShelf(Resource toAdd){
        personalDashboard.activateLeaderWarehouse(toAdd);
    }

    /**
     * this method is used to create/activate a leader production. "toAdd" is the resource that a
     * player has to pay to activate the leader Production
     *  @param toAdd is the resource that the player want to obtain from leaderProduction
     */
    public void addLeaderProduction(Resource toAdd){
        personalDashboard.activateLeaderProduction(toAdd);
    }

    /**
     * this method get all the victory points of the player
     * in particular, it get the victory points from the dashboard,
     * from the personal track and from the leader cards owned
     * @return the sum of all victory points
     */
    public int getVictoryPoints(){
        return personalDashboard.getVictoryPoints() +
                super.getPersonalTrack().getVictoryPoints() +
                personalLeaderCards.stream().
                        filter(LeaderCard::isActive).
                        flatMapToInt(x -> IntStream.of(x.getVictoryPoints())).sum();
        /*int victoryPoints = 0;
        victoryPoints = victoryPoints + personalDashboard.getVictoryPoints();
        victoryPoints = victoryPoints + super.getPersonalTrack().getVictoryPoints();
        victoryPoints = victoryPoints + personalLeaderCards.stream().filter(LeaderCard::isActive).flatMapToInt(x -> IntStream.of(x.getVictoryPoints())).sum();
        return victoryPoints;*/
    }
}
