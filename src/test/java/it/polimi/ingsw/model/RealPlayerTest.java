package it.polimi.ingsw.model;

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
}
