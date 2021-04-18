package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.*;
import it.polimi.ingsw.model.Marble.GreyMarble;
import it.polimi.ingsw.model.Marble.YellowMarble;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RealPlayerTest {

    List<LeaderCard> leaderCards = new ArrayList<>();
    RealPlayer player;

    /**
     * this test verifies the correct initialization of the Track through the RealPlayer: position = 0, none of the
     * popeFavorTile is active neither can be activated
     */
    @Test
    void testGetPersonalTrack1() {
        int i;
        player = new RealPlayer("pippo", leaderCards);
        assertEquals(0,player.getPersonalTrack().getPosition());
        for (i=1; i<=3; i++) {
            assertFalse(player.getPersonalTrack().checkFavorTile(i));
            assertFalse(player.getPersonalTrack().getPopeFavorTiles(i).getActive());
        }
    }

    /**
     * this test verifies the correct function of the track after a progress
     */
    @Test
    void testGetPersonalTrack2 () {
        player = new RealPlayer("pippo", leaderCards);
        player.getPersonalTrack().positionProgress(12);
        assertEquals(12, player.getPersonalTrack().getPosition());
        assertTrue(player.getPersonalTrack().checkFavorTile(1));
        assertFalse(player.getPersonalTrack().checkFavorTile(2));
        assertFalse(player.getPersonalTrack().checkFavorTile(3));
    }

    /**
     * this test verifies that the constructor instantiates a empty List of whiteMarbles
     */
    @Test
    void testGetLeaderWhiteMarbles1 () {
        player = new RealPlayer("pippo", leaderCards);
        assertTrue(player.getLeaderWhiteMarbles().isEmpty());

    }

    /**
     *this test verifies that when a Resource associated to leaderWhiteMarble is a input of addLeaderWhiteMarble method
     * the arrayList leaderWhiteMarbles contains the right marble associated to the resource
     */
    @Test
    void testGetLeaderWhiteMarbles2 () {
        player = new RealPlayer("pippo", leaderCards);
        player.addLeaderWhiteMarble(new Coin());
        assertEquals(new YellowMarble(), player.getLeaderWhiteMarble(1));
    }

    /**
     *this test verifies that the method activateProduction called with empty CollectionResources in input doesn't
     * change the state of the Strongbox and of the Warehouse
     */
    @Test
    void testActivateProduction1 () {
        player = new RealPlayer("pippo", leaderCards);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources empty = new ShelfCollection(ResourceType.COIN);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        coins.add(new Coin());
        coins.add(new Coin());
        shields.add(new Shield());
        shields.add(new Shield());
        player.getPersonalDashboard().addToBuffer(coins);
        player.fillStrongboxWithBuffer();
        player.addResourcesToWarehouse(coins,3);
        player.addResourcesToWarehouse(shields, 2);
        player.activateProduction(empty,empty,empty);
        assertEquals(shields, player.getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources());
        assertEquals(coins, player.getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources());
        assertEquals(coins, player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources());

    }

    /**
     *this test verifies that the method activateProduction return false if the input CollectionResources
     * toPayFromWarehouse contains a CollectionResources not effectively contained in Warehouse. The state of the
     * Warehouse and of the Strongbox is unchanged
     */
    @Test
    void testActivateProduction2 () {
        player = new RealPlayer("pippo", leaderCards);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        coins.add(new Coin());
        coins.add(new Coin());
        shields.add(new Shield());
        shields.add(new Shield());
        player.getPersonalDashboard().addToBuffer(coins);
        player.fillStrongboxWithBuffer();
        player.addResourcesToWarehouse(coins,3);
        assertFalse(player.activateProduction(shields, coins, coins));
        assertEquals(coins, player.getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources());
        assertEquals(coins, player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources());

    }

    /**
     *this test verifies that the method activateProduction return false if the input CollectionResources
     *toPayFromStrongbox contains a CollectionResources not effectively contained in Strongbox. The state of the
     *Warehouse and of the Strongbox is unchanged
     */
    @Test
    void testActivateProduction3 () {
        player = new RealPlayer("pippo", leaderCards);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        coins.add(new Coin());
        coins.add(new Coin());
        shields.add(new Shield());
        shields.add(new Shield());
        player.getPersonalDashboard().addToBuffer(coins);
        player.fillStrongboxWithBuffer();
        player.addResourcesToWarehouse(coins,3);
        assertFalse(player.activateProduction(coins, shields, coins));
        assertEquals(coins, player.getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources());
        assertEquals(coins, player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources());
    }

    /**
     *this test verifies the correct behaviour of activateProduction method when the inputs toPayFromWarehouse and
     * toPayFromStrongbox contain resources effectively in warehouse or strongbox: these resources are removed.
     */
    @Test
    void testActivateProduction4 () {
        player = new RealPlayer("pippo", leaderCards);
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        CollectionResources empty = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin());
        shields.add(new Shield());
        shields.add(new Shield());
        player.getPersonalDashboard().addToBuffer(coins);
        player.fillStrongboxWithBuffer();
        player.addResourcesToWarehouse(coins,3);
        assertTrue(player.activateProduction(coins, coins, coins));
        assertEquals(empty, player.getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources());
        assertEquals(empty, player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources());
    }

    /**
     *this test verifies that the addDiscount method instantiate a discounted dashboard that effectively has the input
     * resource of the method as discounted resource
     */
    @Test
    void testAddDiscount1 () {
        int i;
        player = new RealPlayer("pippo", leaderCards);
        player.addDiscount(new Coin());
        player.addDiscount(new Shield());
        assertTrue(player.getPersonalDashboard().getDiscount().contains(new Coin()));
        assertTrue(player.getPersonalDashboard().getDiscount().contains(new Shield()));
        CollectionResources empty = new ShelfCollection(ResourceType.COIN);
        assertEquals(empty, player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources());
        for(i=1; i<=3; i++)
            assertEquals(empty, player.getPersonalDashboard().getPersonalWarehouse().getShelf(i).getResources());
    }

    /**
     *this test verifies that when a player is instantiated then it has 0 victoryPoints
     */
    @Test
    void testGetVictoryPoints1 () {
        player = new RealPlayer("pippo", leaderCards);
        assertEquals(0, player.getVictoryPoints());
    }

    /**
     *this test verifies the correct accumulation of victoryPoints through leader cards (every activated leaderCArd
     * has victory points), position in track manager (each position matches a number of victory points) and resources in
     * dashboard (every 5 resources match 1 victory points)
     */
    @Test
    void testGetVictoryPoints2 () {
        LeaderCardRequirements requirements = new LevelRequired(CardColor.YELLOW);
        leaderCards.add(new NewDiscount(requirements, 3, new Coin()));
        leaderCards.add(new NewDiscount(requirements, 4, new Coin()));
        player = new RealPlayer("pippo", leaderCards);
        player.activateLeaderCard(1); //0pv player does not meet the requirements
        player.getPersonalTrack().positionProgress(6); // 2pv
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin());
        coins.add(new Coin());
        coins.add(new Coin());
        coins.add(new Coin());
        coins.add(new Coin());
        player.getPersonalDashboard().addToBuffer(coins);
        player.fillStrongboxWithBuffer();//1pv
        assertEquals(3, player.getVictoryPoints());
        DevelopmentCard card1 = new DevelopmentCard(CardColor.GREEN, 1,0,null,null,null,0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.YELLOW, 2,0,null,null,null,0);
        player.locateDevelopmentCard(card1, 1);
        player.locateDevelopmentCard(card2, 1);
        player.activateLeaderCard(1); //3pv
        assertEquals(6, player.getVictoryPoints());
    }


    /**
     *this test verifies that the method addFaithPoints works correctly: it check that after the call of the method the
     * position in TrackManager is increased of the input of addFaithPoints
     */
    @Test
    void testAddFaithPoints () {
        player = new RealPlayer("pippo", leaderCards);
        player.addFaithPoints(6);
        assertEquals(6, player.getPersonalTrack().getPosition());
    }

    /**
     *this test verifies the correct behaviour of checkVaticanReport method: it returns true only when the position in
     * trackManager has passed or is in the last position of the i-th popeFavorTile where i is the input of
     * checkVaticanReport
     */
    @Test
    void testCheckVaticanReport () {
        player = new RealPlayer("pippo", leaderCards);
        assertFalse(player.checkVaticanReport(1));
        player.getPersonalTrack().positionProgress(10);
        assertTrue(player.checkVaticanReport(1));
        assertFalse(player.checkVaticanReport(2));
        assertFalse(player.checkVaticanReport(3));
    }

    /**
     *this test verifies that when vaticanReport is called and the position in TrackManager is inside the PopeFavorTile
     * positions referred to the input, then this popeFavorTile is active in the RealPlayer
     */
    @Test
    void testVaticanReport () {
        player = new RealPlayer("pippo", leaderCards);
        player.getPersonalTrack().positionProgress(13);
        player.vaticanReport(2);
        assertTrue(player.getPersonalTrack().getPopeFavorTiles(2).getActive());
    }
    /**
     * this test verifies that when the Dashboard of the player doesn't contain the requirement of a leaderCard the
     * method activateLeaderCard returns false
     */
    @Test
    void testActivateLeaderCard1() {
        leaderCards.add(new NewWhiteMarble(new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.YELLOW))),2, new Coin()));
        leaderCards.add(new NewWhiteMarble(new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.YELLOW))),2, new Stone()));
        player = new RealPlayer("pippo", leaderCards);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.BLUE
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.PURPLE
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        assertFalse(player.activateLeaderCard(1));
        assertFalse(player.activateLeaderCard(2));
        assertTrue(player.getLeaderWhiteMarbles().isEmpty());
    }

    /**
     * this test verifies that when the Dashboard of the player contains the colorRequirement of the LeaderCard owned
     * then the LeaderCard can be activated and we can observe what changes: in this case we add a new marble at the
     * list WhiteMarbles in player
     */
    @Test
    void testActivateLeaderCard2 () {
        leaderCards.add(new NewWhiteMarble(new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.GREEN, CardColor.YELLOW))),2, new Coin()));
        leaderCards.add(new NewWhiteMarble(new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.YELLOW))),2, new Stone()));
        player = new RealPlayer("pippo", leaderCards);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.BLUE
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.YELLOW
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        assertTrue(player.activateLeaderCard(1));
        assertEquals(player.getLeaderWhiteMarble(1), new YellowMarble());
        assertTrue(player.activateLeaderCard(2));
        assertEquals(player.getLeaderWhiteMarble(2), new GreyMarble());
    }

    /**
     *this test verifies that a leaderCard NewDiscount with a ResourceRequirement can be activated only when the
     * resources required are inside strongbox or warehouse, in this case the resource in the leaderCard is added to
     * DiscountedResources list and the state of warehouse and strongbox is unchanged
     */
    @Test
    void testActivateLeaderCard3() {
        leaderCards.add(new NewDiscount(new ResourcesRequired(new Coin()), 2, new Stone()));
        leaderCards.add(new NewDiscount(new ResourcesRequired(new Stone()), 2, new Shield()));
        player = new RealPlayer("pippo", leaderCards);
        CollectionResources stones = new ShelfCollection(ResourceType.STONE);
        stones.add(new Stone());
        stones.add(new Stone());
        stones.add(new Stone());
        player.addResourcesToWarehouse(stones,3);
        player.getPersonalDashboard().addToBuffer(stones);
        player.fillStrongboxWithBuffer();
        CollectionResources coins = new ShelfCollection(ResourceType.COIN);
        coins.add(new Coin());
        coins.add(new Coin());
        CollectionResources shields = new ShelfCollection(ResourceType.SHIELD);
        shields.add(new Shield());
        player.addResourcesToWarehouse(coins,2);
        player.getPersonalDashboard().addToBuffer(coins);
        player.fillStrongboxWithBuffer();
        assertFalse(player.activateLeaderCard(1));
        assertTrue(player.getPersonalDashboard().getDiscount().getMaps().isEmpty());
        assertTrue(player.activateLeaderCard(2));
        assertEquals(player.getPersonalDashboard().getDiscount(), shields);
        // verifies the state of strongbox is unchanged
        assertTrue(player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources().containsAll(coins));
        assertTrue(player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources().containsAll(stones));
        // verifies the state of warehouse is unchanged
        assertEquals(player.getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources(), stones);
        assertEquals(player.getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources(), coins);
    }

    /**
     * this test verifies that a leaderCard NewShelf with a LevelRequirement can be activated only when the card of
     * level 2 of the color of the requirement is locate in the dashboard, in this case a new Shelf is added to the
     * warehouse (index 4)
     */
    @Test
    void testActivateLeaderCard4() {
        leaderCards.add(new NewShelf(new LevelRequired(CardColor.BLUE),2, new Servant()));
        leaderCards.add(new NewShelf(new LevelRequired(CardColor.PURPLE),2, new Coin()));
        player = new RealPlayer("pippo", leaderCards);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.BLUE
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.YELLOW
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        assertTrue(player.activateLeaderCard(1));
        assertNotNull(player.getPersonalDashboard().getPersonalWarehouse().getShelf(4));
        assertFalse(player.activateLeaderCard(2));
    }
    /**
     *this test verifies that a leaderCard NewShelf with a LevelRequirement can be activated only when the card of level 2
     * of the color of the requirement is locate in the dashboard, in this case a new Shelf is added to the warehouse.
     * This test activated 2 leaderCard NewShelf and then verify the existence of the shelf 4 and 5 and it verifies their
     * types
     */
    @Test
    void testActivateLeaderCard5() {
        leaderCards.add(new NewShelf(new LevelRequired(CardColor.BLUE),2, new Servant()));
        leaderCards.add(new NewShelf(new LevelRequired(CardColor.YELLOW),2, new Coin()));
        player = new RealPlayer("pippo", leaderCards);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.BLUE
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.YELLOW
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        assertTrue(player.activateLeaderCard(1));
        assertNotNull(player.getPersonalDashboard().getPersonalWarehouse().getShelf(4));
        assertEquals(player.getPersonalDashboard().getPersonalWarehouse().getShelf(4).getResourceType(), ResourceType.SERVANT);
        assertTrue(player.activateLeaderCard(2));
        assertNotNull(player.getPersonalDashboard().getPersonalWarehouse().getShelf(5));
        assertEquals(player.getPersonalDashboard().getPersonalWarehouse().getShelf(5).getResourceType(), ResourceType.COIN);

    }
    /**
     *this test verifies that a leaderCard NewProduction with a LevelRequirement can be activated only when the card of
     * level 2 of the color of the requirement is locate in the dashboard, in this case a new input is added to a list of
     * leaderProduction
     */
    @Test
    void testActivateLeaderCard6() {
        leaderCards.add(new NewProduction(new LevelRequired(CardColor.BLUE),3,new Shield()));
        leaderCards.add(new NewProduction(new LevelRequired(CardColor.GREEN),3,new Stone()));
        player = new RealPlayer("pippo", leaderCards);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.BLUE
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.YELLOW
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        assertNull(player.getPersonalDashboard().getPersonalProductionPower().getInput(1));
        assertTrue(player.activateLeaderCard(1));
        assertEquals(player.getPersonalDashboard().getPersonalProductionPower().getInput(1), new Shield());
        assertFalse(player.activateLeaderCard(2));

    }
    /**
     *this test verifies that a leaderCard can be discarded only if it isn't active, in this case if the discardLeaderCard
     * method is called then return true
     */
    @Test
    void testDiscardLeaderCard() {
        leaderCards.add(new NewShelf(new LevelRequired(CardColor.BLUE),2, new Servant()));
        leaderCards.add(new NewShelf(new LevelRequired(CardColor.YELLOW),2, new Coin()));
        player = new RealPlayer("pippo", leaderCards);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.BLUE
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        player.locateDevelopmentCard(new DevelopmentCard(
                CardColor.YELLOW
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        player.activateLeaderCard(1);
        assertFalse(player.discardLeaderCard(1));
        assertEquals(0, player.getPersonalTrack().getPosition());
        assertTrue(player.discardLeaderCard(2));

    }

}
