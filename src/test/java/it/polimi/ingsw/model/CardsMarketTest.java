package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Resources.*;
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
     *this test verifies the random disposition of the cards in the matrix, indeed it is very unlikely to find two
     * equals matrix declared consecutively, the method counts the number of equal cards and assert that this number
     * is minor of the total cards
     */
    @Test
    void testShuffleMarket() {
        market = new CardsMarket();
        CardsMarket market2 = new CardsMarket();
        int counter =0;
        int i,j;
        for (i = 1; i < 4; i++) {
            for (j = 0; j<4; j++) {
                if(market2.popCard(i, CardColor.BLUE).equals(market.popCard(i,CardColor.BLUE)))
                    counter++;
            }
        }
        for (i = 1; i < 4; i++) {
            for (j = 0; j<4; j++) {
                if(market2.popCard(i,CardColor.GREEN).equals(market.popCard(i,CardColor.GREEN)))
                    counter++;
            }
        }
        for (i = 1; i < 4; i++) {
            for (j = 0; j<4; j++) {
                if(market2.popCard(i,CardColor.YELLOW).equals(market.popCard(i,CardColor.YELLOW)))
                    counter++;
            }
        }
        for (i = 1; i < 4; i++) {
            for (j = 0; j<4; j++) {
                if(market2.popCard(i,CardColor.PURPLE).equals(market.popCard(i,CardColor.PURPLE)))
                    counter++;
            }
        }
        assertTrue(0<=counter&& counter<48);

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

    /**
     * this test verify that the get card method return null when the inputs are incorrect
     * this method calls the method checkCard, so this test verify also that method
     */
    @Test
    void testGetNull(){
        market = new CardsMarket();

        assertNull(market.getCard( 1, null));
        assertNull(market.getCard( 3, null));
        assertNull(market.getCard( 0, CardColor.BLUE));
        assertNull(market.getCard( 4, CardColor.BLUE));
        assertNull(market.getCard( -2, CardColor.PURPLE));
        assertNull(market.getCard( 6, CardColor.GREEN));
    }

    /**
     * this test verify that the method checkCard return the right value when given the right inputs
     */
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

    /**
     * this test verifies that the method show in cardsMarket return a matrix of Development Card of which each element
     * is contained on the top of the List of the respective position in CardsMarket
     */
    @Test
    void testShow1() {
        int i, j;
        market = new CardsMarket();
        DevelopmentCard[][] toReturn = new DevelopmentCard[3][4];
        toReturn[0][0] = market.getCard(3, CardColor.GREEN);
        toReturn[1][0] = market.getCard(2, CardColor.GREEN);
        toReturn[2][0] = market.getCard(1, CardColor.GREEN);
        toReturn[0][1] = market.getCard(3, CardColor.BLUE);
        toReturn[1][1] = market.getCard(2, CardColor.BLUE);
        toReturn[2][1] = market.getCard(1, CardColor.BLUE);
        toReturn[0][2] = market.getCard(3, CardColor.YELLOW);
        toReturn[1][2] = market.getCard(2, CardColor.YELLOW);
        toReturn[2][2] = market.getCard(1, CardColor.YELLOW);
        toReturn[0][3] = market.getCard(3, CardColor.PURPLE);
        toReturn[1][3] = market.getCard(2, CardColor.PURPLE);
        toReturn[2][3] = market.getCard(1, CardColor.PURPLE);

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 4; j++)
                assertEquals(toReturn[i][j], market.show()[i][j]);
        }
    }
    /**
     * this test verifies that the method show in CardsMarket return a matrix of Development Card of which each element
     * is contained on the top of the List of the respective position in CardsMarket, if a List is empty then in
     * the respective position there is a null
     */
    @Test
    void testShow2() {
        int i,j;
        market = new CardsMarket();

        for (i=0;i<4;i++) {
            market.getCardMatrix()[0][0].remove(0);
        }
        for (i=0;i<4;i++) {
            market.getCardMatrix()[1][2].remove(0);
        }

        DevelopmentCard[][] toReturn = new DevelopmentCard[3][4];
        toReturn[0][0]=null;
        toReturn[1][0] = market.getCard(2, CardColor.GREEN);
        toReturn[2][0] = market.getCard(1, CardColor.GREEN);
        toReturn[0][1] = market.getCard(3, CardColor.BLUE);
        toReturn[1][1] = market.getCard(2, CardColor.BLUE);
        toReturn[1][2]=null;
        toReturn[2][1] = market.getCard(1, CardColor.BLUE);
        toReturn[0][2] = market.getCard(3, CardColor.YELLOW);
        toReturn[2][2] = market.getCard(1, CardColor.YELLOW);
        toReturn[0][3] = market.getCard(3, CardColor.PURPLE);
        toReturn[1][3] = market.getCard(2, CardColor.PURPLE);
        toReturn[2][3] = market.getCard(1, CardColor.PURPLE);

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 4; j++) {
                assertEquals(toReturn[i][j], market.show()[i][j]);
            }
        }
    }

}