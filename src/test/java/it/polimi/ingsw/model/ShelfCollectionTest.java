package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the ShelfCollection
 */
class ShelfCollectionTest {

    /**
     * this attribute represent the shelfCollection to test
     */
    private ShelfCollection shelf;

    /**
     *this test verify that method isCompatible(resource) return true only when the resource is of the same type of
     * these inside the shelf
     */
    @Test
    void testIsCompatibleResource(){
        shelf = new ShelfCollection(ResourceType.COIN);
        assertFalse(shelf.isCompatible(new Servant()));
        assertFalse(shelf.isCompatible(new Stone()));
        assertFalse(shelf.isCompatible(new Shield()));
        assertTrue(shelf.isCompatible(new Coin()));
    }

    /**
     * this test verifies that getType method return the typeResources which is in input in the constructor
     */
    @Test
    void testGetType(){
        shelf = new ShelfCollection(ResourceType.COIN);
        assertEquals(ResourceType.COIN, shelf.getType());
    }


    /**
     *this test verifies that IsCompatible(CollectionResources) return true only when the collection resources contains
     *  only Resources of the same type of the attribute type in shelf
     */
    @Test
    void testIsCompatibleCollectionResources(){
        shelf = new ShelfCollection(ResourceType.COIN);
        assertFalse(shelf.isCompatible(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Shield(), new Stone())))));
        assertTrue(shelf.isCompatible(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin(), new Coin())))));
        assertFalse(shelf.isCompatible(new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield())))));
    }


    /**
     * this test verifies that the add method return true only when the input resources is of the same type of the shelf
     * and only in this case increases the size of 1
     */
    @Test
    void testAdd(){
        shelf = new ShelfCollection(ResourceType.COIN);
        assertFalse(shelf.add(new Shield()));
        assertEquals(0,shelf.getSize());
        assertTrue(shelf.add(new Coin()));
        assertEquals(1,shelf.getSize());
    }


    /**
     *this test verifies that the sum method return true only when the collection contains only resources of the same type
     * of the shelf and in that case increases his size of the size of the collectionResources in in input. When is not true
     *the size of the shelf is unaffected
     */
    @Test
    void testSum(){
        shelf = new ShelfCollection(ResourceType.COIN);
        shelf.add(new Coin());
        assertFalse(shelf.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Shield(), new Stone())))));
        assertEquals(1,shelf.getSize());
        assertTrue(shelf.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin(), new Coin())))));
        assertEquals(5,shelf.getSize());
    }

    /**
     *this test verifies that containsAll method return true when all the resources are of the same type of the type of the
     * shelf and in number <=
     */
    @Test
    void testContainsAll(){
        shelf = new ShelfCollection(ResourceType.COIN);
        shelf.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin(), new Coin()))));
        assertTrue(shelf.containsAll(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin())))));
        assertFalse(shelf.containsAll(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin(), new Coin(), new Shield())))));
        assertFalse(shelf.containsAll(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin(), new Coin(), new Coin())))));
    }

    /**
     * this test verifies that the sub method return true when the collectionResources in input have all the elements of
     * the same type of the shelf and in quantity <= of the shelf, in that case remove the elements from the shelf
     */
    @Test
    void testSub(){
        shelf = new ShelfCollection(ResourceType.COIN);
        shelf.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin(), new Coin()))));
        assertFalse(shelf.sub(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin(), new Coin(), new Coin())))));
        assertEquals(4,shelf.getSize());
        assertFalse(shelf.sub(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Shield(), new Stone())))));
        assertEquals(4,shelf.getSize());
        assertTrue(shelf.sub(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin())))));
        assertEquals(1,shelf.getSize());
    }

    /**
     * this test verifies t
     */
    @Test
    void testRemove(){
        shelf = new ShelfCollection(ResourceType.COIN);
        shelf.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Coin(), new Coin()))));
        assertFalse(shelf.remove(new Shield()));
        assertEquals(4,shelf.getSize());
        assertFalse(shelf.remove(new Stone()));
        assertEquals(4,shelf.getSize());
        assertFalse(shelf.remove(new Servant()));
        assertEquals(4,shelf.getSize());
        assertTrue(shelf.remove(new Coin()));
        assertEquals(3, shelf.getSize());
        assertTrue(shelf.remove(new Coin()));
        assertTrue(shelf.remove(new Coin()));
        assertTrue(shelf.remove(new Coin()));
        assertEquals(0, shelf.getSize());
        assertFalse(shelf.remove(new Coin()));
        assertEquals(0, shelf.getSize());
    }

}
