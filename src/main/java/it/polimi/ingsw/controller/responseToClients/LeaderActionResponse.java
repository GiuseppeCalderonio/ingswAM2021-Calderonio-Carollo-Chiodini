package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.LeaderCard.LeaderCard;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinLeaderCard;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;

import java.util.List;
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
    private final ThinTrack track4;

    /**
     * this attribute represent the leader cards of the player to update
     */
    private final List<ThinLeaderCard> cards4;

    /**
     * this constructor create the response starting from the client,
     * getting from him all the data needed and setting all of them
     *
     * @param client this is the client that send the response
     */
    public LeaderActionResponse(ClientHandler client){

        RealPlayer player = client.getGame().getActualPlayer();
        this.nickname4 = player.getNickname();
        this.track4 = new ThinTrack(player);
        this.cards4 = player.getPersonalLeaderCards().stream().map(LeaderCard::getThin).collect(Collectors.toList());


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

        ThinPlayer toChange = getPlayerToChange(client, nickname4);
        toChange.setTrack(track4);
        toChange.setLeaderCards(cards4.stream().map(ThinPlayer::recreate).collect(Collectors.toList()));
        client.show();

        super.updateClient(client);
    }
}
