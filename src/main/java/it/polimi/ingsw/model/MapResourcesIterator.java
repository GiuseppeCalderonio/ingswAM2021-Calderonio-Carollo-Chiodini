package it.polimi.ingsw.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class MapResourcesIterator implements Iterator<Resource> {

    private int counter;
    private final Resource resource;

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
