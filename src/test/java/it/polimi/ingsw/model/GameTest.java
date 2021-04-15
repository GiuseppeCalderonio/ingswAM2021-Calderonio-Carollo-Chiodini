package it.polimi.ingsw.model;

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

    @Test
    public void TestCorrectTurn() {
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second"));
        game = new Game(nicknames);
        assertEquals(-2, game.getTurnManager());
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 2);
        assertEquals(-1, game.getTurnManager());
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 3, 1);
        assertEquals(0, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(0).getNickname(), game.getActualPlayer().getNickname());
        game.endTurn();
        assertEquals(1, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(1).getNickname(), game.getActualPlayer().getNickname());
        game.endTurn();
        assertEquals(0, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(0).getNickname(), game.getActualPlayer().getNickname());
        game.endTurn();
        assertEquals(1, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(1).getNickname(), game.getActualPlayer().getNickname());
        game.endTurn();
        assertEquals(0, game.getTurnManager() % 2);
        assertEquals(game.getPlayers().get(0).getNickname(), game.getActualPlayer().getNickname());
    }

    /**
     * this method check if the player select the correct number of shelf, we suppose that the player hasn't
     * leaderShelf
     */
    @Test
    public void TestShelfSelected() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 2);
        assertEquals(3, game.getPlayers().get(0).getPersonalDashboard().getPersonalWarehouse().getNumOfShelves());
        game.getActualPlayer().addLeaderShelf(new Coin());
        assertEquals(4, game.getPlayers().get(0).getPersonalDashboard().getPersonalWarehouse().getNumOfShelves());
        game.getActualPlayer().addLeaderShelf(new Shield());
        assertEquals(5, game.getPlayers().get(0).getPersonalDashboard().getPersonalWarehouse().getNumOfShelves());
        game.getActualPlayer().addLeaderShelf(new Servant());
        assertEquals(5, game.getPlayers().get(0).getPersonalDashboard().getPersonalWarehouse().getNumOfShelves());
    }

    /**
     * this method create a game with only one player, and check the shift resource without the loose of resource
     */
    @Test
    public void TestShiftResources1() {
        List<String> nicknames = new ArrayList<>(Collections.singletonList("first"));
        game = new Game(nicknames);
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin()); collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SHIELD);
        collection1.add(new Shield()); collection1.add(new Shield());
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
        collection.add(new Coin()); collection.add(new Coin());
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
        collection.add(new Coin()); collection.add(new Coin());
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
    public void TestShiftResources4(){
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin()); collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SHIELD);
        collection1.add(new Shield());
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        game.getActualPlayer().addLeaderShelf(new Coin());
        game.getActualPlayer().addResourcesToWarehouse(collection, 4);
        game.getActualPlayer().addResourcesToWarehouse(collection1,1);
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
    public void TestShiftResources5(){
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin()); collection.add(new Coin()); collection.add(new Coin());
        ShelfCollection collection1 = new ShelfCollection(ResourceType.SHIELD);
        collection1.add(new Shield()); collection1.add(new Shield());
        ShelfCollection collection2 = new ShelfCollection(ResourceType.SERVANT);
        collection2.add(new Servant());
        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        game.getActualPlayer().addResourcesToWarehouse(collection , 3);
        game.getActualPlayer().addResourcesToWarehouse(collection1,2);
        game.getActualPlayer().addResourcesToWarehouse(collection2,1);
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
    public void TestShiftResources6(){
        List<String> nicknames = new ArrayList<>(Arrays.asList("first", "second", "third"));
        game = new Game(nicknames);
        CollectionResources toInitialise = new CollectionResources();
        CollectionResources toInitialise2 = new CollectionResources();
        CollectionResources toInitialise3 = new CollectionResources();


        ShelfCollection collection = new ShelfCollection(ResourceType.COIN);
        collection.add(new Coin());

        ShelfCollection collection1 = new ShelfCollection(ResourceType.SHIELD);
        collection1.add(new Shield()); collection1.add(new Shield());

        ShelfCollection collection2 = new ShelfCollection(ResourceType.SERVANT);
        collection2.add(new Servant());

        game.initialiseGame(game.getPlayers().get(0).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Servant());
        game.initialiseGame(game.getPlayers().get(1).getNickname(), toInitialise, 1, 3);
        game.initialiseGame(game.getPlayers().get(2).getNickname(), toInitialise, 1, 3);
        toInitialise.add(new Coin()); toInitialise2.add(new Servant());
        toInitialise3.add(new Shield());   toInitialise3.add(new Shield());

        game.insertInWarehouse(3,new Coin() , toInitialise);
        game.insertInWarehouse(2,new Servant() , toInitialise2);
        game.insertInWarehouse(1,new Shield() , toInitialise3);

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
}