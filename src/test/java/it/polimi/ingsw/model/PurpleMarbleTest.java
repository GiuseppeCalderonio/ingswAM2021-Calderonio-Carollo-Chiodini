package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.PurpleMarble;
import it.polimi.ingsw.model.Marble.YellowMarble;
import it.polimi.ingsw.model.Resources.Coin;
import it.polimi.ingsw.model.Resources.Servant;
import it.polimi.ingsw.model.Resources.Shield;
import it.polimi.ingsw.model.Resources.Stone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the purple marble
 */
class PurpleMarbleTest {

    /**
     * this test verify that the purple marble has not faith points to return
     */
    @Test
    public void testFaithPoints(){
        Marble purple = new PurpleMarble();
        int counter = 0;
        for (int i = 0; i <= 20 ; i++){
            counter = counter + purple.faithPoints();
        }
        assertEquals(0, counter);
    }

    /**
     * this test verify that the blue marble conversion return a shield type resource
     */
    @Test
    public void testConversion(){
        Marble purple = new PurpleMarble();
        assertTrue(purple.convert() instanceof Servant);
        assertFalse(purple.convert() instanceof Shield);
        assertFalse(purple.convert() instanceof Stone);
        assertFalse(purple.convert() instanceof Coin);
    }

    /**
     * this test verify that the equals method works correctly
     */
    @Test
    public void testEquals(){
        Marble first = new PurpleMarble();
        Marble second = new PurpleMarble();
        assertEquals(second, first);
        assertEquals(first, second);
        Marble third = new YellowMarble();
        assertNotEquals(third, first);
        assertNotEquals(first, third);
    }
}