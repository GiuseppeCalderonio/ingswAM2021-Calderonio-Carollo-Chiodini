package it.polimi.ingsw.model;

import java.util.List;

/**
 * this class extends CollectionResources and represent a set of resources with only one type, called "a set of compatible resources"
 */
public class ShelfCollection extends CollectionResources {

    /**
     * this attribute represent the unique type of resource that can be stored in the set
     */
    private final ResourceType type;

    /**
     * this constructor create an empty set of resources
     * @param type this is the type
     */

    public ShelfCollection(ResourceType type) {
        super();
        this.type = type;
    }

    /**
     * this constructor create a set of resources, it requires that the parameter resources contains only one type of resource
     * @param type this is the type
     * @param resources this is the list that initialise the collection
     */
    public ShelfCollection(ResourceType type, List<Resource> resources){
        super(resources);
        this.type = type;
    }

    /**
     * this method gets the type associated with the set of resources
     * @return the type of yhe resource
     */
    public ResourceType getType() {
        return type;
    }
    /**
     * this method verify if the resource in input has the same type of the ShelfCollection that calls this method
     * @param toVerify this is the resource to verify
     * @return true if the type of ShelfCollection has the same type of the resource toVerify, false if they are not the same
     */
    @Override
    public boolean isCompatible(Resource toVerify){
        try{
            return type.equals(toVerify.getType());
        } catch (NullPointerException e){
            return true;
        }

    }

    /**
     * this method verify if every resource of the set in input has
     * the same type of the ShelfCollection that calls this method
     * @param toVerify this is the CollectionResource to verify
     * @return true if the ShelfCollection has the same type of every resource
     * of the set in input, false if not
     */
    public boolean isCompatible(CollectionResources toVerify){
        return toVerify.getMaps().stream().allMatch(x -> type.equals(x.getResource().getType()));
        /*for (MapResources map : toVerify.getMaps()){
            if (!type.equals(map.getResource().getType())) return false;
        }
        return true;*/
    }
    /**
     * this method add a resource to the set
     * @param toAdd this is the resources to add
     * @return true if the set is compatible with the parameter, false if it isn't
     */
    @Override
    public boolean add(Resource toAdd) {
        if(this.isCompatible(toAdd)) return super.add(toAdd);
        return false;
    }
    /**
     * this method remove a resource from the set when it is stored and compatible with ShelfResources, do anything if is not
     * @param toRemove this is the resource to remove from the set
     * @return true if the resource is stored and correctly removed and the resource is compatible with ShelfResources, false if the resource is not stored in the set or the types are not compatible
     */
     @Override
    public boolean remove(Resource toRemove) {
        if(this.isCompatible(toRemove) && this.getSize() > 0) {
            super.remove(toRemove);
            return true;
        }
        return false;
    }

    /**
     * this method sum two set of compatible resources
     * @param toSum the set of resources to sum from the actual set
     * @return true if the sets are compatible, false if they aren't
     */
    @Override
    public boolean sum(CollectionResources toSum) {
        if(this.isCompatible(toSum)) return super.sum(toSum);
        return false;
    }

    /**
     * this method subtract two set of resources if it is possible, anything if not possible
     * @param toSub this is the set of resources to subtract
     * @return true if all the resources stored in the set toSub are contained in the set that calls the method
     */
    @Override
    public boolean sub(CollectionResources toSub) {
        if(this.isCompatible(toSub) && this.containsAll(toSub)) return super.sub(toSub);
        return false;
    }

    /**
     * this method compare two sets of compatible resources
     * @param toCompare this is the set of resources to compare
     * @return true if all the resources stored in the set toSub are contained in the set that calls the method and the two sets of resources are compatible, false if they aren't
     */
    @Override
    public boolean containsAll(CollectionResources toCompare) {
        if(this.isCompatible(toCompare)) return super.containsAll(toCompare);
        return false;
    }

}
