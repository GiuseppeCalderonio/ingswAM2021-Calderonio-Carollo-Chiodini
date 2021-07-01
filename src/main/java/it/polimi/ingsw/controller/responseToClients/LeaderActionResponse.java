package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.thinModelComponents.ThinLeaderCard;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * this class represent the leader action response.
 * in particular, when a client do a leader action, can
 * change his leader cards (activating or discarding), and can
 * advance in the faith track (discarding), so this data have to be sent
 */
public class LeaderActionResponse extends ResponseToClient{

    /**
     * this attribute represent the nickname of the player to update
     */
    private final String nickname4;

    /**
     * this attribute represent the faith track of the player to update
     */
    private final Map<String, ThinTrack> track4;

    /**
     * this attribute represent the leader cards of the player to update
     */
    private final List<ThinLeaderCard> cards4;

    /**
     * this attribute represent the thin warehouse to update
     */
    private final ThinWarehouse warehouse4;

    /**
     * this constructor create the response starting from the client,
     * getting from him all the data needed and setting all of them.
     * it also hide the leader cards if the nickname of the actual player
     * of the game is not equal with the nickname of the client
     * passed in input
     *
     * @param client this is the client that send the response
     */
    public LeaderActionResponse(ClientHandler client){

        RealPlayer player = client.getGame().getActualPlayer();
        this.nickname4 = player.getNickname();
        this.track4 = loadTrack(client);
        this.cards4 = player.getPersonalLeaderCards().stream().map(LeaderCard::getThin).collect(Collectors.toList());
        this.warehouse4 = new ThinWarehouse(player);

    }

    /**
     * this method update the state of a client.
     * in particular, it shows the message sent from the server and,
     * if the command is not of dynamic type ResponseToClient,
     * set the values that have to change after a model's change
     *
     * @param view this is the view to update
     */
    @Override
    public void updateClient(View view) {

        view.getModel().updateLeaderCards(cards4, nickname4);
        view.getModel().updateTrack(track4);
        view.getModel().updateWarehouse(warehouse4, nickname4);
        view.showCli();

        super.updateClient(view);
    }
}
