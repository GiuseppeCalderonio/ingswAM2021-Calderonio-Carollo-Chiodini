package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the level required for a leader card
 */
class LevelRequiredTest {

    LeaderCardRequirements requirements;
    RealPlayer player = new RealPlayer("player", null);

    /**
     * this test verify that a player with no cards
     * don't meets the requirements
     */
    @Test
    void testContainsRequirements1(){
        requirements = new LevelRequired(CardColor.BLUE);
        assertFalse(requirements.containsRequirements(player));
    }

    /**
     * this test verify that a player who
     * contains only level 1 cards doesn't meet
     * the requirements
     */
    @Test
    void testContainsRequirements2(){
        requirements = new LevelRequired(CardColor.GREEN);
        DevelopmentCard card1 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.PURPLE, 1,0,null,null,null,0);
        DevelopmentCard card3 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 2);
        player.locateDevelopmentCard(card3, 3);
        assertFalse(requirements.containsRequirements(player));
    }

    /**
     * this test verify that a player who
     * contains a level 2 card with a different color
     * from that one in the leader card doesn't meets
     * the requirements
     */
    @Test
    void testContainsRequirements3(){
        requirements = new LevelRequired(CardColor.GREEN);
        DevelopmentCard card1 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.PURPLE, 2,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 1);
        assertFalse(requirements.containsRequirements(player));
    }

    /**
     * this test verify that a player who
     * contains a level 2 card with the same color
     * of that one in the leader card meets
     * the requirements
     */
    @Test
    void testContainsRequirements4(){
        requirements = new LevelRequired(CardColor.BLUE);
        DevelopmentCard card1 = new DevelopmentCard(CardColor.YELLOW, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.BLUE, 2,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 1);
        assertTrue(requirements.containsRequirements(player));
    }

    /**
     * this test verify that a player who
     * contains two level 2 card with the same color
     * of that one in the leader card meets
     * the requirements
     */
    @Test
    void testContainsRequirements5(){
        requirements = new LevelRequired(CardColor.YELLOW);
        DevelopmentCard card1 = new DevelopmentCard(CardColor.PURPLE, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.YELLOW, 2,0,null,null,null,0);
        DevelopmentCard card3 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        DevelopmentCard card4 = new DevelopmentCard(CardColor.YELLOW, 2,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 1);
        player.locateDevelopmentCard(card3, 2);
        player.locateDevelopmentCard(card4, 2);
        assertTrue(requirements.containsRequirements(player));
    }

    /**
     * this test verify that a player who
     * contains two level 2 card with the same color
     * of that one in the leader card meets
     * the requirements
     */
    @Test
    void testContainsRequirements6(){
        requirements = new LevelRequired(CardColor.GREEN);
        DevelopmentCard card1 = new DevelopmentCard(CardColor.PURPLE, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.YELLOW, 2,0,null,null,null,0);
        DevelopmentCard card3 = new DevelopmentCard(CardColor.GREEN, 3,0,null,null,null,0);
        DevelopmentCard card4 = new DevelopmentCard(CardColor.YELLOW, 1,0,null,null,null,0);
        DevelopmentCard card5 = new DevelopmentCard(CardColor.GREEN, 2,0,null,null,null,0);
        DevelopmentCard card6 = new DevelopmentCard(CardColor.BLUE, 3,0,null,null,null,0);
        DevelopmentCard card7 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        DevelopmentCard card8 = new DevelopmentCard(CardColor.GREEN, 2,0,null,null,null,0);
        DevelopmentCard card9 = new DevelopmentCard(CardColor.PURPLE, 3,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 1);
        player.locateDevelopmentCard(card3, 1);
        player.locateDevelopmentCard(card4, 2);
        player.locateDevelopmentCard(card5, 2);
        player.locateDevelopmentCard(card6, 2);
        player.locateDevelopmentCard(card7, 3);
        player.locateDevelopmentCard(card8, 3);
        player.locateDevelopmentCard(card9, 3);
        assertTrue(requirements.containsRequirements(player));
    }

}