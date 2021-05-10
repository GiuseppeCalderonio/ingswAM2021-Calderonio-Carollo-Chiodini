package it.polimi.ingsw.model;
import static org.junit.jupiter.api.Assertions.*;

import it.polimi.ingsw.model.PlayerAndComponents.StrongBox;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

/**
 * this class test all methods of StrongBox class
 */
public class StrongBoxTest{

    /**
     * this method count all of resources contained in strongbox and check if the resource number is correct. Furthermore check the method isEmpty every time we add or remove a resource from strongbox
     */
    @Test
    public void countTotalStrongboxResources1() {
        CollectionResources resourcesGroup = new CollectionResources();
        StrongBox SB = new StrongBox();
        assertTrue(SB.isEmpty());
        assertEquals(0, SB.getNumberOfResources());
        resourcesGroup.add(new Coin()); resourcesGroup.add(new Coin()); resourcesGroup.add(new Coin()); resourcesGroup.add(new Coin()); resourcesGroup.add(new Coin());
        resourcesGroup.add(new Shield()); resourcesGroup.add(new Shield()); resourcesGroup.add(new Shield());
        assertEquals(0, SB.getNumberOfResources());
        resourcesGroup.add(new Servant()); resourcesGroup.add(new Servant());
        SB.addResources(resourcesGroup);
        assertEquals(10, SB.getNumberOfResources());
        assertFalse(SB.isEmpty());
        SB.removeResources(resourcesGroup);
        assertEquals(0, SB.getNumberOfResources());
        assertTrue(SB.isEmpty());
    }

    /**
     * this method count all of resources contained in strongbox and check if the resource number is correct. Furthermore check the method isEmpty every time we add or remove a resource from strongbox
     */
    @Test
    public void countTotalStrongboxResources2(){
        CollectionResources resourcesGroup2 = new CollectionResources();
        CollectionResources resourcesGroup = new CollectionResources();
        StrongBox SB = new StrongBox();
        assertTrue(SB.isEmpty());
        assertEquals(0 , SB.getNumberOfResources());
        resourcesGroup2.add(new Coin()); resourcesGroup2.add(new Shield()); resourcesGroup2.add(new Stone());
        SB.addResources(resourcesGroup2);
        assertFalse(SB.isEmpty());
        assertEquals(3 , SB.getNumberOfResources());
        resourcesGroup.add(new Servant());
        SB.addResources(resourcesGroup);
        assertEquals(4 , SB.getNumberOfResources());
        resourcesGroup2.remove(new Coin()); resourcesGroup2.remove(new Shield ());
        SB.removeResources(resourcesGroup2);
        assertFalse(SB.isEmpty());
        assertEquals(3 , SB.getNumberOfResources());
    }

    /**
     * this test check at the same time three methods of strongbox class:add , remove and getStrongboxResources.
     * We insert e remove resources in strongbox a lot of time and then we check which resources are contained in strongbox.
     */
    @Test
    public void checkResourcesInStrongbox1(){
        CollectionResources resourcesGroup = new CollectionResources();
        StrongBox SB = new StrongBox();
        assertEquals(resourcesGroup , SB.getStrongboxResources());
        resourcesGroup.add(new Coin()); resourcesGroup.add(new Coin()); resourcesGroup.add(new Coin()); resourcesGroup.add(new Coin()); resourcesGroup.add(new Coin());
        resourcesGroup.add(new Shield()); resourcesGroup.add(new Shield()); resourcesGroup.add(new Shield());
        assertTrue(SB.addResources(resourcesGroup));
        assertEquals(resourcesGroup, SB.getStrongboxResources());
        assertFalse(SB.removeResources(resourcesGroup));
        assertNotEquals(resourcesGroup , SB.getStrongboxResources());
    }

    /**
     * this test check at the same time three methods of strongbox class:add , remove and getStrongboxResources.
     * We insert e remove resources in strongbox a lot of time and then we check which resources are contained in strongbox.
     */
    @Test
    public void checkResourcesInStrongbox2() {
        CollectionResources resourcesGroup = new CollectionResources();
        StrongBox SB = new StrongBox();
        resourcesGroup.add(new Servant()); resourcesGroup.add(new Servant());
        assertTrue(SB.addResources(resourcesGroup));
        resourcesGroup.remove(new Servant());
        assertTrue(SB.removeResources(resourcesGroup));
        assertEquals(resourcesGroup, SB.getStrongboxResources());
        assertFalse(SB.removeResources(resourcesGroup));
        assertNotEquals(resourcesGroup , SB.getStrongboxResources());
        resourcesGroup.remove(new Servant());
        assertEquals(resourcesGroup , SB.getStrongboxResources());
    }

    /**
     * this test check at the same time three methods of strongbox class:add , remove and getStrongboxResources.
     * We insert e remove resources in strongbox a lot of time and then we check which resources are contained in strongbox.
     */
    @Test
    public void checkResourcesInStrongbox3sss() {
        CollectionResources resourcesGroup = new CollectionResources();
        StrongBox SB = new StrongBox();
        resourcesGroup.add(new Servant());
        resourcesGroup.add(new Coin());
        assertTrue(SB.addResources(resourcesGroup));
        resourcesGroup.remove(new Servant());
        assertTrue(SB.removeResources(resourcesGroup));
        assertNotEquals(resourcesGroup, SB.getStrongboxResources());
        resourcesGroup.remove(new Coin());
        assertNotEquals(resourcesGroup, SB.getStrongboxResources());
    }

    /**
     * this test check only add and remove methods when we want to insert or delete zero resources
     */
    @Test
    public void checkVoidInsertOrDelete(){
        CollectionResources resourcesGroup = new CollectionResources();
        StrongBox SB = new StrongBox();
        assertTrue(SB.addResources(resourcesGroup));
        assertEquals(0 , SB.getNumberOfResources());
        resourcesGroup.add(new Coin());
        assertTrue(SB.addResources(resourcesGroup));
        resourcesGroup.remove(new Coin());
        assertTrue(SB.removeResources(resourcesGroup));
        assertEquals(1 , SB.getNumberOfResources());
    }

    /**
     * this test check the remove of a resource that is not contained in strongbox
     */
    @Test
    public void removeParticularResource(){
        CollectionResources resourcesGroup = new CollectionResources();
        CollectionResources resourcesGroup2 = new CollectionResources();
        StrongBox SB = new StrongBox();
        resourcesGroup.add(new Servant());
        resourcesGroup2.add(new Shield());
        assertTrue(SB.addResources(resourcesGroup));
        SB.removeResources(resourcesGroup2);
        assertEquals(1 , SB.getNumberOfResources());
    }

}

