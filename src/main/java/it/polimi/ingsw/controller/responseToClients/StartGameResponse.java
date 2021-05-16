package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class represent the start game response.
 * in particular, when the game start, everything have to be sent in broadcast:
 * the cards market, the marble market, the lonely marble, the solo token,
 * the actual player and the opponents
 * */
public class StartGameResponse extends ResponseToClient{

    /**
     * this attribute represent the cards market to update
     */
    private DevelopmentCard[][] cardsMarket1;

    /**
     * this attribute represent the marble market to update
     */
    private Marble[][] marbleMarket1;

    /**
     * this attribute represent the lonely marble to update
     */
    private Marble lonelyMarble1;

    /**
     * this attribute represent the token to update
     */
    private SoloToken soloToken;

    /**
     * this attribute represent the actual player to update
     */
    private ThinPlayer actualPlayer;

    /**
     * this attribute represent the list of opponents to update
     */
    private List<ThinPlayer> opponents;

    /**
     * this constructor create the response starting from the client, getting
     * all the data needed to set the attributes.
     * it also send a message with the possible commands, that will be empty
     * for the players that don't play the turn, will be full of possible action
     * for the player that plays the turn
     *
     * @param client this is the client from which get the data
     */
    public StartGameResponse(ClientHandler client){
        super(Status.ACCEPTED);

        Game game = client.getGame();

        updateMarbleMarket(game);
        updateCardsMarket(game);
        updateSoloToken(game);
        updatePlayers(game, client.getNickname());
    }

    /**
     * this method update the state of a client.
     * in particular, it shows the message sent from the server and,
     * if the command is not of dynamic type ResponseToClient,
     * set the values that have to change after a model's change
     *
     * @param client this is the client to update
     */
    @Override
    public void updateClient(Client client) {
        client.createGame(cardsMarket1, marbleMarket1, lonelyMarble1, soloToken, actualPlayer, opponents);
        /*
        client.getGame().setCardsMarket(cardsMarket1);
        client.getGame().setMarbleMarket(marbleMarket1);
        client.getGame().setLonelyMarble(lonelyMarble1);
        client.getGame().setSoloToken(soloToken);
        client.getGame().setMyself(actualPlayer);
        // this part of code is used to use the constructor that recreate the leader cards from the thin ones
        List<ThinPlayer> opponents = this.opponents.stream().
                map(ThinPlayer::new).
                collect(Collectors.toList());
        client.getGame().setOpponents(opponents);

         */
        client.show();


        super.updateClient(client);
    }

    /**
     * this method update the players.
     * in particular, the parameter nickname is the nickname of the player that have
     * to be set as actualPlayer, and every other player will be collected into a list,
     * and every leader card inactive will be hidden.
     * if there is a single game running, the opponents will consist into a singleton list
     * containing lorenzo il magnifico
     * @param game this is the game from which get the data to initialise the actualPlayer
     *             and the opponents
     * @param nickname this is the nickname of the player to change
     */
    private void updatePlayers(Game game, String nickname){
        // create the thin single player relative to the player that is playing the turn
        actualPlayer = new ThinPlayer(game.findPlayer(nickname));
        // if the game is a single game
        if (game.getPlayers().size() == 1){
            ThinPlayer lorenzo = new ThinPlayer(game.getLorenzoIlMagnifico());
            opponents = new ArrayList<>(Collections.singletonList(lorenzo));
        } // if the game is not a single game
        else{
            opponents = game.getPlayers().stream().
                    filter(player -> !game.findPlayer(nickname).equals(player)).
                    map(ThinPlayer::new).
                    collect(Collectors.toList());
            hideLeaderCards(opponents);
        }

    }

    /**
     * this method update the marble market, in particular, get from the game
     * the data and set them to the attributes marbleMarket and lonelyMarble
     * @param game this is the game from which get the data to update the marble market
     */
    private void updateMarbleMarket(Game game){
        marbleMarket1 = game.getMarketBoard().getMarketTray();
        lonelyMarble1 = game.getMarketBoard().getLonelyMarble();
    }

    /**
     * this method update the cards market, in particular, get from the game
     * the data and set them to the attribute cardsMarket
     * @param game this is the game from which get the data to update the cards market
     */
    private void updateCardsMarket(Game game){
        cardsMarket1 = game.getSetOfCard().show();
    }

    /**
     * this method update the marble market, in particular, get from the game
     * the data and set them to the attribute token
     * if the game isn't a single game, the solo token will be null
     *
     * @param game this is the game from which get the data to update the token
     */
    private void updateSoloToken(Game game){
        try {
            soloToken = game.getSoloTokens().get(game.getSoloTokens().size() - 1);
        } catch (IndexOutOfBoundsException | NullPointerException e){ // when there isn't a singleGame
            soloToken = null;
        }
    }


}
