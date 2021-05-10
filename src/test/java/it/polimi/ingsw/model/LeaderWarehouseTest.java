package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PlayerAndComponents.LeaderWarehouse;
import it.polimi.ingsw.model.PlayerAndComponents.Warehouse;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * this class test the leader warehouse
 */
class LeaderWarehouseTest {

    /**
     * this attribute represent the leader warehouse to test
     */
    private Warehouse leaderWarehouse;

    /**
     * this test verify that the constructor create an equal warehouse adding the shelves correctly
     * when the shelf is empty
     */
    @Test
    void testConstructor1(){
        Warehouse warehouse= new Warehouse();
        leaderWarehouse = new LeaderWarehouse(new Stone(), warehouse);
        assertEquals(0, leaderWarehouse.getNumberOfResources());
        assertEquals(ResourceType.STONE, leaderWarehouse.getShelf(4).getResourceType());
        leaderWarehouse.addShelf(new Coin());
        assertEquals(0, leaderWarehouse.getNumberOfResources());
        assertEquals(ResourceType.COIN, leaderWarehouse.getShelf(5).getResourceType());
    }

    /**
     * this test verify that the constructor create an equal warehouse adding the shelves correctly
     * when the warehouse isn't empty
     */
    @Test
    void testConstructor2(){
        Warehouse warehouse= new Warehouse();
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        servants.add(new Servant());
        servants.add(new Servant());
        servants.add(new Servant());
        warehouse.addResources(coins, 1);
        warehouse.addResources(servants, 3);
        leaderWarehouse = new LeaderWarehouse(new Stone(), warehouse);
        assertEquals(4, leaderWarehouse.getNumberOfResources());
        assertEquals(ResourceType.STONE, leaderWarehouse.getShelf(4).getResourceType());
        assertEquals(coins, leaderWarehouse.getShelf(1).getResources());
        assertEquals(servants, leaderWarehouse.getShelf(3).getResources());
        leaderWarehouse.addShelf(new Coin());
        assertEquals(4, leaderWarehouse.getNumberOfResources());
        assertEquals(ResourceType.COIN, leaderWarehouse.getShelf(5).getResourceType());
        assertEquals(coins, leaderWarehouse.getShelf(1).getResources());
        assertEquals(servants, leaderWarehouse.getShelf(3).getResources());
    }

    /**
     * this method verify that a leader shelf just created is empty
     * using a getter of shelf
     */
    @Test
    void testGetShelf1(){
        leaderWarehouse = new LeaderWarehouse(new Shield(), new Warehouse());
        assertTrue(leaderWarehouse.getShelf(4).isEmpty());
        leaderWarehouse.addShelf(new Stone());
        assertTrue(leaderWarehouse.getShelf(5).isEmpty());
    }

    /**
     * this test verify that the method getShelf works when adding
     * resources to the leader shelves
     */
    @Test
    void testGetShelf2(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        leaderWarehouse.addResources(coins, 4);
        assertEquals(coins, leaderWarehouse.getShelf(4).getResources());
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        stones.add(new Stone());
        stones.add(new Stone());
        leaderWarehouse.addShelf(new Stone());
        leaderWarehouse.addResources(stones, 5);
        assertEquals(stones, leaderWarehouse.getShelf(5).getResources());
    }

    /**
     * this test verify that the add method works in normal conditions,
     * adding compatible resources to the leader shelves
     */
    @Test
    void testAddResources1(){
        leaderWarehouse = new LeaderWarehouse(new Servant(), new Warehouse());
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        servants.add(new Servant());
        servants.add(new Servant());
        assertEquals(0, leaderWarehouse.addResources(servants, 4));
        assertEquals(servants, leaderWarehouse.getShelf(4).getResources());
        leaderWarehouse.addShelf(new Stone());
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        stones.add(new Stone());
        assertEquals(0, leaderWarehouse.addResources(stones, 5));
        assertEquals(stones, leaderWarehouse.getShelf(5).getResources());
    }

    /**
     * this test verify that is not possible to add a resource in a leaderShelf
     * when the resource is not compatible with the shelf even if the shelf is empty
     */
    @Test
    void testAddResources2(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        leaderWarehouse.addShelf(new Servant());
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        shields.add(new Shield());
        shields.add(new Shield());
        stones.add(new Stone());
        assertEquals(shields.getSize(), leaderWarehouse.addResources(shields, 4));
        assertEquals(stones.getSize(), leaderWarehouse.addResources(stones, 4));
        assertEquals(shields.getSize(), leaderWarehouse.addResources(shields, 5));
        assertEquals(stones.getSize(), leaderWarehouse.addResources(stones, 5));
    }

