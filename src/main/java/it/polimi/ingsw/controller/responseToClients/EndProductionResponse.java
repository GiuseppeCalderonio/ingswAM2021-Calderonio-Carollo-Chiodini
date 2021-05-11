package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;

/**
 * this class represent the end production response.
 * in particular, this response is used when a player
 * end a production, so the strongbox could change because
 * the strongbox changes are visible only at the end of
 * a production, to avoid more productions with the same
 * resources gained from a previous production
 */
public class EndProductionResponse extends ResponseToClient{

    /**
     * this attribute represent the nickname of the player to update
     */
    private final String nickname7;

    /**
     * this attribute represent the strongbox of the player to update
     */
    private final CollectionResources strongbox3;

    /**
     * this constructor create the response starting from the client,
     * getting from him all the data needed and setting all of them
     *
     * @param client this is the client that send the response
     */
    public EndProductionResponse(ClientHandler client){

        RealPlayer player = client.getGame().getActualPlayer();
        this.nickname7 = player.getNickname();
        this.strongbox3 = player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources();

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


        ThinPlayer toChange = getPlayerToChange(client, nickname7);
        toChange.setStrongbox(strongbox3);
        client.show();

        super.updateClient(client);
    }
}
