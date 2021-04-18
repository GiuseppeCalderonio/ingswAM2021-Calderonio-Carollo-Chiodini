package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.WhiteMarble;
import it.polimi.ingsw.model.Marble.YellowMarble;
import it.polimi.ingsw.model.Resources.Coin;
import it.polimi.ingsw.model.Resources.Servant;
import it.polimi.ingsw.model.Resources.Shield;
import it.polimi.ingsw.model.Resources.Stone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the yellow marble
 */
class YellowMarbleTest {

    /**
     * this test verify that the yellow marble has not faith points to return
     */
    @Test
    public void testFaithPoints(){
        Marble yellow = new YellowMarble();
        int counter = 0;
        for (int i = 0; i <= 20 ; i++){
            counter = counter + yellow.faithPoints();
        }
        assertEquals(0, counter);
    }

    /**
     * this test verify that the blue marble conversion return a shield type resource
     */
    @Test
    public void testConversion(){
        Marble yellow = new YellowMarble();
        assertTrue(yellow.convert() instanceof Coin);
        assertFalse(yellow.convert() instanceof Servant);
        assertFalse(yellow.convert() instanceof Stone);
        assertFalse(yellow.convert() instanceof Shield);
    }

    /**
     * this test verify that the equals method works correctly
     */
    @Test
    public void testEquals(){
        Marble first = new YellowMarble();
        Marble second = new YellowMarble();
        assertEquals(second, first);
        assertEquals(first, second);
        Marble third = new WhiteMarble();
        assertNotEquals(third, first);
        assertNotEquals(first, third);
    }
}