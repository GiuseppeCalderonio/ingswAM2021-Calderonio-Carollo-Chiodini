package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinLeaderCard;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class StartGameResponse extends ResponseToClient{


    private DevelopmentCard[][] cardsMarket1;
    private Marble[][] marbleMarket1;
    private Marble lonelyMarble1;
    private SoloToken soloToken;
    private ThinPlayer actualPlayer;
    private List<ThinPlayer> opponents;

    public StartGameResponse(ClientHandler client, String message, List<String> possibleCommands){
        super(message, possibleCommands );

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
        client.setCardsMarket(cardsMarket1);
        client.setMarbleMarket(marbleMarket1);
        client.setLonelyMarble(lonelyMarble1);
        client.setSoloToken(soloToken);
        client.setMyself(actualPlayer);
        client.setOpponents(opponents);
        client.show();


        super.updateClient(client);
    }

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

    private void updateMarbleMarket(Game game){
        marbleMarket1 = game.getMarketBoard().getMarketTray();
        lonelyMarble1 = game.getMarketBoard().getLonelyMarble();
    }

    private void updateCardsMarket(Game game){
        cardsMarket1 = game.getSetOfCard().show();
    }

    private void updateSoloToken(Game game){
        try {
            soloToken = game.getSoloTokens().get(game.getSoloTokens().size() - 1);
        } catch (IndexOutOfBoundsException | NullPointerException e){ // when there isn't a singleGame
            soloToken = null;
        }
    }

    /**
     * this method hide the leader cards of a thin player, in fact the cards
     * of a player different from the owner, when another player did not activate
     * a leader card, should not be visible
     * @param players these are the player with the leader cards to hide
     */
    private void hideLeaderCards(List<ThinPlayer> players){
        players.stream().
                flatMap(player -> player.getThinLeaderCards().stream()).
                filter(leaderCard -> !leaderCard.isActive()).
                forEach(ThinLeaderCard::hide);
    }
}
