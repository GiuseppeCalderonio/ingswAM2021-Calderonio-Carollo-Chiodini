package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

public class ProductionResponse extends ResponseToClient{

    private final String nickname2;
    private final ThinWarehouse warehouse2;
    private final CollectionResources strongbox2;
    private final ThinTrack track2;

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
     * @param client this is the client to update
     */
    @Override
    public void updateClient(Client client) {

        ThinPlayer toChange = getPlayerToChange(client, nickname2);
        toChange.setStrongbox(strongbox2);
        toChange.setWarehouse(warehouse2);
        toChange.setTrack(track2);
        client.show();

        super.updateClient(client);
    }
}
