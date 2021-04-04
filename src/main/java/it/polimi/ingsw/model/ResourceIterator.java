package it.polimi.ingsw.model;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class ResourceIterator implements Iterator<Resource> {

    //private CollectionResources Resources;
    private List<MapResources> maps;

    public ResourceIterator (CollectionResources resources){
        maps = resources.getMaps();
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
        return !maps.isEmpty();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public Resource next() {
        try{
            Resource toReturn = maps.get(0).getResource();
            int toUpdate = maps.get(0).getCardinality() - 1;
            maps.set(0 , new MapResources(toReturn,toUpdate));
            if(maps.get(0).getCardinality() == 0) {
                maps.remove(0);
            }
            return toReturn;
        } catch (NullPointerException e){
            throw new NoSuchElementException("Resources finished");
        }
    }
}
