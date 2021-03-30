package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the blue marble
 */
class BlueMarbleTest {

    /**
     * this test verify that the blue marble has not faith points to return
     */
    @Test
    public void testFaithPoints(){
        Marble blue = new BlueMarble();
        int counter = 0;
        for (int i = 0; i <= 20 ; i++){
            counter = counter + blue.faithPoints();
        }
        assertEquals(0, counter);
    }

    /**
     * this test verify that the blue marble conversion return a shield type resource
     */
    @Test
    public void testConversion(){
        Marble blue = new BlueMarble();
        assertTrue(blue.convert() instanceof Shield);
        assertFalse(blue.convert() instanceof Servant);
        assertFalse(blue.convert() instanceof Stone);
        assertFalse(blue.convert() instanceof Coin);
    }

    /**
     * this test verify that the equals method works correctly
     */
    @Test
    public void testEquals(){
        Marble first = new BlueMarble();
        Marble second = new BlueMarble();
        assertEquals(second, first);
        assertEquals(first, second);
        Marble third = new YellowMarble();
        assertNotEquals(third, first);
        assertNotEquals(first, third);
    }
}