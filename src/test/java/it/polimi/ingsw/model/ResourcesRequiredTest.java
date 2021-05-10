package it.polimi.ingsw.model;

import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements;
import it.polimi.ingsw.model.LeaderCard.ResourcesRequired;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * this class is used to test ResourceRequired class; we assume that the required resources
 * are always 5
 */
public class ResourcesRequiredTest {

    /**
     * this test create an empty player and check if the containsRequirements method return false
     */
    @Test
    public void TestEmptyPlayerAndZeroResources() {
        LeaderCardRequirements requirements;
        RealPlayer player = new RealPlayer("player", null);
        requirements = new ResourcesRequired(new Coin());
        assertFalse(requirements.containsRequirements(player));
    }

    /**
     * this test create a player that has only 1 resource and check if
     * the containsRequirements method return false
     */
    @Test
    public void TestOnlyOneResource(){
        CollectionResources warehouseResource = new ShelfCollection(ResourceType.COIN);
        warehouseResource.add(new Coin()); warehouseResource.add(new Coin()); warehouseResource.add(new Coin());
        LeaderCardRequirements requirements;
        RealPlayer player = new RealPlayer("player", null);
        requirements = new ResourcesRequired(new Coin());
        player.addResourcesToWarehouse(warehouseResource,1);
        assertFalse(requirements.containsRequirements(player));
    }

    /**
     * this test create a player that has 4 resources and check if
     * the containsRequirements method return false
     */
    @Test
    public void TestFourResourcesToAPlayer (){
        CollectionResources warehouseResource = new ShelfCollection(ResourceType.COIN);
        warehouseResource.add(new Coin());         warehouseResource.add(new Coin());
        CollectionResources resourcesList = new CollectionResources();
        resourcesList.add(new Coin()); resourcesList.add(new Coin());
        LeaderCardRequirements requirements;
        RealPlayer player = new RealPlayer("player", null);
        player.getPersonalDashboard().addToBuffer(resourcesList);
        player.fillStrongboxWithBuffer();
        requirements = new ResourcesRequired(new Coin());
        player.addResourcesToWarehouse(warehouseResource,3);
        assertFalse(requirements.containsRequirements(player));
    }

    /**
     * this test create a player that has 5 resources and check if
     * the containsRequirements method return true
     */
    @Test
    public void TestFiveResourceAndAssertTrue(){
        CollectionResources warehouseResource = new ShelfCollection(ResourceType.COIN);
        warehouseResource.add(new Coin());         warehouseResource.add(new Coin());
        CollectionResources resourcesList = new CollectionResources();
        resourcesList.add(new Coin()); resourcesList.add(new Coin());
        LeaderCardRequirements requirements;
        RealPlayer player = new RealPlayer("player", null);
        player.getPersonalDashboard().addToBuffer(resourcesList);
        player.fillStrongboxWithBuffer();
        player.addLeaderShelf(new Coin());
        requirements = new ResourcesRequired(new Coin());
        player.addResourcesToWarehouse(warehouseResource,3);
        warehouseResource.remove(new Coin());
        player.addResourcesToWarehouse(warehouseResource , 4);
        assertTrue(requirements.containsRequirements(player));
    }

    /**
     * this test create a player that has more than 5 resources and check if
     * the containsRequirements method return true
     */
    @Test
    public void TestFiveResourceAndAssertTrue2() {
        CollectionResources warehouseResource = new ShelfCollection(ResourceType.COIN);
        warehouseResource.add(new Coin());
        warehouseResource.add(new Coin());
        CollectionResources resourcesList = new CollectionResources();
        resourcesList.add(new Coin()); resourcesList.add(new Coin()); resourcesList.add(new Coin()); resourcesList.add(new Coin());
        LeaderCardRequirements requirements;
        RealPlayer player = new RealPlayer("player", null);
        player.getPersonalDashboard().addToBuffer(resourcesList);
        player.fillStrongboxWithBuffer();
        player.addLeaderShelf(new Coin());
        requirements = new ResourcesRequired(new Coin());
        player.addResourcesToWarehouse(warehouseResource, 3);
        warehouseResource.remove(new Coin()); warehouseResource.remove(new Coin()); warehouseResource.remove(new Coin());
        player.addResourcesToWarehouse(warehouseResource, 4);
        assertTrue(requirements.containsRequirements(player));
    }

    /**
     * this test create a player that has more than 5 randomly resources and check if
     * the containsRequirements method return false
     */
    @Test
    public void TestFiveResourceAndAssertFalse1(){
        CollectionResources warehouseResource = new ShelfCollection(ResourceType.COIN);
        warehouseResource.add(new Coin()); warehouseResource.add(new Coin());
        CollectionResources resourcesList = new CollectionResources();
        resourcesList.add(new Shield()); resourcesList.add(new Shield()); resourcesList.add(new Shield()); resourcesList.add(new Shield()); resourcesList.add(new Coin());
        LeaderCardRequirements requirements;
        RealPlayer player = new RealPlayer("player", null);
        player.getPersonalDashboard().addToBuffer(resourcesList);
        player.fillStrongboxWithBuffer();
        player.addLeaderShelf(new Coin());
        requirements = new ResourcesRequired(new Coin());
        player.addResourcesToWarehouse(warehouseResource, 2);
        warehouseResource.remove(new Coin());
        player.addResourcesToWarehouse(warehouseResource, 4);
        assertFalse(requirements.containsRequirements(player));
    }

    /**
     * this test create a player that has more than 5 randomly resources and check if
     * the containsRequirements method return false
     */
    @Test
    public void TestFiveResourceAndAssertFalse2(){
        CollectionResources warehouseResource = new ShelfCollection(ResourceType.COIN);
        CollectionResources warehouseResource2 = new ShelfCollection(ResourceType.SHIELD);
        warehouseResource2.add(new Shield());
        warehouseResource.add(new Coin()); warehouseResource.add(new Coin()); warehouseResource.add(new Coin());
        CollectionResources resourcesList = new CollectionResources();
        resourcesList.add(new Shield()); resourcesList.add(new Shield()); resourcesList.add(new Shield()); resourcesList.add(new Shield()); resourcesList.add(new Coin());
        LeaderCardRequirements requirements;
        RealPlayer player = new RealPlayer("player", null);
        player.getPersonalDashboard().addToBuffer(resourcesList);
        player.fillStrongboxWithBuffer();
        player.addLeaderShelf(new Coin());
        requirements = new ResourcesRequired(new Coin());
        player.addResourcesToWarehouse(warehouseResource, 1);
        warehouseResource.remove(new Coin());
        player.addResourcesToWarehouse(warehouseResource, 4);
        player.addResourcesToWarehouse(warehouseResource2, 2);
        assertFalse(requirements.containsRequirements(player));
    }
}