package it.polimi.ingsw.model;

/**
 * this class represent the strongbox of the dashboard
 */
public class StrongBox {

    /**
     * this attribute represent a collection with all the resources in the strongbox
     */
    private final CollectionResources strongboxResources;

    /**
     * this constructor create an empty strongbox
     */
    public StrongBox(){
        strongboxResources = new CollectionResources();
    }

    /**
     * this method add the input resources in the strongbox
     * @param toAdd this is the collection of resources to add
     * @return true
     */
    public boolean addResources(CollectionResources toAdd){
        return strongboxResources.sum(toAdd);
    }

    /**
     * this method remove the input resources from the strongbox
     * it requires that the resources to remove are contained in the strongbox
     * @param toRemove this is the collection of resources to remove
     * @return false if the strongbox after the operation is empty, true if it is not
     */
    public boolean removeResources(CollectionResources toRemove){
        strongboxResources.sub(toRemove);
        return !this.isEmpty();
    }

    /**
     * this method get the resources contained in the strongbox as a collection of resources
     * @return the resources contained in the strongbox as a collection of resources
     */
    public CollectionResources getStrongboxResources() {
        return strongboxResources;
    }

    /**
     * this method check if the strongbox is empty
     * @return true if the strongbox is empty, false if it isn't
     */
    public boolean isEmpty(){
        return strongboxResources.getSize() == 0;
    }

    /**
     * this method get the total number of all the resources contained in the strongbox
     * @return the total number of all the resources contained in the strongbox
     */
    public int getNumberOfResources(){
        return strongboxResources.getSize();
    }
}
