package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the method of the the development card
 */
class DevelopmentCardTest {

    /**
     * this test verify that the getters of the card work correctly
     */
    @Test
    public void testGetters(){
        CardColor color = CardColor.GREEN;
        int level = 2;
        int victoryPoints = 5;
        CollectionResources cost = new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Coin())));
        CollectionResources input = new CollectionResources(new ArrayList<>(Collections.singletonList(new Servant())));
        CollectionResources output = new CollectionResources(new ArrayList<>());
        int faithPoints = 0;
        DevelopmentCard card = new DevelopmentCard(color
                ,level
                ,victoryPoints
                ,cost
                ,input
                ,output
                ,faithPoints);
        assertEquals(color, card.getColor());
        assertEquals(level, card.getLevel());
        assertEquals(victoryPoints, card.getVictoryPoints());
        assertEquals(input, card.getProductionPowerInput());
        assertEquals(output, card.getProductionPowerOutput());
        assertEquals(faithPoints, card.getProductionPowerFaithPoints());
    }

    /**
     * this test verify that the equals method works correctly fro the development card
     */
    @Test
    public void testEquals(){
        DevelopmentCard card1 = new DevelopmentCard(CardColor.PURPLE
                ,3
                ,6
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(), new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(), new Stone())))
                ,2);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.PURPLE
                ,3
                ,6
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(), new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(), new Stone())))
                ,2);
        DevelopmentCard card3 = new DevelopmentCard(CardColor.YELLOW
                ,3
                ,6
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Shield())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Stone(), new Stone())))
                ,2);
        assertEquals(card2, card1);
        assertEquals(card1, card2);
        assertNotEquals(card3, card1);
    }
}