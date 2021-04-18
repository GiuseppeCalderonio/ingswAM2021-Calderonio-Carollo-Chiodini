package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Marble.GreyMarble;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.YellowMarble;
import it.polimi.ingsw.model.Resources.Coin;
import it.polimi.ingsw.model.Resources.Servant;
import it.polimi.ingsw.model.Resources.Shield;
import it.polimi.ingsw.model.Resources.Stone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the grey marble
 */
class GreyMarbleTest {

    /**
     * this test verify that the grey marble has not faith points to return
     */
    @Test
    public void testFaithPoints(){
        Marble grey = new GreyMarble();
        int counter = 0;
        for (int i = 0; i <= 20 ; i++){
            counter = counter + grey.faithPoints();
        }
        assertEquals(0, counter);
    }

    /**
     * this test verify that the blue marble conversion return a shield type resource
     */
    @Test
    public void testConversion(){
        Marble grey = new GreyMarble();
        assertTrue(grey.convert() instanceof Stone);
        assertFalse(grey.convert() instanceof Servant);
        assertFalse(grey.convert() instanceof Shield);
        assertFalse(grey.convert() instanceof Coin);
    }

    /**
     * this test verify that the equals method works correctly
     */
    @Test
    public void testEquals(){
        Marble first = new GreyMarble();
        Marble second = new GreyMarble();
        assertEquals(second, first);
        assertEquals(first, second);
        Marble third = new YellowMarble();
        assertNotEquals(third, first);
        assertNotEquals(first, third);
    }
}