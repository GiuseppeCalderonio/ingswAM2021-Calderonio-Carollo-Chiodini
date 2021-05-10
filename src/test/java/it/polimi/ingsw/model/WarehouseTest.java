package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PlayerAndComponents.Warehouse;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the warehouse
 */
class WarehouseTest {

    /**
     * this attribute represent the warehouse to test
     */
    private Warehouse warehouse;

    /**
     * this test verify that the getShelf return null if the input are not correct and the other way around
     */
    @Test
    void testGetShelf1(){
        warehouse = new Warehouse();
        assertNotNull(warehouse.getShelf(1));
        assertNotNull(warehouse.getShelf(2));
        assertNotNull(warehouse.getShelf(3));
    }

    /**
     * this test verify that the getShelf get the correct shelf with the right resources
     */
    @Test
    void testGetShelf2(){
        warehouse = new Warehouse();
        CollectionResources shelfCollection1 = new ShelfCollection(ResourceType.COIN);
        shelfCollection1.add(new Coin());
        shelfCollection1.add(new Coin());
        warehouse.addResources(shelfCollection1, 2);
        CollectionResources shelfCollection2 = new ShelfCollection(ResourceType.COIN);
        shelfCollection2.add(new Coin());
        shelfCollection2.add(new Coin());
        assertEquals(shelfCollection2 , warehouse.getShelf(2).getResources());
    }

    /**
     * this test verify that the method getShelfFromType return 0 if there is no shelf with
     * the type of resource requested, and return the correct shelf it there is that one
     */
    @Test
    void testGetShelfFromType1(){
        warehouse = new Warehouse();
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        shields.add(new Shield());
        coins.add(new Coin());
        servants.add(new Servant());
        assertEquals(0, warehouse.getShelfFromType(ResourceType.COIN));
        assertEquals(0, warehouse.getShelfFromType(ResourceType.STONE));
        warehouse.addResources(shields , 3);
        warehouse.addResources(servants , 2);
        warehouse.addResources(coins , 1);
        assertEquals(3, warehouse.getShelfFromType(ResourceType.SHIELD));
        assertEquals(2, warehouse.getShelfFromType(ResourceType.SERVANT));
        assertEquals(1, warehouse.getShelfFromType(ResourceType.COIN));
    }

    /**
     * this test verify that given a warehouse with a not empty shelf, and removing all
     * of the resources in that specific one, the method getShelfFromType,
     * when the type is of the resources just removed, returns 0
     */
    @Test
    void testGetShelfFromType2(){
        warehouse = new Warehouse();
        CollectionResources stones = new CollectionResources();
        stones.add(new Stone());
        stones.add(new Stone());
        warehouse.addResources(stones , 2);
        warehouse.removeResources(stones);
        assertEquals(0, warehouse.getShelfFromType(ResourceType.STONE));
    }

    /**
     * this method verify that the method getShelfFromType returns
     * the right values after a shift of resources
     */
    @Test
    void testGetShelfFromType3(){
        warehouse = new Warehouse();
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        shields.add(new Shield());
        shields.add(new Shield());
        coins.add(new Coin());
        warehouse.addResources(shields, 3);
        warehouse.shiftResources(1, 3);
        assertEquals(1 , warehouse.getShelfFromType(ResourceType.SHIELD));
        warehouse.addResources(coins , 2);
        warehouse.shiftResources(1, 2);
        assertEquals(1 , warehouse.getShelfFromType(ResourceType.COIN));
        assertEquals(2 , warehouse.getShelfFromType(ResourceType.SHIELD));
    }

    /**
     * this test verify that the method addResources add the resources correctly in the shelf selected
     */
    @Test
    void testAddResources1(){
        warehouse = new Warehouse();
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        servants.add(new Servant());
        servants.add(new Servant());
        CollectionResources toAdd = new ShelfCollection(ResourceType.SERVANT, new ArrayList<>(Arrays.asList(new Servant(), new Servant())));
        warehouse.addResources(toAdd, 2);
        assertEquals(servants , warehouse.getShelf(2).getResources());
    }

    /**
     * this test verify that the method addResources with one resource as argument
     * return 0 if there is enough space to add a resource of the same type of the shelf,
     * return 1 if the shelf selected is full of resources
     */
    @Test
    void testAddResources2(){
        warehouse = new Warehouse();
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        stones.add(new Stone());
        assertEquals(0, warehouse.addResources(stones , 2));
        assertEquals(0, warehouse.addResources(stones , 2));
        assertEquals(1, warehouse.addResources(stones , 2));
        assertEquals(1, warehouse.addResources(stones , 2));
    }

