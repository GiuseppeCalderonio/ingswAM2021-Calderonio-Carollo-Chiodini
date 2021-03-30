package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the Servant
 */
class ServantTest {

    /**
     * this test verify that the getType method return a servant type
     */
    @Test
    public void testGetType(){
        Resource servant = new Servant();
        assertEquals(ResourceType.SERVANT, servant.getType());
    }

    /**
     * this test verify that the method convertInMarble return a purple marble
     */
    @Test
    public void testConvertInMarble(){
        Resource servant = new Servant();
        assertEquals(new PurpleMarble(), servant.convertInMarble());
    }

    /**
     * this test verify that the method equals work correctly
     */
    @Test
    public void testEquals(){
        Resource firstServant = new Servant();
        Resource secondServant = new Servant();
        Resource different = new Stone();
        assertEquals(secondServant, firstServant);
        assertEquals(firstServant, secondServant);
        assertNotEquals(different, firstServant);
    }
}