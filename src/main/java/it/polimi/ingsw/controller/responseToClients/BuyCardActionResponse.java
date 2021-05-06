package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.thinModelComponents.ThinProductionPower;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

public class BuyCardActionResponse extends ResponseToClient{

    private final String nickname6;
    private final CollectionResources strongbox6;
    private final ThinWarehouse warehouse6;
    private final DevelopmentCard card6;
    private final int level6;
    private final CardColor color6;
    private final ThinProductionPower productionPower;

    public BuyCardActionResponse(ClientHandler client, DevelopmentCard card, int level, CardColor color){
        RealPlayer player = client.getGame().getActualPlayer();
        this.nickname6 = player.getNickname();
        this.warehouse6 = new ThinWarehouse(player);
        this.strongbox6 = player.getPersonalDashboard().getPersonalStrongbox().getStrongboxResources();
        this.card6 = card;
        this.level6 = level;
        this.color6 = color;
        this.productionPower = new ThinProductionPower(player);
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

        ThinPlayer toChange = getPlayerToChange(client, nickname6);
        toChange.setStrongbox(strongbox6);
        toChange.setWarehouse(warehouse6);
        toChange.setProductionPower(productionPower);
        client.getCardsMarket()[level6 - 1][color6.getIndex()] = card6;
        client.show();

        super.updateClient(client);
    }
}
