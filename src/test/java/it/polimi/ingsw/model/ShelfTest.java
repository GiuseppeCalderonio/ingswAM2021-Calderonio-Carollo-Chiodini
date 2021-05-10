package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the Shelf
 */
class ShelfTest {

    /**
     * this attribute represent the shelf to test
     */
    private Shelf shelf;

    /**
     * this test verify that the method getCapacity works correctly
     */
    @Test
    void testGetCapacity(){
        shelf = new Shelf(3);
        assertEquals(3,shelf.getCapacity());
    }
    /**
     * this test verify that the shelf contains correctly the resource just added
     */
    @Test
    void testAddResources1(){
        shelf = new Shelf(2);
        assertFalse(shelf.addResource(new Shield()));
        CollectionResources collection = new CollectionResources();
        collection.add(new Shield());
        assertEquals(collection, shelf.getResources());
    }

    /**
     * this test verify that is impossible to insert a resource when the shelf reach his max capacity
     * and return true because the resource must be discarded
     */
    @Test
    void testAddResources2(){
        shelf = new Shelf(1);
        assertFalse(shelf.addResource(new Coin()));
        assertTrue(shelf.addResource(new Coin()));
        CollectionResources collection = new CollectionResources();
        collection.add(new Coin());
        assertEquals(collection, shelf.getResources());
    }

    /**
     * this test verify that is impossible to insert a new resource when the shelf isn't empty and contains
     * a different type resource, and return true because the new resource must be discarded
     */
    @Test
    void testAddResources3(){
        shelf = new Shelf(3);
        CollectionResources collection = new CollectionResources();
        collection.add(new Servant());
        shelf.addResource(new Servant());
        assertTrue(shelf.addResource(new Stone()));
        assertEquals(collection, shelf.getResources());

        collection.add(new Servant());
        assertFalse(shelf.addResource(new Servant()));
        assertTrue(shelf.addResource(new Shield()));
        assertTrue(shelf.addResource(new Coin()));
        assertEquals(collection, shelf.getResources());
    }

    /**
     * this test verify if the method isEmpty works correctly
     */
    @Test
    void testIsEmpty(){
        shelf = new Shelf(2);
        assertTrue(shelf.isEmpty());
        shelf.addResource(new Coin());
        assertFalse(shelf.isEmpty());
    }

    /**
     * this test verify if the method removeAll remove all the resources from a shelf correctly
     * trying to remove all the resources from an empty shelf, then from a normal shelf
     */
    @Test
    void removeAll(){
        shelf = new Shelf(3);
        shelf.removeAll();
        assertTrue(shelf.isEmpty());
        shelf.addResource(new Coin());
        shelf.addResource(new Coin());
        shelf.addResource(new Coin());
        shelf.removeAll();
        assertTrue(shelf.isEmpty());
    }

    /**
     * this test verify that the method removeResource remove a resource from a shelf with one resources
     */
    @Test
    void testRemoveResource1(){
        shelf = new Shelf(1);
        shelf.addResource(new Stone());
        assertEquals(0 , shelf.removeResource(new Stone()));
        assertTrue(shelf.isEmpty());
    }

    /**
     * this test verify that a resource can be removed after an add
     */
    @Test
    void testRemoveResource2(){
        shelf = new Shelf(3);
        shelf.addResource(new Shield());
        shelf.addResource(new Shield());
        shelf.addResource(new Shield());
        assertEquals(2, shelf.removeResource(new Shield()));
        assertEquals(1, shelf.removeResource(new Shield()));
        shelf.addResource(new Shield());
        assertEquals(1, shelf.removeResource(new Shield()));
    }

    /**
     * this test verify that a shelf has no type if it is empty returning null,
     * and the type of the resource contained if it contains one
     */
    @Test
    void testGetResourceType(){
        shelf = new Shelf(2);
        assertNull(shelf.getResourceType());
        shelf.addResource(new Coin());
        assertEquals(ResourceType.COIN, shelf.getResourceType());
        shelf.removeResource(new Coin());
        assertNull(shelf.getResourceType());
    }

    /**
     * this test verify that a shelf with a capacity of 2 resources has the right amount of free slots
     */
    @Test
    void testGetFreeSlot1(){
        shelf = new Shelf( 2);
        assertEquals(2, shelf.getFreeSlots());
        shelf.addResource(new Servant());
        assertEquals(1, shelf.getFreeSlots());
        shelf.removeResource(new Servant());
        assertEquals(2, shelf.getFreeSlots());
    }

    /**
     * this test verify that a shelf with capacity 1 has 0 free slot after adding a resource
     */
    @Test
    void testGetFreeSlot2(){
        shelf = new Shelf(1);
        shelf.addResource( new Stone());
        assertEquals(0 , shelf.getFreeSlots());
    }

    /**
     * this test verify that after the method removeAll the capacity equals the free slots
     */
    @Test
    void testCapacityAndFreeSlots(){
        shelf = new Shelf(3);
        shelf.addResource(new Servant());
        shelf.addResource(new Servant());
        shelf.addResource(new Servant());
        assertEquals(0, shelf.getFreeSlots());
        shelf.removeAll();
        assertEquals(shelf.getCapacity(), shelf.getFreeSlots());
    }

    /**
     * this test verify that the shelf can change his type of resource when it is empty, without losing his properties
     */
    @Test
    void testChangeResource(){
        shelf = new Shelf(2);
        shelf.addResource(new Coin());
        shelf.removeResource(new Coin());
        shelf.addResource(new Servant());
        assertEquals(ResourceType.SERVANT, shelf.getResourceType());
        assertTrue(shelf.addResource(new Coin()));
        assertEquals(1,shelf.getFreeSlots());
        shelf.addResource(new Servant());
        shelf.removeAll();
        shelf.addResource(new Stone());
        assertEquals(ResourceType.STONE, shelf.getResourceType());
        assertTrue(shelf.addResource(new Servant()));
        assertTrue(shelf.addResource(new Coin()));
    }
}