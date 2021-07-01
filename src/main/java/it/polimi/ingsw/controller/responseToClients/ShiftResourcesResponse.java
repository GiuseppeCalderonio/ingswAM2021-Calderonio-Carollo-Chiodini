package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

import java.util.HashMap;
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

        try{ // if the game is a single game
            Player lorenzo = client.getGame().getLorenzoIlMagnifico();
            tracks.put(lorenzo.getNickname(), new ThinTrack(lorenzo));
        }catch (NullPointerException ignored){ }

        this.tracks1 = loadTrack(client);
        this.warehouse1 = new ThinWarehouse(player);
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

        view.getModel().updateTrack(tracks1);
        view.getModel().updateWarehouse(warehouse1, nickname1);
        view.showCli();

        super.updateClient(view);
    }
}
