package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

public class ShiftResourcesResponse extends ResponseToClient{

    private final String nickname1;
    private final ThinTrack track1;
    private final ThinWarehouse warehouse1;

    /**
     * this constructor create the response starting from the client,
     * getting from him all the data needed and setting all of them
     * @param client this is the client that send the response
     */
    public ShiftResourcesResponse(ClientHandler client){
        RealPlayer player = client.getGame().getActualPlayer();
        this.nickname1 = player.getNickname();
        this.track1 = new ThinTrack(player);
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
        toChange.setTrack(track1);
        toChange.setWarehouse(warehouse1);
        client.show();

        super.updateClient(client);
    }
}
