package it.polimi.ingsw.model.Resources;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * this class represent a generic set of resources
 */

public class CollectionResources implements Iterable<Resource> {
    /**
     * this attribute is the list in which are stored all the resources as a Map
     */
    private List<MapResources> resources;

    /**
     * this constructor initialise an empty list of resources
     */
    public CollectionResources(){

        resources = new ArrayList<>();
    }

    /**
     * this constructor initialise the set from a list of resources given in input
     * @param resources this is the list of resources to initialise
     */
    public CollectionResources(List<Resource> resources) {

        this.resources = new ArrayList<>();
        resources.forEach( this::add);
    }

    /**
     * this getter return the maps of resources
     * @return the maps of resources
     */
    public List<MapResources> getMaps() {

        return resources;
    }

    /**
     * this method return the total number of resources stored in the set
     * @return the size of the
     */
    public int getSize(){

        return resources.stream().flatMapToInt(x -> IntStream.of(x.getCardinality())).sum();
    }

    /**
     * this method returns the map associated with the resource given in input
     * @param toGet this is the resource that may be searched in the set of maps
     * @return the map associated with the resource given in input, an empty map if the resource is not contained
     */
    private MapResources getMap(Resource toGet){
        for (MapResources map : resources){
            if (map.getResource().equals(toGet)) return map;
        }
        return new MapResources(null, 0);
    }

    /**
     * this method check if the resource toCheck is contained in the set of resources that calls
     * the method
     * @param toCheck this is the resource to check
     * @return true if the resource is in the set, false otherwise
     */
    public boolean contains(Resource toCheck){
        return resources.stream().anyMatch(x -> x.getResource().equals(toCheck));
    }

    /**
     * this method always return true, but the subclass ShelfResources inherit it
     * @param toVerify this is the parameter to verify
     * @return true
     */
    public boolean isCompatible(Resource toVerify){

        return true;
    }

    /**
     * this method always return true, but the subclass ShelfResources inherit it
     * @param toVerify this is the parameter to verify
     * @return true
     */
    public boolean isCompatible(CollectionResources toVerify){

        return true;
    }

    /**
     * this method add a resource to the set
     * if the resource is null do anything
     * @param toAdd this is the resources to add
     * @return true
     */
    public boolean add(Resource toAdd){
        if (toAdd == null) return true;
        if (!this.contains(toAdd)) resources.add(new MapResources(toAdd , 1));
        else {
            resources.stream().filter(x -> x.getResource().equals(toAdd)).forEach(x ->x.add(1));
        }
        return true;
    }

    /**
     * this method remove a resource from the set when it is stored, do anything if is not
     * @param toRemove this is the resource to remove from the set
     * @return true if the resource was stored and correctly removed, false if the resource was not stored in the set
     */
    public boolean remove(Resource toRemove) {
        if (!this.contains(toRemove)) return false;
        resources.stream().filter(x -> x.getResource().equals(toRemove)).forEach(x ->x.remove(1));
        resources = resources.stream().filter( x -> !x.isEmpty()).collect(Collectors.toList());
        return true;
    }

    /**
     * this method sum two sets of resources
     * @param toSum the set of resources to sum from the actual set
     * @return true
     */
    public boolean sum(CollectionResources toSum){
        toSum.forEach(this::add);
        return true;
    }

    /**
     * this method subtract two sets of resources if it is possible, anything if not possible
     * @param toSub this is the set of resources to subtract
     * @return true if all the resources stored in the set toSub are contained in the set that calls the method, false otherwise
     */
    public boolean sub(CollectionResources toSub){
        if(!this.containsAll(toSub)) return false;
        toSub.forEach(this::remove);
        resources = resources.stream().filter( x -> !x.isEmpty()).collect(Collectors.toList());
        return true;
    }

    /**
     * this method compare two sets of resources
     * @param toCompare this is the set of resources to compare
     * @return true if all the resources stored in the set toSub are contained in the set that calls the method, false if they aren't
     */
    public boolean containsAll(CollectionResources toCompare){
        for (MapResources map : toCompare.resources){
            if (this.getMap(map.getResource()).isEmpty()) return false;
            if (this.getMap(map.getResource()).getCardinality() < map.getCardinality()) return false;
        }
        return true;
    }

    /**
     * this method verify if two sets of resources are equals,
     * @param toCompare the set of resources to verify
     * @return true if every resource of the set that calls the method is in
     * the set toCompare and the other way around, false otherwise
     */
    public boolean equals(Object toCompare){
        if(toCompare == this) return true;
        if(!(toCompare instanceof CollectionResources)) return false;
        CollectionResources collectionToCompare = (CollectionResources) toCompare;
        return (this.containsAll( collectionToCompare) && collectionToCompare.containsAll(this));
    }

    /**
     * Returns an iterator over elements of type {@code Resource}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<Resource> iterator() {
        CollectionResources toPass = new CollectionResources();
        resources.forEach( map -> map.forEach(toPass::add));
        return new ResourceIterator(toPass);
    }

    /**
     * this method get the list associated with the collection
     * @return the list associated with the collection
     */
    public List<Resource> asList(){
        List<Resource> toReturn = new ArrayList<>();
        this.forEach(toReturn::add);
        return toReturn;
    }

    @Override
    public String toString() {
        return resources.toString();
    }

}

