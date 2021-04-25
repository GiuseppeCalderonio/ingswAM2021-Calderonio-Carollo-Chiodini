package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the MapResource class
 */
class MapResourcesTest {

    /**
     * this test verify that the getter methods work correctly
     */
    @Test
    public void testGetters(){
        MapResources map = new MapResources(new Coin(), 2);
        assertEquals(new Coin(), map.getResource());
        assertEquals(2 , map.getCardinality());
    }

    /**
     * this test verify that the add and remove methods work correctly
     */
    @Test
    public void testAddRemove(){
        MapResources map = new MapResources(new Servant(), 1);
        map.add(1);
        assertEquals(2,map.getCardinality());
        map.add(4);
        assertEquals(6,map.getCardinality());
        map.remove(1);
        assertEquals(5,map.getCardinality());
        map.remove(2);
        assertEquals(3,map.getCardinality());
        map.remove(3);
        assertEquals(0,map.getCardinality());
    }

    /**
     * this test verify that the equals method works correctly
     */
    @Test
    public void testEquals(){
        MapResources map1 = new MapResources(new Coin(), 4);
        MapResources map2 = new MapResources(new Coin(), 4);
        MapResources map3 = new MapResources(new Coin(), 3);
        MapResources map4 = new MapResources(new Servant(), 4);
        assertEquals(map1, map2);
        assertEquals(map2, map1);
        assertNotEquals(map1, map3);
        assertNotEquals(map3, map1);
        assertNotEquals(map1, map4);
        map3.add(1);
        assertEquals(map1, map3);
    }

    /**
     * this test verify that the method isEmpty works correctly
     */
    @Test
    public void testIsEmpty(){
        MapResources map1 = new MapResources(new Shield(), 1);
        assertFalse(map1.isEmpty());
        map1.remove(1);
        assertTrue(map1.isEmpty());
        map1.remove(1);
        assertTrue(map1.isEmpty());
    }

    /**
     * this test verify that an iterator doesn't change a collection when
     * have to iterate to an empty mapResources
     */
    @Test
    public void testIterator(){
        MapResources map1 = new MapResources(new Stone(), 0);
        MapResources map2 = new MapResources(new Stone(), 0);
        for( Resource ignored : map1){
            map2.add(1);
        }
        assertEquals(map1, map2);
        assertEquals(0 , map1.getCardinality());
        assertEquals(0 , map2.getCardinality());
    }

    /**
     * this test verify that the iterator works correctly in normal conditions
     */
    @Test
    public void testIterator2(){
        MapResources map1 = new MapResources(new Stone(), 4);
        MapResources map2 = new MapResources(new Stone(), 0);
        for( Resource ignored : map1){
            map2.add(1);
        }
        assertEquals(map1, map2);
        assertEquals(4 , map1.getCardinality());
        assertEquals(4 , map2.getCardinality());
    }

}