    /**
     * this test verify that is not possible to add a resource in a leaderShelf
     * when the resource is not compatible with the shelf
     */
    @Test
    void tesAddResources3(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        leaderWarehouse.addShelf(new Servant());
        CollectionResources coin = new ShelfCollection(ResourceType.COIN);
        CollectionResources servant = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        coin.add(new Coin());
        servant.add(new Servant());
        leaderWarehouse.addResources(coin, 4);
        leaderWarehouse.addResources(servant, 5);
        shields.add(new Shield());
        shields.add(new Shield());
        stones.add(new Stone());
        assertEquals(shields.getSize(), leaderWarehouse.addResources(shields, 4));
        assertEquals(stones.getSize(), leaderWarehouse.addResources(stones, 4));
        assertEquals(shields.getSize(), leaderWarehouse.addResources(shields, 5));
        assertEquals(stones.getSize(), leaderWarehouse.addResources(stones, 5));
    }

    /**
     * this test verify that is not possible to add resources in a full shelf
     * and return all of them
     */
    @Test
    void testAddResources4(){
        leaderWarehouse = new LeaderWarehouse(new Shield(), new Warehouse());
        leaderWarehouse.addShelf(new Servant());
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        shields.add(new Shield());
        shields.add(new Shield());
        servants.add(new Servant());
        servants.add(new Servant());
        leaderWarehouse.addResources(shields, 4);
        leaderWarehouse.addResources(servants, 5);
        assertEquals(shields.getSize(), leaderWarehouse.addResources(shields, 4));
        assertEquals(servants.getSize(), leaderWarehouse.addResources(servants, 5));
    }

