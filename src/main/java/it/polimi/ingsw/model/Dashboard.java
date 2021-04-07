package it.polimi.ingsw.model;

/**
 * this class implements all methods of a personal dashboard
 */

public class Dashboard {
    /**
     * this attribute contains the strongbox linked to the player
     */
    private final StrongBox personalStrongbox;
    /**
     * this attribute contains the ProductionPower linked to the player
     */
    private ProductionPower personalProductionPower;
    /**
     *  this attribute is the buffer where we insert resources obtained from marble market; at the end of the turn we insert
     *  these resources in strongbox
     */
    private CollectionResources buffer;
    /**
     * this attribute contains the player personalWarehouse
     */
    private Warehouse personalWarehouse;

    /**
     * this constructor create each object but don't initialize anything, indeed
     * when the game starts warehouse, strongbox , the buffer and personal production power
     * are empty
     */
    public Dashboard(){
        personalStrongbox = new StrongBox();
        personalProductionPower = new ProductionPower();
        buffer = new CollectionResources();
        personalWarehouse = new Warehouse();
    }

    /**
     * this constructor is used to initialize DiscountDashboard when a player activate a leader card that generate a discount when someone
     * buy a development card from market
     *  @param dashboard is the actual dashboard
     */
    protected Dashboard( Dashboard dashboard){
        this.personalStrongbox = dashboard.personalStrongbox;
        this.personalProductionPower = dashboard.personalProductionPower;
        this.buffer = dashboard.buffer;
        this.personalWarehouse = dashboard.personalWarehouse;
    }

    /**
     * this method is a getter of personalWarehouse
     * @return the state of  player personalWarehouse,so we can see and use
     * all resources contained in this strongbox
     */
    public Warehouse getPersonalWarehouse() {
        return personalWarehouse;
    }

    /**
     * this method is a getter of personalProductionPower
     * @return the state of personalProductionPower that a player has activated
     */
    public ProductionPower getPersonalProductionPower() {
        return personalProductionPower;
    }

    /**
     * this method is a getter of the buffer
     * @return all resources contained in buffer
     */
    public CollectionResources getBuffer() {
        return buffer;
    }

    /**
     * this method is a getter of personalStrongbox
     * @return the state of a player personalStrongbox, so we can see and use
     * all resources contained in this strongbox
     */
    public StrongBox getPersonalStrongbox() {
        return personalStrongbox;
    }

    /**
     * this method is used to shift resources between two shelves,
     * @param source contains the number associate to a shelf. this is the shelf where i take the resource
     * @param destination contains the number associate to a shelf. this is the shelf where i put the resource contained
     *                    in source shelf
     * @return the number of discarded resources
     */
    public int shiftShelfResources(int source , int destination){
        return personalWarehouse.shiftResources(source , destination);
    }

    /**
     * this method is used to locate a card in one of the three position in dashboard.
     * @param position is =1 or =2 or =3; is the position where the card is locate in dashboard
     * @param toPlace contains the developmentCard that the player wants to insert in dashboard
     * @return true if the card has been correctly inserted, else false
     */
    public boolean placeDevelopmentCard (DevelopmentCard toPlace , int position) {
        return personalProductionPower.placeCard(toPlace , position);
    }

    /**
     * this method insert in strongbox all resources contained in buffer
     */
    public void fillStrongboxWithBuffer(){

        personalStrongbox.addResources(buffer);
        buffer = new CollectionResources();
    }

    /**
     * this method is used to remove resources from strongBox, if there aren't enough resources
     * the method return false
     * @param toRemove contains the resources that a player wants to take from strongbox
     * @return true if this operation is a success else false
     */
    public boolean removeFromStrongbox (CollectionResources toRemove) {
        if(!(personalStrongbox.getStrongboxResources().containsAll(toRemove))) return false;
        personalStrongbox.removeResources(toRemove);
        return true;
    }

    /**
     *this method is used to remove resources from Warehouse, if there aren't enough resources
     * the method return false
     * @param toRemove contains the resources that a player wants to take from warehouse
     * @return true if this operation is a success else false
     */
    public boolean removeFromWarehouse (CollectionResources toRemove){
        if(!(personalWarehouse.getTotalResources().containsAll(toRemove))) return false;
        personalWarehouse.removeResources(toRemove);
        return true;
    }

    /**
     * this method is used to add all of resources obtained from production power into the buffer
     * @param toAdd contains all resources to add in the buffer
     */
    public void addToBuffer(CollectionResources toAdd){
        buffer.sum(toAdd);
    }

    /**
     * this method insert a resource in warehouse
     * @param toAdd this is the resource that a player want to insert in warehouse
     * @param numShelf this is the number of warehouse shelf
     * @return the number of rejected resources
     */
    public int addResourcesToWarehouse(CollectionResources toAdd , int numShelf) {
        return personalWarehouse.addResources(toAdd,numShelf);
    }

    /**
     * this method is used to create/activate a leader production. "toCreate" is the resource that a
     * player has to pay to activate the leaderProduction
     *  @param toCreate is the resource that the player want to obtain from leaderProduction
     */
    public void activateLeaderProduction  (Resource toCreate) {
        if (!(personalProductionPower instanceof LeaderProduction)) {
            personalProductionPower = new LeaderProduction(toCreate, personalProductionPower);
        } else {
            personalProductionPower.addLeaderProduction(toCreate);
        }
    }

    /**
     * this method is called when the player activate a shelf leader card,
     * and it create a new leader shelf
     * @param toCreate this is the resource of the leader shelf associated with the leader card
     */
    public void activateLeaderWarehouse (Resource toCreate){
        if(!(personalWarehouse instanceof LeaderWarehouse)){
            personalWarehouse = new LeaderWarehouse(toCreate , personalWarehouse);
        }
        else{personalWarehouse.addShelf(toCreate);}
    }

    /**
     * this method get the resources contained in the dashboard, getting
     * them from warehouse and strongbox
     * @return a collection with all the resources
     */
    public CollectionResources getTotalResources(){
        CollectionResources toReturn = new CollectionResources();
        toReturn.sum(personalWarehouse.getTotalResources());
        toReturn.sum(personalStrongbox.getStrongboxResources());
        return toReturn;
    }

    /**
     * this method is a getter, when a player activate a leader card that generate a discount
     * we save the discount resource in CollectionResource and this method is used to see the state of this collection
     * @return the list of discounted resources
     */
    public CollectionResources getTotalDiscountResource(){
        return this.getTotalResources();
    }

    /**
     * this method count some victoryPoints. It count all of card contained in dashboard and divide this number for 5; then sum
     * all victoryPoints written on each card present in dashboard
     * @return the sum of victory points relative to development card in dashboard and the total of resources owned
     */
    public int getVictoryPoints(){
        return (getTotalResources().getSize()/5)+ personalProductionPower.getVictoryPoints();
    }

    /**
     * here this method do NOTHING. this method is used in discountDashboard
     * @return an empty collection
     */
    public CollectionResources getDiscount(){
        return new CollectionResources();
    }

    /**
     * here this method do NOTHING. this method is used in discountDashboard
     */
    public void addDiscount(Resource discount){

    }
}