package it.polimi.ingsw.model;

import java.util.*;
import java.util.stream.*;

/**
 * this class represent a generic set of resources
 */

public class CollectionResources {
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
        for (Resource toAdd : resources){
            this.add(toAdd);
        }

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
        for (MapResources map : resources){
            if (map.getResource().equals(toCheck)) return true;
        }
        return false;
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
     * @param toAdd this is the resources to add
     * @return true
     */
    public boolean add(Resource toAdd){
        if (!this.contains(toAdd)) resources.add(new MapResources(toAdd , 1));
        else {
            for (MapResources map : resources){
                if (map.getResource().equals(toAdd)) map.add(1);
            }
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
        for (MapResources map : resources) {
            if (map.getResource().equals(toRemove)) map.remove(1);
        }
        resources = resources.stream().filter( x -> !x.isEmpty()).collect(Collectors.toList());
        return true;
    }

    /**
     * this method sum two sets of resources
     * @param toSum the set of resources to sum from the actual set
     * @return true
     */
    public boolean sum(CollectionResources toSum){
        for (MapResources map : toSum.resources) {
            for (int i = 0; i < map.getCardinality(); i++) add(map.getResource());
        }
        return true;
    }

    /**
     * this method subtract two sets of resources if it is possible, anything if not possible
     * @param toSub this is the set of resources to subtract
     * @return true if all the resources stored in the set toSub are contained in the set that calls the method, false otherwise
     */
    public boolean sub(CollectionResources toSub){
        if(!this.containsAll(toSub)) return false;
        for (MapResources map : toSub.resources){
            // local variable that contains the map of resources associated with "map"
            MapResources thisResources = this.getMap(map.getResource());
            int cardinality = thisResources.getCardinality();
            resources.remove(thisResources);
            resources.add(new MapResources(map.getResource(), cardinality - map.getCardinality()));
        }
        resources = resources.stream().filter( x -> !x.isEmpty()).collect(Collectors.toList());
        return true;
    }

    /**
     * this method compare two sets of resources
     * @param toCompare this is the set of resources to compare
     * @return true if all the resources stored in the set toSub are contained in the set that calls the method, false if they aren't
     */
    public boolean containsAll(CollectionResources toCompare){
        // this statement verify that every resource of toCompare is contained in this
        //if (!resources.containsAll(toCompare.resources)) return false;
        // this statement verify for each resource in toCompare that his cardinality is minor that the cardinality of the same resource of this
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
        return (containsAll( collectionToCompare) && collectionToCompare.containsAll(this));
    }
}

