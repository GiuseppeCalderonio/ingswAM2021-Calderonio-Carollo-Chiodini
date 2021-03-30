package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
/**
 * this class test the Coin
 */
class CoinTest {

    /**
     * this test verify that the getType method return a coin type
     */
    @Test
    public void testGetType(){
        Resource coin = new Coin();
        assertEquals(ResourceType.COIN, coin.getType());
    }

    /**
     * this test verify that the method convertInMarble return a yellow marble
     */
    @Test
    public void testConvertInMarble(){
        Resource coin = new Coin();
        assertEquals(new YellowMarble(), coin.convertInMarble());
    }

    /**
     * this test verify that the method equals work correctly
     */
    @Test
    public void testEquals(){
        Resource firstCoin = new Coin();
        Resource secondCoin = new Coin();
        Resource different = new Stone();
        assertEquals(secondCoin, firstCoin);
        assertEquals(firstCoin, secondCoin);
        assertNotEquals(different, firstCoin);
    }
}