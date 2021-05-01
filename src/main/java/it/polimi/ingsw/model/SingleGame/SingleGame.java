package it.polimi.ingsw.model.SingleGame;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.EndGameException;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.CollectionResources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SingleGame extends Game {

    private final List<SoloToken> soloTokens;
    private final Player lorenzoIlMagnifico;


    /**
     * this is the constructor of the class, it initialize the three boolean of vatican report
     * at false; set the nickname of players; initialize randomly MarbleMarket and CardsMarket, create all leaderCards and assign four
     * of them at the player, then initialize lorenzoIlMagnifico and all the soloTokens (7) in random order
     *
     * @param nicknames this is the nickname of the single player
     */
    public SingleGame(List<String> nicknames) {
        super(nicknames);
        lorenzoIlMagnifico = new Player();
        soloTokens = new ArrayList<>();
        soloTokens.add(new TrackToken(2, false));
        soloTokens.add(new TrackToken(2, false));
        soloTokens.add(new TrackToken(1, true));
        soloTokens.add(new CardToken(CardColor.BLUE));
        soloTokens.add(new CardToken(CardColor.YELLOW));
        soloTokens.add(new CardToken(CardColor.GREEN));
        soloTokens.add(new CardToken(CardColor.PURPLE));
        Collections.shuffle(soloTokens);
    }

    public Player getLorenzoIlMagnifico() {
        return lorenzoIlMagnifico;
    }

    @Override
    public List<SoloToken> getSoloTokens() {
        return soloTokens;
    }

    /**
     * this method call the method action of the last token in the deck, if this return true it shuffles the deck else
     * it inserts the token caught in the first position of the deck
     * @throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     *                          or when a column of the cardsMarket is empty
     */
    @Override
    public void endTurn() throws EndGameException{
        SoloToken temp;
        if (soloTokens.get(6).action(this, super.getSetOfCard()))
            Collections.shuffle(soloTokens);
        else {
            temp = soloTokens.remove(6);
            soloTokens.add(0, temp);
        }
        checkEndGame();
    }

    /**
     *this method verifies if the there are the conditions to end the game: it checks the conditions of the superclass
     * and furthermore and if there is a empty column in CardsMarket
     * @throws EndGameException when a player reach the final vatican report, or a player buy more than 6 development cards,
     *                          or when a column of the cardsMarket is empty
     */
    @Override
    public void checkEndGame() throws EndGameException{
        if ( super.getSetOfCard().checkMissColumn()) throw new EndGameException("Game Finished");
        super.checkEndGame();
    }

    /**
     * this method is used to add faithPoints to LorenzoIlMagnifico
     * @param actualPlayer is the single player
     * @param toAdd is the number of faithTrack position to add to lorenzoIlMagnifico
     * @throws EndGameException when a player reach the final vatican report
     */
    public void addFaithPointsExceptTo (RealPlayer actualPlayer, int toAdd) throws EndGameException{
        lorenzoIlMagnifico.addFaithPoints(toAdd);
        handleVaticanReport();
    }

    /**
     * like in the superclass but at the end it calls checkEndGame to end the game immediately if it needs to be ended
     * @param level this is the level of the card to buy
     * @param color this is the color of the card to buy
     * @param position this is the position in which place the card in the dashboard
     * @param toPayFromWarehouse these are the resources to pay from warehouse
     * @throws EndGameException when a player buy more than 6 development cards
     */
    @Override
    public synchronized void buyCard(int level, CardColor color, int position, CollectionResources toPayFromWarehouse) throws EndGameException{
        super.buyCard(level, color, position, toPayFromWarehouse);
        checkEndGame();
    }

    /**
     *this method handle the vatican reports like in the superclass but for only the single player and
     * lorenzoIlMagnifico. At the end it calls checkEndGame to end the game immediately if it needs to be ended
     * @throws EndGameException when a player reach the final vatican report
     */
    @Override
    protected synchronized void handleVaticanReport() throws EndGameException{
        boolean [] singleVaticanReports = super.getVaticanReports();
        int i=0;
        while((singleVaticanReports[i]) && (i < 3)) i++;
        if (i > 2) return;
        i++;
        if (getActualPlayer().checkVaticanReport(i) || lorenzoIlMagnifico.checkVaticanReport(i)) {
            setVaticanReports(i);
            getActualPlayer().vaticanReport(i);
            lorenzoIlMagnifico.vaticanReport(i);
        }
        checkEndGame();
    }

    /**
     * this method return the nickname of the winner.
     * in particular, it get the nickname of the player with more victory points,
     * if there are some players with the same number of victory points, the method return
     * the player with more victory points and more resources,
     * if there are some players with the same amount of resources, return
     * a random player.
     * if there is a single game, it gets the single player nickname when
     * his number of cards is >= 7 or if his position reached the last vatican report
     *
     * @return the nickname of the winner
     */
    @Override
    public String getWinner() {
        // if the player has more that 7 cards in his dashboard or if he reached the last vatican report
        if (getActualPlayer().getPersonalDashboard().getPersonalProductionPower().getNumOfCards() >= 7 ||
                getActualPlayer().getPersonalTrack().getPopeFavorTiles()[2].getActive())
        // get the nickname player
        return getActualPlayer().getNickname();
        // else

        // get the nickname of lorenzoIlMagnifico
        return lorenzoIlMagnifico.getNickname();
    }
}