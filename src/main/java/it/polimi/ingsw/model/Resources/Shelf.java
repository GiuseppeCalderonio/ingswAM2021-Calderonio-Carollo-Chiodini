package it.polimi.ingsw.model.Resources;

import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.ResourceType;
import it.polimi.ingsw.model.Resources.ShelfCollection;

/**
 * this class represent a single shelf of the dashboard
 */
public class Shelf {

    /**
     * this attribute represent the resources contained in the shelf
     */
    private ShelfCollection resources = null;

    /**
     * this attribute represent the maximum number of resources that a specific shelf can store
     */
    private final int capacity;

    /**
     * this constructor initialise an empty shelf
     * @param capacity this is the capacity to set at the shelf
     */
    public Shelf (int capacity){
        this.capacity = capacity;
    }

    /**
     * this method get the type of the resources stored in the shelf, null if the shelf is empty
     * @return the type of the resources stored in the shelf, null if the shelf is empty
     */
    public ResourceType getResourceType(){
        try{
            return resources.getType();
        } catch (NullPointerException e){
            return null;
        }
    }
    /**
     * getter method for private attribute resources
     * @return the shelfCollection
     */
    public CollectionResources getResources(){
        if(resources == null) return new CollectionResources();
        return resources;
    }

    /**
     * getter method for private attribute capacity
     * @return capacity of the shelf
     */
    public int getCapacity(){
        return capacity;
    }


    /**
     * this method add one resource in shelf when the resource is of the same type of
     * the type of the shelf and if the shelf is not full, do anything otherwise
     * @param toAdd resource
     * @return true when the resource can't be added, false when it can be added
     */
    public boolean addResource(Resource toAdd){
        try{
            if(resources.getSize()<capacity && resources.isCompatible(toAdd)){
                resources.add(toAdd);
                return false;
            }
            return true;
        } catch (NullPointerException e){
            resources = new ShelfCollection(toAdd.getType());
            resources.add(toAdd);
            return false;
        }
    }

    /**
     * return true when the shelf is empty
     * @return true when the shelf is empty
     */
    public boolean isEmpty() {
        return resources == null;
    }

    /**
     * this method empty the shelf
     */
    public void removeAll(){
        this.resources = null;
    }

    /**
     * this method remove resource from the shelf when it is contained in the shelf
     * @param toRemove resource
     * @return the new size of the shelf
     */
    public int removeResource(Resource toRemove){
        resources.remove(toRemove);
        if(resources.getSize() == 0){
            resources = null;
            return 0;
        }
        return resources.getSize();
    }

    /**
     * this method return the free resource slots of the shelf
     * @return the free resource slots of the shelf
     */
    public int getFreeSlots(){
        try{
            return (capacity-resources.getSize());
        } catch (NullPointerException e){
            return capacity;
        }
    }
}

