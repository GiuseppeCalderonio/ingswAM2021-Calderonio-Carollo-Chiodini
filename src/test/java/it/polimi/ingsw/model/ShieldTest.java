package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Marble.BlueMarble;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.ResourceType;
import it.polimi.ingsw.model.Resources.Shield;
import it.polimi.ingsw.model.Resources.Stone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * this class test the Shield
 */
class ShieldTest {

    /**
     * this test verify that the getType method return a shield type
     */
    @Test
    public void testGetType(){
        Resource shield = new Shield();
        assertEquals(ResourceType.SHIELD, shield.getType());
    }

    /**
     * this test verify that the method convertInMarble return a blue marble
     */
    @Test
    public void testConvertInMarble(){
        Resource shield = new Shield();
        assertEquals(new BlueMarble(), shield.convertInMarble());
    }

    /**
     * this test verify that the method equals work correctly
     */
    @Test
    public void testEquals(){
        Resource firstShield = new Shield();
        Resource secondShield = new Shield();
        Resource different = new Stone();
        assertEquals(secondShield, firstShield);
        assertEquals(firstShield, secondShield);
        assertNotEquals(different, firstShield);
    }

}