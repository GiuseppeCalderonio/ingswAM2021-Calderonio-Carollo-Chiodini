package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests the methods of CardsMarket
 */
class CardsMarketTest {

    private CardsMarket market;

    /**
     * this test verify that the constructor create the market correctly
     */
    @Test
    public void testCreation() {
        CardsMarket market = new CardsMarket();
        assertNotNull(market);
    }

    /**
     * this test verify that the method pop get the card with the right requirements requested
     */
    @Test
    public void testPopCard1() {
        market = new CardsMarket();
        DevelopmentCard card = market.popCard(1, CardColor.GREEN);
        assertEquals(CardColor.GREEN, card.getColor());
        assertEquals(1, card.getLevel());
        card = market.popCard(3, CardColor.PURPLE);
        assertEquals(CardColor.PURPLE, card.getColor());
        assertEquals(3, card.getLevel());
    }

    /**
     * this test verify that that the card that we want to pop is the same of the one we get with the same parameters
     */
    @Test
    void testPopCard2() {
        market = new CardsMarket();
        DevelopmentCard buffer;
        buffer = market.getCard(1, CardColor.YELLOW);
        assertEquals(buffer , market.popCard(1, CardColor.YELLOW));
    }

    /**
     * this test verifies that the method checkMissColumn returns true only when all of the three
     * decks of developmentCard with same color are empty
     */
    @Test
    void testCheckMissColumn() {
        market = new CardsMarket();
        int i;
        assertFalse(market.checkMissColumn());
        for (i = 0; i < 4; i++) {
            market.popCard(1, CardColor.YELLOW);
        }
        assertFalse(market.checkMissColumn());
        for (i = 0; i < 4; i++) {
            market.popCard(2, CardColor.YELLOW);
        }
        assertFalse(market.checkMissColumn());
        for (i = 0; i < 4; i++) {
            market.popCard(3, CardColor.YELLOW);
        }
        assertTrue(market.checkMissColumn());
    }

    /**
     * this test verifies that every card in a position of market (Arraylist) are reached by a get
     */
    @Test
    void testGetCard() {
        market = new CardsMarket();

        ArrayList<DevelopmentCard> list = new ArrayList<>();
        list.add(new DevelopmentCard(CardColor.GREEN
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0));
        list.add(new DevelopmentCard(CardColor.GREEN
                ,3
                ,11
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Shield(), new Shield(), new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,3));
        list.add(new DevelopmentCard(CardColor.GREEN
                ,3
                ,10
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Shield(), new Servant(), new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Stone(), new Stone())))
                ,1));
        list.add(new DevelopmentCard(CardColor.GREEN
                ,3
                ,9
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Shield(), new Shield())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(), new Stone(), new Stone())))
                ,2));

        assertTrue(list.contains(market.getCard(3,CardColor.GREEN)));
        market.popCard(3,CardColor.GREEN);
        assertTrue(list.contains(market.getCard(3,CardColor.GREEN)));
        market.popCard(3,CardColor.GREEN);
        assertTrue(list.contains(market.getCard(3,CardColor.GREEN)));
        market.popCard(3,CardColor.GREEN);
        assertTrue(list.contains(market.getCard(3,CardColor.GREEN)));
        market.popCard(3,CardColor.GREEN);

    }
    @Test
    void testCheckCard(){
        market = new CardsMarket();
        assertTrue(market.checkCard(2, CardColor.BLUE));
        market.popCard(2, CardColor.BLUE);
        assertTrue(market.checkCard(2, CardColor.BLUE));
        market.popCard(2, CardColor.BLUE);
        assertTrue(market.checkCard(2, CardColor.BLUE));
        market.popCard(2, CardColor.BLUE);
        assertTrue(market.checkCard(2, CardColor.BLUE));
        market.popCard(2, CardColor.BLUE);
        assertFalse(market.checkCard(2, CardColor.BLUE));
    }
}