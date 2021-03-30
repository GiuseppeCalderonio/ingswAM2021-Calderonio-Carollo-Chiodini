package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the ShelfCollection
 */
class ShelfCollectionTest {

    CollectionResources shelf;

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

}