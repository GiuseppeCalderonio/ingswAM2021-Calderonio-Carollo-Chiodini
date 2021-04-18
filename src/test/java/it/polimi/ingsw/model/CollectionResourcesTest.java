package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the methods of CollectionResources
 */
public class CollectionResourcesTest {

    private CollectionResources collection;

    /**
     * this test verifies the size of a empty CollectionResources
     */
    @Test
    void testGetSize1() {
        collection = new CollectionResources();
        assertEquals(0, collection.getSize());
    }

    /**
     *this test verify the size of a not empty CollectionResources
     */
    @Test
    public void testGetSize2(){
        collection = new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Shield(), new Servant(), new Stone())));
        assertEquals(5 , collection.getSize());
    }

    /**
     * this test verify that the method contains work correctly
     */
    @Test
    void testContains(){
        collection = new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin(), new Shield(), new Stone())));
        assertTrue(collection.contains(new Coin()));
        assertTrue(collection.contains(new Shield()));
        assertFalse(collection.contains(new Servant()));
        assertTrue(collection.contains(new Stone()));
    }

    /**
     * this test verify the size of a collectionResources after add and remove
     */
    @Test
    void testGetAddRemove() {
        collection = new CollectionResources();
        collection.add(new Servant());
        collection.add(new Servant());
        collection.add(new Servant());
        collection.add(new Shield());
        assertEquals(4 , collection.getSize());
        collection.remove(new Servant());
        collection.remove(new Servant());
        assertEquals(2, collection.getSize());
    }

    /**
     * this test check if the method remove(resource) works correctly: if a the resource of type in input is inside the CollectionResources
     * then removes it from CollectionResources and return true otherwise doesn't modify the CollectionResources and return false
     */
    @Test
    void testRemove(){
        Resource servant = new Servant();
        collection = new CollectionResources();
        collection.add(servant);
        assertFalse(collection.remove(new Shield()));
        assertEquals(1,collection.getSize());
        assertTrue(collection.remove(new Servant()));
        assertEquals(0,collection.getSize());
    }

    /**
     * this test check that size is \oldSize + toAddSize and collection contains toAdd
     */
    @Test
    void testSum(){
        int sizeCollection;
        int sizeToAdd;
        CollectionResources toAdd = new CollectionResources();
        collection = new CollectionResources();
        collection.add(new Servant());
        collection.add(new Shield());
        toAdd.add(new Coin());
        toAdd.add(new Stone());
        sizeCollection = collection.getSize();
        sizeToAdd = toAdd.getSize();
        collection.sum(toAdd);
        assertEquals(sizeCollection + sizeToAdd, collection.getSize());
        assertTrue(collection.containsAll(toAdd));
    }

    /** check that sub return true if toSub is inside collection and decrease
     * his size of toSub.getSize() else return false and collection is equals to \oldCollection
     */
    @Test
    void testSub1() {
        CollectionResources toSub = new CollectionResources();
        collection = new CollectionResources();
        toSub.add(new Coin());
        toSub.add(new Stone());
        assertFalse(collection.sub(toSub));
        assertEquals(0,collection.getSize());
        collection.add(new Servant());
        collection.add(new Shield());
        assertFalse(collection.sub(toSub));
        assertEquals(2,collection.getSize());
        collection.add(new Coin());
        collection.add(new Stone());
        assertTrue(collection.sub(toSub));
        assertEquals(2,collection.getSize());
    }

    /**
     * this test verify if the method sub work correctly with two CollectionResources of the same type
     */
    @Test
    void testSub2(){
        collection = new CollectionResources();
        CollectionResources toRemove = new CollectionResources();
        collection.add(new Coin());
        toRemove.add(new Coin());
        toRemove.add(new Coin());
        assertFalse(collection.sub(toRemove));
        collection.add(new Coin());
        toRemove.add(new Coin());
        assertFalse(collection.sub(toRemove));
        collection.add(new Coin());
        collection.add(new Coin());
        collection.add(new Coin());
        assertTrue(collection.sub(toRemove));
        assertEquals(2, collection.getSize());
    }

    /**
     * this test verify that an empty CollectionResources always contains another empty CollectionResources
     */
    @Test
    void testContainsAll1(){
        collection = new CollectionResources();
        CollectionResources parameter = new CollectionResources();
        assertTrue(collection.containsAll(parameter));
        assertTrue(parameter.containsAll(collection));
    }

    /**
     * this test verify that a not empty CollectionResources always contains a empty CollectionResources, but not the reverse
    */
    @Test
    void testContainsAll2() {
        collection = new CollectionResources();
        CollectionResources parameter = new CollectionResources();
        collection.add(new Servant());
        assertTrue(collection.containsAll(parameter));
        assertFalse(parameter.containsAll(collection));
    }

    /**
     * this test verify that a CollectionResources with 3 servants contains another CollectionResources with
     * 2 servants but not the reverse
     */
    @Test
    void testContainsAll3(){
        collection = new CollectionResources();
        CollectionResources toCompare = new CollectionResources();
        collection.add(new Servant());
        collection.add(new Servant());
        collection.add(new Servant());
        toCompare.add(new Servant());
        toCompare.add(new Servant());
        assertTrue(collection.containsAll(toCompare));
        assertFalse(toCompare.containsAll(collection));
    }

    /**
     * this test verify that the method containsAll return false when his parameter has elements which collection doesn't contains and
     * if collection contains all the elements of the input of the method containsAll return true
     */
    @Test
    void testContainsAll4() {
        collection = new CollectionResources();
        CollectionResources parameter = new CollectionResources();
        collection.add(new Servant());
        parameter.add(new Shield());
        parameter.add(new Servant());
        assertFalse(collection.containsAll(parameter));
        collection.add(new Shield());
        collection.add(new Coin());
        assertTrue(collection.containsAll(parameter));
    }

    /**
     * this test verify that two empty CollectionResources are equals
     */
    @Test
    void testEquals1(){
        collection = new CollectionResources();
        CollectionResources parameter = new CollectionResources();
        assertEquals(collection , parameter);
    }

    /**
     * check that collection.equals == true <==> collection has the same elements of the input CollectionResources
     */
    @Test
    void testEquals2(){
        collection = new CollectionResources();
        CollectionResources parameter = new CollectionResources();
        collection.add(new Servant());
        parameter.add(new Shield());
        parameter.add(new Servant());
        assertNotEquals(collection , parameter);
        collection.add(new Shield());
        assertEquals(collection , parameter);
    }

    /**
     * this test verify the equals when two Collections have the same resource
     */
    @Test
    public void testEquals3(){
        collection = new CollectionResources();
        CollectionResources collection2 = new CollectionResources();
        collection.add(new Coin());
        collection2.add(new Coin());
        assertEquals(collection, collection2);
    }

    /**
     * this test verify that adding 4 resources manually or use the
     * for each is the same
     */
    @Test
    void testIterator1(){
        collection = new CollectionResources();
        collection.add(new Coin());
        collection.add(new Servant());
        collection.add(new Shield());
        collection.add(new Stone());
        int counter = 0;
        CollectionResources collection2 = new CollectionResources();
        for(Resource r: collection){
            counter++;
            collection2.add(r);
        }
        assertEquals(4, counter);
        assertEquals(collection , collection2);
    }

    /**
     * this test verify that an iterator in an empty collectionResources
     * do anything
     */
    @Test
    void testIterator2(){
        collection = new CollectionResources();
        CollectionResources collection2 = new CollectionResources();
        int counter = 0;
        for (Resource r : collection2){
            counter++;
            collection2.add(r);
        }
        assertEquals(0, counter);
        assertEquals(collection2, collection);
    }

    /**
     * this test verify that an iterator works when have to iterate in
     * a collectionResources that contains just one type of resource
     */
    @Test
    void testIterator3(){
        collection = new CollectionResources();
        collection.add(new Coin());
        collection.add(new Coin());
        collection.add(new Coin());
        int counter = 0;
        CollectionResources collection2 = new CollectionResources();
        for(Resource r: collection){
            counter++;
            collection2.add(r);
        }
        assertEquals(3, counter);
        assertEquals(collection , collection2);
    }

    /**
     * this test verify that an empty collectionResources return
     * an empty list
     */
    @Test
    void testAsList1(){
        collection = new CollectionResources();
        assertTrue(collection.asList().isEmpty());
    }

    /**
     * this test create a non empty collectionResources and
     * verify that return the right list associated
     */
    @Test
    void testAsList2(){
        collection = new CollectionResources();
        List<Resource> list= new ArrayList<>();
        collection.add(new Coin());
        collection.add(new Coin());
        collection.add(new Shield());
        collection.add(new Servant());
        list.add(new Coin());
        list.add(new Coin());
        list.add(new Shield());
        list.add(new Servant());
        assertEquals(list, collection.asList());
    }
}