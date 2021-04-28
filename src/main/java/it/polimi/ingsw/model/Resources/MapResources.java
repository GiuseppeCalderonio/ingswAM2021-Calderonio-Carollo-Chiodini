package it.polimi.ingsw.model.Resources;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * this class simulate a map of resources
 */
public class MapResources implements Iterable<Resource> {

    /**
     * this attribute is the resource contained in the map
     */
    private final Resource resource;

    /**
     * this attribute is the cardinality associated with the resource
     */
    private int cardinality;

    /**
     * this constructor initialise a new map
     * @param resource this is the resource to initialise
     * @param cardinality this is the cardinality to initialise
     */
    public MapResources(Resource resource, int cardinality){
        this.resource = resource;
        this .cardinality = cardinality;
    }

    /**
     * this method returns the resource of the map
     * @return the resource of the map
     */
    public Resource getResource() {

        return resource;
    }

    /**
     * this method returns the cardinality associated with the resource of the map
     * @return the cardinality of the resource
     */
    public int getCardinality() {

        return cardinality;
    }

    /**
     * this method increase the cardinality associated with the map of the resource
     * @param toAdd this is the integer to sum with cardinality
     */
    public void add(int toAdd){

        cardinality = cardinality + toAdd;
    }

    /**
     * this method decrease the cardinality associated with the map of the resource
     * @param toRemove this is the integer to subtract with cardinality
     */
    public void remove(int toRemove){

        cardinality = cardinality - toRemove;
    }

    /**
     * this method verify if two maps of resources are equals
     * @param toCompare this is the map to compare with
     * @return true if the two maps have the same resource with the same cardinality, false otherwise
     */
    @Override
    public boolean equals(Object toCompare){
        if (this == toCompare) return true;
        if (! (toCompare instanceof MapResources) ) return false;
        return resource.equals(((MapResources) toCompare).getResource()) &&
                cardinality == ((MapResources) toCompare).getCardinality();
    }

    /**
     * this method verify if the map is empty, in particular, if the cardinality is less equal than zero
     * @return true if cardinality is more than zero, false otherwise
     */
    public boolean isEmpty(){

        return cardinality <= 0;
    }

    @Override
    public String toString() {
        return "<"
                 + resource +
                "," + cardinality +
                '>';
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<Resource> iterator() {
        return new MapResourcesIterator(this.cardinality, this.resource);
    }
}
