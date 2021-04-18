package it.polimi.ingsw.model;

import it.polimi.ingsw.model.PlayerAndComponents.LeaderShelf;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test LeaderShelf
 */
class LeaderShelfTest {
    private Shelf leaderShelf;

    /**
     * this test verify if the method getResources always return the only resource that can be stored
     * in the leaderShelf after calling every method that can change the shelf
     */
    @Test
    void testGetResource(){
        leaderShelf = new LeaderShelf(ResourceType.STONE);
        assertEquals(ResourceType.STONE, leaderShelf.getResourceType());
        leaderShelf.addResource(new Stone());
        assertEquals(ResourceType.STONE, leaderShelf.getResourceType());
        leaderShelf.removeResource(new Stone());
        assertEquals(ResourceType.STONE, leaderShelf.getResourceType());
        leaderShelf.addResource(new Stone());
        assertEquals(ResourceType.STONE, leaderShelf.getResourceType());
        leaderShelf.removeAll();
        assertEquals(ResourceType.STONE, leaderShelf.getResourceType());
    }

    /**
     * this test verify that i can only add a resource of the same type of the shelf,
     * even if the shelf is empty
     */
    @Test
    void testAddResources1(){
        leaderShelf = new LeaderShelf(ResourceType.COIN);
        assertTrue(leaderShelf.addResource(new Stone()));
        assertFalse(leaderShelf.addResource(new Coin()));
        assertTrue(leaderShelf.addResource(new Stone()));
        leaderShelf.removeResource(new Coin());
        assertTrue(leaderShelf.addResource(new Stone()));
        assertFalse(leaderShelf.addResource(new Coin()));
        leaderShelf.removeAll();
        assertFalse(leaderShelf.addResource(new Coin()));
        assertTrue(leaderShelf.addResource(new Stone()));
    }

    /**
     * this test verify that the leader shelf can only store 2 resources
     */
    @Test
    void testAddResources2(){
        leaderShelf = new LeaderShelf(ResourceType.SHIELD);
        assertFalse(leaderShelf.addResource(new Shield()));
        assertFalse(leaderShelf.addResource(new Shield()));
        assertTrue(leaderShelf.addResource(new Shield()));
        assertTrue(leaderShelf.addResource(new Shield()));
        assertEquals(2 , leaderShelf.getCapacity() - leaderShelf.getFreeSlots());
        assertEquals(1, leaderShelf.removeResource(new Shield()));
        assertFalse(leaderShelf.addResource(new Shield()));
        assertTrue(leaderShelf.addResource(new Shield()));
    }
}