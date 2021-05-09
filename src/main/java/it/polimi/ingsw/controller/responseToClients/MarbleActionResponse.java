package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.Marble.Marble;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

public class MarbleActionResponse extends ResponseToClient{

    private final String nickname3;
    private final ThinTrack track3;
    private final ThinWarehouse warehouse3;
    private final Marble[][] marbleMarket3;
    private final Marble lonelyMarble3;


    public MarbleActionResponse(ClientHandler client) {
        RealPlayer player = client.getGame().getActualPlayer();
        this.nickname3 = player.getNickname();
        this.track3 = new ThinTrack(player);
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
     * @param client this is the client to update
     */
    @Override
    public void updateClient(Client client) {

        ThinPlayer toChange = getPlayerToChange(client, nickname3);
        toChange.setTrack(track3);
        toChange.setWarehouse(warehouse3);
        client.setLonelyMarble(lonelyMarble3);
        client.setMarbleMarket(marbleMarket3);
        client.show();

        super.updateClient(client);
    }
}
