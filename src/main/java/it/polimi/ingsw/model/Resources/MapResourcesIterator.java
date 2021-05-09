package it.polimi.ingsw.model.Resources;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * this class is used to iterate elements on the class map resources
 */
public class MapResourcesIterator implements Iterator<Resource> {

    /**
     * this attribute represent the number of resources contained into the map to iterate
     */
    private int counter;

    /**
     * this attribute represent the resource associated to the map to iterate
     */
    private final Resource resource;

    /**
     * this constructor create the iterator from the number of resources of the map, and
     * the resource associated with the map
     * @param counter this is the number of resources of the map
     * @param resource this is the resource associated with the map
     */
    public MapResourcesIterator( int counter, Resource resource){
        this.counter = counter;
        this.resource = resource;
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return counter != 0;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Resource next() {
        if (!hasNext()) throw new NoSuchElementException("Resources finished");
        counter--;
        return resource;
    }
}
