package it.polimi.ingsw.model;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.Coin;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.model.SingleGame.CardToken;
import it.polimi.ingsw.model.SingleGame.SingleGame;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.model.SingleGame.TrackToken;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * this class test the singeGame
 */
class SingleGameTest {

    Game singleGame = new SingleGame(Collections.singletonList("player"));

    /**
     * this test verify that the creation of a singleGame is correct
     */
    @Test
    void testConstructor(){
        // initialise the game
        Game test = new SingleGame(Collections.singletonList("player"));
        test.initialiseGame("player", new CollectionResources(), 1, 3);
        assertEquals("player", test.getPlayers().get(0).getNickname());
        assertEquals("player", test.getActualPlayer().getNickname());
        assertThrows(IndexOutOfBoundsException.class , () -> test.getPlayers().get(1));
        assertEquals("LorenzoIlMagnifico", test.getLorenzoIlMagnifico().getNickname());
        List<SoloToken> soloTokens = new ArrayList<>();
        soloTokens.add(new TrackToken(2, false));
        soloTokens.add(new TrackToken(2, false));
        soloTokens.add(new TrackToken(1, true));
        soloTokens.add(new CardToken(CardColor.BLUE));
        soloTokens.add(new CardToken(CardColor.YELLOW));
        soloTokens.add(new CardToken(CardColor.GREEN));
        soloTokens.add(new CardToken(CardColor.PURPLE));
        assertTrue(test.getSoloTokens().containsAll(soloTokens));
        assertTrue(soloTokens.containsAll(test.getSoloTokens()));
    }

    /**
     * this test create a single game, and verify that the method does not throw EndGameException,
     * then remove all the card except one of a specified color from the CardsMarket and
     * verify that the method does not throw EndGameException, then remove the last card and
     * verify that the method throws EndGameException
     */
    @Test
    void testCheckEndGame(){
        // initialise the game
        singleGame.initialiseGame("player", new CollectionResources(), 1, 3);
        assertDoesNotThrow(() -> singleGame.checkEndGame());
        // remove every card of a color except for one
        // cards of level 3
        singleGame.getSetOfCard().popCard(3, CardColor.PURPLE);
        singleGame.getSetOfCard().popCard(3, CardColor.PURPLE);
        singleGame.getSetOfCard().popCard(3, CardColor.PURPLE);
        singleGame.getSetOfCard().popCard(3, CardColor.PURPLE);
        // cards of level 2
        singleGame.getSetOfCard().popCard(2, CardColor.PURPLE);
        singleGame.getSetOfCard().popCard(2, CardColor.PURPLE);
        singleGame.getSetOfCard().popCard(2, CardColor.PURPLE);
        singleGame.getSetOfCard().popCard(2, CardColor.PURPLE);
        // cards of level 2
        singleGame.getSetOfCard().popCard(1, CardColor.PURPLE);
        singleGame.getSetOfCard().popCard(1, CardColor.PURPLE);
        singleGame.getSetOfCard().popCard(1, CardColor.PURPLE);
        assertDoesNotThrow(() -> singleGame.checkEndGame());
        // pop the last card from the column
        singleGame.getSetOfCard().popCard(1, CardColor.PURPLE);
        assertThrows(EndGameException.class ,() -> singleGame.checkEndGame());
    }

    /**
     * this test verify that the method addFaithPointsExceptTo works correctly,
     * adding 3 faith points to Lorenzo
     */
    @Test
    void testAddFaithPointsExceptTo(){
        // initialise the game
        singleGame.initialiseGame("player", new CollectionResources(), 1, 3);
        assertDoesNotThrow(() -> singleGame.checkEndGame());
        // adds 3 points indirectly to Lorenzo
        singleGame.addFaithPointsExceptTo(singleGame.getActualPlayer(), 3);

        assertEquals(3, singleGame.getLorenzoIlMagnifico().getPersonalTrack().getPosition());
    }

