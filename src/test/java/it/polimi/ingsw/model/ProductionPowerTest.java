package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * here is tested the class ProductionPower, in every test we check the correct insertion of cards in personal dashboard and the action of a method of ProductionPower class
 */

public class ProductionPowerTest {
    /**
     * this method sum faith points of all card in dashboard and check if this sum is correct
     */
    @Test
    public void countTotalVictoryPoints1() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cards1 = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cards2 = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cards3 = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cards1, 1);
        prodPower.placeCard(cards2, 1);
        prodPower.placeCard(cards3, 1);
        assertEquals(17, prodPower.getVictoryPoints());
    }

    /**
     * this method sum faith points of all card in dashboard and check if this sum is correct
     */
    @Test
    public void countTotalVictoryPoints2() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cardsFirst = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSecond = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThird = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cardsThird, 2);
        prodPower.placeCard(cardsFirst, 2);
        prodPower.placeCard(cardsSecond, 2);
        assertEquals(6, prodPower.getVictoryPoints());
    }

    /**
     * this method sum faith points of all card in dashboard and check if this sum is correct
     */
    @Test
    public void countTotalVictoryPoints3() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cardsFirsts = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSeconds = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThirds = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard (cardsThirds , 3);
        prodPower.placeCard (cardsSeconds , 3);
        prodPower.placeCard (cardsFirsts , 3);
        DevelopmentCard cardsFirst = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSecond = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThird = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cardsThird, 2);
        prodPower.placeCard(cardsFirst, 2);
        prodPower.placeCard(cardsSecond, 2);
        assertEquals(7 , prodPower.getVictoryPoints());
    }

    /**
     *  this check count the number of all cards contained in personal dashboard. We insert some cards in the dashboard and the we do the count
     */
    @Test
    public void countTotalCardInDashboard1() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cards1 = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cards2 = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cards3 = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cards1, 1);
        prodPower.placeCard(cards2, 1);
        prodPower.placeCard(cards3, 1);
        assertEquals(3, prodPower.getNumOfCards());
    }

    /**
     *  this check count the number of all cards contained in personal dashboard. We insert some cards in the dashboard and the we do the count
     */
    @Test
    public void countTotalCardInDashboard2() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cardsFirst = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSecond = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThird = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cardsThird, 2);
        prodPower.placeCard(cardsFirst, 2);
        prodPower.placeCard(cardsSecond, 2);
        assertEquals(2, prodPower.getNumOfCards());
    }

    /**
     *  this check count the number of all cards contained in personal dashboard. We insert some cards in the dashboard and the we do the count
     */
    @Test
    public void countTotalCardInDashboard3() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cardsFirst = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSecond = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThird = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cardsThird, 2);
        prodPower.placeCard(cardsFirst, 2);
        prodPower.placeCard(cardsSecond, 2);
        DevelopmentCard cardsFirsts = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSeconds = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThirds = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard (cardsThirds , 3);
        prodPower.placeCard (cardsSeconds , 3);
        prodPower.placeCard (cardsFirsts , 3);
        assertEquals(3 , prodPower.getNumOfCards());
    }

    /**
     * this method check and return the selected card. this card must be contained in personal dashboard
     */
    @Test
    public void getCards1() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cards1 = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cards2 = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cards3 = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cards1, 3);
        prodPower.placeCard(cards2, 3);
        prodPower.placeCard(cards3, 3);
        assertEquals(cards3, prodPower.getCard(3));
        assertNull( prodPower.getCard(1));
    }

    /**
     * this method check and return the selected card. this card must be contained in personal dashboard
     */
    @Test
    public void getCards2() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cardsFirst = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSecond = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThird = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cardsThird, 1);
        prodPower.placeCard(cardsFirst, 1);
        prodPower.placeCard(cardsSecond, 1);
        assertEquals(cardsSecond, prodPower.getCard(1));
        assertNull( prodPower.getCard(2));
    }

    /**
     * this method check and return the selected card. this card must be contained in personal dashboard
     */
    @Test
    public void getCards3() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cardsFirsts = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSeconds = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThirds = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard (cardsThirds , 2);
        prodPower.placeCard (cardsSeconds , 2);
        prodPower.placeCard (cardsFirsts , 2);
        DevelopmentCard cards1 = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cards2 = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cards3 = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cards1, 3);
        prodPower.placeCard(cards2, 3);
        prodPower.placeCard(cards3, 3);
        DevelopmentCard cardsFirst = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSecond = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThird = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cardsThird, 1);
        prodPower.placeCard(cardsFirst, 1);
        prodPower.placeCard(cardsSecond, 1);
        assertEquals(cards3 , prodPower.getCard(3));
        assertEquals(cardsSecond , prodPower.getCard(1));
        assertEquals(cardsFirsts , prodPower.getCard(2));
    }

    /**
     *this method check the correct placement of a card in dashboard
     */
    @Test
    public void checkPlacementPositionOfCard1(){
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cards1 = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cards2 = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cards3 = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        DevelopmentCard cardsFirst = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsThird = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        assertTrue(prodPower.placeCard(cards1 , 3));
        prodPower.placeCard(cards2 , 3);
        assertTrue(prodPower.placeCard(cards3 , 3));
        assertFalse(prodPower.placeCard(cardsThird , 3));
        assertTrue(prodPower.placeCard(cardsFirst , 2));
        assertFalse(prodPower.placeCard(cardsThird, 1));
    }

    /**
     *this method check the correct placement of a card in dashboard
     */
    @Test
    public void checkPlacementPositionOfCard2() {
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cards1 = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cards2 = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cards3 = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        DevelopmentCard cardsFirst = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsSecond = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsThird = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        assertFalse(prodPower.placeCard(cards3 , 1));
        assertFalse(prodPower.placeCard(cards2 , 1));
        assertTrue(prodPower.placeCard(cards1 , 1));
        assertFalse(prodPower.placeCard(cards3 , 1));
        assertTrue(prodPower.placeCard(cards2 , 1));
        assertTrue(prodPower.placeCard(cards3 , 1));
        assertTrue(prodPower.placeCard(cardsFirst, 3));
        assertFalse(prodPower.placeCard(cardsSecond, 2));
        assertTrue(prodPower.placeCard(cardsSecond, 3));
        assertTrue(prodPower.placeCard(cardsThird, 3));
    }
}
