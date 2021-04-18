package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Marble.GreyMarble;
import it.polimi.ingsw.model.Resources.Coin;
import it.polimi.ingsw.model.Resources.Resource;
import it.polimi.ingsw.model.Resources.ResourceType;
import it.polimi.ingsw.model.Resources.Stone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * this class test the Stone
 */
class StoneTest {

    /**
     * this test verify that the getType method return a stone type
     */
    @Test
    public void testGetType(){
        Resource stone = new Stone();
        assertEquals(ResourceType.STONE, stone.getType());
    }

    /**
     * this test verify that the method convertInMarble return a grey marble
     */
    @Test
    public void testConvertInMarble(){
        Resource stone = new Stone();
        assertEquals(new GreyMarble(), stone.convertInMarble());
    }

    /**
     * this test verify that the method equals work correctly
     */
    @Test
    public void testEquals(){
        Resource firstStone = new Stone();
        Resource secondStone = new Stone();
        Resource different = new Coin();
        assertEquals(secondStone, firstStone);
        assertEquals(firstStone, secondStone);
        assertNotEquals(different, firstStone);
    }
}