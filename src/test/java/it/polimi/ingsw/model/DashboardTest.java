package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the dashboard
 */
class DashboardTest {

    Dashboard dashboard;

    /**
     * this test verify that the method placeCard return
     * true if and only if the position in input is
     * between 1 to 3
     */
    @Test
    void testPlaceCard(){
        dashboard = new Dashboard();
        DevelopmentCard card = new DevelopmentCard(null,1,3,null,null,null,0);
        assertFalse(dashboard.placeDevelopmentCard(card,-1));
        assertFalse(dashboard.placeDevelopmentCard(card,0));
        assertTrue(dashboard.placeDevelopmentCard(card,1));
        assertTrue(dashboard.placeDevelopmentCard(card,3));
        assertFalse(dashboard.placeDevelopmentCard(card,4));
        assertFalse(dashboard.placeDevelopmentCard(card,5));
    }

    /**
     * this test verify that is possible to remove from the strongbox
     * an empty collection of resources and does not change the state
     */
    @Test
    void testRemoveFromStrongbox1(){
        dashboard = new Dashboard();
        CollectionResources resources = new CollectionResources();
        resources.add(new Servant()); resources.add(new Coin());
        assertTrue(dashboard.removeFromStrongbox(new CollectionResources()));
        assertTrue(dashboard.getPersonalStrongbox().isEmpty());
        dashboard.addToBuffer(resources);
        dashboard.fillStrongboxWithBuffer();
        dashboard.removeFromStrongbox(new CollectionResources());
        assertEquals(resources, dashboard.getPersonalStrongbox().getStrongboxResources());
    }