    /**
     * this test create a single game,
     * adds 5 faith points to the player, 8 to lorenzoIlMagnifico and verify that
     * the fist vatican report got activated from both, then
     * adds 3 faith points to the player, 13 to lorenzoIlMagnifico and verify that
     * the second vatican report got activated only from lorenzo, then
     * adds 13 faith points to the player and verify that trows the EndGameException and that
     * the third vatican report got activated only from the player
     */
    @Test
    void testHandleVaticanReport(){
        // initialise the game
        singleGame.initialiseGame("player", new CollectionResources(), 1, 3);
        RealPlayer realPlayer = singleGame.getActualPlayer();
        Player lorenzo = singleGame.getLorenzoIlMagnifico();
        // adds 5 faith points to the actual player and 8 to lorenzo
        singleGame.addFaithPointsExceptTo(realPlayer, 5); // lorenzoPosition = 5
        singleGame.addFaithPointsTo(realPlayer, 8); //playerPosition = 8

        assertTrue(singleGame.getVaticanReports()[0]);
        assertTrue(realPlayer.getPersonalTrack().getPopeFavorTiles(1).getActive());
        assertTrue(lorenzo.getPersonalTrack().getPopeFavorTiles(1).getActive());
        // adds 3 faith points to the actual player and 13 to lorenzo
        singleGame.addFaithPointsExceptTo(realPlayer, 13); //lorenzoPosition = 18
        singleGame.addFaithPointsTo(realPlayer, 3); // player position = 11

        assertTrue(singleGame.getVaticanReports()[1]);
        assertFalse(realPlayer.getPersonalTrack().getPopeFavorTiles(2).getActive());
        assertTrue(lorenzo.getPersonalTrack().getPopeFavorTiles(2).getActive());
        // adds 13 faith points to the actual player and verify that throws EndGameException
        assertThrows(EndGameException.class, () -> singleGame.addFaithPointsTo(realPlayer, 13)); //player position = 24
        assertTrue(singleGame.getVaticanReports()[2]);
        assertTrue(realPlayer.getPersonalTrack().getPopeFavorTiles(3).getActive());
        assertFalse(lorenzo.getPersonalTrack().getPopeFavorTiles(3).getActive());
    }

    /**
     * this test create a single game, buy 6 cards for the player, and
     * after he has bought the 7° card, verify that trows an EndGameException,
     * and that the card got placed correctly into the dashboard
     */
    @Test
    void buyCard(){
        // initialise the game
        singleGame.initialiseGame("player", new CollectionResources(), 1, 3);
        //creating a discount to pay cards gratis
        singleGame.getActualPlayer().addDiscount(new Coin());
        // creating the cost of a card
        CollectionResources cost = new CollectionResources();
        cost.add(new Coin());
        //adding it to the strongbox to ensure the buyCard works correctly
        singleGame.getActualPlayer().getPersonalDashboard().getPersonalStrongbox().addResources(cost);
        // creating 7 cards
        DevelopmentCard card1blue = new DevelopmentCard(CardColor.BLUE, 1, 0, cost, null, null, 0);
        DevelopmentCard card1green = new DevelopmentCard(CardColor.GREEN, 1, 0, cost, null, null, 0);
        DevelopmentCard card2green = new DevelopmentCard(CardColor.GREEN, 2, 0, cost, null, null, 0);
        DevelopmentCard card3green = new DevelopmentCard(CardColor.GREEN, 3, 0, cost, null, null, 0);
        DevelopmentCard card1yellow = new DevelopmentCard(CardColor.YELLOW, 1, 0, cost, null, null, 0);
        DevelopmentCard card2yellow = new DevelopmentCard(CardColor.YELLOW, 2, 0, cost, null, null, 0);
        DevelopmentCard card3yellow = new DevelopmentCard(CardColor.YELLOW, 3, 0, cost, null, null, 0);
        // add the cards to the cardsMarket
        List<DevelopmentCard>[][] cardsMarket = singleGame.getSetOfCard().getCardMatrix();
        //green cards
        cardsMarket[2][0].remove(3);
        cardsMarket[2][0].add(card1green);
        cardsMarket[1][0].remove(3);
        cardsMarket[1][0].add(card2green);
        cardsMarket[0][0].remove(3);
        cardsMarket[0][0].add(card3green);
        // yellow cards
        cardsMarket[2][2].remove(3);
        cardsMarket[2][2].add(card1yellow);
        cardsMarket[1][2].remove(3);
        cardsMarket[1][2].add(card2yellow);
        cardsMarket[0][2].remove(3);
        cardsMarket[0][2].add(card3yellow);
        // blue card
        cardsMarket[2][1].remove(3);
        cardsMarket[2][1].add(card1blue);
        // buying the cards
        // green cards
        singleGame.buyCard(1, CardColor.GREEN, 1 , new CollectionResources());
        singleGame.buyCard(2, CardColor.GREEN, 1 , new CollectionResources());
        singleGame.buyCard(3, CardColor.GREEN, 1 , new CollectionResources());
        // yellow cards
        singleGame.buyCard(1, CardColor.YELLOW, 2 , new CollectionResources());
        singleGame.buyCard(2, CardColor.YELLOW, 2 , new CollectionResources());
        singleGame.buyCard(3, CardColor.YELLOW, 2 , new CollectionResources());
        // buy the last card and verify that trows the exception
        assertThrows(EndGameException.class, () -> singleGame.buyCard(1, CardColor.BLUE, 3 , new CollectionResources()));
        assertTrue(singleGame.containsInStrongbox(cost));
        // verify that the player bought the card
        assertEquals(card1blue, singleGame.getActualPlayer().getPersonalDashboard().getPersonalProductionPower().getCard(3));
    }

