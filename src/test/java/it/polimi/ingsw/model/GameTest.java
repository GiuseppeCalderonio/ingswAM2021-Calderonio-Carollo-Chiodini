package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.CardsMarket;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.LeaderCard.*;
import it.polimi.ingsw.model.Marble.*;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the game
 */
class GameTest {

    Game game;

    /**
     * this test verify that every player got created with the associated string
     */
    @Test
    void testConstructor1() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        assertEquals(nicknames, game.getPlayers().stream().map(RealPlayer::getNickname).collect(Collectors.toList()));
    }

    /**
     * this test verify that every player has the correct leader cards
     * creating two arraylists, one with all the leader cards of the game, and
     * one with all the leader cards of every player, and matching them
     */
    @Test
    void testConstructor2() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames); // create a mew game
        //create the leader cards

        List<LeaderCard> leaders = new ArrayList<>(); // create an arraylist of leaderCard
        LeaderCardRequirements requirements1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.BLUE, CardColor.YELLOW)));
        LeaderCardRequirements requirements2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.PURPLE, CardColor.PURPLE, CardColor.GREEN)));
        LeaderCardRequirements requirements3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.GREEN, CardColor.PURPLE)));
        LeaderCardRequirements requirements4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.YELLOW, CardColor.BLUE)));

        //NewWhiteMarble leaderCards
        leaders.add(new NewWhiteMarble(requirements2, 5, new Coin()));
        leaders.add(new NewWhiteMarble(requirements1, 5, new Stone()));
        leaders.add(new NewWhiteMarble(requirements4, 5, new Servant()));
        leaders.add(new NewWhiteMarble(requirements3, 5, new Shield())); //NewWhiteMarble leaderCard
        //NewShelf leaderCards
        leaders.add(new NewShelf(new ResourcesRequired(new Shield()), 3, new Coin()));
        leaders.add(new NewShelf(new ResourcesRequired(new Servant()), 3, new Shield()));
        leaders.add(new NewShelf(new ResourcesRequired(new Stone()), 3, new Servant()));
        leaders.add(new NewShelf(new ResourcesRequired(new Coin()), 3, new Stone())); //NewShelf leaderCard
        //NewProduction leaderCards
        leaders.add(new NewProduction(new LevelRequired(CardColor.GREEN), 4, new Coin()));
        leaders.add(new NewProduction(new LevelRequired(CardColor.PURPLE), 4, new Stone()));
        leaders.add(new NewProduction(new LevelRequired(CardColor.BLUE), 4, new Servant()));
        leaders.add(new NewProduction(new LevelRequired(CardColor.YELLOW), 4, new Shield())); //NewProduction leaderCard

        LeaderCardRequirements requirement1 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.PURPLE)));
        LeaderCardRequirements requirement2 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.BLUE)));
        LeaderCardRequirements requirement3 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.PURPLE)));
        LeaderCardRequirements requirement4 = new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.YELLOW, CardColor.GREEN)));

        //NewDiscount leaderCards
        leaders.add(new NewDiscount(requirement1, 2, new Coin()));
        leaders.add(new NewDiscount(requirement2, 2, new Stone()));
        leaders.add(new NewDiscount(requirement3, 2, new Shield()));
        leaders.add(new NewDiscount(requirement4, 2, new Servant())); //NewDiscount leaderCard

        //create the arraylist that contains all the leader cards of every player

        List<LeaderCard> playerLeaderCards = new ArrayList<>();
        game.getPlayers().
                forEach(realPlayer -> playerLeaderCards.addAll(realPlayer.getPersonalLeaderCards()));

        assertTrue(leaders.containsAll(playerLeaderCards));
        assertTrue(playerLeaderCards.containsAll(leaders)); //all the leader cards must be equal to the leader cards of every player
    }

    /**
     * this method verify that all the vatican reports are false
     */
    @Test
    void testVaticanReport() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        assertFalse(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
    }

    /**
     * this test check that the method checkInitialising
     * accept only empty CollectionResources in input
     * when passed the first player nickname as parameter
     */
    @Test
    void testCheckInitialising1() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        assertTrue(game.checkInitialising("first", toInitialise));
        toInitialise.add(new Coin());
        assertFalse(game.checkInitialising("first", toInitialise));
    }

    /**
     * this test check that the method checkInitialising
     * accept only a CollectionResource with 1 resource in input
     * when passed the second player nickname as parameter
     */
    @Test
    void testCheckInitialising2() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        String secondPlayer = game.getPlayers().get(1).getNickname();
        assertFalse(game.checkInitialising(secondPlayer, toInitialise));
        toInitialise.add(new Coin());
        assertTrue(game.checkInitialising(secondPlayer, toInitialise));
        toInitialise.add(new Servant());
        assertFalse(game.checkInitialising(secondPlayer, toInitialise));
        toInitialise.remove(new Servant());
        toInitialise.add(new Coin());
        assertFalse(game.checkInitialising(secondPlayer, toInitialise));
    }

    /**
     * this test check that the method checkInitialising
     * accept only a CollectionResource with 1 resource in input
     * when passed the third player nickname as parameter
     */
    @Test
    void testCheckInitialising3() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        String thirdPlayer = game.getPlayers().get(2).getNickname();
        assertFalse(game.checkInitialising(thirdPlayer, toInitialise));
        toInitialise.add(new Coin());
        assertTrue(game.checkInitialising(thirdPlayer, toInitialise));
        toInitialise.add(new Servant());
        assertFalse(game.checkInitialising(thirdPlayer, toInitialise));
        toInitialise.remove(new Servant());
        toInitialise.add(new Coin());
        assertFalse(game.checkInitialising(thirdPlayer, toInitialise));
    }

    /**
     * this test check that the method checkInitialising
     * accept only a CollectionResource with 2 resources in input
     * when passed the fourth player nickname as parameter
     */
    @Test
    void testCheckInitialising4() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        String fourthPlayer = game.getPlayers().get(3).getNickname();
        assertFalse(game.checkInitialising(fourthPlayer, toInitialise));
        toInitialise.add(new Coin());
        assertFalse(game.checkInitialising(fourthPlayer, toInitialise));
        toInitialise.add(new Servant());
        assertTrue(game.checkInitialising(fourthPlayer, toInitialise));
        toInitialise.remove(new Servant());
        toInitialise.add(new Coin());
        assertTrue(game.checkInitialising(fourthPlayer, toInitialise));
        toInitialise.add(new Coin());
        assertFalse(game.checkInitialising(fourthPlayer, toInitialise));
    }

    /**
     * this test verify that the initialising do nt accept nicknames
     * in input not contained into the arraylist of players
     */
    @Test
    void testCheckInitialising5() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        assertFalse(game.checkInitialising("not", toInitialise));
        assertFalse(game.checkInitialising("SecoNd", toInitialise));
        String firstPlayer = game.getPlayers().get(0).getNickname();
        assertTrue(game.checkInitialising(firstPlayer, toInitialise));
    }

    /**
     * this test initialise a game of 4 players and verify if the game
     * assign correctly the resources chosen, when the fourth player
     * choose 2 different resources
     */
    @Test
    void testInitialiseGame1() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 2, 1);
        assertEquals(toInitialise, game.getPlayers().get(0).getTotalResources());
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 4, 3);
        assertEquals(toInitialise, game.getPlayers().get(1).getTotalResources());
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 3, 2);
        assertEquals(toInitialise, game.getPlayers().get(2).getTotalResources());
        toInitialise.add(new Stone());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 4);
        assertEquals(toInitialise, game.getPlayers().get(3).getTotalResources());
    }

    /**
     * this test initialise a game of 4 players and verify if the game
     * assign correctly the resources chosen, when the fourth player
     * choose the same resource
     */
    @Test
    void testInitialiseGame2() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 2, 1);
        assertEquals(toInitialise, game.getPlayers().get(0).getTotalResources());
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 4, 3);
        assertEquals(toInitialise, game.getPlayers().get(1).getTotalResources());
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 3, 2);
        assertEquals(toInitialise, game.getPlayers().get(2).getTotalResources());
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 4);
        assertEquals(toInitialise, game.getPlayers().get(3).getTotalResources());
    }

    /**
     * this test get a copy of the leader cards, remove them, then
     * remove the same leader cards with the initialization and verify
     * that the copy is equal with the leader card of the player after
     * the initialization
     */
    @Test
    void testInitialiseGame3() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        List<LeaderCard> cards = new ArrayList<>(game.getPlayers().get(0).getPersonalLeaderCards());
        cards.remove(3);
        cards.remove(1);
        game.initialiseGame("first", new CollectionResources(), 2, 4);
        assertEquals(cards, game.getPlayers().get(0).getPersonalLeaderCards());
    }

    @Test
    void testInitialiseGame4() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);

        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(0, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(1, game.getPlayers().get(2).getPersonalTrack().getPosition());
        assertEquals(1, game.getPlayers().get(3).getPersonalTrack().getPosition());
    }

    /**
     * this method verify that the increment of the turn works correctly
     */
    @Test
    public void TestCorrectTurn() {
        // initialise the game
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second"));
        game = new Game(nicknames);
        assertEquals(-2, game.getTurnManager());
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 2);
        assertEquals(-1, game.getTurnManager());
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 3, 1);
        // verify that incrementing the turn the actual players update correctly and the turnManager increase as well
        assertEquals(0, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(0), game.getActualPlayer());
        game.endTurn();
        assertEquals(1, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(1), game.getActualPlayer());
        game.endTurn();
        assertEquals(0, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(0), game.getActualPlayer());
        game.endTurn();
        assertEquals(1, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(1), game.getActualPlayer());
        game.endTurn();
        assertEquals(0, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(0), game.getActualPlayer());
    }

    /**
     * this method check if the player select the correct number of shelf
     */
    @Test
    public void TestShelfSelected() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 2);
        assertTrue(game.checkShelfSelected(1));
        assertTrue(game.checkShelfSelected(2));
        assertTrue(game.checkShelfSelected(3));
        assertFalse(game.checkShelfSelected(0));
        assertFalse(game.checkShelfSelected(-1));
        assertFalse(game.checkShelfSelected(4));
        assertEquals(3, game.getPlayers().get(0).getPersonalDashboard().getPersonalWarehouse().getNumOfShelves());
        game.getActualPlayer().addLeaderShelf(new Coin());
        assertTrue(game.checkShelfSelected(2));
        assertTrue(game.checkShelfSelected(4));
        assertFalse(game.checkShelfSelected(5));
        assertEquals(4, game.getPlayers().get(0).getPersonalDashboard().getPersonalWarehouse().getNumOfShelves());
        game.getActualPlayer().addLeaderShelf(new Shield());
        assertTrue(game.checkShelfSelected(5));
        assertFalse(game.checkShelfSelected(6));
        assertEquals(5, game.getPlayers().get(0).getPersonalDashboard().getPersonalWarehouse().getNumOfShelves());
        game.getActualPlayer().addLeaderShelf(new Servant());
        assertEquals(5, game.getPlayers().get(0).getPersonalDashboard().getPersonalWarehouse().getNumOfShelves());

        assertTrue(game.checkShelfSelected(2));
    }

    /**
     * this method create a game with only one player, and check the shift resource without the loose of resource
     */
    @Test
    public void TestShiftResources1() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());
        collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SHIELD);
        collection1.add(new Shield());
        collection1.add(new Shield());
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 2);
        game.getActualPlayer().addResourcesToWarehouse(collection, 2);
        game.getActualPlayer().addResourcesToWarehouse(collection1, 3);
        game.shiftResources(3, 2);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources(), collection1);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources(), collection);

    }

    /**
     * this method create a game with only one player and check if the shift between a normal shelf and
     * leaderWarehouse shelf is correct
     */
    @Test
    public void TestShiftResources2() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());
        collection.add(new Coin());
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 2);
        game.getActualPlayer().addResourcesToWarehouse(collection, 3);
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addResourcesToWarehouse(collection, 4);
        game.shiftResources(4, 3);
        toInitialise.add(new Coin());
        collection.add(new Coin());
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(4).getResources(), toInitialise);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources(), collection);
    }

    /**
     * this method create a game with only one player and check if the shift between a normal shelf and
     * leaderWarehouse shelf is correct
     */
    @Test
    public void TestShiftResources3() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());
        collection.add(new Coin());
        CollectionResources toInitialise2 = new CollectionResources();
        toInitialise2.add(new Coin());
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 2);
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addResourcesToWarehouse(collection, 4);
        game.shiftResources(4, 2);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(4).getResources(), toInitialise2);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources(), toInitialise2);
        game.shiftResources(4, 2);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(4).getResources(), toInitialise);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources(), collection);
    }

    /**
     * this method create a game with three players and check if the shift between a normal shelf and
     * leaderWarehouse shelf is not correct. we don't eliminate the resource so we don't have to add
     * faithPoints to other players
     */
    @Test
    public void TestShiftResources4() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());
        collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SHIELD);
        collection1.add(new Shield());
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addResourcesToWarehouse(collection, 4);
        game.getActualPlayer().addResourcesToWarehouse(collection1, 1);
        game.shiftResources(4, 2);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(0, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(1, game.getPlayers().get(2).getPersonalTrack().getPosition());
        game.shiftResources(4, 1);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(0, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(1, game.getPlayers().get(2).getPersonalTrack().getPosition());
    }

    /**
     * this method create a game with three players and check if the shift between two normal shelves is not correct.
     * we eliminate the resources and have to add faithPoints to other players
     */
    @Test
    public void TestShiftResources5() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());
        collection.add(new Coin());
        collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SHIELD);
        collection1.add(new Shield());
        collection1.add(new Shield());
        ShelfCollection collection2 = new ShelfCollection(ResourceType.SERVANT);
        collection2.add(new Servant());
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        game.getActualPlayer().addResourcesToWarehouse(collection, 3);
        game.getActualPlayer().addResourcesToWarehouse(collection1, 2);
        game.getActualPlayer().addResourcesToWarehouse(collection2, 1);
        game.shiftResources(3, 1);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(2, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(3, game.getPlayers().get(2).getPersonalTrack().getPosition());
        game.shiftResources(2, 1);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(3, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(4, game.getPlayers().get(2).getPersonalTrack().getPosition());
    }

    /**
     * this method create a game with three players and check if the shift between two normal shelves is not correct.
     * we eliminate the resources and have to add faithPoints to other players
     */
    @Test
    public void TestShiftResources6() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        CollectionResources toInitialise2 = new CollectionResources();
        CollectionResources toInitialise3 = new CollectionResources();


        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());

        ShelfCollection collection1 = new ShelfCollection(ResourceType.SHIELD);
        collection1.add(new Shield());
        collection1.add(new Shield());

        ShelfCollection collection2 = new ShelfCollection(ResourceType.SERVANT);
        collection2.add(new Servant());

        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        toInitialise2.add(new Servant());
        toInitialise3.add(new Shield());
        toInitialise3.add(new Shield());

        game.insertInWarehouse(3, new Coin(), toInitialise);
        game.insertInWarehouse(2, new Servant(), toInitialise2);
        game.insertInWarehouse(1, new Shield(), toInitialise3);

        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(1, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(2, game.getPlayers().get(2).getPersonalTrack().getPosition());
        game.shiftResources(3, 1);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(1, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(2, game.getPlayers().get(2).getPersonalTrack().getPosition());
        game.shiftResources(1, 2);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(1, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(2, game.getPlayers().get(2).getPersonalTrack().getPosition());
    }

    /**
     * This test check if the method addFaithPointsTo add faith points correctly to a player
     */
    @Test
    public void testAddFaithPointsTo1() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        game.addFaithPointsTo(game.getPlayers().get(0), 0);
        game.addFaithPointsTo(game.getPlayers().get(1), 7);
        game.addFaithPointsTo(game.getPlayers().get(2), 6);
        game.addFaithPointsTo(game.getPlayers().get(3), 25);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(7, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(7, game.getPlayers().get(2).getPersonalTrack().getPosition());
        assertEquals(24, game.getPlayers().get(3).getPersonalTrack().getPosition());
    }


    /**
     * this test create a game with one player, add to him a card with 2 faith points
     * as power output production, then adds to the player the necessary resources to
     * activate it, and verify that activating it adds to the player the 2 faith points
     */
    @Test
    void testAddFaithPointTo2() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);

        DevelopmentCard toActivate = new DevelopmentCard(CardColor.PURPLE
                , 1
                , 1
                , new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(), new Servant())))
                , new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                , new CollectionResources(new ArrayList<>())
                , 2); // create a card with level 1 and 2 faith points as output

        game.getActualPlayer().locateDevelopmentCard(toActivate, 1);
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().
                addResources(new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(), new Servant())))); // giving him enough resources to activate the production
        game.activateProduction(1, new CollectionResources()); // activate the production
        assertEquals(2, game.getPlayers().get(0).getPersonalTrack().getPosition()); // the player have 2 faith points

    }

    /**
     * this test check if we add faith points when a player reject one or two leader card
     */
    @Test
    public void testAddFaithPointsTo3A() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        game.discardLeaderCard(2);
        assertEquals(1, game.getPlayers().get(0).getPersonalTrack().getPosition());
        game.discardLeaderCard(1);
        assertEquals(2, game.getPlayers().get(0).getPersonalTrack().getPosition());
    }

    /**
     * this test check if we add faith points when a player reject one or two leader card
     */
    @Test
    public void testAddFaithPointsTo3b() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        game.activateLeaderCard(1);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        game.discardLeaderCard(2);
        assertEquals(1, game.getPlayers().get(0).getPersonalTrack().getPosition());
    }


    /**
     * this test create a game with 2 players, ad to one of them a
     * leader production, and verify that the player, when he activate it,
     * receive a faith point
     */
    @Test
    void testAddFaithPointsTo4() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3); // 0 faith points

        game.endTurn(); // the actualPlayer is the second one
        game.getActualPlayer().addLeaderProduction(new Servant()); //add a leader production
        game.activateLeaderProduction(1, new Coin(), false); // activate the first leader production gaining a coin as output and paying from strongbox

        assertEquals(1, game.getPlayers().get(1).getPersonalTrack().getPosition()); // the second player has 1 faith point
    }

    /**
     * this test create a game with only one player and then
     * make him select a red marble, finally verify that
     * the player gained a faith point
     */
    @Test
    void testAddFaithPointsTo5() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);

        List<Marble> marbles = new ArrayList<>(Arrays.asList(new RedMarble(), new WhiteMarble(), new BlueMarble(), new YellowMarble()));
        game.convert(marbles);
        assertEquals(1, game.getPlayers().get(0).getPersonalTrack().getPosition());
    }


    /**
     * this test check if handle vatican report is activated correctly, one player activate the pope favor tile and
     * no one except him is in the pope favor tile area
     */
    @Test
    public void TestHandleVaticanReport1() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        assertFalse(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(0), 10);
        assertEquals(10, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertTrue(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
    }

    /**
     * this test check if handle vatican report is activated correctly, one player activate the pope favor tile and
     * there is another player in the pope favor tile area. Then the first player go to the second vatican area and activates the
     * second pope favor tile
     */
    @Test
    public void TestHandleVaticanReport2() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        game.addFaithPointsTo(game.getPlayers().get(2), 2);
        game.addFaithPointsTo(game.getPlayers().get(3), 2);
        game.addFaithPointsTo(game.getPlayers().get(0), 6);
        game.addFaithPointsTo(game.getPlayers().get(1), 10);
        assertTrue(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(2), 2);
        game.addFaithPointsTo(game.getPlayers().get(3), 2);
        game.addFaithPointsTo(game.getPlayers().get(0), 6);
        game.addFaithPointsTo(game.getPlayers().get(1), 10);
        assertTrue(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(3).getActive());
        assertTrue(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertFalse(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(1).getActive());
    }

    /**
     * this test check if handle vatican report is activated correctly, one player activate the pope favor tile and
     * we check if the pope favor tile is not activated when another player reach the position of the first one
     */
    @Test
    public void TestHandleVaticanReport3() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        assertFalse(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(0), 4);
        game.addFaithPointsTo(game.getPlayers().get(1), 10);
        assertFalse(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(0), 7);
        assertFalse(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
    }

    /**
     * this test check if handle vatican report is activated correctly, in particular check what happens
     * if a vatican report is activated by two players simultaneously
     */
    @Test
    public void TestHandleVaticanReport4A() {
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());
        collection.add(new Coin());
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        game.addFaithPointsTo(game.getPlayers().get(1), 7);
        game.addFaithPointsTo(game.getPlayers().get(2), 6);
        game.addFaithPointsTo(game.getPlayers().get(3), 3);
        game.insertInWarehouse(1, new Coin(), collection);
        assertFalse(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
    }

    /**
     * this test check if handle vatican report is activated correctly, in particular check what happens
     * if a vatican report is activated by three players simultaneously
     */
    @Test
    public void TestHandleVaticanReport4B() {
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());
        collection.add(new Coin());
        collection.add(new Coin());
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        game.addFaithPointsTo(game.getPlayers().get(0), 4);
        game.addFaithPointsTo(game.getPlayers().get(1), 7);
        game.addFaithPointsTo(game.getPlayers().get(2), 6);
        game.addFaithPointsTo(game.getPlayers().get(3), 6);
        game.insertInWarehouse(1, new Coin(), collection);
        assertEquals(9, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(9, game.getPlayers().get(2).getPersonalTrack().getPosition());
        assertEquals(9, game.getPlayers().get(3).getPersonalTrack().getPosition());
        assertFalse(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
    }

    /**
     * this test check if all vatican reports are activated correctly
     */
    @Test
    public void TestHandleVaticanReport5() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.addFaithPointsTo(game.getPlayers().get(0), 7);
        assertFalse(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(0), 1);
        assertTrue(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);

        game.addFaithPointsTo(game.getPlayers().get(0), 7);
        assertFalse(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(0), 1);
        assertTrue(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertTrue(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);

        game.addFaithPointsTo(game.getPlayers().get(0), 7);
        assertFalse(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(3).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(3).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertTrue(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(0), 1);
        assertTrue(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(3).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(3).getActive());
        assertTrue(game.getVaticanReports()[0]);
        assertTrue(game.getVaticanReports()[1]);
        assertTrue(game.getVaticanReports()[2]);
    }

    /**
     * this test check if handle vatican report is activated correctly, one player activate all the pope favor tile, this player is followed
     * by another player and the pope favor tile are correctly activated for both player. All other players arent in the pope tile area
     */
    @Test
    public void TestHandleVaticanReport6() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        game.addFaithPointsTo(game.getPlayers().get(0), 5);
        game.addFaithPointsTo(game.getPlayers().get(1), 4);
        assertFalse(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(2), 8);
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        assertTrue(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertFalse(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(1).getActive());
        game.addFaithPointsTo(game.getPlayers().get(0), 7);
        game.addFaithPointsTo(game.getPlayers().get(1), 7);
        assertTrue(game.getVaticanReports()[0]);
        assertFalse(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(2), 8);
        assertTrue(game.getVaticanReports()[0]);
        assertTrue(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        assertTrue(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertTrue(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertFalse(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(2).getActive());
        game.addFaithPointsTo(game.getPlayers().get(0), 7);
        game.addFaithPointsTo(game.getPlayers().get(1), 7);
        assertTrue(game.getVaticanReports()[0]);
        assertTrue(game.getVaticanReports()[1]);
        assertFalse(game.getVaticanReports()[2]);
        game.addFaithPointsTo(game.getPlayers().get(2), 8);
        assertTrue(game.getVaticanReports()[0]);
        assertTrue(game.getVaticanReports()[1]);
        assertTrue(game.getVaticanReports()[2]);
        assertTrue(game.getPlayers().get(0).getPersonalTrack().getPopeFavorTiles(3).getActive());
        assertFalse(game.getPlayers().get(1).getPersonalTrack().getPopeFavorTiles(3).getActive());
        assertTrue(game.getPlayers().get(2).getPersonalTrack().getPopeFavorTiles(3).getActive());
        assertFalse(game.getPlayers().get(3).getPersonalTrack().getPopeFavorTiles(3).getActive());
    }


    /**
     * this test create a game with 3 players, call the method
     * addFaithPointsExceptTo and verify that all the players
     * except that one in the input got correctly the faith points
     */
    @Test
    void testAddFaithPointsExceptTo1() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        game.addFaithPointsExceptTo(game.getPlayers().get(0), 2);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(2, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(3, game.getPlayers().get(2).getPersonalTrack().getPosition());
    }

    /**
     * this test create a game with 4 players, call the method
     * addFaithPointsExceptTo and verify that all the players
     * except that one in the input got correctly the faith points
     */
    @Test
    void testAddFaithPointsExceptTo2() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        game.addFaithPointsExceptTo(game.getPlayers().get(2), 2);
        assertEquals(2, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(2, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(1, game.getPlayers().get(2).getPersonalTrack().getPosition());
        assertEquals(3, game.getPlayers().get(3).getPersonalTrack().getPosition());
    }

    /**
     * this test create a game with 4 players, the first one do a shift
     * losing 2 resources, and then verify that all the other players
     * progress of 2 positions in their personal faith track
     */
    @Test
    void testAddFaithPointsExceptTo3() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        CollectionResources toAdd = new CollectionResources();
        toAdd.add(new Coin());
        toAdd.add(new Stone());
        toAdd.add(new Stone());
        toAdd.add(new Stone()); // contains 1 coin and 3 stones
        game.insertInWarehouse(1, new Coin(), toAdd);
        game.insertInWarehouse(3, new Stone(), toAdd);
        game.shiftResources(1, 3);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition()); // first player position 0
        assertEquals(2, game.getPlayers().get(1).getPersonalTrack().getPosition()); // second player position 2
        assertEquals(3, game.getPlayers().get(2).getPersonalTrack().getPosition()); // third player position 3
        assertEquals(3, game.getPlayers().get(3).getPersonalTrack().getPosition()); // fourth player position 3
    }

    /**
     * this test create a game with 4 players,
     * the first one add 3 resources to his warehouse
     * losing 2 of them, and then verify that all the other players
     * progress of 2 positions in their personal faith track
     */
    @Test
    void testAddFaithPointsExceptTo4() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third", "fourth"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin());
        game.initialiseGame(game.getPlayers().get(3).getNickname(), toInitialise, 1, 3);
        CollectionResources toAdd = new CollectionResources();
        toAdd.add(new Stone());
        toAdd.add(new Stone());
        toAdd.add(new Stone()); // contains 3 stones
        game.insertInWarehouse(1, new Stone(), toAdd);
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition()); // first player position 0
        assertEquals(2, game.getPlayers().get(1).getPersonalTrack().getPosition()); // second player position 2
        assertEquals(3, game.getPlayers().get(2).getPersonalTrack().getPosition()); // third player position 3
        assertEquals(3, game.getPlayers().get(3).getPersonalTrack().getPosition()); // fourth player position 3
    }

    /**
     * this test verify that the method checkBuyCard return false when
     * the color is null and when the level is not between 1 and 3
     */
    @Test
    void testCheckBuyCard1() {
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);

        assertFalse(game.checkBuyCard(0, CardColor.BLUE));
        assertFalse(game.checkBuyCard(4, CardColor.BLUE));
        assertFalse(game.checkBuyCard(-1, CardColor.BLUE));
        assertFalse(game.checkBuyCard(1, null));
        assertFalse(game.checkBuyCard(2, null));
        assertFalse(game.checkBuyCard(3, null));
    }

    /**
     * this test create a game, empty a deck of cards in the cardsMarket
     * and then verify that the method checkBuyCard return false
     * when passed as input that specific deck
     * to be more safe, the method adds a lot of resources
     * to the player
     */
    @Test
    void testCheckBuyCard2() {
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        // empty the cardsMarket
        game.getSetOfCard().popCard(1, CardColor.BLUE);
        game.getSetOfCard().popCard(1, CardColor.BLUE);
        game.getSetOfCard().popCard(1, CardColor.BLUE);
        game.getSetOfCard().popCard(1, CardColor.BLUE);
        // add to the player a collectionResources big enough to pay every card of level 1
        CollectionResources toAdd = new CollectionResources();
        toAdd.add(new Coin());
        toAdd.add(new Coin());
        toAdd.add(new Coin());
        toAdd.add(new Coin());
        toAdd.add(new Stone());
        toAdd.add(new Stone());
        toAdd.add(new Stone());
        toAdd.add(new Stone());
        toAdd.add(new Shield());
        toAdd.add(new Shield());
        toAdd.add(new Shield());
        toAdd.add(new Shield());
        toAdd.add(new Servant());
        toAdd.add(new Servant());
        toAdd.add(new Servant());
        toAdd.add(new Servant());
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(toAdd);

        assertFalse(game.checkBuyCard(1, CardColor.BLUE));
    }

    /**
     * this test create a game with one player, then verify
     * that a player with no resources cannot buy
     * a card
     */
    @Test
    void testCheckBuyCard3() {
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);

        assertFalse(game.checkBuyCard(1, CardColor.PURPLE));
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, adds all
     * the resources to the strongbox and verify that
     * the card can be bought, then remove some resources
     * to the strongbox and verify that the card is not affordable anymore
     */
    @Test
    void testCheckBuyCard4() {
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add the card just created to the cards market
        CardsMarket cardsMarket = game.getSetOfCard();
        cardsMarket.popCard(1, CardColor.PURPLE);
        cardsMarket.getCardMatrix()[2][3].add(card);
        //adds the resources to the strongbox, with some more resource
        CollectionResources addInStrongbox = new CollectionResources(cost.asList());
        addInStrongbox.add(new Coin());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 3 servants, 3 coins, 4 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);

        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources().remove(new Coin());
        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources().remove(new Coin());
        assertFalse(game.checkBuyCard(1, CardColor.PURPLE));
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, adds all
     * the resources to the warehouse and verify that
     * the card can be bought, then remove some resources
     * to the warehouse and verify that the card is not affordable anymore
     */
    @Test
    void testCheckBuyCard5(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add the card just created to the cards market
        CardsMarket cardsMarket = game.getSetOfCard();
        cardsMarket.popCard(1, CardColor.PURPLE);
        cardsMarket.getCardMatrix()[2][3].add(card);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        // add some resources to warehouse
        CollectionResources addInWarehouse = new CollectionResources(cost.asList());
        addInWarehouse.add(new Coin());
        addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); //addInWarehouse contains 3 servants, 3 coins, 4 stones
        game.insertInWarehouse(1, new Coin(), addInWarehouse); // 1° shelf:[coin]
        game.insertInWarehouse(2, new Stone(), addInWarehouse); // 2° shelf:[stone,stone]
        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,servant]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,coin]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,stone]

        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Coin());
        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().getPersonalDashboard().removeFromWarehouse(toRemove); // 4° shelf:[coin,EMPTY]
        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().getPersonalDashboard().removeFromWarehouse(toRemove); // 4° shelf:[EMPTY,EMPTY]
        assertFalse(game.checkBuyCard(1, CardColor.PURPLE));
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, adds some
     * the resources to the warehouse and some to the strongbox,
     * and verify that the card can be bought, then remove some resources
     * to the warehouse and verify that the card is not affordable anymore
     */
    @Test
    void testCheckBuyCard6() {
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add the card just created to the cards market
        CardsMarket cardsMarket = game.getSetOfCard();
        cardsMarket.popCard(1, CardColor.PURPLE);
        cardsMarket.getCardMatrix()[2][3].add(card);
        //adds some resources to the strongbox
        CollectionResources addInStrongbox = new CollectionResources();
        addInStrongbox.add(new Coin());
        addInStrongbox.add(new Servant());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 1 coin, 1 servant, 2 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        //adds some resources to the warehouse
        CollectionResources addInWarehouse = new CollectionResources();
        addInWarehouse.add(new Coin()); addInWarehouse.add(new Coin());
        addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant());
        addInWarehouse.add(new Stone()); //addInWarehouse contains 2 servants, 2 coin, 1 stone

        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,EMPTY]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,EMPTY]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,EMPTY]
        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Coin());

        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources().remove(new Coin());
        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().getPersonalDashboard().removeFromWarehouse(toRemove); // 4° shelf:[EMPTY,EMPTY]
        assertFalse(game.checkBuyCard(1, CardColor.PURPLE));
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, adds some
     * the resources to the warehouse and some to the strongbox,
     * and verify that the card can't be bought, then add a discount
     * and verify that the card now can be bought
     */
    @Test
    void testCheckBuyCard7(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add the card just created to the cards market
        CardsMarket cardsMarket = game.getSetOfCard();
        cardsMarket.popCard(1, CardColor.PURPLE);
        cardsMarket.getCardMatrix()[2][3].add(card);
        //adds some resources to the strongbox
        CollectionResources addInStrongbox = new CollectionResources();
        addInStrongbox.add(new Servant());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 1 servant, 2 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        //adds some resources to the warehouse
        CollectionResources addInWarehouse = new CollectionResources();
        addInWarehouse.add(new Coin());
        addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant());
        addInWarehouse.add(new Stone()); //addInWarehouse contains 2 servants, 1 coin, 1 stone

        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,EMPTY]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,EMPTY]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,EMPTY]
        CollectionResources discount = new CollectionResources();
        discount.add(new Coin());

        assertFalse(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().addDiscount(new Coin());
        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, adds some
     * the resources to the warehouse and some to the strongbox,
     * and verify that the card can be bought, then add a discount
     * and verify that the card can be bought
     */
    @Test
    void testCheckBuyCard8(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add the card just created to the cards market
        CardsMarket cardsMarket = game.getSetOfCard();
        cardsMarket.popCard(1, CardColor.PURPLE);
        cardsMarket.getCardMatrix()[2][3].add(card);
        //adds some resources to the strongbox
        CollectionResources addInStrongbox = new CollectionResources();
        addInStrongbox.add(new Coin()); addInStrongbox.add(new Coin());
        addInStrongbox.add(new Servant());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 2 coins, 1 servant, 2 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        //adds some resources to the warehouse
        CollectionResources addInWarehouse = new CollectionResources();
        addInWarehouse.add(new Coin()); addInWarehouse.add(new Coin());
        addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant());
        addInWarehouse.add(new Stone()); //addInWarehouse contains 3 servants, 2 coin, 1 stone

        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,servant]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,coin]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,EMPTY]
        CollectionResources discount = new CollectionResources();
        discount.add(new Coin());

        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().addDiscount(new Coin());
        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, adds some
     * the resources to the warehouse and some to the strongbox,
     * and verify that the card can't be bought, then add 2 discount resources
     * and verify that the card now can be bought, then remove a
     * resource and verify that the card can't be bought
     */
    @Test
    void testCheckBuyCard9(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add the card just created to the cards market
        CardsMarket cardsMarket = game.getSetOfCard();
        cardsMarket.popCard(1, CardColor.PURPLE);
        cardsMarket.getCardMatrix()[2][3].add(card);
        //adds some resources to the strongbox
        CollectionResources addInStrongbox = new CollectionResources();
        addInStrongbox.add(new Servant());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 1 servant, 2 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        //adds some resources to the warehouse
        CollectionResources addInWarehouse = new CollectionResources();
        addInWarehouse.add(new Coin());
        addInWarehouse.add(new Servant());
        addInWarehouse.add(new Stone()); //addInWarehouse contains 1 servants, 1 coin, 1 stone

        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,EMPTY,EMPTY]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,EMPTY]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,EMPTY]
        CollectionResources discount = new CollectionResources();
        discount.add(new Coin());

        assertFalse(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().addDiscount(new Coin()); // new coin discount
        assertFalse(game.checkBuyCard(1, CardColor.PURPLE));
        game.getActualPlayer().addDiscount(new Servant()); //new servant discount
        assertTrue(game.checkBuyCard(1, CardColor.PURPLE));
        //delete a coin
        game.getActualPlayer().getPersonalDashboard().removeFromWarehouse(discount);
        assertFalse(game.checkBuyCard(1, CardColor.PURPLE));
    }

    /**
     * this test verify that the method chekPlacement return false
     * when the input position is not between 1 and 3
     */
    @Test
    void testCheckPlacement1(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,null
                ,null
                ,null
                ,-3);

        assertFalse(game.checkPlacement(card, 0));
        assertFalse(game.checkPlacement(card, 4));
        assertFalse(game.checkPlacement(card, -1));
        assertFalse(game.checkPlacement(card, 5));
    }

    /**
     * this test verify that the method checkPlacement return false
     * when the input is a card of level 2 and the position in which
     * the player want to place it is an empty deck
     * then return true when the card is of level 1
     */
    @Test
    void testCheckPlacement2(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //creating a card of level1
        DevelopmentCard card1 = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,null
                ,null
                ,null
                ,-3);
        //creating a card of level2
        DevelopmentCard card2 = new DevelopmentCard(CardColor.PURPLE
                ,2
                ,-3
                ,null
                ,null
                ,null
                ,-3);

        assertFalse(game.checkPlacement(card2, 1));
        assertTrue(game.checkPlacement(card1, 1));
    }

    /**
     * this test create a game with one player, add to him some resources
     * all into the warehouse and create a card with the same ones,
     * then calls the method checkWarehouseResources with that card and
     * all the warehouse resources and verify that return true, then remove a
     * resource from the warehouse and verify that return false with the same inputs
     * then add a new discount with that resource and verify that return true with
     * the same inputs
     */
    @Test
    void testCheckWarehouseResources1(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        // add some resources to warehouse
        CollectionResources addInWarehouse = new CollectionResources(cost.asList());
        addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); //addInWarehouse contains 3 servants, 2 coins, 4 stones
        game.insertInWarehouse(2, new Stone(), addInWarehouse); // 2° shelf:[stone,stone]
        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,servant]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,coin]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,stone]

        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Coin());
        CollectionResources toPayFromWarehouse = new CollectionResources();
        toPayFromWarehouse.sum(card.getCost());
        assertTrue(game.checkWarehouseResources(card, toPayFromWarehouse));
        game.getActualPlayer().getPersonalDashboard().removeFromWarehouse(toRemove); // 4° shelf:[coin,EMPTY]
        toPayFromWarehouse.sub(toRemove);
        assertFalse(game.checkWarehouseResources(card, toPayFromWarehouse));
        game.getActualPlayer().addDiscount(new Coin()); // add a new coin discount
        assertTrue(game.checkWarehouseResources(card, toPayFromWarehouse));
    }

    /**
     * this test create a game with one player, add to him some resources
     * all into the strongbox and create a card with the same ones,
     * then calls the method checkWarehouseResources with that card and
     * all the warehouse resource (none) and verify that return true, then remove a
     * resource from the strongbox and verify that return false with the same inputs
     * then add a new discount with that resource and verify that return true with
     * the same inputs
     */
    @Test
    void testCheckWarehouseResources2(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //adds the resources to the strongbox, with some more resource
        CollectionResources addInStrongbox = new CollectionResources(cost.asList());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 3 servants, 2 coins, 4 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);

        assertTrue(game.checkWarehouseResources(card, new CollectionResources()));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources().remove(new Coin());
        assertFalse(game.checkWarehouseResources(card, new CollectionResources()));
        game.getActualPlayer().addDiscount(new Coin());
        assertTrue(game.checkWarehouseResources(card, new CollectionResources()));
    }

    /**
     * this test create a game with one player, add to him some resources
     * and create a card with the same ones,
     * then calls the method checkWarehouseResources with that card and
     * all the warehouse resources and verify that return true, then remove a
     * resource from the strongbox and verify that return false with the same inputs
     * then add a new discount with that resource and verify that return true with
     * the same inputs
     */
    @Test
    void testCheckWarehouseResources3(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //adds some resources to the strongbox
        CollectionResources addInStrongbox = new CollectionResources();
        addInStrongbox.add(new Coin());
        addInStrongbox.add(new Servant());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 1 coin, 1 servant, 2 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        //adds some resources to the warehouse
        CollectionResources addInWarehouse = new CollectionResources();
        addInWarehouse.add(new Coin());
        addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant());
        addInWarehouse.add(new Stone()); //addInWarehouse contains 2 servants, 1 coin, 1 stone

        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,EMPTY]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,EMPTY]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,EMPTY]
        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Coin());

        CollectionResources toPayFromWarehouse = new CollectionResources();
        toPayFromWarehouse.add(new Coin());
        toPayFromWarehouse.add(new Servant()); toPayFromWarehouse.add(new Servant()); // adds 2 servants and 1 coin
        assertTrue(game.checkWarehouseResources(card, toPayFromWarehouse));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources().remove(new Coin());
        assertFalse(game.checkWarehouseResources(card, toPayFromWarehouse));
        game.getActualPlayer().addDiscount(new Coin());
        assertTrue(game.checkWarehouseResources(card, toPayFromWarehouse));
    }

    /**
     * this test verify that after that the player buy a card
     * that one got placed correctly in the dashboard
     */
    @Test
    void testBuyCard1(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        // get the cost of the card
        DevelopmentCard card = game.getSetOfCard().getCard(1, CardColor.YELLOW);
        // add all the resources to the player
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(new CollectionResources(card.getCost().asList()));
        //buy the card
        game.buyCard(1, CardColor.YELLOW,2, new CollectionResources());
        assertEquals(card, game.getActualPlayer().getPersonalDashboard().getPersonalProductionPower().getCard(2));
        assertTrue(game.getActualPlayer().getTotalResources().asList().isEmpty());
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, adds all
     * the resources to the warehouse and buy the card,
     * and verify that the resources got removed correctly
     */
    @Test
    void testBuyCard2(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add the card just created to the cards market
        CardsMarket cardsMarket = game.getSetOfCard();
        cardsMarket.popCard(1, CardColor.PURPLE);
        cardsMarket.getCardMatrix()[2][3].add(card);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        // add some resources to warehouse
        CollectionResources addInWarehouse = new CollectionResources(cost.asList());
        addInWarehouse.add(new Coin());
        addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); //addInWarehouse contains 3 servants, 2 coins, 4 stones
        game.insertInWarehouse(2, new Stone(), addInWarehouse); // 2° shelf:[stone,stone]
        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,servant]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,coin]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,stone]
        //buy the card paying the cost all from warehouse
        CollectionResources toPayFromWarehouse = new CollectionResources(cost.asList());
        game.buyCard(1, CardColor.BLUE,1 ,toPayFromWarehouse);
        // create the collectionResources equals to the warehouse remaining resources, 4 stones
        CollectionResources remaining = new CollectionResources();
        remaining.add(new Stone()); remaining.add(new Stone()); remaining.add(new Stone()); remaining.add(new Stone());

        assertEquals(remaining, game.getActualPlayer().getTotalResources());
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, adds all
     * the resources to the strongbox and buy the card,
     * and verify that the resources got removed correctly
     */
    @Test
    void testBuyCard3() {
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add the card just created to the cards market
        CardsMarket cardsMarket = game.getSetOfCard();
        cardsMarket.popCard(1, CardColor.PURPLE);
        cardsMarket.getCardMatrix()[2][3].add(card);
        //adds the resources to the strongbox, with some more resource
        CollectionResources addInStrongbox = new CollectionResources(cost.asList());
        addInStrongbox.add(new Coin());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 3 servants, 3 coins, 4 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //buy the card paying the cost all from strongbox
        game.buyCard(1, CardColor.PURPLE,1 ,new CollectionResources());
        // create the collectionResources equals to the warehouse remaining resources, 4 stones and 1 coin
        CollectionResources remaining = new CollectionResources();
        remaining.add(new Stone()); remaining.add(new Stone()); remaining.add(new Stone()); remaining.add(new Stone());
        remaining.add(new Coin());

        assertEquals(remaining, game.getActualPlayer().getTotalResources());
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, adds some
     * resources to the actual player, and buy the card,
     * and verify that the resources got removed correctly
     */
    @Test
    void testBuyCard4(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        //add the card just created to the cards market
        CardsMarket cardsMarket = game.getSetOfCard();
        cardsMarket.popCard(1, CardColor.PURPLE);
        cardsMarket.getCardMatrix()[2][3].add(card);
        //adds some resources to the strongbox
        CollectionResources addInStrongbox = new CollectionResources();
        addInStrongbox.add(new Coin()); addInStrongbox.add(new Coin());
        addInStrongbox.add(new Servant());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 2 coins, 1 servant, 2 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        //adds some resources to the warehouse
        CollectionResources addInWarehouse = new CollectionResources();
        addInWarehouse.add(new Coin()); addInWarehouse.add(new Coin());
        addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant());
        addInWarehouse.add(new Stone()); //addInWarehouse contains 3 servants, 2 coin, 1 stone

        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,servant]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,coin]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,EMPTY]
        // pay 2 servants and 1 coin from the warehouse
        CollectionResources toPayFromWarehouse = new CollectionResources();
        toPayFromWarehouse.add(new Coin());
        toPayFromWarehouse.add(new Servant()); toPayFromWarehouse.add(new Servant());

        assertTrue(game.checkWarehouseResources(card, toPayFromWarehouse));

        game.buyCard(1, CardColor.PURPLE, 2, toPayFromWarehouse);
        // 3° shelf:[servant,EMPTY,EMPTY]
        // 4° shelf:[coin,EMPTY]
        // 5° shelf:[stone,EMPTY]
        // strongbox = [<coin,1>,<stone,2>]
        //verify the strongbox resources remaining
        CollectionResources remainingInStrongbox = new CollectionResources();
        remainingInStrongbox.add(new Coin());
        remainingInStrongbox.add(new Stone()); remainingInStrongbox.add(new Stone());
        assertEquals(remainingInStrongbox, game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources());
        //verify the warehouse resources remaining
        CollectionResources remainingInWarehouse = new CollectionResources();
        remainingInWarehouse.add(new Coin());
        remainingInWarehouse.add(new Stone());
        remainingInWarehouse.add(new Servant());
        assertEquals(remainingInWarehouse, game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getTotalResources());
    }

    /**
     * this test verifies the selectRow method that must return a list of marbles corresponding to the selected row of
     * the MarbleTray modifying the tray and the lonely marble as per game rules
     */
    @Test
    void testSelectRow() {
        int i;
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        List<Marble> marbles = new ArrayList<>();
        for (i = 3; i >= 0; i--) {
            marbles.add(game.getMarketBoard().getMarble(0, i));
        }
        assertEquals(game.selectRow(1), marbles);
        marbles.remove(3);
        marbles.add(0, game.getMarketBoard().getMarble(0, 3));
        assertEquals(game.selectRow(1), marbles);
    }


    /**
     *this test verifies the selectColumn method that must return a list of marbles corresponding to the selected column
     * of the MarbleTray modifying the tray and the lonely marble as per game rules
     */
    @Test
    void testSelectColumn() {
        int i;
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        List<Marble> marbles = new ArrayList<>();
        for (i = 2; i >= 0; i--) {
            marbles.add(game.getMarketBoard().getMarble(i, 0));
        }
        assertEquals(game.selectColumn(1), marbles);
        marbles.remove(2);
        marbles.add(0, game.getMarketBoard().getMarble(2, 0));
        assertEquals(game.selectColumn(1), marbles);
    }

    /**
     *this test verifies that the method changeWhiteMarble return false when the input list is null
     */
    @Test
    void testChangeWhiteMarble1() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        assertFalse(game.changeWhiteMarble(null, 1));
    }

    /**
     *this test verifies that the method changeWhiteMarble return false when the input index is less or equal to 0 or
     * it is greater than 2
     */
    @Test
    void testChangeWhiteMarble2(){
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        List<Marble> marbles = new ArrayList<>();
        marbles.add(new WhiteMarble());
        marbles.add(new BlueMarble());
        marbles.add(new WhiteMarble());
        assertFalse(game.changeWhiteMarble(marbles, -1));
        assertFalse(game.changeWhiteMarble(marbles, 0));
        assertFalse(game.changeWhiteMarble(marbles, 3));
        assertFalse(game.changeWhiteMarble(marbles, 4));
    }

    /**
     *this test verifies that the method changeWhiteMarble return false when there aren't leader white marbles activated
     */
    @Test
    void testChangeWhiteMarble3(){
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        List<Marble> marbles = new ArrayList<>();
        marbles.add(new WhiteMarble());
        marbles.add(new BlueMarble());
        marbles.add(new WhiteMarble());
        assertFalse(game.changeWhiteMarble(marbles, 1));
        assertFalse(game.changeWhiteMarble(marbles, 2));
    }

    /**
     *this test verifies that the method changeWhiteMarble return false when the input list doesn't contain any white 
     * marbles while the actual player has some activated leader white marbles
     */
    @Test
    void testChangeWhiteMarble4(){
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        game.getActualPlayer().addLeaderWhiteMarble(new Coin());
        game.getActualPlayer().addLeaderWhiteMarble(new Servant());
        List<Marble> marbles = new ArrayList<>();
        marbles.add(new YellowMarble());
        marbles.add(new BlueMarble());
        marbles.add(new PurpleMarble());
        assertFalse(game.changeWhiteMarble(marbles, 1));
    }

    /**
     * this test create a game with 3 players, create a collectionResources with
     * 4 coins and other some resources, then adds the coins to the third shelf
     * and verify that it has 3 coins, and that every other player gain 1 faith point
     */
    @Test
    void testInsertInWarehouse1(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        // create the collection to add = [<Coin,4>,<Stone,2>,<Servant,1>,<Shield,1>]
        CollectionResources toAdd = new CollectionResources();
        toAdd.add(new Coin()); toAdd.add(new Coin()); toAdd.add(new Coin()); toAdd.add(new Coin());
        toAdd.add(new Stone()); toAdd.add(new Stone());
        toAdd.add(new Servant());
        toAdd.add(new Shield());
        // create a copy of the collection toAdd
        CollectionResources copy = new CollectionResources(toAdd.asList());
        // add 3 coins to the 3° shelf
        game.insertInWarehouse(3, new Coin(), toAdd);
        // create a collection with 3 coins
        CollectionResources coins = new CollectionResources();
        coins.add(new Coin()); coins.add(new Coin()); coins.add(new Coin());

        assertEquals(coins, game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources());
        assertEquals(0 , game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(1 , game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(2 , game.getPlayers().get(2).getPersonalTrack().getPosition());
        assertEquals(copy, toAdd);
    }

    /**
     * this test create a game with 3 players, add to the first one
     * a shield to the 2° shelf and try to add some coins to it,
     * it does not add the resources and add 4 faith points
     * to every other player
     */
    @Test
    void testInsertInWarehouse2(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        // create the collection to add = [<Coin,4>,<Stone,1>,<Servant,1>,<Shield,1>]
        CollectionResources toAdd = new CollectionResources();
        toAdd.add(new Coin()); toAdd.add(new Coin()); toAdd.add(new Coin()); toAdd.add(new Coin());
        toAdd.add(new Stone());
        toAdd.add(new Servant());
        toAdd.add(new Shield());
        // create a copy of the collection toAdd
        // add 1 stone to the 2° shelf
        game.insertInWarehouse(2, new Stone(), toAdd);
        // try to add 4 coins to the 2° shelf
        game.insertInWarehouse(2, new Coin(), toAdd);
        // create a collection with 1 stone
        CollectionResources stone = new CollectionResources();
        stone.add(new Stone());

        assertEquals(stone, game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources());
        assertEquals(0 , game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(4 , game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(5 , game.getPlayers().get(2).getPersonalTrack().getPosition());
    }

    /**
     * this test create a game with 3 players, add to the first one
     * a coin type leader shelf and try to add 4 coins to it,
     * the method insertInWarehouse add the resources and add 2 faith points
     * to every other player
     */
    @Test
    void testInsertInWarehouse3(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        // add a coin leaderShelf
        game.getActualPlayer().addLeaderShelf(new Coin());
        // create the collection to add = [<Coin,4>,<Stone,2>,<Servant,1>,<Shield,1>]
        CollectionResources toAdd = new CollectionResources();
        toAdd.add(new Coin()); toAdd.add(new Coin()); toAdd.add(new Coin()); toAdd.add(new Coin());
        toAdd.add(new Stone()); toAdd.add(new Stone());
        toAdd.add(new Servant());
        toAdd.add(new Shield());
        // create a copy of the collection toAdd
        // add 3 coins to the 3° shelf
        game.insertInWarehouse(4, new Coin(), toAdd);
        // create a collection with 2 coins
        CollectionResources coins = new CollectionResources();
        coins.add(new Coin()); coins.add(new Coin());

        assertEquals(coins, game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(4).getResources());
        assertEquals(0 , game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(2 , game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(3 , game.getPlayers().get(2).getPersonalTrack().getPosition());
    }

    /**
     * this test create a game with 3 players, add to the first one
     * a stone type leader shelf and try to add 4 coins to it,
     * the method insertInWarehouse doesn't add the resources
     * and add 4 faith points to every other player
     */
    @Test
    void testInsertInWarehouse4() {
        //initialise the game
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        // add a coin leaderShelf
        game.getActualPlayer().addLeaderShelf(new Stone());
        // create the collection to add = [<Coin,4>,<Stone,2>,<Servant,1>,<Shield,1>]
        CollectionResources toAdd = new CollectionResources();
        toAdd.add(new Coin());
        toAdd.add(new Coin());
        toAdd.add(new Coin());
        toAdd.add(new Coin());
        toAdd.add(new Stone());
        toAdd.add(new Stone());
        toAdd.add(new Servant());
        toAdd.add(new Shield());
        // create a copy of the collection toAdd
        // add 3 coins to the 3° shelf
        game.insertInWarehouse(4, new Coin(), toAdd);

        assertEquals(0, game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(4).getResources().getSize());
        assertEquals(0, game.getPlayers().get(0).getPersonalTrack().getPosition());
        assertEquals(4, game.getPlayers().get(1).getPersonalTrack().getPosition());
        assertEquals(5, game.getPlayers().get(2).getPersonalTrack().getPosition());
    }

    /**
     *this test verifies that the method change white marble return false if a generic leader card not LeaderWhiteMarble
     * is active but when a LeaderWhiteMarble is active the method returns true and change a WhiteMarble in the input
     * list
     */
    @Test
    void testChangeWhiteMarble5() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        List<Marble> marbles = new ArrayList<>(); // initialize list of marbles
        marbles.add(new WhiteMarble());
        marbles.add(new BlueMarble());
        marbles.add(new WhiteMarble());
        List<Marble> marbles2 = new ArrayList<>();
        marbles2.add(new WhiteMarble());
        marbles2.add(new BlueMarble());
        marbles2.add(new GreyMarble());
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        game.getActualPlayer().getPersonalLeaderCards().add(new NewProduction(new LevelRequired(CardColor.GREEN),3,new Stone()));
        game.getActualPlayer().locateDevelopmentCard(new DevelopmentCard(
                CardColor.BLUE
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        game.getActualPlayer().locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        game.getActualPlayer().locateDevelopmentCard(new DevelopmentCard(
                CardColor.YELLOW
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        game.getActualPlayer().activateLeaderCard(3);
        assertFalse(game.changeWhiteMarble(marbles,1));
        game.getActualPlayer().getPersonalLeaderCards().add(new NewWhiteMarble(new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.YELLOW))),2, new Stone()));
        assertTrue(game.getActualPlayer().activateLeaderCard(4));
        assertTrue(game.changeWhiteMarble(marbles,1));
        assertTrue(marbles.containsAll(marbles2));
        assertTrue(marbles2.containsAll(marbles));
    }

    /**
     *this test verifies the behaviour of the method changeWhiteMarble, it adds two leader white marble, then it
     *calls the method with the desired input and at the end it verifies the correct change of marbles in the list
     */
    @Test
    void testChangeWhiteMarble6() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        game.getActualPlayer().getPersonalLeaderCards().add(new NewWhiteMarble(new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.BLUE, CardColor.YELLOW))),2, new Coin()));
        game.getActualPlayer().getPersonalLeaderCards().add(new NewWhiteMarble(new ColorRequired(new ArrayList<>(Arrays.asList(CardColor.GREEN, CardColor.YELLOW))),2, new Shield()));
        game.getActualPlayer().locateDevelopmentCard(new DevelopmentCard(
                CardColor.BLUE
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        game.getActualPlayer().locateDevelopmentCard(new DevelopmentCard(
                CardColor.GREEN
                ,2
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        game.getActualPlayer().locateDevelopmentCard(new DevelopmentCard(
                CardColor.YELLOW
                ,3
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),1);
        game.getActualPlayer().locateDevelopmentCard(new DevelopmentCard(
                CardColor.PURPLE
                ,1
                ,12
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(), new Shield(), new Shield(), new Shield(), new Coin(), new Coin(), new Coin(), new Coin())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Stone())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(), new Shield(), new Shield(), new Shield())))
                ,0),2);
        game.getActualPlayer().activateLeaderCard(3);
        game.getActualPlayer().activateLeaderCard(4);
        List<Marble> marbles = new ArrayList<>(); // initialize list of marbles
        marbles.add(new WhiteMarble());
        marbles.add(new WhiteMarble());
        marbles.add(new WhiteMarble());
        marbles.add(new GreyMarble());
        game.changeWhiteMarble(marbles,1);
        game.changeWhiteMarble(marbles,1);
        game.changeWhiteMarble(marbles,2);
        List<Marble> marbles2 = new ArrayList<>(); // initialize list of marbles
        marbles2.add(new YellowMarble());
        marbles2.add(new YellowMarble());
        marbles2.add(new BlueMarble());
        marbles2.add(new GreyMarble());
        assertTrue(marbles.containsAll(marbles2));
        assertTrue(marbles2.containsAll(marbles));
    }

    /**
     *this test verifies that if the input of the method convert is a list of only white or red marbles than it returns
     * a empty list
     */
    @Test
    void testConvert1(){
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        List<Marble> marbles = new ArrayList<>(); // initialize list of marbles
        marbles.add(new WhiteMarble());
        marbles.add(new WhiteMarble());
        marbles.add(new RedMarble());
        assertTrue(game.convert(marbles).getMaps().isEmpty());
    }

    /**
     *this test verifies that if the input of the method convert is a list of white or red marbles and other types of
     * marbles than it returns the CollectionResources containing the resources corresponding to the other marbles (not
     * red or white)
     */
    @Test
    void testConvert2() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        List<Marble> marbles = new ArrayList<>(); // initialize list of marbles
        marbles.add(new YellowMarble());
        marbles.add(new WhiteMarble());
        marbles.add(new BlueMarble());
        marbles.add(new RedMarble());
        CollectionResources converted = new CollectionResources();
        converted.add(new Coin());
        converted.add(new Shield());
        assertEquals(game.convert(marbles), converted);
    }

    /**
     *this test verifies that if the input of the method convert is a list of marble neither red nor white than it
     *  returns the CollectionResources containing the resources corresponding to the marbles
     */
    @Test
    void testConvert3() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        List<Marble> marbles = new ArrayList<>(); // initialize list of marbles
        marbles.add(new YellowMarble());
        marbles.add(new YellowMarble());
        marbles.add(new BlueMarble());
        marbles.add(new GreyMarble());
        CollectionResources converted = new CollectionResources();
        converted.add(new Coin());
        converted.add(new Coin());
        converted.add(new Shield());
        converted.add(new Stone());
        assertEquals(game.convert(marbles), converted);
    }