    /**
     * this test verify that the method addResources if the
     * resources to add are higher then the actual free slots,
     * return the right amount of resources discarded
     */
    @Test
    void testAddResources5(){
        leaderWarehouse = new LeaderWarehouse(new Stone(), new Warehouse());
        leaderWarehouse.addShelf(new Coin());
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin());
        stones.add(new Stone());
        stones.add(new Stone());
        stones.add(new Stone());
        leaderWarehouse.addResources(new ShelfCollection(ResourceType.COIN, new ArrayList<>(Collections.singletonList(new Coin()))), 5);
        assertEquals(1 , leaderWarehouse.addResources(stones, 4));
        assertEquals(1 , leaderWarehouse.addResources(coins, 5));
    }

    /**
     * this test verify that after adding some resources and removing all of them from a leader shelf,
     * it continue to accept only the resources of his specific type
     */
    @Test
    void testAddResources6(){
        leaderWarehouse = new LeaderWarehouse(new Shield(), new Warehouse());
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        shields.add(new Shield());
        leaderWarehouse.addResources(shields, 4);
        leaderWarehouse.removeResources(shields);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        assertEquals(1, leaderWarehouse.addResources(coins, 4));
    }

    /**

     * this method is used to test a particular case of warehouse insertion. The player has two coin in the third shelf
     * and one coin in a leader shelf; we try to insert one coin in the third shelf and another one in the leader shelf
     */
    @Test
    public void testAddResources7(){
        CollectionResources coins1 = new ShelfCollection(ResourceType.COIN);
        coins1.add(new Coin());
        CollectionResources coins2 = new ShelfCollection(ResourceType.COIN);
        coins2.add(new Coin()); coins2.add(new Coin());
        CollectionResources coins3 = new ShelfCollection(ResourceType.COIN);
        coins3.add(new Coin()); coins3.add(new Coin()); coins3.add(new Coin());
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        assertEquals(0 , leaderWarehouse.addResources(coins1,4));
        assertEquals(0 , leaderWarehouse.addResources(coins2, 3));
        assertEquals(0 , leaderWarehouse.addResources(coins2,3));
        assertEquals(leaderWarehouse.getShelf(3).getResources() , coins3);
        assertEquals(leaderWarehouse.getShelf(4).getResources() , coins2);
    }

    /**
     * this method is used to test a particular case of warehouse insertion. The player has two coin in the third shelf
     * and one coin in a leader shelf; we try to insert one coin in the third shelf and another one in the leader shelf
     */
    @Test
    public void testAddResources8(){
        CollectionResources coins1 = new ShelfCollection(ResourceType.COIN);
        coins1.add(new Coin());
        CollectionResources coins2 = new ShelfCollection(ResourceType.COIN);
        coins2.add(new Coin()); coins2.add(new Coin());
        CollectionResources coins3 = new ShelfCollection(ResourceType.COIN);
        coins3.add(new Coin()); coins3.add(new Coin()); coins3.add(new Coin());
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        assertEquals(0 , leaderWarehouse.addResources(coins1,4));
        assertEquals(0 , leaderWarehouse.addResources(coins2, 3));
        assertEquals(1 , leaderWarehouse.addResources(coins3,3));
        assertEquals(leaderWarehouse.getShelf(3).getResources() , coins3);
        assertEquals(leaderWarehouse.getShelf(4).getResources() , coins2);
    }

    /**
     * this method is used to test a particular case of warehouse insertion. The player has two coin in the third shelf
     * and one coin in a leader shelf; we try to insert one coin in the third shelf and another one in the leader shelf
     */
    @Test
    public void testAddResources9(){
        CollectionResources coins1 = new ShelfCollection(ResourceType.COIN);
        coins1.add(new Coin());
        CollectionResources coins2 = new ShelfCollection(ResourceType.COIN);
        coins2.add(new Coin()); coins2.add(new Coin());
        CollectionResources coins3 = new ShelfCollection(ResourceType.COIN);
        coins3.add(new Coin()); coins3.add(new Coin()); coins3.add(new Coin());
        leaderWarehouse = new LeaderWarehouse(new Shield(), new Warehouse());
        assertEquals(0 , leaderWarehouse.addResources(coins3, 3));
        assertEquals(2 , leaderWarehouse.addResources(coins2,3));
        leaderWarehouse.addShelf(new Coin());
        assertEquals(0 , leaderWarehouse.addResources(coins2,3));
        assertEquals(leaderWarehouse.getShelf(3).getResources() , coins3);
        assertEquals(leaderWarehouse.getShelf(5).getResources() , coins2);
    }



    /**
     * this test verify that is possible that exist a shelf with one type, and a leader shelf
     * of the same type
     */
    @Test
    void testMoreThanOneShelf(){
        leaderWarehouse = new LeaderWarehouse(new Servant(), new Warehouse());
        CollectionResources servants1 = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources servants2 = new ShelfCollection(ResourceType.SERVANT);
        servants2.add(new Servant());
        servants1.add(new Servant());
        servants1.add(new Servant());
        leaderWarehouse.addResources(servants1, 1);
        leaderWarehouse.addResources(servants1, 4);
        assertEquals(servants2, leaderWarehouse.getShelf(1).getResources());
        assertEquals(servants1, leaderWarehouse.getShelf(4).getResources());
    }

    /**
     * this test verify that the method removeResources when have an
     * empty collection as parameter, does not change the state of the warehouse
     */
    @Test
    void RemoveResources1(){
        leaderWarehouse = new LeaderWarehouse(new Servant(), new Warehouse());
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        servants.add(new Servant());
        servants.add(new Servant());
        stones.add(new Stone());
        stones.add(new Stone());
        coins.add(new Coin());
        coins.add(new Coin());
        leaderWarehouse.addResources(servants, 4);
        leaderWarehouse.addResources(stones, 3);
        leaderWarehouse.addResources(coins, 2);
        leaderWarehouse.removeResources(new CollectionResources());
        assertEquals(servants, leaderWarehouse.getShelf(4).getResources());
        assertEquals(stones, leaderWarehouse.getShelf(3).getResources());
        assertEquals(coins, leaderWarehouse.getShelf(2).getResources());
    }

    /**
     * this test verify that the remove method works correctly when the resources
     * to remove are all from the first 3 shelves
     */
    @Test
    void RemoveResources2(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources stone = new ShelfCollection(ResourceType.STONE);
        shields.add(new Shield());
        shields.add(new Shield());
        shields.add(new Shield());
        stone.add(new Stone());
        leaderWarehouse.addResources(shields, 3);
        leaderWarehouse.addResources(stone,1);
        CollectionResources toRemove = new CollectionResources();
        toRemove.sum(stone);
        toRemove.sum(shields);
        leaderWarehouse.removeResources(toRemove);
        assertTrue(leaderWarehouse.getShelf(1).isEmpty());
        assertTrue(leaderWarehouse.getShelf(3).isEmpty());
    }

    /**
     * this test verify that the remove method works correctly when the resources
     * to remove are all from the leader shelves
     */
    @Test
    void RemoveResources3(){
        leaderWarehouse = new LeaderWarehouse(new Servant(), new Warehouse());
        leaderWarehouse.addShelf(new Stone());
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        stones.add(new Stone());
        stones.add(new Stone());
        servants.add(new Servant());
        CollectionResources toRemove = new CollectionResources();
        toRemove.sum(stones);
        toRemove.sum(servants);
        leaderWarehouse.removeResources(toRemove);
        assertTrue(leaderWarehouse.getShelf(4).isEmpty());
        assertTrue(leaderWarehouse.getShelf(5).isEmpty());
    }

    /**
     * this test verify that the method remove correctly resources from
     * the normal shelves and the leader shelves, when there aren't type of
     * shelves in common
     */
    @Test
    void RemoveResources4(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        leaderWarehouse.addShelf(new Stone());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN, new ArrayList<>(Arrays.asList(new Coin(), new Coin())));
        CollectionResources stones = new ShelfCollection(ResourceType.STONE, new ArrayList<>(Collections.singletonList(new Stone())));
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD, new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield())));
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT, new ArrayList<>(Collections.singletonList(new Servant())));
        leaderWarehouse.addResources(coins, 4);
        leaderWarehouse.addResources(stones, 5);
        leaderWarehouse.addResources(shields, 3);
        leaderWarehouse.addResources(servants, 1);
        CollectionResources toRemove = new CollectionResources();
        coins.remove(new Coin());
        toRemove.sum(coins);
        toRemove.sum(stones);
        shields.remove(new Shield());
        toRemove.sum(shields);
        shields.remove(new Shield());
        toRemove.add(new Servant());
        leaderWarehouse.removeResources(toRemove);
        assertEquals(coins, leaderWarehouse.getShelf(4).getResources() );
        assertEquals(shields, leaderWarehouse.getShelf(3).getResources() );
        assertTrue( leaderWarehouse.getShelf(5).isEmpty() );
        assertTrue( leaderWarehouse.getShelf(1).isEmpty() );
    }

    /**
     * this test verify that the method remove, when there are a normal shelf with
     * the same type of a leader shelf, it remove the resources from the normal one
     * and only once the normal shelf is empty start removing the resources from the
     * leader one
     */
    @Test
    void RemoveResources5(){
        leaderWarehouse = new LeaderWarehouse(new Servant(), new Warehouse());
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources toRemove = new ShelfCollection(ResourceType.SERVANT);
        servants.add(new Servant());
        servants.add(new Servant());// two servants
        toRemove.add(new Servant());// one servant
        leaderWarehouse.addResources(servants, 3);
        leaderWarehouse.addResources(servants, 4);
        leaderWarehouse.removeResources(toRemove);
        assertEquals(toRemove, leaderWarehouse.getShelf(4).getResources());
        assertEquals(servants, leaderWarehouse.getShelf(3).getResources());
        leaderWarehouse.removeResources(toRemove);
        assertTrue(leaderWarehouse.getShelf(4).isEmpty());
        leaderWarehouse.removeResources(toRemove);
        assertEquals(toRemove, leaderWarehouse.getShelf(3).getResources());
        leaderWarehouse.removeResources(toRemove);
        assertTrue(leaderWarehouse.getShelf(3).isEmpty());
    }

    /**
     * this test create a leader warehouse full of resources, it remove all of them from it
     * and verify if the warehouse is empty
     */
    @Test
    void RemoveResources6(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        leaderWarehouse.addShelf(new Shield());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        coins.add(new Coin());
        coins.add(new Coin());
        coins.add(new Coin());
        shields.add(new Shield());
        shields.add(new Shield());
        servants.add(new Servant());
        leaderWarehouse.addResources(servants, 1);
        leaderWarehouse.addResources(shields, 2);
        leaderWarehouse.addResources(coins, 3);
        leaderWarehouse.addResources(coins, 4);
        leaderWarehouse.addResources(shields, 5);
        CollectionResources toRemove = new CollectionResources();
        toRemove.sum(servants);
        toRemove.sum(coins);
        toRemove.sum(coins);
        toRemove.remove(new Coin());
        toRemove.sum(shields);
        toRemove.sum(shields);
        leaderWarehouse.removeResources(toRemove);
        assertEquals(0, leaderWarehouse.getNumberOfResources());
        assertEquals(leaderWarehouse.getTotalResources().getSize(), 0);
    }

    /**
     * this test create a not empty warehouse with resources added
     * randomly, it remove not all the resources from it randomly,
     * and then verify the correct state
     */
    @Test
    void RemoveResources7(){
        leaderWarehouse = new LeaderWarehouse(new Stone(), new Warehouse());
        leaderWarehouse.addShelf(new Servant());
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        stones.add(new Stone());
        servants.add(new Servant());
        servants.add(new Servant());
        shields.add(new Shield());
        shields.add(new Shield());
        leaderWarehouse.addResources(stones, 1);
        leaderWarehouse.addResources(stones, 4);
        leaderWarehouse.addResources(servants, 5);
        leaderWarehouse.addResources(shields, 3);
        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Shield());
        toRemove.add(new Stone());
        toRemove.sum(servants);
        leaderWarehouse.removeResources(toRemove);
        shields.remove(new Shield());
        assertTrue(leaderWarehouse.getShelf(4).isEmpty());
        assertEquals(stones, leaderWarehouse.getShelf(1).getResources());
        assertTrue(leaderWarehouse.getShelf(5).isEmpty());
        assertEquals(shields, leaderWarehouse.getShelf(3).getResources());
    }

    /**
     * this test verify that the method getTotalResources
     * in a leader warehouse empty return
     * an empty collectionResources
     */
    @Test
    void testGetTotalResources1(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        leaderWarehouse.addShelf(new Servant());
        assertEquals(0 , leaderWarehouse.getTotalResources().getSize());
    }

    /**
     * this test verify that the method getTotalResources get
     * all the resources correctly when only the leader shelves
     * contain resources
     */
    @Test
    void testGetTotalResources2(){
        leaderWarehouse = new LeaderWarehouse(new Stone(), new Warehouse());
        leaderWarehouse.addShelf(new Shield());
        CollectionResources stone = new ShelfCollection(ResourceType.STONE);
        CollectionResources shield = new ShelfCollection(ResourceType.SHIELD);
        stone.add(new Stone());
        shield.add(new Shield());
        leaderWarehouse.addResources(stone, 4);
        leaderWarehouse.addResources(shield, 5);
        CollectionResources toVerify = new CollectionResources();
        toVerify.add(new Stone());
        toVerify.add(new Shield());
        assertEquals(toVerify, leaderWarehouse.getTotalResources());
    }

    /**
     * this test verify that the method getTotalResources get correctly
     * all the resources when contained in normal shelves and leader shelves both
     */
    @Test
    void testGetTotalResources3(){
        leaderWarehouse = new LeaderWarehouse(new Stone(), new Warehouse());
        leaderWarehouse.addShelf(new Shield());
        CollectionResources stone = new ShelfCollection(ResourceType.STONE);
        CollectionResources shield = new ShelfCollection(ResourceType.SHIELD);
        stone.add(new Stone());
        shield.add(new Shield());
        leaderWarehouse.addResources(stone, 4);
        leaderWarehouse.addResources(shield, 5);
        leaderWarehouse.addResources(shield, 2);
        CollectionResources toVerify = new CollectionResources();
        toVerify.add(new Stone());
        toVerify.add(new Shield());
        toVerify.add(new Shield());
        assertEquals(toVerify, leaderWarehouse.getTotalResources());
    }

    /**
     * this test verify that the method getTotalResources
     * in a leader warehouse empty return 0
     */
    @Test
    void testGetNumberOfResources1(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        leaderWarehouse.addShelf(new Servant());
        assertEquals(0 , leaderWarehouse.getNumberOfResources());
    }

    /**
     * this test verify that the method getNumberOfResources get
     * the number of resources contained correctly when only the leader shelves
     * contain resources
     */
    @Test
    void testGetNumberOfResources2(){
        leaderWarehouse = new LeaderWarehouse(new Stone(), new Warehouse());
        leaderWarehouse.addShelf(new Shield());
        CollectionResources stone = new ShelfCollection(ResourceType.STONE);
        CollectionResources shield = new ShelfCollection(ResourceType.SHIELD);
        stone.add(new Stone());
        shield.add(new Shield());
        leaderWarehouse.addResources(stone, 4);
        leaderWarehouse.addResources(shield, 5);
        assertEquals(2, leaderWarehouse.getNumberOfResources());
    }

    /**
     * this test verify that the method getNumberOfResources get
     * the number of resources contained correctly when the leader shelves
     * and the normal shelves contain resources
     */
    @Test
    void testGetNumberOfResources3(){
        leaderWarehouse = new LeaderWarehouse(new Stone(), new Warehouse());
        leaderWarehouse.addShelf(new Shield());
        CollectionResources stone = new ShelfCollection(ResourceType.STONE);
        CollectionResources shield = new ShelfCollection(ResourceType.SHIELD);
        stone.add(new Stone());
        shield.add(new Shield());
        leaderWarehouse.addResources(stone, 4);
        leaderWarehouse.addResources(shield, 5);
        leaderWarehouse.addResources(shield, 2);
        assertEquals(3, leaderWarehouse.getNumberOfResources());
    }

    /**
     * this test verify that the getter of resources and number of resources
     * work correctly when the first leader shelf is empty
     * but not the second one
     */
    @Test
    void testGetResources(){
        leaderWarehouse = new LeaderWarehouse(new Stone(), new Warehouse());
        leaderWarehouse.addShelf(new Shield());
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        shields.add(new Shield());
        shields.add(new Shield());
        leaderWarehouse.addResources(shields, 5);
        assertEquals(2, leaderWarehouse.getNumberOfResources());
        assertEquals(shields, leaderWarehouse.getTotalResources());
    }

    /**
     * this test verify that shifting 2 empty shelves doesn't
     * change the internal state
     */
    @Test
    void testShiftResources1(){
        leaderWarehouse = new LeaderWarehouse(new Servant(), new Warehouse()); // create the 4° shelf
        leaderWarehouse.addShelf(new Coin()); // create the 5° shelf
        leaderWarehouse.shiftResources(4, 5); // shift two empty shelves
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin()); // add 2 coins
        leaderWarehouse.addResources(coins, 2); // 2° shelf: [coin, coin]
        assertTrue(leaderWarehouse.getShelf(4).isEmpty());
        assertTrue(leaderWarehouse.getShelf(5).isEmpty());
        assertEquals(coins, leaderWarehouse.getTotalResources()); // warehouse contains only 2 coins
    }

    /**
     * this test shift two leader shelves not empty and verify that
     * return -1
     */
    @Test
    void testShiftResources2(){
        leaderWarehouse = new LeaderWarehouse(new Servant(), new Warehouse());
        leaderWarehouse.addShelf(new Coin());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        coins.add(new Coin());
        coins.add(new Coin()); //add 2 coins
        servants.add(new Servant()); //adds 1 servant
        leaderWarehouse.addResources(coins, 5); // 5° shelf: [coin,coin]
        leaderWarehouse.addResources(servants, 4); // 4° shelf: [servant,EMPTY]
        assertEquals(-1, leaderWarehouse.shiftResources(4, 5));
    }

    /**
     * this test verify that shifting two empty shelves,
     * one leader and one normal, does not change the internal state of the
     * warehouse
     */
    @Test
    void testShiftResources3(){
        leaderWarehouse = new LeaderWarehouse(new Servant(), new Warehouse());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin()); // add 2 coins
        leaderWarehouse.addResources(coins, 3); // 3° shelf: [coin,coin,coin]
        assertEquals(-1, leaderWarehouse.shiftResources(4, 2));
        assertEquals(coins, leaderWarehouse.getTotalResources()); // warehouse contains only 2 coins
    }

    /**
     * this test shift one empty leader shelf with a non empty shelf,
     * when the 2 shelves have incompatible types,
     * and verify the all the resources shifted got discarded
     */
    @Test
    void testShiftResources4(){
        leaderWarehouse = new LeaderWarehouse(new Servant(), new Warehouse());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin()); // add 2 coins
        leaderWarehouse.addResources(coins, 3); // 3° shelf: [coin,coin,EMPTY]
        assertEquals(-1, leaderWarehouse.shiftResources(4, 3));
        assertEquals(coins.getSize(), leaderWarehouse.getNumberOfResources()); // warehouse contains only 2 coins
        assertEquals(-1, leaderWarehouse.shiftResources(3, 4));
        assertEquals(coins.getSize(), leaderWarehouse.getNumberOfResources()); // warehouse contains only 2 coins
    }

    /**
     * this test verify that shifting a leader shelf with itself
     * does not change the state
     */
    @Test
    void testShiftResources5(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin()); // add 2 coins
        leaderWarehouse.addResources(coins, 4); // 4° shelf: [coin,coin]
        assertEquals(0 , leaderWarehouse.shiftResources(4, 4));
        assertEquals(coins, leaderWarehouse.getTotalResources()); // warehouse contains only 2 coins
    }

    /**
     * this test shift a resource from a full leader shelf to a normal empty shelf
     * with capacity > 1,when the resources in the leader shelf are not contained in
     * the normal shelves, and verify that the resource get shifted correctly
     */
    @Test
    void testShiftResources6(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin()); // add 2 coins
        leaderWarehouse.addResources(coins, 4); // 4° shelf: [coin,coin]
        assertEquals(0, leaderWarehouse.shiftResources(4,3));
        coins.remove(new Coin()); // contains 1 coin
        assertEquals(coins, leaderWarehouse.getShelf(3).getResources()); // 3° shelf: [coin,EMPTY,EMPTY]
        assertEquals(coins, leaderWarehouse.getShelf(4).getResources()); // 4° shelf: [coin,EMPTY]
        assertEquals(0, leaderWarehouse.shiftResources(3,4));
        coins.add(new Coin()); // contains 2 coins
        assertTrue( leaderWarehouse.getShelf(3).isEmpty()); // 3° shelf: [EMPTY,EMPTY,EMPTY]
        assertEquals(coins, leaderWarehouse.getShelf(4).getResources()); // 4° shelf: [coin,coin]
    }

    /**
     * this test shift a resource from a full leader shelf to a normal empty shelf
     * with capacity = 1,when the resources in the leader shelf are not contained in
     * the normal shelves, and verify that the resource get shifted correctly
     */
    @Test
    void testShiftResources7(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin()); // contains 2 coins
        leaderWarehouse.addResources(coins, 4); // 4° shelf: [coin,coin]
        assertEquals(0, leaderWarehouse.shiftResources(4,1));
        coins.remove(new Coin()); // contain 1 coin
        assertEquals(coins, leaderWarehouse.getShelf(1).getResources()); // 4° shelf: [coin,EMPTY]
        assertEquals(coins, leaderWarehouse.getShelf(4).getResources()); // 4° shelf: [coin,EMPTY]
    }

    /**
     * this test shift a resource from a full leader shelf to a not empty
     * normal shelf with capacity 1, when the resources in the leader shelf are
     * not contained in the normal shelves, and verify that return -1,
     * without changing the state of the warehouse
     */
    @Test
    void testShiftResources8(){
        leaderWarehouse = new LeaderWarehouse(new Stone(), new Warehouse());
        leaderWarehouse.addShelf(new Coin());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin()); //contains 2 coins
        CollectionResources shield = new ShelfCollection(ResourceType.SHIELD);
        shield.add(new Shield()); // contains 1 shield
        leaderWarehouse.addResources(coins, 5);  // 5° shelf: [coin,coin]
        leaderWarehouse.addResources(shield, 1); // 1° shelf: [shield]
        assertEquals(-1, leaderWarehouse.shiftResources(5,1));
        assertEquals(-1, leaderWarehouse.shiftResources(1,5));
        assertEquals(coins, leaderWarehouse.getShelf(5).getResources()); //5° shelf has 2 coins
        assertEquals(shield, leaderWarehouse.getShelf(1).getResources()); //1° shelf has 1 shield
    }

    /**
     * this test shift a resource from a leader shelf not empty to a normal shelf empty,
     * when the resources in the leader shelf are contained in the normal shelves
     * but not in the one selected, and verify that the method return -1
     * without changing the state of the warehouse
     */
    @Test
    void testShiftResources9(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin()); // contains 1 coin
        leaderWarehouse.addResources(coins, 4); // 4° shelf: [coin,EMPTY]
        leaderWarehouse.addResources(coins, 2); // 2° shelf: [coin,EMPTY]
        assertEquals(-1, leaderWarehouse.shiftResources(4, 3));
        assertEquals(coins, leaderWarehouse.getShelf(4).getResources()); // 4° shelf: [coin,EMPTY]
        assertEquals(coins, leaderWarehouse.getShelf(2).getResources()); // 2° shelf: [coin,EMPTY]
    }

    /**
     * this test shift a resource from a leader shelf not empty to a normal shelf not empty,
     * when the resources in the leader shelf are contained in the normal shelves
     * but not in the one selected, and verify that the method return -1
     * without changing the state of the warehouse
     */
    @Test
    void testShiftResources10(){
        leaderWarehouse = new LeaderWarehouse(new Coin(), new Warehouse());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin()); // contains 2 coins
        leaderWarehouse.addResources(coins, 4); // 4° shelf: [coin,coin]
        leaderWarehouse.addResources(coins, 2); // 2° shelf: [coin,coin]
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        stones.add(new Stone());
        stones.add(new Stone());
        stones.add(new Stone()); // contains 3 stones
        leaderWarehouse.addResources(stones, 3); // 3° shelf: [stone, stone, stone]
        assertEquals(-1, leaderWarehouse.shiftResources(4, 3));
        assertEquals(-1, leaderWarehouse.shiftResources(3, 4));
        assertEquals(stones, leaderWarehouse.getShelf(3).getResources()); // 3° shelf: [stone, stone, stone]
        assertEquals(coins, leaderWarehouse.getShelf(2).getResources()); // 2° shelf: [coin,coin]
        assertEquals(coins, leaderWarehouse.getShelf(4).getResources()); // 4° shelf: [coin,coin]
    }

    /**
     * this test shift a resource from a normal shelf not empty to a leader shelf empty with
     * capacity 2 then roll-back, when the resources in the leader shelf are contained
     * in the normal shelf selected, and verify that shift the resource cyclical
     */
    @Test
    void testShiftResources11(){
        leaderWarehouse = new LeaderWarehouse(new Stone(), new Warehouse());
        leaderWarehouse.addShelf(new Coin());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin()); // contain 1 coin
        leaderWarehouse.addResources(coins, 2); // 2° shelf: [coin,EMPTY]
        assertEquals(0, leaderWarehouse.shiftResources(2, 5));
        assertEquals(coins, leaderWarehouse.getShelf(5).getResources()); // 5° shelf: [coin,EMPTY]
        assertTrue(leaderWarehouse.getShelf(2).isEmpty()); // 2° shelf: [EMPTY,EMPTY]
        assertEquals(0, leaderWarehouse.shiftResources(5, 2));
        assertEquals(coins, leaderWarehouse.getShelf(2).getResources()); // 2° shelf: [coin,EMPTY]
        assertTrue(leaderWarehouse.getShelf(5).isEmpty()); // 5° shelf: [EMPTY,EMPTY]

        leaderWarehouse.addResources(coins, 2); // 2° shelf: [coin,coin]
        assertEquals(0, leaderWarehouse.shiftResources(2, 5));
        assertEquals(coins, leaderWarehouse.getShelf(2).getResources()); // 2° shelf: [coin,EMPTY]
        assertEquals(coins, leaderWarehouse.getShelf(5).getResources()); // 5° shelf: [coin,EMPTY]
        assertEquals(0, leaderWarehouse.shiftResources(5, 2));
        coins.add(new Coin()); // contains 2 coins
        assertEquals(coins, leaderWarehouse.getShelf(2).getResources()); // 2° shelf: [coin,coin]
        assertTrue(leaderWarehouse.getShelf(5).isEmpty()); // 5° shelf: [EMPTY,EMPTY]
    }

    /**
     * this test shift a resource from a leader shelf not empty to a normal shelf not empty with
     * capacity 1, when the resources in the leader shelf are contained
     * in the normal shelf selected, and verify that the method return -1,
     * without changing the warehouse state
     */
    @Test
    void testShiftResources12(){
        leaderWarehouse = new LeaderWarehouse(new Shield(), new Warehouse());
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        shields.add(new Shield());
        shields.add(new Shield()); // contains 2 shields
        leaderWarehouse.addResources(shields, 4); // 4° shelf: [shield,shield]
        leaderWarehouse.addResources(shields, 1); // 1° shelf: [shield]
        assertEquals(-1, leaderWarehouse.shiftResources(1, 4));
        assertEquals(shields, leaderWarehouse.getShelf(4).getResources()); // 4° shelf: [shield,shield]
        shields.remove(new Shield());
        assertEquals(shields, leaderWarehouse.getShelf(1).getResources()); // 1° shelf: [shield]
    }

    /**
     * this test shift a resource from a leader shelf not empty to a normal shelf not empty with
     * capacity 3, when the resources in the leader shelf are contained
     * in the normal shelf selected, and verify that the method return -1,
     * without changing the warehouse state
     */
    @Test
    void testShiftResources13(){
        leaderWarehouse = new LeaderWarehouse(new Shield(), new Warehouse());
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        shields.add(new Shield());
        shields.add(new Shield());
        shields.add(new Shield()); // contains 3 shields
        leaderWarehouse.addResources(shields, 4); // 4° shelf: [shield,shield]
        leaderWarehouse.addResources(shields, 3); // 3° shelf: [shield,shield,shield]
        assertEquals(-1, leaderWarehouse.shiftResources(3, 4));
        assertEquals(-1, leaderWarehouse.shiftResources(4, 3));
        assertEquals(shields, leaderWarehouse.getShelf(3).getResources()); // 3° shelf: [shield,shield,shield]
        shields.remove(new Shield()); // contains 2 shields
        assertEquals(shields, leaderWarehouse.getShelf(4).getResources()); // 4° shelf: [shield,shield]
    }

    /**
     * this method verify that the method getNumOfShelves
     * return 4 when create only one leader shelf
     * return 5 when create the second leader shelf
     */
    @Test
    void getNumOfShelves(){
        leaderWarehouse = new LeaderWarehouse(new Shield(), new Warehouse());
        assertEquals(4, leaderWarehouse.getNumOfShelves());
        leaderWarehouse.addShelf(new Coin());
        assertEquals(5, leaderWarehouse.getNumOfShelves());
    }

}