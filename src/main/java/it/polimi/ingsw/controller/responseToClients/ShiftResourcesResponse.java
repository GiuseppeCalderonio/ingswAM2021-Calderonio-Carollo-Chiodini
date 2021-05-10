package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class represent the shift resources response.
 * in particular, when a player complete successfully a shift resources action,
 * the server have to send in broadcast the new game state, sending
 * the nickname of the player that did the shift, the new track of
 * every player, and the new warehouse of the player that did the change
 */
public class ShiftResourcesResponse extends ResponseToClient{

    /**
     * this attribute represent the nickname of the player to update
     */
    private final String nickname1;

    /**
     * this attribute represent a map containing the nickname as key and
     * the thin track as value
     */
    private final Map<String, ThinTrack> tracks1;

    /**
     * this attribute represent the warehouse of the client to update
     */
    private final ThinWarehouse warehouse1;

    /**
     * this constructor create the response starting from the client,
     * getting from him all the data needed and setting all of them
     * @param client this is the client that send the response
     */
    public ShiftResourcesResponse(ClientHandler client){
        RealPlayer player = client.getGame().getActualPlayer();
        this.nickname1 = player.getNickname();

        Map<String, ThinTrack> tracks = new HashMap<>();

        client.getGame().getPlayers().
                forEach(player1 -> tracks.put(player1.getNickname(), new ThinTrack(player1)));

        this.tracks1 = tracks;
        this.warehouse1 = new ThinWarehouse(player);
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

        ThinPlayer toChange = getPlayerToChange(client, nickname1);
        updateTracks(client);
        toChange.setWarehouse(warehouse1);
        client.show();

        super.updateClient(client);
    }

    /**
     * this method update the track of every player
     * @param client this is the client to update
     */
    private void updateTracks(Client client){
        List<ThinPlayer> players = new ArrayList<>(client.getOpponents());
        players.add(client.getMyself());

        players.forEach(player -> player.setTrack(tracks1.get(player.getNickName())));

    }
}
