package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PlayerAndComponents.Dashboard;
import it.polimi.ingsw.model.PlayerAndComponents.DiscountDashboard;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the discount dashboard
 */
class DiscountDashboardTest {

    /**
     * this attribute represent the discounted dashboard to test
     */
    private Dashboard discounted;

    /**
     * this test verify that a discounted dashboard got created
     * correctly when the initial dashboard is empty
     */
    @Test
    void testCreation1(){
        discounted = new DiscountDashboard(new Coin(), new Dashboard());
        CollectionResources resources = new CollectionResources();
        resources.add(new Coin());
        assertEquals(resources, discounted.getDiscount());
        discounted.addDiscount(new Stone());
        resources.add(new Stone());
        assertEquals(resources, discounted.getDiscount());
    }

    /**
     * this test verify that a discounted dashboard got created
     * correctly when the initial dashboard is not empty
     */
    @Test
    void testCreation2(){
        discounted = new Dashboard();
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources resources = new CollectionResources();
        CollectionResources discountedResources = new CollectionResources();
        coins.add(new Coin());
        servants.add(new Servant());
        servants.add(new Servant());
        resources.sum(coins); resources.sum(servants);
        discountedResources.add(new Shield());
        discounted.addResourcesToWarehouse(servants, 2);
        discounted.addResourcesToWarehouse(coins, 1);
        discounted = new DiscountDashboard(new Shield(), discounted);
        assertEquals(resources, discounted.getTotalResources());
        assertEquals(discountedResources, discounted.getDiscount());
        discounted.addToBuffer(resources);
        discounted.fillStrongboxWithBuffer();
        resources.sum(resources);
        discounted.addDiscount(new Coin());
        discountedResources.add(new Coin());
        assertEquals(discountedResources, discounted.getDiscount());
        assertEquals(resources, discounted.getTotalResources());
    }

    /**
     * this test add two discount resources to a dashboard
     * and then verify if the method getTotalDiscountedResources
     * return them when the dashboard is empty
     */
    @Test
    void testGetTotalDiscountResources1(){
        discounted = new DiscountDashboard(new Servant(), new Dashboard());
        discounted.addDiscount(new Stone());
        CollectionResources resources = new CollectionResources();
        resources.add(new Servant());
        resources.add(new Stone());
        assertEquals(resources, discounted.getTotalDiscountResource());
        assertEquals(new CollectionResources(), discounted.getTotalResources());
    }

    /**
     * this test add two discount resources to a dashboard
     * and then verify if the method getTotalDiscountedResources
     * return them plus the other resources stored
     */
    @Test
    void testGetTotalDiscountResources2(){
        discounted = new DiscountDashboard(new Stone(), new Dashboard());
        discounted.addDiscount(new Shield());
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources servants = new ShelfCollection(ResourceType.SERVANT);
        CollectionResources toAdd = new CollectionResources();
        CollectionResources toVerify = new CollectionResources();
        coins.add(new Coin());
        servants.add(new Servant()); servants.add(new Servant()); servants.add(new Servant());
        toAdd.add(new Stone()); toAdd.add(new Coin()); toAdd.add(new Shield());
        discounted.addResourcesToWarehouse(coins, 1);
        discounted.addResourcesToWarehouse(servants, 3);
        discounted.addToBuffer(toAdd);
        assertTrue(discounted.getPersonalStrongbox().isEmpty());
        discounted.fillStrongboxWithBuffer();
        toVerify.sum(coins); toVerify.sum(servants); toVerify.sum(toAdd);
        toVerify.add(new Stone()); toVerify.add(new Shield());
        assertEquals(toVerify, discounted.getTotalDiscountResource());
    }

}