    /**
     * this test create a not empty strongbox and remove some resources from it,
     * not all, after that verify the correct state
     */
    @Test
    void testRemoveFromStrongbox2(){
        dashboard = new Dashboard();
        CollectionResources toAdd = new CollectionResources();
        CollectionResources toRemove = new CollectionResources();
        toAdd.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Servant(), new Shield(), new Coin(), new Servant()))));
        toRemove.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Servant()))));
        dashboard.addToBuffer(toAdd);
        dashboard.fillStrongboxWithBuffer();
        assertTrue( dashboard.removeFromStrongbox(toRemove));
        toAdd.sub(toRemove);
        assertEquals(toAdd, dashboard.getPersonalStrongbox().getStrongboxResources());
    }

    /**
     * this test create a not empty strongbox, then it removes
     * all the resources from it, and verify that the strongbox
     * is empty
     */
    @Test
    void testRemoveFromStrongbox3(){
        dashboard = new Dashboard();
        CollectionResources resources = new CollectionResources();
        resources.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Servant(), new Shield(), new Coin(), new Servant()))));
        dashboard.addToBuffer(resources);
        dashboard.fillStrongboxWithBuffer();
        assertTrue( dashboard.removeFromStrongbox(resources));
        assertTrue( dashboard.getPersonalStrongbox().isEmpty());
    }

    /**
     * this method create a not empty strongbox and try to remove resources
     * not contained from it, but it return false
     * the strongbox does not contain any resource in common
     * with the resources to remove
     */
    @Test
    void testRemoveFromStrongbox4(){
        dashboard = new Dashboard();
        CollectionResources toAdd = new CollectionResources();
        CollectionResources toRemove = new CollectionResources();
        toAdd.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Servant(), new Coin(), new Servant()))));
        toRemove.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(), new Shield()))));
        dashboard.addToBuffer(toAdd);
        dashboard.fillStrongboxWithBuffer();
        assertFalse( dashboard.removeFromStrongbox(toRemove));
        assertEquals(toAdd, dashboard.getPersonalStrongbox().getStrongboxResources());
    }

    /**
     * this method create a not empty strongbox and try to remove resources
     * not contained from it, but it return false
     * the strongbox does contain some resource in common, but not all
     * with the resources to remove
     */
    @Test
    void testRemoveFromStrongbox5(){
        dashboard = new Dashboard();
        CollectionResources toAdd = new CollectionResources();
        CollectionResources toRemove = new CollectionResources();
        toAdd.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Servant(), new Coin(), new Servant()))));
        toRemove.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(), new Shield(), new Coin(), new Coin()))));
        dashboard.addToBuffer(toAdd);
        dashboard.fillStrongboxWithBuffer();
        assertFalse( dashboard.removeFromStrongbox(toRemove));
        assertEquals(toAdd, dashboard.getPersonalStrongbox().getStrongboxResources());
    }

    /**
     * this test verify that is possible to remove from the strongbox
     * an empty collection of resources and does not change the state
     */
    @Test
    void testRemoveFromWarehouse1(){
        dashboard = new Dashboard();
        assertTrue(dashboard.removeFromWarehouse(new CollectionResources()));
        assertEquals(0 , dashboard.getPersonalWarehouse().getNumberOfResources());
        CollectionResources servants = new CollectionResources();
        servants.add(new Servant()); servants.add(new Servant());
        dashboard.addResourcesToWarehouse(servants, 3);
        dashboard.removeFromWarehouse(new CollectionResources());
        assertEquals(0 , dashboard.getPersonalWarehouse().getNumberOfResources());
    }

    /**
     * this test create a not empty warehouse and remove some resources from it,
     * not all, after that verify the correct state
     */
    @Test
    void testRemoveFromWarehouse2(){
        dashboard = new Dashboard();
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        coins.add(new Coin()); coins.add(new Coin());
        servants.add(new Servant()); servants.add(new Servant());
        shields.add(new Shield());
        dashboard.addResourcesToWarehouse(shields, 1);
        dashboard.addResourcesToWarehouse(coins, 2);
        dashboard.addResourcesToWarehouse(servants, 3);
        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Coin()); toRemove.add(new Servant());
        assertTrue( dashboard.removeFromWarehouse(toRemove));
        toRemove.add(new Shield());
        assertEquals(toRemove, dashboard.getPersonalWarehouse().getTotalResources());
    }

    /**
     * this test create a not empty warehouse, then it removes
     * all the resources from it, and verify that the warehouse
     * is empty
     */
    @Test
    void testRemoveFromWarehouse3(){
        dashboard = new Dashboard();
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        coins.add(new Coin()); coins.add(new Coin());
        servants.add(new Servant()); servants.add(new Servant());
        shields.add(new Shield());
        dashboard.addResourcesToWarehouse(shields, 1);
        dashboard.addResourcesToWarehouse(coins, 2);
        dashboard.addResourcesToWarehouse(servants, 3);
        CollectionResources toRemove = new CollectionResources();
        toRemove.sum(coins);
        toRemove.sum(servants);
        toRemove.sum(shields);
        assertTrue(dashboard.removeFromWarehouse(toRemove));
        assertEquals(0, dashboard.getPersonalWarehouse().getNumberOfResources());
    }

    /**
     * this method create a not empty warehouse and try to remove resources
     * not contained from it, but it return false
     * the warehouse does not contain any resource in common
     * with the resources to remove
     */
    @Test
    void testRemoveFromWarehouse4(){
        dashboard = new Dashboard();
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources toVerify = new CollectionResources();
        coins.add(new Coin()); coins.add(new Coin());
        servants.add(new Servant()); servants.add(new Servant());
        dashboard.addResourcesToWarehouse(coins, 2);
        dashboard.addResourcesToWarehouse(servants, 3);
        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Shield()); toRemove.add(new Stone());
        assertFalse(dashboard.removeFromWarehouse(toRemove));
        toVerify.sum(coins); toVerify.sum(servants);
        assertEquals(toVerify, dashboard.getPersonalWarehouse().getTotalResources());
    }

    /**
     * this method create a not empty warehouse and try to remove resources
     * not contained from it, but it return false
     * the warehouse does contain some resource in common, but not all
     * with the resources to remove
     */
    @Test
    void testRemoveFromWarehouse5(){
        dashboard = new Dashboard();
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources toVerify = new CollectionResources();
        coins.add(new Coin()); coins.add(new Coin());
        servants.add(new Servant()); servants.add(new Servant());
        dashboard.addResourcesToWarehouse(coins, 2);
        dashboard.addResourcesToWarehouse(servants, 3);
        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Shield()); toRemove.add(new Servant()); toRemove.add(new Coin());
        assertFalse(dashboard.removeFromWarehouse(toRemove));
        toVerify.sum(coins); toVerify.sum(servants);
        assertEquals(toVerify, dashboard.getPersonalWarehouse().getTotalResources());
    }

    /**
     * this test verify the correct creation of leader productions
     * when the decks of cards are all empty
     */
    @Test
    void testActivateLeaderProduction1(){
        dashboard = new Dashboard();
        dashboard.activateLeaderProduction(new Coin());
        assertEquals(0, dashboard.getPersonalProductionPower().getVictoryPoints());
        assertEquals(new Coin(), dashboard.getPersonalProductionPower().getInput(1));
        dashboard.activateLeaderProduction(new Servant());
        assertEquals(0, dashboard.getPersonalProductionPower().getVictoryPoints());
        assertEquals(new Servant(), dashboard.getPersonalProductionPower().getInput(2));
    }

    /**
     * this test verify the correct creation of leader productions
     * when the decks of cards are not all empty
     */
    @Test
    void testActivateLeaderProduction2(){
        dashboard = new Dashboard();
        DevelopmentCard card1 = new DevelopmentCard(null,1,3,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(null,1,6,null,null,null,0);
        dashboard.placeDevelopmentCard(card1, 1);
        dashboard.activateLeaderProduction(new Stone());
        assertEquals(new Stone(), dashboard.getPersonalProductionPower().getInput(1));
        assertEquals(card1, dashboard.getPersonalProductionPower().getCard(1));
        assertEquals(3, dashboard.getPersonalProductionPower().getVictoryPoints());
        dashboard.placeDevelopmentCard(card2, 3);
        dashboard.activateLeaderProduction(new Shield());
        assertEquals(new Shield(), dashboard.getPersonalProductionPower().getInput(2));
        assertEquals(card2, dashboard.getPersonalProductionPower().getCard(3));
        assertEquals(9, dashboard.getPersonalProductionPower().getVictoryPoints());
    }

    /**
     * this test verify the correct creation of leader shelfs
     * on warehouse when it is empty
     */
    @Test
    void testActivateLeaderWarehouse1(){
        dashboard = new Dashboard();
        dashboard.activateLeaderWarehouse(new Shield());
        assertEquals(ResourceType.SHIELD, dashboard.getPersonalWarehouse().getShelf(4).getResourceType());
        assertEquals(new CollectionResources(), dashboard.getPersonalWarehouse().getTotalResources());
        dashboard.activateLeaderWarehouse(new Coin());
        assertEquals(ResourceType.COIN, dashboard.getPersonalWarehouse().getShelf(5).getResourceType());
        assertEquals(new CollectionResources(), dashboard.getPersonalWarehouse().getTotalResources());
    }

    /**
     * this test verify the correct creation of leader shelfs
     * on warehouse when the warehouse isn't empty
     */
    @Test
    void testActivateLeaderWarehouse2(){
        dashboard = new Dashboard();
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        CollectionResources toVerify = new CollectionResources();
        servants.add(new Servant());
        stones.add(new Stone()); stones.add(new Stone());
        dashboard.addResourcesToWarehouse(servants, 1);
        dashboard.activateLeaderWarehouse(new Coin());
        toVerify.sum(servants);
        assertEquals(ResourceType.COIN, dashboard.getPersonalWarehouse().getShelf(4).getResourceType());
        assertEquals(toVerify, dashboard.getPersonalWarehouse().getTotalResources());
        dashboard.addResourcesToWarehouse(stones, 2);
        dashboard.activateLeaderWarehouse(new Stone());
        toVerify.sum(stones);
        assertEquals(ResourceType.STONE, dashboard.getPersonalWarehouse().getShelf(5).getResourceType());
        assertEquals(toVerify, dashboard.getPersonalWarehouse().getTotalResources());
    }

    /**
     * this test verify the method getTotalResources when the warehouse
     * and the strongbox are both empty
     */
    @Test
    void testGetTotalResources1(){
        dashboard = new Dashboard();
        assertEquals(new CollectionResources(), dashboard.getTotalResources());
    }

    /**
     * this test verify the method getTotalResources when the
     * warehouse isn't empty and the strongbox is
     */
    @Test
    void testGetTotalResources2(){
        dashboard = new Dashboard();
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        CollectionResources toVerify = new CollectionResources();
        servants.add(new Servant());
        stones.add(new Stone()); stones.add(new Stone());
        toVerify.sum(servants); toVerify.sum(stones);
        dashboard.addResourcesToWarehouse(servants, 1);
        dashboard.addResourcesToWarehouse(stones, 2);
        assertEquals(toVerify, dashboard.getTotalResources());
    }

    /**
     * this test verify the method getTotalResources when the
     * warehouse is empty and the strongbox isn't
     */
    @Test
    void testGetTotalResources3(){
        dashboard = new Dashboard();
        CollectionResources toAdd = new CollectionResources();
        toAdd.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Servant(), new Coin(), new Shield()))));
        dashboard.addToBuffer(toAdd);
        dashboard.fillStrongboxWithBuffer();
        assertEquals(toAdd, dashboard.getTotalResources());
    }

    /**
     * this test verify the method getTotalResources when
     * the warehouse and the strongbox aren't empty
     */
    @Test
    void testGetTotalResources4(){
        dashboard = new Dashboard();
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        CollectionResources toVerify = new CollectionResources();
        servants.add(new Servant());
        stones.add(new Stone()); stones.add(new Stone());
        toVerify.sum(servants); toVerify.sum(stones);
        dashboard.addResourcesToWarehouse(servants, 1);
        dashboard.addResourcesToWarehouse(stones, 2);
        CollectionResources toAdd = new CollectionResources();
        toAdd.sum(new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Servant(), new Coin(), new Shield()))));
        dashboard.addToBuffer(toAdd);
        dashboard.fillStrongboxWithBuffer();
        toVerify.sum(toAdd);
        assertEquals(toVerify, dashboard.getTotalResources());
    }

    /**
     * this test verify that an empty dashboard return 0 victory points
     */
    @Test
    void testGetVictoryPoints1(){
        dashboard = new Dashboard();
        assertEquals(0, dashboard.getVictoryPoints());
    }

    /**
     * this test verify that the getter of victory points
     * get them correctly when there are 0 resources stored
     * and some cards with some victory points
     */
    @Test
    void testGetVictoryPoints2(){
        dashboard = new Dashboard();
        DevelopmentCard card1 = new DevelopmentCard(null,1,3,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(null,2,2,null,null,null,0);
        DevelopmentCard card3 = new DevelopmentCard(null,1,5,null,null,null,0);
        dashboard.placeDevelopmentCard(card1, 1);
        dashboard.placeDevelopmentCard(card2, 1);
        dashboard.placeDevelopmentCard(card3, 3);
        assertEquals(10, dashboard.getVictoryPoints());
    }

    /**
     * this test verify that adding a number between 0 and 4 resources
     * the method getVictoryPoints always return 0, and after adding
     * the fifth resource, the method return 1
     */
    @Test
    void testGetVictoryPoints3(){
        dashboard = new Dashboard();
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        stones.add(new Stone());
        shields.add(new Shield());
        CollectionResources resources = new CollectionResources();
        resources.add(new Coin());
        dashboard.addResourcesToWarehouse(stones, 1);
        assertEquals(0, dashboard.getVictoryPoints());
        dashboard.activateLeaderWarehouse(new Shield());
        dashboard.addResourcesToWarehouse(shields, 4);
        assertEquals(0, dashboard.getVictoryPoints());
        dashboard.addToBuffer(resources);
        dashboard.fillStrongboxWithBuffer();
        assertEquals(0, dashboard.getVictoryPoints());
        dashboard.addToBuffer(resources);
        dashboard.fillStrongboxWithBuffer();
        assertEquals(0, dashboard.getVictoryPoints());
        dashboard.addToBuffer(stones);
        dashboard.fillStrongboxWithBuffer();
        assertEquals(1, dashboard.getVictoryPoints());
    }

    /**
     * this test create a dashboard with some cards and some resources
     * and verify the correct return of the method getVictoryPoints
     */
    @Test
    void testGetVictoryPoints4(){
        dashboard = new Dashboard();
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        stones.add(new Stone()); stones.add(new Stone()); stones.add(new Stone()); // adding 3 stones
        shields.add(new Shield()); shields.add(new Shield()); shields.add(new Shield()); // adding 3 shields
        coins.add(new Coin()); coins.add(new Coin()); coins.add(new Coin()); // adding 3 coins
        CollectionResources resources = new CollectionResources();
        resources.sum(stones); resources.sum(coins); resources.sum(shields); // "resources" contains 9 resources
        dashboard.activateLeaderWarehouse(new Stone());
        dashboard.activateLeaderWarehouse(new Coin());
        dashboard.addResourcesToWarehouse(stones, 1); // 1 resource
        dashboard.addResourcesToWarehouse(coins, 2); // 2 resources
        dashboard.addResourcesToWarehouse(shields, 3); // 3 resources
        dashboard.addResourcesToWarehouse(stones, 4); // 2 resources
        dashboard.addResourcesToWarehouse(coins, 5); // 2 resources
        dashboard.addToBuffer(resources);
        dashboard.fillStrongboxWithBuffer();
        DevelopmentCard card1 = new DevelopmentCard(null,1,3,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(null,2,2,null,null,null,0);
        DevelopmentCard card3 = new DevelopmentCard(null,1,5,null,null,null,0);
        dashboard.placeDevelopmentCard(card1, 1);
        dashboard.placeDevelopmentCard(card2, 1);
        dashboard.placeDevelopmentCard(card3, 3); // 3 + 5 + 2 + LOWER[(9 + 1 + 2 + 3 + 2 + 2) / 5] =
        assertEquals(13, dashboard.getVictoryPoints());
    }
}