package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.*;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the leader cards, but only the subclasses
 */
class LeaderCardTest {

    LeaderCard leaderCard;
    LeaderCard leaderCard1;
    LeaderCard leaderCard2;
    LeaderCard leaderCard3;
    RealPlayer player = new RealPlayer("player", null);
    LeaderCardRequirements requirements1;     LeaderCardRequirements requirements2;

    /**
     * this test check if two leaderCard are equals.
     * In particular newShelves leaderCard
     */
    @Test
    public void testEquals1(){
        leaderCard = new NewShelf(new ResourcesRequired(new Shield()),3,new Coin());
        leaderCard1 = new NewShelf(new ResourcesRequired(new Servant()),3,new Servant());
        leaderCard2 = new NewShelf(new ResourcesRequired(new Shield()),3,new Coin());
        assertEquals(leaderCard, leaderCard2);
        assertNotEquals(leaderCard, leaderCard1);
    }

    /**
     * this test check if two leaderCard are equals.
     * In particular newDiscount leaderCard
     */
    @Test
    public void testEquals2(){
        requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.PURPLE)));
        requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.BLUE)));
        leaderCard = new NewDiscount(requirements1,2,new Shield());
        leaderCard1 = new NewDiscount(requirements2,2,new Stone());
        leaderCard2 = new NewDiscount(requirements1,2,new Shield());
        assertEquals(leaderCard, leaderCard2);
        assertNotEquals(leaderCard, leaderCard1);
    }

    /**
     * this test check if two leaderCard are equals.
     * In particular newWhiteMarble leaderCard
     */
    @Test
    public void testEquals3(){
        requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.BLUE, CardColor.YELLOW)));
        requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.PURPLE, CardColor.GREEN)));
        leaderCard = new NewWhiteMarble(requirements2,5,new Coin());
        leaderCard1 = new NewWhiteMarble(requirements1,5,new Stone());
        leaderCard2 = new NewWhiteMarble(requirements2,5,new Coin());
        assertEquals(leaderCard, leaderCard2);
        assertNotEquals(leaderCard, leaderCard1);
    }

    /**
     * this test check if two leaderCard are equals.
     * In particular newProduction leaderCard
     */

    @Test
    public void testEquals4(){
        leaderCard = new NewProduction(new LevelRequired(CardColor.GREEN),4,new Coin());
        leaderCard1 = new NewProduction(new LevelRequired(CardColor.PURPLE),4,new Stone());
        leaderCard2 = new NewProduction(new LevelRequired(CardColor.GREEN),4,new Coin());
        assertEquals(leaderCard, leaderCard2);
        assertNotEquals(leaderCard, leaderCard1);
    }

    /**
     * this test verify that cards with different effects are always not
     * equals, when the cards respect the requirements of the game
     */
    @Test
    void testEquals5(){
        requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.PURPLE)));
        requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.PURPLE, CardColor.GREEN)));
        leaderCard = new NewShelf(new ResourcesRequired(new Shield()),3,new Coin());
        leaderCard1 = new NewDiscount(requirements1,2,new Stone());
        leaderCard2 = new NewWhiteMarble(requirements2,5,new Coin());
        leaderCard3 = new NewProduction(new LevelRequired(CardColor.GREEN),4,new Coin());
        assertNotEquals(leaderCard, leaderCard1);
        assertNotEquals(leaderCard1, leaderCard2);
        assertNotEquals(leaderCard2, leaderCard3);
    }


    /**
     * this test verify that the method setActive set
     * correctly a leader card from inactive to active
     */
    @Test
    void testSetActive(){
        leaderCard = new NewProduction(null, 0, null);
        assertFalse(leaderCard.isActive());
        leaderCard.setActive();
        assertTrue(leaderCard.isActive());
    }

    /**
     * this test activate two new white marble leader cards
     * checking if they can be actually activated correctly
     * and verify that the marbles are correctly set in the
     * player
     */
    @Test
    void testActivateWhiteMarbleCard(){
        List<CardColor> color1 = new ArrayList<>(Collections.singletonList(CardColor.GREEN));
        List<CardColor> color2 = new ArrayList<>(Collections.singletonList(CardColor.BLUE));
        DevelopmentCard card1 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.BLUE, 2,0,null,null,null,0);
        leaderCard1 = new NewWhiteMarble(new ColorRequired(color1), 0, new Coin());
        leaderCard2 = new NewWhiteMarble(new ColorRequired(color2), 0, new Servant());
        player = new RealPlayer("player", new ArrayList<>(Arrays.asList(leaderCard1, leaderCard2)));
        assertFalse(leaderCard1.activateCard(player));
        player.locateDevelopmentCard(card1, 1);
        leaderCard1.activateCard(player);
        assertTrue(leaderCard1.isActive());
        assertEquals(player.getLeaderWhiteMarble(1).convert(), leaderCard1.getResource());
        assertFalse(leaderCard2.activateCard(player));
        player.locateDevelopmentCard(card2, 1);
        leaderCard2.activateCard(player);
        assertTrue(leaderCard2.isActive());
        assertEquals(player.getLeaderWhiteMarble(1).convert(), leaderCard1.getResource());
        assertEquals(player.getLeaderWhiteMarble(2).convert(), leaderCard2.getResource());
    }


    /**
     * this test activate two new shelf leader cards
     * and verify that the new shelfs got created correctly
     * checking if they can be actually activated
     * then verify that the leader shelfs are available for
     * insert of resources
     */
    @Test
    void testActivateShelfCard(){
        List<CardColor> color1 = new ArrayList<>(Collections.singletonList(CardColor.GREEN));
        List<CardColor> color2 = new ArrayList<>(Collections.singletonList(CardColor.BLUE));
        DevelopmentCard card1 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.BLUE, 2,0,null,null,null,0);
        leaderCard1 = new NewShelf(new ColorRequired(color1), 0, new Coin());
        assertFalse(leaderCard1.activateCard(player));
        player.locateDevelopmentCard(card1, 1);
        leaderCard1.activateCard(player);
        assertTrue(leaderCard1.isActive());
        assertEquals(player.getPersonalDashboard().getPersonalWarehouse().getShelf(4).getResourceType(), leaderCard1.getResource().getType());
        CollectionResources coin = new ShelfCollection(ResourceType.COIN);
        coin.add(new Coin());
        assertEquals(0, player.addResourcesToWarehouse(coin, 4));
        leaderCard2 = new NewShelf(new ColorRequired(color2), 0, new Servant());
        assertFalse(leaderCard2.activateCard(player));
        player.locateDevelopmentCard(card2, 1);
        leaderCard2.activateCard(player);
        assertTrue(leaderCard2.isActive());
        assertEquals(player.getPersonalDashboard().getPersonalWarehouse().getShelf(4).getResourceType(), leaderCard1.getResource().getType());
        assertEquals(player.getPersonalDashboard().getPersonalWarehouse().getShelf(5).getResourceType(), leaderCard2.getResource().getType());
        CollectionResources servant = new ShelfCollection(ResourceType.SERVANT);
        servant.add(new Servant());
        assertEquals(0, player.addResourcesToWarehouse(servant, 5));
    }

    /**
     *this test is used to activate discount leader card and
     * to check if the discounted resource is equal
     * to the resource contained in the leader card
     * the test also verify if the cards can be activated
     */
    @Test
    void testActiveDiscountCard(){
        List<CardColor> color1 = new ArrayList<>(Collections.singletonList(CardColor.GREEN));
        List<CardColor> color2 = new ArrayList<>(Collections.singletonList(CardColor.BLUE));
        DevelopmentCard card1 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.BLUE, 2,0,null,null,null,0);
        leaderCard1 = new NewDiscount(new ColorRequired(color1), 0, new Coin());
        leaderCard2 = new NewDiscount(new ColorRequired(color2), 0, new Shield());
        assertFalse(leaderCard1.activateCard(player));
        player.locateDevelopmentCard(card1, 1);
        leaderCard1.activateCard(player);
        assertTrue(leaderCard1.isActive());
        assertFalse(leaderCard2.isActive());
        CollectionResources toVerify = new CollectionResources();
        toVerify.add(new Coin());
        assertEquals(toVerify, player.getPersonalDashboard().getDiscount());
        assertFalse(leaderCard2.activateCard(player));
        player.locateDevelopmentCard(card2, 1);
        leaderCard2.activateCard(player);
        assertTrue(leaderCard1.isActive());
        assertTrue(leaderCard2.isActive());
        toVerify.add(new Shield());
        assertEquals(toVerify, player.getPersonalDashboard().getDiscount());
    }

    /**

     *this test is used to activate newProduction leader card
     * and to check if the created resource is equal
     * to the resource contained in the leader card
     * the test also verify if the cards can be activated
     */
    @Test
    void testActivateProductionCard() {
        requirements1 = new ColorRequired(new ArrayList<>(Collections.singletonList(CardColor.BLUE)));
        requirements2 = new ColorRequired(new ArrayList<>(Collections.singletonList(CardColor.GREEN)));
        leaderCard1 = new NewProduction(requirements1, 2, new Shield());
        leaderCard2 = new NewProduction(requirements2, 2, new Stone());
        DevelopmentCard card1 = new DevelopmentCard(CardColor.BLUE, 1, 0, null, null, null, 0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.GREEN, 2, 0, null, null, null, 0);
        assertFalse(leaderCard1.activateCard(player));
        assertFalse(leaderCard2.activateCard(player));
        assertFalse(leaderCard1.isActive());
        assertFalse(leaderCard1.isActive());
        player.locateDevelopmentCard(card1, 3);
        assertTrue(leaderCard1.activateCard(player));
        assertTrue(leaderCard1.isActive());
        assertFalse(leaderCard2.activateCard(player));
        assertFalse(leaderCard2.isActive());
        assertNull(player.getPersonalDashboard().getPersonalProductionPower().getInput(2));
        assertEquals(player.getPersonalDashboard().getPersonalProductionPower().getInput(1), leaderCard1.getResource());
        player.locateDevelopmentCard(card2, 3);
        assertTrue(leaderCard1.isActive());
        assertTrue(leaderCard2.activateCard(player));
        assertTrue(leaderCard2.isActive());
        assertEquals(leaderCard2.getResource(), player.getPersonalDashboard().getPersonalProductionPower().getInput(2));

    }
}