//------------------ACTIVATE PRODUCTIONS----------------------------------------------------

    /**
     * this test verify that the development card position in dashboard is correct when a player select
     * a production power from a development card
     */
    @Test
    public void testCheckPositionCard1(){
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE,1,1 ,new CollectionResources(new ArrayList<>(Arrays.asList(new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Collections.singletonList(new Coin())))
                ,new CollectionResources(new ArrayList<>())
                ,1);
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());         collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SERVANT);
        collection1.add(new Servant());         collection1.add(new Servant());
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        game.insertInWarehouse(2 , new Coin() , collection); game.insertInWarehouse(3 , new Servant() , collection1);
        game.getActualPlayer().getPersonalDashboard().placeDevelopmentCard( card , 3);
        game.getActualPlayer().addLeaderProduction(new Coin());
        assertFalse(game.checkProduction(-1)); assertFalse(game.checkProduction(6));
        assertFalse(game.checkProduction(-3)); assertFalse(game.checkProduction(9));
        assertTrue(game.checkProduction(0)); assertTrue(game.checkProduction(4));
        assertFalse(game.checkProduction(5));
        game.getActualPlayer().addLeaderProduction(new Servant());
        assertTrue(game.checkProduction(3));
        assertTrue(game.checkProduction(5));
    }

    /**
     * this test verify that the development card position in dashboard is correct when a player select
     * a production power from a development card
     */
    @Test
    public void testCheckPositionCard2(){
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        assertTrue(game.checkProduction(0)); assertFalse(game.checkProduction(1));
        assertFalse(game.checkProduction(2)); assertFalse(game.checkProduction(3));
        assertFalse(game.checkProduction(4)); assertFalse(game.checkProduction(5));
    }

    /**
     * this test insert a normal card in dashboard and two resources in leader warehouse and check if the player can activate the card;
     * the i reject a resource and we check if we can activate again the card
     */
    @Test
    public void testCheckActivateProductionFromLeaderShelf(){
        DevelopmentCard card = new DevelopmentCard(CardColor.GREEN,1,10,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Shield(),new Shield(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Servant()))) //input
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Stone(),new Stone()))),1);
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN); collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SERVANT); collection1.add(new Servant());
        game.getActualPlayer().addLeaderShelf(new Coin()); game.getActualPlayer().addLeaderShelf(new Servant());
        game.insertInWarehouse(4 , new Coin() , collection);
        game.getActualPlayer().getPersonalDashboard().placeDevelopmentCard( card , 3);
        assertFalse(game.checkProduction(3));
        game.insertInWarehouse(5 , new Servant() , collection1);
        assertTrue(game.checkProduction(3));
    }

    /**
     * this test insert a normal card in dashboard and two resources in strongbox and check if the player can activate the card;
     * the i reject a resource and we check if we can activate again the card
     */
    @Test
    public void testCheckActivateProductionFromStrongbox(){
        DevelopmentCard card = new DevelopmentCard(CardColor.GREEN,1,10,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Shield(),new Shield(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Servant()))) //input
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Stone(),new Stone()))),1);
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN); collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SERVANT); collection1.add(new Servant());
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(collection);
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(collection1);
        game.getActualPlayer().getPersonalDashboard().placeDevelopmentCard( card , 3);
        assertTrue(game.checkProduction(3));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().removeResources(collection);
        assertFalse(game.checkProduction(3));
    }

    /**
     * this test insert a normal card in dashboard , one resource in leader warehouse and one in strongbox and check if the player can activate the card;
     * then i reject a resource and we check if we can activate again the card
     */
    @Test
    public void testCheckActivateProductionFromStrongboxAndWarehouse(){
        DevelopmentCard card = new DevelopmentCard(CardColor.GREEN,1,10,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Shield(),new Shield(),new Shield(),new Servant(),new Servant())))
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Coin(),new Servant()))) //input
                ,new CollectionResources(new ArrayList<>(Arrays.asList(new Shield(),new Shield(),new Stone(),new Stone()))),1);
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN); collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SERVANT); collection1.add(new Servant());
        game.getActualPlayer().addLeaderShelf(new Servant()); game.getActualPlayer().addLeaderShelf(new Coin());
        game.insertInWarehouse(5 , new Coin() , collection);
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(collection1);
        game.getActualPlayer().getPersonalDashboard().placeDevelopmentCard( card , 3);
        assertTrue(game.checkProduction(3));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().removeResources(collection1);
        assertFalse(game.checkProduction(3));
        game.insertInWarehouse(4 , new Servant() , collection1);
        assertTrue(game.checkProduction(3));
    }

    /**
     * in this test we add a leader production and a resource to the strongbox, we verify if we can activate the leader production.
     * Then we remove a resource and verify if we can not activate the leader card
     */
    @Test
    public void testCheckActivateLeaderFromStrongbox(){
        CollectionResources collection = new CollectionResources();
        collection.add(new Coin());
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(collection); // add one coin in strongbox
        game.getActualPlayer().addLeaderProduction(new Coin());
        assertTrue(game.checkProduction(4));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().removeResources(collection);
        assertFalse(game.checkProduction(4));
    }

    /**
     * in this test we add a leader production and a resource to the strongbox, we verify if we can activate the leader production.
     * Then we remove a resource and verify if we can not activate the leader card
     */
    @Test
    public void testCheckActivateLeaderFromWarehouse1(){
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderProduction(new Coin());
        assertFalse(game.checkProduction(4));
        game.insertInWarehouse(4 , new Coin() , collection);
        assertTrue(game.checkProduction(4));
    }

    /**
     * this test create a game with one player, adds some resources
     * to the warehouse, create a card with an input contained in the warehouse,
     * adds it to the actual player dashboard,
     * verify that the method checkActivateProduction with the position
     * of the card in input and the warehouse resources return true,
     * then delete a coin from the warehouse and verify that returns false
     * with tha same inputs
     */
    @Test
    void testCheckActivateProduction1(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected input
        CollectionResources input = new CollectionResources();
        input.add(new Servant()); input.add(new Servant()); input.add(new Servant());
        input.add(new Coin()); input.add(new Coin()); //input contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,input
                ,input
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        // adds the card to the dashboard
        game.getActualPlayer().locateDevelopmentCard(card, 1);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        // add some resources to warehouse
        CollectionResources addInWarehouse = new CollectionResources(input.asList());
        addInWarehouse.add(new Coin());
        addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); //addInWarehouse contains 3 servants, 3 coins, 4 stones
        game.insertInWarehouse(1, new Coin(), addInWarehouse); // 1° shelf:[coin]
        game.insertInWarehouse(2, new Stone(), addInWarehouse); // 2° shelf:[stone,stone]
        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,servant]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,coin]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,stone]

        CollectionResources toRemove = new CollectionResources(); toRemove.add(new Coin());
        CollectionResources toPayFromWarehouse = new CollectionResources(input.asList());
        assertTrue(game.checkActivateProduction(1, toPayFromWarehouse));
        game.getActualPlayer().getPersonalDashboard().removeFromWarehouse(toRemove); // 4° shelf:[coin,EMPTY]
        assertTrue(game.checkActivateProduction(1, toPayFromWarehouse));
        game.getActualPlayer().getPersonalDashboard().removeFromWarehouse(toRemove); // 4° shelf:[EMPTY,EMPTY]
        assertFalse(game.checkActivateProduction(1, toPayFromWarehouse));
        toPayFromWarehouse.remove(new Coin());
        assertFalse(game.checkActivateProduction(1, toPayFromWarehouse));
    }

    /**
     * this test create a game with one player, adds some resources
     * to the strongbox, create a card with an input contained in the strongbox,
     * adds it to the actual player dashboard,
     * verify that the method checkActivateProduction with the position
     * of the card in input and the warehouse resources (none) return true,
     * then delete a coin from the strongbox and verify that returns false
     * with tha same inputs
     */
    @Test
    void testCheckActivateProduction2(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources input = new CollectionResources();
        input.add(new Servant()); input.add(new Servant()); input.add(new Servant());
        input.add(new Coin()); input.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,input
                ,input
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        // adds the card in the actual player dashboard
        game.getActualPlayer().locateDevelopmentCard(card, 1);
        //adds the resources to the strongbox, with some more resource
        CollectionResources addInStrongbox = new CollectionResources(input.asList());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 3 servants, 2 coins, 4 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);

        assertTrue(game.checkActivateProduction(1, new CollectionResources()));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources().remove(new Coin());
        assertFalse(game.checkActivateProduction(1, new CollectionResources()));
    }

    /**
     * this test create a game with one player, adds some resources
     * to the dashboard, create a card with an input contained in the dashboard,
     * adds it to the actual player dashboard cards,
     * verify that the method checkActivateProduction with the position
     * of the card in input and the warehouse resources return true,
     * then delete a coin from the strongbox and verify that returns false
     * with tha same inputs
     */
    @Test
    void testCheckActivateProduction3(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost
        CollectionResources input = new CollectionResources();
        input.add(new Servant()); input.add(new Servant()); input.add(new Servant());
        input.add(new Coin()); input.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,input
                ,input
                ,new CollectionResources(new ArrayList<>())
                ,-3);
        // adds the card in the actual player dashboard
        game.getActualPlayer().locateDevelopmentCard(card, 1);
        //adds some resources to the strongbox
        CollectionResources addInStrongbox = new CollectionResources();
        addInStrongbox.add(new Coin());
        addInStrongbox.add(new Servant());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 1 coin, 1 servant, 2 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        //adds some resources to the warehouse
        CollectionResources addInWarehouse = new CollectionResources();
        addInWarehouse.add(new Coin());
        addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant());
        addInWarehouse.add(new Stone()); //addInWarehouse contains 2 servants, 1 coin, 1 stone

        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,EMPTY]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,EMPTY]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,EMPTY]
        CollectionResources toRemove = new CollectionResources();
        toRemove.add(new Coin());

        CollectionResources toPayFromWarehouse = new CollectionResources();
        toPayFromWarehouse.add(new Coin());
        toPayFromWarehouse.add(new Servant()); toPayFromWarehouse.add(new Servant()); // adds 2 servants and 1 coin

        assertTrue(game.checkActivateProduction(1, toPayFromWarehouse));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources().remove(new Coin());
        assertFalse(game.checkActivateProduction(1, toPayFromWarehouse));
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, input, output, adds all
     * the resources to the warehouse and activate the card,
     * finally verify that the resources got removed correctly
     * and that the output got added in the strongbox correctly
     */
    @Test
    void testActivateProduction1(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost, input e output
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,cost
                ,cost
                ,-3);
        // adds the card in the actual player dashboard
        game.getActualPlayer().locateDevelopmentCard(card, 1);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        // add some resources to warehouse
        CollectionResources addInWarehouse = new CollectionResources(cost.asList());
        addInWarehouse.add(new Coin());
        addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); addInWarehouse.add(new Stone()); //addInWarehouse contains 3 servants, 2 coins, 4 stones
        game.insertInWarehouse(2, new Stone(), addInWarehouse); // 2° shelf:[stone,stone]
        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,servant]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,coin]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,stone]
        //activate the card paying the cost all from warehouse
        CollectionResources toPayFromWarehouse = new CollectionResources(cost.asList());
        game.activateProduction(1 ,toPayFromWarehouse);
        // create the collectionResources equals to the warehouse remaining resources, 4 stones
        CollectionResources remaining = new CollectionResources();
        remaining.add(new Stone()); remaining.add(new Stone()); remaining.add(new Stone()); remaining.add(new Stone());

        assertEquals(remaining, game.getActualPlayer().getTotalResources());
        assertEquals(cost, game.getActualPlayer().getPersonalDashboard().getBuffer());
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, input, output, adds all
     * the resources to the strongbox and activate the card,
     * finally verify that the resources got removed correctly
     * and that the output got added in the strongbox correctly
     */
    @Test
    void testActivateProduction2(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost, input e output
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost contains 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,cost
                ,cost
                ,-3);
        // adds the card in the actual player dashboard
        game.getActualPlayer().locateDevelopmentCard(card, 1);
        //adds the resources to the strongbox, with some more resource
        CollectionResources addInStrongbox = new CollectionResources(cost.asList());
        addInStrongbox.add(new Coin());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 3 servants, 3 coins, 4 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //buy the card paying the cost all from strongbox
        game.activateProduction(1 ,new CollectionResources());
        // create the collectionResources equals to the warehouse remaining resources, 4 stones and 1 coin
        CollectionResources remaining = new CollectionResources();
        remaining.add(new Stone()); remaining.add(new Stone()); remaining.add(new Stone()); remaining.add(new Stone());
        remaining.add(new Coin());

        assertEquals(remaining, game.getActualPlayer().getTotalResources());
        assertEquals(cost, game.getActualPlayer().getPersonalDashboard().getBuffer());
    }

    /**
     * this test create a game with one player, adds
     * a card to the market with a selected cost, input, output, adds all
     * the resources to the dashboard and activate the card,
     * finally verify that the resources got removed correctly
     * and that the output got added in the strongbox correctly
     */
    @Test
    void testActivateProduction3(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        //create a card with a selected cost, input e output
        CollectionResources cost = new CollectionResources();
        cost.add(new Servant()); cost.add(new Servant()); cost.add(new Servant());
        cost.add(new Coin()); cost.add(new Coin()); //cost, input and output contain 3 servant, and 2 coins
        DevelopmentCard card = new DevelopmentCard(CardColor.PURPLE
                ,1
                ,-3
                ,cost
                ,cost
                ,cost
                ,-3);
        // adds the card in the actual player dashboard
        game.getActualPlayer().locateDevelopmentCard(card, 1);
        //adds some resources to the strongbox
        CollectionResources addInStrongbox = new CollectionResources();
        addInStrongbox.add(new Coin()); addInStrongbox.add(new Coin());
        addInStrongbox.add(new Servant());
        addInStrongbox.add(new Stone()); addInStrongbox.add(new Stone()); //addInStrongbox contains 2 coins, 1 servant, 2 stones
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(addInStrongbox);
        //add 2 leader shelves
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addLeaderShelf(new Stone());
        //adds some resources to the warehouse
        CollectionResources addInWarehouse = new CollectionResources();
        addInWarehouse.add(new Coin()); addInWarehouse.add(new Coin());
        addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant()); addInWarehouse.add(new Servant());
        addInWarehouse.add(new Stone()); //addInWarehouse contains 3 servants, 2 coin, 1 stone

        game.insertInWarehouse(3, new Servant(), addInWarehouse); // 3° shelf:[servant,servant,servant]
        game.insertInWarehouse(4, new Coin(), addInWarehouse); // 4° shelf:[coin,coin]
        game.insertInWarehouse(5, new Stone(), addInWarehouse); // 5° shelf:[stone,EMPTY]
        // pay 2 servants and 1 coin from the warehouse
        CollectionResources toPayFromWarehouse = new CollectionResources();
        toPayFromWarehouse.add(new Coin());
        toPayFromWarehouse.add(new Servant()); toPayFromWarehouse.add(new Servant());

        game.activateProduction( 1, toPayFromWarehouse);
        // 3° shelf:[servant,EMPTY,EMPTY]
        // 4° shelf:[coin,EMPTY]
        // 5° shelf:[stone,EMPTY]
        // strongbox = [<coin,1>,<stone,2>]
        //verify the strongbox resources remaining
        CollectionResources remainingInStrongbox = new CollectionResources();
        remainingInStrongbox.add(new Coin());
        remainingInStrongbox.add(new Stone()); remainingInStrongbox.add(new Stone());
        assertEquals(remainingInStrongbox, game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources());
        //verify the warehouse resources remaining
        CollectionResources remainingInWarehouse = new CollectionResources();
        remainingInWarehouse.add(new Coin());
        remainingInWarehouse.add(new Stone());
        remainingInWarehouse.add(new Servant());
        assertEquals(remainingInWarehouse, game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getTotalResources());
        assertEquals(cost, game.getActualPlayer().getPersonalDashboard().getBuffer());
    }

    //--------BASIC PRODUCTIONS---------------------------------------------------------------------------------------

    /**
     * this method check if the basicProductionCheck return false if a method parameter is null
     */
    @Test
    public void testCheckBasicProduction1(){
        CollectionResources strongbox= new CollectionResources();
        strongbox.add(new Servant()); strongbox.add(new Servant()); strongbox.add(new Shield()); strongbox.add(new Shield()); strongbox.add(new Shield());
        CollectionResources payWarehouse = new CollectionResources(); CollectionResources payStrongbox = new CollectionResources();
        payStrongbox.add(new Servant()); payStrongbox.add(new Shield());
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(strongbox);
        assertFalse(game.checkActivateBasicProduction(payWarehouse , payStrongbox , null));
    }

    /**
     * this method insert some resource in warehouse and check if the player can pay for the basic production. Return false if
     * there aren't enough resources
     */
    @Test
    public void testCheckBasicProduction2(){
        ShelfCollection collection2 = new ShelfCollection(ResourceType.SERVANT);
        collection2.add(new Servant()); collection2.add(new Servant());
        ShelfCollection collection3 = new ShelfCollection(ResourceType.SHIELD);
        collection3.add(new Shield()); collection3.add(new Shield()); collection3.add(new Shield());
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        CollectionResources payWarehouse = new CollectionResources(); CollectionResources payStrongbox = new CollectionResources();
        payWarehouse.add(new Servant()); payWarehouse.add(new Shield());
        assertFalse(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
        game.insertInWarehouse(2 , new Servant() , collection2);
        assertFalse(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
        game.insertInWarehouse(3 , new Shield() , collection3);
        assertTrue(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
    }

    /**
     * this method insert some resource in strongbox and check if the player can pay for the basic production. Return false if
     * there aren't enough resources
     */
    @Test
    public void testCheckBasicProduction3(){
        CollectionResources strongbox= new CollectionResources();
        strongbox.add(new Servant());
        CollectionResources payWarehouse = new CollectionResources(); CollectionResources payStrongbox = new CollectionResources();
        payStrongbox.add(new Servant()); payStrongbox.add(new Shield());
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        assertFalse(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(strongbox);
        assertFalse(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
        strongbox.add(new Shield());
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(strongbox);
        assertTrue(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
        payStrongbox.add(new Servant());
        assertFalse(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
    }

    /**
     * this method insert some resource in strongbox and warehouse and check if the player can pay for the basic production. Return false if
     * there aren't enough resources
     */
    @Test
    public void testCheckBasicProduction4(){
        ShelfCollection collection2 = new ShelfCollection(ResourceType.SERVANT);
        collection2.add(new Servant()); collection2.add(new Servant());
        CollectionResources payWarehouse = new CollectionResources(); CollectionResources payStrongbox = new CollectionResources();
        payWarehouse.add(new Servant());
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        assertFalse(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
        game.insertInWarehouse(2 , new Servant() , collection2);
        assertFalse(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
        payStrongbox.add(new Shield());
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(payStrongbox);
        assertTrue(game.checkActivateBasicProduction(payWarehouse , payStrongbox , new Coin()));
    }

    /**
     * this method test if the basic production is activated correctly, the player takes all resources from warehouse
     */
    @Test
    public void testBasicProductionFromWarehouse(){
        CollectionResources collectionB= new CollectionResources();
        collectionB.add(new Coin());
        CollectionResources payWarehouse = new CollectionResources(); CollectionResources payStrongbox = new CollectionResources();
        ShelfCollection collection2 = new ShelfCollection(ResourceType.SERVANT);
        collection2.add(new Servant()); collection2.add(new Servant());
        ShelfCollection collection3 = new ShelfCollection(ResourceType.SHIELD);
        collection3.add(new Shield()); collection3.add(new Shield()); collection3.add(new Shield());
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        game.insertInWarehouse(2 , new Servant() , collection2); game.insertInWarehouse(3 , new Shield() , collection3);
        payWarehouse.add(new Servant()); payWarehouse.add(new Shield());
        game.activateBasicProduction(payWarehouse,payStrongbox,new Coin());
        assertEquals(game.getActualPlayer().getPersonalDashboard().getBuffer() , collectionB);
        CollectionResources collection4= new CollectionResources();
        collection4.add(new Servant());
        CollectionResources collection5= new CollectionResources();
        collection5.add(new Shield()); collection5.add(new Shield());
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(1).getResources(), payStrongbox);//shelf 1
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources(), collection4);//shelf 2
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources(), collection5);//shelf 3
    }

    /**
     * this method test if the basic production is activated correctly, the player takes all resources from strongbox
     */
    @Test
    public void testBasicProductionFromStrongbox(){
        CollectionResources collectionB= new CollectionResources();
        collectionB.add(new Coin());
        CollectionResources strongbox= new CollectionResources();
        strongbox.add(new Servant()); strongbox.add(new Servant()); strongbox.add(new Shield()); strongbox.add(new Shield()); strongbox.add(new Shield());
        CollectionResources payWarehouse = new CollectionResources(); CollectionResources payStrongbox = new CollectionResources();
        payStrongbox.add(new Servant()); payStrongbox.add(new Shield());
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(strongbox);
        game.activateBasicProduction(payWarehouse,payStrongbox,new Coin());
        assertEquals(game.getActualPlayer().getPersonalDashboard().getBuffer() , collectionB);
        strongbox.remove(new Servant()); strongbox.remove(new Shield());
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources(), strongbox);
    }

    /**
     * this method test if the basic production is activated correctly, the player takes one resource from strongbox and one from leaderShelf
     */
    @Test
    public void testBasicProductionFromStrongboxAndWarehouse(){
        CollectionResources empty= new CollectionResources();
        CollectionResources secondShelf= new CollectionResources(); secondShelf.add(new Servant());
        CollectionResources collectionB= new CollectionResources();
        collectionB.add(new Coin());
        CollectionResources strongbox= new CollectionResources();
        strongbox.add(new Shield()); strongbox.add(new Shield()); strongbox.add(new Shield());
        ShelfCollection collection2 = new ShelfCollection(ResourceType.SERVANT);
        collection2.add(new Servant()); collection2.add(new Servant());
        CollectionResources payWarehouse = new CollectionResources(); CollectionResources payStrongbox = new CollectionResources();
        payWarehouse.add(new Servant()); payStrongbox.add(new Shield());
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(strongbox);// insert resources in strongbox
        game.insertInWarehouse(2, new Servant() ,collection2);// insert resources in warehouse
        game.activateBasicProduction(payWarehouse,payStrongbox,new Coin());
        assertEquals(game.getActualPlayer().getPersonalDashboard().getBuffer() , collectionB);
        strongbox.remove(new Shield());
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().getStrongboxResources(), strongbox);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(1).getResources(), empty);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(2).getResources(), secondShelf);
        assertEquals(game.getActualPlayer().getPersonalDashboard().getPersonalWarehouse().getShelf(3).getResources(), empty);
    }




    //--------LEADER PRODUCTIONS----------------------------------------------------------------------------------------

    /**
     * this test create a game with one player, adds him a leader production,
     * then verify that the method return false because the player
     * does not contain any resource
     */
    @Test
    void testCheckActivateLeaderProduction1(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        // create the leader production
        game.getActualPlayer().addLeaderProduction(new Coin());

        assertFalse(game.checkActivateLeaderProduction(1, true));
        assertFalse(game.checkActivateLeaderProduction(1, false));
    }

    /**
     * this test create a game with one player, adds him a leader production,
     * adds to his warehouse the resource needed to activate the production
     * then verify that the method return false for the strongbox
     * true for the warehouse
     */
    @Test
    void testCheckActivateLeaderProduction2(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        // create the leader production
        game.getActualPlayer().addLeaderProduction(new Coin());
        //adds the resource needed to activate the production in warehouse
        CollectionResources toAddInWarehouse = new CollectionResources(); toAddInWarehouse.add(new Coin());
        game.insertInWarehouse(1, new Coin(), toAddInWarehouse);

        assertTrue(game.checkActivateLeaderProduction(1, true));
        assertFalse(game.checkActivateLeaderProduction(1, false));
    }

    /**
     * this test create a game with one player, adds him a leader production,
     * adds to his strongbox the resource needed to activate the production
     * then verify that the method return true for the strongbox
     * false for the warehouse
     */
    @Test
    void testCheckActivateLeaderProduction3(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        // create the leader production
        game.getActualPlayer().addLeaderProduction(new Coin());
        //adds the resource needed to activate the production in strongbox
        CollectionResources toAddInStrongbox= new CollectionResources(); toAddInStrongbox.add(new Coin());
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(toAddInStrongbox);

        assertTrue(game.checkActivateLeaderProduction(1, false));
        assertFalse(game.checkActivateLeaderProduction(1, true));
    }

    /**
     * this test create a game with one player, adds him a leader production,
     * adds to his strongbox and warehouse the resource needed to activate the production
     * then verify that the method return true for the strongbox
     * true for the warehouse
     */
    @Test
    void testCheckActivateLeaderProduction4(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        // create the leader production
        game.getActualPlayer().addLeaderProduction(new Coin());
        //adds the resource needed to activate the production in warehouse
        CollectionResources toAddInWarehouse = new CollectionResources(); toAddInWarehouse.add(new Coin());
        game.insertInWarehouse(1, new Coin(), toAddInWarehouse);
        //adds the resource needed to activate the production in strongbox
        CollectionResources toAddInStrongbox= new CollectionResources(); toAddInStrongbox.add(new Coin());
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(toAddInStrongbox);

        assertTrue(game.checkActivateLeaderProduction(1, true));
        assertTrue(game.checkActivateLeaderProduction(1, false));
    }

    /**
     * this test create a game with one player, adds him a coin leader production,
     * adds to his warehouse a coin, activate that production and verify
     * that the warehouse coin got removed correctly and the resources got
     * added correctly to the buffer
     */
    @Test
    void testActivateLeaderProduction1(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        // create the leader production
        game.getActualPlayer().addLeaderProduction(new Coin());
        //adds the resource needed to activate the production in warehouse
        CollectionResources toAddInWarehouse = new CollectionResources(); toAddInWarehouse.add(new Coin());
        game.insertInWarehouse(1, new Coin(), toAddInWarehouse);
        // activate the production
        game.activateLeaderProduction(1, new Stone(), true);

        CollectionResources output = new CollectionResources(); output.add(new Stone());
        assertTrue(game.getActualPlayer().getTotalResources().asList().isEmpty());
        assertEquals(output, game.getActualPlayer().getPersonalDashboard().getBuffer());

    }

    /**
     * this test create a game with one player, adds him a coin leader production,
     * adds to his warehouse a coin, activate that production and verify
     * that the warehouse coin got removed correctly and the resources got
     * added correctly to the buffer
     */
    @Test
    void testActivateLeaderProduction2(){
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        // create the leader production
        game.getActualPlayer().addLeaderProduction(new Coin());
        //adds the resource needed to activate the production in strongbox
        CollectionResources toAddInStrongbox= new CollectionResources(); toAddInStrongbox.add(new Coin());
        game.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(toAddInStrongbox);

        // activate the production
        game.activateLeaderProduction(1, new Stone(), false);

        CollectionResources output = new CollectionResources(); output.add(new Stone());
        assertTrue(game.getActualPlayer().getTotalResources().asList().isEmpty());
        assertEquals(output, game.getActualPlayer().getPersonalDashboard().getBuffer());
    }

    /**
     * this test create a game with one person, and verify that
     * the method checkLeaderCard return true only for 1 and 2,
     * then discard a leader card and verify that return true only fro 1
     * then discard the last leader card an verify that return always false
     */
    @Test
    void testCheckLeaderCard() {
        //initialise the game
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        game.initialiseGame(game.getPlayers().get(0).getNickname(), new CollectionResources(), 1, 3);
        // verify the corner cases
        assertFalse(game.checkLeaderCard(-1));
        assertFalse(game.checkLeaderCard(0));
        assertTrue(game.checkLeaderCard(1));
        assertTrue(game.checkLeaderCard(2));
        assertFalse(game.checkLeaderCard(3));
        assertFalse(game.checkLeaderCard(4));
        //discard a leader card
        game.discardLeaderCard(1);

        assertTrue(game.checkLeaderCard(1));
        assertFalse(game.checkLeaderCard(2));
        // discard the last leader card
        game.discardLeaderCard(1);

        assertFalse(game.checkLeaderCard(1));
        assertFalse(game.checkLeaderCard(2));
    }


}