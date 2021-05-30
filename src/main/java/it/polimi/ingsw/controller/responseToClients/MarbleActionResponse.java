package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

import java.util.HashMap;
import java.util.Map;

/**
 * this class represent the marble action response.
 * in particular, when a player get some resources from the marble market, change
 * the marble market (get the resources from there), his warehouse (place the resources there) ,
 * the new tracks for every player (when someone discard resources)
 */
public class MarbleActionResponse extends ResponseToClient{

    /**
     * this attribute represent the nickname of the client to update
     */
    private final String nickname3;

    /**
     * this attribute represent a map containing the nickname as key and
     * the thin track as value
     */
    private final Map<String, ThinTrack> tracks;

    /**
     * this attribute represent the warehouse of the client to update
     */
    private final ThinWarehouse warehouse3;

    /**
     * this attribute represent the marble market to update
     */
    private final Marble[][] marbleMarket3;

    /**
     * this attribute represent the lonely marble to update
     */
    private final Marble lonelyMarble3;

    /**
     * this constructor create the response starting from the client,
     * getting from him all the data needed and setting all of them
     * @param client this is the client that send the response
     */
    public MarbleActionResponse(ClientHandler client) {
        RealPlayer player = client.getGame().getActualPlayer();
        this.nickname3 = player.getNickname();
        Map<String, ThinTrack> tracks = new HashMap<>();

        client.getGame().getPlayers().
                forEach(player1 -> tracks.put(player1.getNickname(), new ThinTrack(player1)));

        try{ // if the game is a single game
            Player lorenzo = client.getGame().getLorenzoIlMagnifico();
            tracks.put(lorenzo.getNickname(), new ThinTrack(lorenzo));
        }catch (NullPointerException ignored){ }

        this.tracks = tracks;
        this.warehouse3 = new ThinWarehouse(player);
        this.marbleMarket3 = client.getGame().getMarketBoard().getMarketTray();
        this.lonelyMarble3 = client.getGame().getMarketBoard().getLonelyMarble();
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

        view.getModel().updateTrack(tracks);
        view.getModel().updateWarehouse(warehouse3, nickname3);
        view.getModel().updateMarbleMarket(marbleMarket3, lonelyMarble3);
        view.showCli();

        super.updateClient(view);
    }
}
