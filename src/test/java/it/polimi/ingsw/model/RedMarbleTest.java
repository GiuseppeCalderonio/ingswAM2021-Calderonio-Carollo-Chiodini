package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the red marble
 */
class RedMarbleTest {

    /**
     * this test verify that the red marble return one faith point
     */
    @Test
    public void testFaithPoints(){
        Marble red = new RedMarble();
        int counter = 0;
        int i;
        for (i = 0; i <= 20 ; i++){
            counter = counter + red.faithPoints();
        }
        assertEquals(i, counter);
    }

    /**
     * this test verify that the blue marble conversion return a shield type resource
     */
    @Test
    public void testConversion(){
        Marble red = new RedMarble();
        assertNull(red.convert());
    }

    /**
     * this test verify that the equals method works correctly
     */
    @Test
    public void testEquals(){
        Marble first = new RedMarble();
        Marble second = new RedMarble();
        assertEquals(second, first);
        assertEquals(first, second);
        Marble third = new YellowMarble();
        assertNotEquals(third, first);
        assertNotEquals(first, third);
    }
}