    /**
     *this test verifies that the method action in a CardToken deletes two cards from the market and and that the
     * endTurn method manages the deck correctly
     */
    @Test
    void testAction1() {
        singleGame.getSoloTokens().remove(6);
        singleGame.getSoloTokens().add(new CardToken(CardColor.BLUE));
        List<DevelopmentCard>[][] cardsMarket = singleGame.getSetOfCard().getCardMatrix();
        assertDoesNotThrow( () -> cardsMarket[2][1].get(2));
        singleGame.endTurn();
        assertThrows(IndexOutOfBoundsException.class, () -> cardsMarket[2][1].get(2));
        assertEquals(singleGame.getSoloTokens().get(0), new CardToken(CardColor.BLUE));
    }
    /**
     *this test verifies that the method action in a TrackToken with faithPoints 2 increases the position of Lorenzo Il
     * Magnifico in his Track of 2 and that the endTurn method manages the deck correctly
     */
    @Test
    void testAction2() {
        singleGame.getSoloTokens().remove(6);
        singleGame.getSoloTokens().add(new TrackToken(2,false));
        assertEquals(0, singleGame.getLorenzoIlMagnifico().getPersonalTrack().getPosition());
        singleGame.endTurn();
        assertEquals(2, singleGame.getLorenzoIlMagnifico().getPersonalTrack().getPosition());
        assertEquals(singleGame.getSoloTokens().get(0), new TrackToken(2,false));
    }

    /**
     *this test verifies that the method action in a TrackToken with faithPoints 1 increases the position of Lorenzo Il
     *Magnifico in his Track of 1
     */
    @Test
    void testAction3() {
        singleGame.getSoloTokens().remove(6);
        singleGame.getSoloTokens().add(new TrackToken(1,true));
        assertEquals(0, singleGame.getLorenzoIlMagnifico().getPersonalTrack().getPosition());
        singleGame.endTurn();
        assertEquals(1, singleGame.getLorenzoIlMagnifico().getPersonalTrack().getPosition());
    }


    /**
     *this test verifies that the method action in a CardToken trows an EndGameException if there are only two card
     * which are removed in a column of the market
     */
    @Test
    void testAction4() {
        int i;
        singleGame.getSoloTokens().remove(6);
        singleGame.getSoloTokens().add(new CardToken(CardColor.BLUE));
        List<DevelopmentCard>[][] cardsMarket = singleGame.getSetOfCard().getCardMatrix();
        for (i=0;i<=2;i++) {
            cardsMarket[0][1].remove(0);
        }
        for (i=0;i<=3;i++) {
            cardsMarket[1][1].remove(0);
        }
        for (i=0;i<=2;i++) {
            cardsMarket[2][1].remove(0);
        }
        assertThrows(EndGameException.class, ()-> singleGame.endTurn());
        assertTrue(cardsMarket[2][1].isEmpty());
        assertTrue(cardsMarket[0][1].isEmpty());
    }

