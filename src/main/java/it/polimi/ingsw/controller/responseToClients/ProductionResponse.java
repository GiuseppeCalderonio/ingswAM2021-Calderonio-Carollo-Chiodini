package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

/**
 * this class represent the production response.
 * in particular, when a player activate correctly a production,
 * may change his depots state (warehouse and strongbox), and eventually
 * can advance into the faith track
 */
public class ProductionResponse extends ResponseToClient{

    /**
     * this attribute represent the nickname of the player to update
     */
    private final String nickname2;

    /**
     * this attribute represent the warehouse of the player to update
     */
    private final ThinWarehouse warehouse2;

    /**
     * this attribute represent the strongbox of the player to update
     */
    private final CollectionResources strongbox2;

    /**
     * this attribute represent the track of the player to update
     */
    private final ThinTrack track2;

    /**
     * this constructor create the response starting from the client,
     * getting from him all the data needed and setting all of them
     * @param client this is the client that send the response
     */
    public ProductionResponse(ClientHandler client){

        RealPlayer player = client.getGame().getActualPlayer();
        this.nickname2 = player.getNickname();
        this.warehouse2 = new ThinWarehouse(player);
        this.strongbox2 = player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources();
        this.track2 = new ThinTrack(player);

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

        view.getModel().updateStrongbox(strongbox2, nickname2);
        view.getModel().updateWarehouse(warehouse2, nickname2);
        view.getModel().updateTrack(track2, nickname2);
        view.showCli();

        super.updateClient(view);
    }
}
