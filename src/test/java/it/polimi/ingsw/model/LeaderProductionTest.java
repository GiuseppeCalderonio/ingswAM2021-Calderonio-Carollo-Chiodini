package it.polimi.ingsw.model;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.PlayerAndComponents.LeaderProduction;
import it.polimi.ingsw.model.PlayerAndComponents.ProductionPower;
import it.polimi.ingsw.model.Resources.Coin;
import it.polimi.ingsw.model.Resources.Servant;
import it.polimi.ingsw.model.Resources.Shield;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class tests all methods of LeaderProduction class
 */
public class LeaderProductionTest {
    /**
     * this method check the resource insertion in stack
     */
    @Test
    public void testAddLeaderProduction(){
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cards1 = new DevelopmentCard(null, 1, 1, null,null, null, 0);
        DevelopmentCard cards2 = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cards3 = new DevelopmentCard(null, 3, 11, null, null, null, 0);
        prodPower.placeCard(cards1, 3);        prodPower.placeCard(cards2, 3);        prodPower.placeCard(cards3, 3);
        prodPower = new LeaderProduction(new Coin() , prodPower);
        assertEquals(prodPower.getInput(1) , new Coin());
        assertNull(prodPower.getInput(2));
        prodPower.addLeaderProduction(new Servant());
        assertEquals(prodPower.getInput(2) , new Servant());
        assertNotNull(prodPower.getInput(2));
    }

    /**
     * this method check if the dynamic type of prodPower contains all productionPower resources plus the resources of activated leader card
     */
    @Test
    public void testCheckEqualsDynamicType (){
        ProductionPower prodPower = new ProductionPower();
        DevelopmentCard cards1 = new DevelopmentCard(null, 1, 1, null,null, null, 0);
        DevelopmentCard cards2 = new DevelopmentCard(null, 2, 5, null, null, null, 0);
        DevelopmentCard cardsFirst = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        DevelopmentCard cardsFirsts = new DevelopmentCard(null, 1, 1, null, null, null, 0);
        prodPower.placeCard(cards1, 3); prodPower.placeCard(cards2, 3); prodPower.placeCard(cardsFirst, 3); prodPower.placeCard(cardsFirsts,1);
        prodPower = new LeaderProduction(new Coin() , prodPower);
        assertEquals(prodPower.getInput(1) , new Coin());
        prodPower.addLeaderProduction(new Shield());
        assertEquals(prodPower.getCard(3).getLevel(),2);
        assertNull(prodPower.getCard(2));
        assertEquals(prodPower.getCard(1).getLevel(),1);
        assertEquals(prodPower.getInput(2) , new Shield());
        assertEquals(3, prodPower.getNumOfCards());
    }

    /**
     *this test verifies that if is called the method activateProduction with wrong input parameters it has no effect
     */
    @Test
    void testActivateAndCheckLeaderProduction1() {
        ProductionPower prodPower = new ProductionPower();
        prodPower = new LeaderProduction(new Coin() , prodPower);
        prodPower.activateLeaderProduction(2);
        prodPower.activateLeaderProduction(5);
        prodPower.activateLeaderProduction(0);
        prodPower.activateLeaderProduction(-1);
        assertFalse(prodPower.isLeaderProductionActivated(1));
        assertFalse(prodPower.isLeaderProductionActivated(2));
    }


    /**
     * this test verifies that if two leader Production are been activated then the method isLeaderProductionActivated
     * with input one and two returns true
     */
    @Test
    void testActivateAndCheckLeaderProduction2() {
        ProductionPower prodPower = new ProductionPower();
        prodPower = new LeaderProduction(new Coin() , prodPower);
        prodPower.addLeaderProduction(new Shield());
        prodPower.activateLeaderProduction(1);
        prodPower.activateLeaderProduction(2);
        assertTrue(prodPower.isLeaderProductionActivated(1));
        assertTrue(prodPower.isLeaderProductionActivated(2));
    }

    /**
     *this test verifies that if we have two leader Productions but we haven't activated yet, then the method
     * isLeaderProductionActivated with input 1 and 2 returns false
     */
    @Test
    void testActivateAndCheckLeaderProduction3() {
        ProductionPower prodPower = new ProductionPower();
        prodPower = new LeaderProduction(new Coin() , prodPower);
        prodPower.addLeaderProduction(new Shield());
        assertFalse(prodPower.isLeaderProductionActivated(1));
        assertFalse(prodPower.isLeaderProductionActivated(2));
    }


    /**
     *this test verifies that after the resetProductions method is called then the method isLeaderProductionActivated
     * returns false for every input parameters
     */
    @Test
    void testResetProductions() {
        ProductionPower prodPower = new ProductionPower();
        prodPower = new LeaderProduction(new Coin() , prodPower);
        prodPower.activateLeaderProduction(1);
        assertTrue(prodPower.isLeaderProductionActivated(1));
        prodPower.resetProductions();
        assertFalse(prodPower.isLeaderProductionActivated(1));
        assertFalse(prodPower.isLeaderProductionActivated(2));
    }
}



