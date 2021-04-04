package it.polimi.ingsw.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

/**
 * this class tests all methods of LeaderProduction class
 */
public class LeaderProductionTest {
    /**
     * this method check the resource insertion in stack
     */
    @Test
    public void addLeaderProduction(){
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
    public void checkEqualsDynamicType (){
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
}