    /**

     * this method is used to test the getWinner() of multiGame and singleGame.
     * We do some turns of the game and then we check what is the player that is winning
     * in that moment.This is the single game test
     */
    @Test
    public void testWinnerOfGame1() {
        Game game = new SingleGame(Collections.singletonList("player"));
        game.initialiseGame("player", new CollectionResources(), 1, 3);

        game.addFaithPointsExceptTo(game.getPlayers().get(0), 15);

        DevelopmentCard card1 = new DevelopmentCard(CardColor.GREEN, 1, 2, null, null, null, 0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.GREEN, 2, 5, null, null, null, 0);
        DevelopmentCard card3 = new DevelopmentCard(CardColor.GREEN, 3, 10, null, null, null, 0);
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card1, 1);
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card2, 1);
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card3, 1);

        game.addFaithPointsTo(game.getPlayers().get(0), 15); game.addFaithPointsTo(game.getPlayers().get(0), 8);
        game.addFaithPointsExceptTo(game.getPlayers().get(0), 8);
        assertThrows(EndGameException.class, ()-> game.addFaithPointsExceptTo(game.getPlayers().get(0), 1));
        assertEquals(game.getLorenzoIlMagnifico().getNickname(), game.getWinner());
    }

    /**
     * this method is used to test the getWinner() of multiGame and singleGame.
     * We do some turns of the game and then we check what is the player that is winning
     * in that moment.This is the single game test
     */
    @Test
    public void testWinnerOfGame2() {
        Game game = new SingleGame(Collections.singletonList("player"));
        game.initialiseGame("player", new CollectionResources(), 1, 3);

        game.addFaithPointsTo(game.getPlayers().get(0), 15); game.addFaithPointsTo(game.getPlayers().get(0), 8);
        assertNotEquals(game.getPlayers().get(0).getNickname(), game.getWinner());
        assertThrows(EndGameException.class, ()-> game.addFaithPointsTo(game.getPlayers().get(0), 1));
        assertEquals(game.getPlayers().get(0).getNickname(), game.getWinner());
    }

    /**
     * this method is used to test the getWinner() of multiGame and singleGame.
     * We do some turns of the game and then we check what is the player that is winning
     * in that moment.This is the single game test
     */
    @Test
    public void testWinnerOfGame3() {
        DevelopmentCard card1 = new DevelopmentCard(CardColor.GREEN, 1, 2, null, null, null, 0);
        DevelopmentCard card2 = new DevelopmentCard(CardColor.GREEN, 2, 5, null, null, null, 0);
        DevelopmentCard card3 = new DevelopmentCard(CardColor.GREEN, 3, 8, null, null, null, 0);
        DevelopmentCard card4 = new DevelopmentCard(CardColor.GREEN, 1, 2, null, null, null, 0);
        DevelopmentCard card5 = new DevelopmentCard(CardColor.GREEN, 2, 5, null, null, null, 0);
        DevelopmentCard card6 = new DevelopmentCard(CardColor.GREEN, 3, 8, null, null, null, 0);
        DevelopmentCard card7 = new DevelopmentCard(CardColor.GREEN, 1, 12, null, null, null, 0);
        Game game = new SingleGame(Collections.singletonList("player"));
        game.initialiseGame("player", new CollectionResources(), 1, 3);
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card1, 1);
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card2, 1);
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card3, 1);
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card4, 2);
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card5, 2);
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card6, 2);
        assertNotEquals(game.getPlayers().get(0).getNickname(), game.getWinner());
        game.getPlayers().get(0).getPersonalDashboard().placeDevelopmentCard(card7, 3);
        assertEquals(game.getPlayers().get(0).getNickname(), game.getWinner());
    }


}