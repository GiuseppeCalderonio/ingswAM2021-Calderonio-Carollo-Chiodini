package it.polimi.ingsw.model;

import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.Marble.WhiteMarble;
import it.polimi.ingsw.model.Marble.YellowMarble;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the white marble
 */
class WhiteMarbleTest {

    /**
     * this test verify that the white marble has not faith points to return
     */
    @Test
    public void testFaithPoints(){
        Marble white = new WhiteMarble();
        int counter = 0;
        for (int i = 0; i <= 20 ; i++){
            counter = counter + white.faithPoints();
        }
        assertEquals(0, counter);
    }

    /**
     * this test verify that the blue marble conversion return a null resource
     */
    @Test
    public void testConversion(){
        Marble white = new WhiteMarble();
        assertNull(white.convert());
    }
    /**
     * this test verify that the equals method works correctly
     */
    @Test
    public void testEquals(){
        Marble first = new WhiteMarble();
        Marble second = new WhiteMarble();
        assertEquals(second, first);
        assertEquals(first, second);
        Marble third = new YellowMarble();
        assertNotEquals(third, first);
        assertNotEquals(first, third);
    }
}