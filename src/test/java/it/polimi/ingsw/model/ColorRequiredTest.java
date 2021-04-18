package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.ColorRequired;
import it.polimi.ingsw.model.LeaderCard.LeaderCardRequirements;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * this class test the color requirements of a leader card
 */
class ColorRequiredTest {

    LeaderCardRequirements requirements1;
    LeaderCardRequirements requirements2;
    RealPlayer player = new RealPlayer("player", null);

    /**
     * this test verify that a player with no cards
     * don't meets the requirements
     */
    @Test
    void testContainsRequirements1(){
        requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.GREEN)));
        requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.GREEN, CardColor.BLUE)));
        assertFalse(requirements1.containsRequirements(player));
        assertFalse(requirements2.containsRequirements(player));
    }

    /**
     * this test verify that a player who
     * contains only one card doesn't meet
     * the requirements
     */
    @Test
    void testContainsRequirements2(){
        requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.GREEN)));
        requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.GREEN, CardColor.BLUE)));
        DevelopmentCard card = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        player.locateDevelopmentCard(card, 1);
        assertFalse(requirements1.containsRequirements(player));
        assertFalse(requirements2.containsRequirements(player));
    }

    /**
     * this test verify that a player who
     * contains cards with colors not contained
     * in the colors needed by the leader card to be activated
     * doesn't meet the requirements
     */
    @Test
    void testContainsRequirements3(){
        requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.YELLOW, CardColor.PURPLE)));
        DevelopmentCard card1 = new DevelopmentCard(CardColor.YELLOW, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.PURPLE, 2,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 1);
        assertFalse(requirements2.containsRequirements(player));
    }

    /**
     * this test verify that a player who
     * contains every card with colors equal with
     * the colors needed by the leader card to be activated
     * meet the requirements
     */
    @Test
    void testContainsRequirements4(){
        requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.GREEN)));
        requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.GREEN, CardColor.PURPLE)));
        DevelopmentCard card1 = new DevelopmentCard(CardColor.PURPLE, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.GREEN, 2,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 1);
        assertTrue(requirements1.containsRequirements(player));
        DevelopmentCard card3 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        player.locateDevelopmentCard(card3, 2);
        assertTrue(requirements1.containsRequirements(player));
        assertTrue(requirements2.containsRequirements(player));
    }

    /**
     * this test verify that a player who
     * contains every card with colors all contained in
     * the colors needed by the leader card to be activated
     * meet the requirements
     */
    @Test
    void testContainsRequirements5(){
        requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.GREEN)));
        requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.GREEN, CardColor.PURPLE)));
        DevelopmentCard card1 = new DevelopmentCard(CardColor.PURPLE, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.GREEN, 2,0,null,null,null,0);
        DevelopmentCard card3 = new DevelopmentCard(CardColor.PURPLE, 1,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 1);
        player.locateDevelopmentCard(card3, 2);
        assertTrue(requirements1.containsRequirements(player));
        DevelopmentCard card4 = new DevelopmentCard(CardColor.GREEN, 2,0,null,null,null,0);
        player.locateDevelopmentCard(card4, 2);
        assertTrue(requirements1.containsRequirements(player));
        assertTrue(requirements2.containsRequirements(player));
    }

    /**
     * this method verify that a player who
     * not contains even a card with colors contained in
     * the colors needed by the leader card to be activated
     * doesn't meet the requirements
     */
    @Test
    void testContainsRequirements6(){
        requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.BLUE)));
        requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.BLUE, CardColor.YELLOW)));
        DevelopmentCard card1 = new DevelopmentCard(CardColor.PURPLE, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.GREEN, 2,0,null,null,null,0);
        DevelopmentCard card3 = new DevelopmentCard(CardColor.PURPLE, 1,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 1);
        player.locateDevelopmentCard(card3, 2);
        assertFalse(requirements1.containsRequirements(player));
        DevelopmentCard card4 = new DevelopmentCard(CardColor.GREEN, 2,0,null,null,null,0);
        player.locateDevelopmentCard(card4, 2);
        assertFalse(requirements1.containsRequirements(player));
        assertFalse(requirements2.containsRequirements(player));
    }
}