    /**
     * this test verify that if the argument of the method addResources is a CollectionResources with more resources
     * than the free spaces of the shelf specified in the index passed as argument, it return the resources discarded
     * the collection in input is a shelfResources oh the same type of the shelf selected
     */
    @Test
    void testAddResources3(){
        warehouse = new Warehouse();
        CollectionResources shield = new ShelfCollection(ResourceType.SHIELD);
        shield.add(new Shield());
        shield.add(new Shield());
        shield.add(new Shield());
        shield.add(new Shield());
        assertEquals(2, warehouse.addResources(shield , 2));
    }

    /**
     * this test verify that the method addResources always return the size of the collectionResources
     * given in input if the shelf selected is full of resources
     */
    @Test
    void testAddResources4(){
        warehouse = new Warehouse();
        warehouse.addResources(new ShelfCollection(ResourceType.COIN, new ArrayList<>(Collections.singletonList(new Coin()))), 1);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        servants.add(new Servant());
        servants.add(new Servant());
        coins.add(new Coin());
        coins.add(new Coin());
        coins.add(new Coin());
        assertEquals(coins.getSize(), warehouse.addResources(coins, 1));
        assertEquals(servants.getSize(), warehouse.addResources(servants, 1));
    }

    /**
     * this test verify that, initialising a warehouse with a full shelf and removing one resource from it, it is possible
     * to add a new resource of the same type of the shelf, and is possible to add every type of resource if
     * the shelf selected has capacity 1
     */
    @Test
    void testAddResources5(){
        warehouse = new Warehouse();
        CollectionResources coin = new ShelfCollection(ResourceType.COIN);
        CollectionResources servant = new ShelfCollection(ResourceType.SERVANT);
        coin.add(new Coin());
        servant.add(new Stone());
        warehouse.addResources(new ShelfCollection( ResourceType.COIN , new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield()))),
                3);
        warehouse.addResources(new ShelfCollection(ResourceType.STONE, new ArrayList<>(Collections.singletonList(new Stone()))),
                1);
        warehouse.removeResources(new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Coin()))));
        assertEquals(0 , warehouse.addResources(coin, 3));
        assertEquals(0, warehouse.addResources(servant, 1));
    }

    /**
     * this test verify that if the method addResources try to add resources in a shelf and
     * the type of the shelf is different from the type of the resources to add,
     * it returns the size of the resources to add
     */
    @Test
    void testAddResources6(){
        warehouse = new Warehouse();
        warehouse.addResources(new ShelfCollection(ResourceType.SERVANT, new ArrayList<>(Collections.singletonList(new Servant()))),
                3);
        CollectionResources coins= new ShelfCollection( ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin());
        coins.add(new Coin());
        assertEquals(coins.getSize(), warehouse.addResources(coins, 3));
    }

    /**
     * this test verify that, initialising a warehouse with a not empty shelf,
     * and after removing all the resources from that one, is possible to add every new resource there
     */
    @Test
    void testAddResources7(){
        warehouse = new Warehouse();
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        stones.add(new Stone());
        shields.add(new Shield());
        shields.add(new Shield());
        warehouse.addResources( shields, 2);
        warehouse.removeResources(shields);
        assertEquals(0 , warehouse.addResources(stones, 2));
    }

    /**
     * this test verify that is not possible to add a resource when
     * there is a shelf different from that one selected that has
     * the same type of the resource in input
     */
    @Test
    void testAddResources8(){
        warehouse = new Warehouse();
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        shields.add(new Shield());
        shields.add(new Shield());
        warehouse.addResources(shields ,1);
        assertEquals(shields.getSize(), warehouse.addResources(shields, 3));
    }

    /**
     * this test verify that, initialising a warehouse with a shelf that contains some resources
     * and removing them from it, it is possible to add in another shelf some new resources having the same type
     * of the initial resources just removed
     */
    @Test
    void testAddResources9(){
        warehouse = new Warehouse();
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin());
        warehouse.addResources(coins, 2);
        warehouse.removeResources(coins);
        assertEquals(0 , warehouse.addResources(coins, 3));
    }

    /**
     * this test verify that the method removeResources removes a resource in the shelf selected correctly
     */
    @Test
    void testRemoveResources1(){
        warehouse = new Warehouse();
        CollectionResources servants1 = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources servants2 = new ShelfCollection(ResourceType.SERVANT);
        servants1.add(new Servant());
        servants2.add(new Servant());
        servants2.add(new Servant());
        warehouse.addResources(servants2, 3);
        servants2.remove(new Servant());
        warehouse.removeResources(servants2);
        assertEquals(servants1, warehouse.getShelf(3).getResources());
    }

    /**
     * this test verify that passing as argument an empty collectionResources the
     * state of the warehouse doesn't change
     */
    @Test
    void testRemoveResources2(){
        warehouse = new Warehouse();
        CollectionResources stones1 = new ShelfCollection(ResourceType.STONE);
        CollectionResources shields1 = new ShelfCollection(ResourceType.SHIELD);
        stones1.add(new Stone());
        shields1.add(new Shield());
        shields1.add(new Shield());
        CollectionResources stones2 = new ShelfCollection(ResourceType.STONE);
        CollectionResources shields2 = new ShelfCollection(ResourceType.SHIELD);
        stones2.add(new Stone());
        shields2.add(new Shield());
        shields2.add(new Shield());
        warehouse.addResources(stones1, 1);
        warehouse.addResources(shields1, 3);
        warehouse.removeResources(new CollectionResources());
        assertEquals(stones2, warehouse.getShelf(1).getResources());
        assertEquals(shields2, warehouse.getShelf(3).getResources());
    }

    /**
     * this test verify that the method RemoveResources remove correctly the resources given in input
     * when the method has to remove resources from more tha one shelf
     */
    @Test
    void testRemoveResources3(){
        warehouse = new Warehouse();
        CollectionResources stones1 = new ShelfCollection(ResourceType.STONE);
        CollectionResources shields1 = new ShelfCollection(ResourceType.SHIELD);
        stones1.add(new Stone());
        stones1.add(new Stone());
        shields1.add(new Shield());
        shields1.add(new Shield());
        shields1.add(new Shield());
        CollectionResources stones2 = new ShelfCollection(ResourceType.STONE);
        CollectionResources shields2 = new ShelfCollection(ResourceType.SHIELD);
        stones2.add(new Stone());
        shields2.add(new Shield());
        shields2.add(new Shield());
        warehouse.addResources(stones1, 2);
        warehouse.addResources(shields1, 3);
        warehouse.removeResources(new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(), new Shield()))));
        assertEquals(stones2, warehouse.getShelf(2).getResources());
        assertEquals(shields2, warehouse.getShelf(3).getResources());
    }

    /**
     * this test verify that when the Resources given in input are equals to all the
     * resources contained in every shelf, the method remove all of them
     * creating an empty warehouse
     */
    @Test
    void testRemoveResources4(){
        warehouse = new Warehouse();
        CollectionResources stones1 = new ShelfCollection(ResourceType.STONE);
        CollectionResources shields1 = new ShelfCollection(ResourceType.SHIELD);
        stones1.add(new Stone());
        stones1.add(new Stone());
        shields1.add(new Shield());
        shields1.add(new Shield());
        shields1.add(new Shield());
        warehouse.addResources(stones1, 2);
        warehouse.addResources(shields1, 3);
        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Stone());
        toRemove.add(new Stone());
        toRemove.add(new Shield());
        toRemove.add(new Shield());
        toRemove.add(new Shield());
        warehouse.removeResources(toRemove);
        assertEquals( 0, warehouse.getNumberOfResources());
        assertEquals(0, warehouse.getTotalResources().getSize());
    }

    /**
     * this test verify that after a shift of resources the resources are shifted correctly
     * in particular the test create two collectionResources, then do the shift and verify that
     * the new shelf shifted are equals to the collections
     */
    @Test
    void testShiftResources1(){
        warehouse = new Warehouse();
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        coins.add(new Coin());
        coins.add(new Coin());
        servants.add(new Servant());
        servants.add(new Servant());
        warehouse.addResources(coins, 2);
        warehouse.addResources(servants, 3);
        warehouse.shiftResources(2, 3);
        assertEquals(coins, warehouse.getShelf(3).getResources());
        assertEquals(servants, warehouse.getShelf(2).getResources());
    }

    /**
     * this test verify that shifting two empty shelves does not change the state of the warehouse
     */
    @Test
    void testShiftResources2(){
        warehouse = new Warehouse();
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        servants.add(new Servant());
        warehouse.addResources(servants, 2);
        warehouse.shiftResources( 1, 3);
        assertTrue(warehouse.getShelf(1).isEmpty());
        assertTrue(warehouse.getShelf(3).isEmpty());
        assertEquals(servants, warehouse.getShelf(2).getResources());
    }

    /**
     * this test verify that shifting the same shelf does not change the state of warehouse
     */
    @Test
    void testShiftResources3(){
        warehouse = new Warehouse();
        warehouse.shiftResources( 2, 2);
        assertTrue(warehouse.getShelf(2).isEmpty());
        CollectionResources shield = new ShelfCollection(ResourceType.SHIELD);
        warehouse.addResources(shield, 2);
        warehouse.shiftResources(2 ,2 );
        assertEquals(shield, warehouse.getShelf(2).getResources());
    }

    /**
     * this test verify that if the method shifts one shelf into another that cannot contain
     * all the resources contained in the first one, it must return all the resources
     * that cannot be contained
     * and also test that the order of the arguments does not care, the result will be the same
     */
    @Test
    void testShiftResources4(){
        warehouse = new Warehouse();
        CollectionResources stones1 = new ShelfCollection(ResourceType.STONE);
        CollectionResources coins1 = new ShelfCollection(ResourceType.COIN);
        stones1.add(new Stone());
        coins1.add(new Coin());
        coins1.add(new Coin());
        coins1.add(new Coin());
        warehouse.addResources(stones1, 1);
        warehouse.addResources(coins1, 3);
        assertEquals(2, warehouse.shiftResources(1,3));
        warehouse = new Warehouse();
        CollectionResources stones2 = new ShelfCollection(ResourceType.STONE);
        CollectionResources coins2 = new ShelfCollection(ResourceType.COIN);
        stones2.add(new Stone());
        coins2.add(new Coin());
        coins2.add(new Coin());
        coins2.add(new Coin());
        warehouse.addResources(stones2, 1);
        warehouse.addResources(coins2, 3);
        assertEquals(2, warehouse.shiftResources(3,1));
    }

    /**
     * this test verify that if two shifts does not discard any resource,
     * this operation is cyclic, in particular calling this method two times
     * not change the state of the warehouse
     */
    @Test
    void testShiftResources5(){
        warehouse = new Warehouse();
        CollectionResources servant = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources shield = new ShelfCollection(ResourceType.SHIELD);
        warehouse.addResources(servant, 1);
        warehouse.addResources(shield, 2);
        warehouse.shiftResources(1, 2);
        warehouse.shiftResources(1, 2);
        assertEquals(servant, warehouse.getShelf(1).getResources());
        assertEquals(shield, warehouse.getShelf(2).getResources());
    }

    /**
     * this test verify that the method getTotalResources in an empty warehouse return an empty collectionResources
     */
    @Test
    void testGetTotalResources1(){
        warehouse = new Warehouse();
        assertEquals( 0, warehouse.getTotalResources().getSize());
    }

    /**
     * this test verify that the method getTotalResources actually return the right resources
     */
    @Test
    void testGetTotalResources2(){
        warehouse = new Warehouse();
        CollectionResources toCompare = new CollectionResources();
        CollectionResources coin = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        coin.add(new Coin());
        servants.add(new Servant());
        servants.add(new Servant());
        shields.add(new Shield());
        shields.add(new Shield());
        shields.add(new Shield());
        warehouse.addResources(coin, 1);
        warehouse.addResources(servants, 2);
        warehouse.addResources(shields, 3);
        toCompare.sum(servants);
        toCompare.sum(coin);
        toCompare.sum(shields);
        assertEquals(toCompare, warehouse.getTotalResources());
    }

    /**
     * this test verify that the number of resources of an empty warehouse is 0
     */
    @Test
    void testGetNumberOfResources1(){
        warehouse = new Warehouse();
        assertEquals(0 , warehouse.getNumberOfResources());
    }

    /**
     * this test verify that the method getTotalResources actually
     * return the right number of resources
     */
    @Test
    void testGetNumberOfResources2(){
        warehouse = new Warehouse();
        CollectionResources coin = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        coin.add(new Coin());
        servants.add(new Servant());
        servants.add(new Servant());
        shields.add(new Shield());
        shields.add(new Shield());
        shields.add(new Shield());
        warehouse.addResources(coin, 1);
        warehouse.addResources(servants, 2);
        warehouse.addResources(shields, 3);
        assertEquals(6, warehouse.getNumberOfResources());
    }

    /**
     * this method verify that the method getNumOfShelves always return 3
     */
    @Test
    void getNumOfShelves(){
        warehouse = new Warehouse();
        assertEquals(3, warehouse.getNumOfShelves());
    }
}