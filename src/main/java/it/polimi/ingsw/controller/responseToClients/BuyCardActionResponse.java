package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.model.DevelopmentCards.CardColor;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.PlayerAndComponents.RealPlayer;
import it.polimi.ingsw.model.Resources.CollectionResources;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.thinModelComponents.ThinProductionPower;
import it.polimi.ingsw.view.thinModelComponents.ThinWarehouse;

/**
 * this class represent the buy card action response.
 * in particular, when a player complete successfully the
 * game action "buy card", his state change, and the cards market too;
 * this response sent in broadcast contains all the data to
 * update correctly the player that receive it
 */
public class BuyCardActionResponse extends ResponseToClient{

    /**
     * this attribute represent the nickname of the player that changed
     */
    private final String nickname6;

    /**
     * this attribute represent the new strongbox state after buying the card
     */
    private final CollectionResources strongbox6;

    /**
     * this attribute represent the new warehouse state after buying the card
     */
    private final ThinWarehouse warehouse6;

    /**
     * this attribute represent the new card of the cards market that have to
     * be replaced with the card of the same color and level (if deck is empty the card is null)
     */
    private final DevelopmentCard card6;

    /**
     * this attribute represent the level of the card bought
     */
    private final int level6;

    /**
     * this attribute represent the color of the card bought
     */
    private final CardColor color6;

    /**
     * this attribute represent the production power state after buying the card
     */
    private final ThinProductionPower productionPower;

    /**
     * this constructor create the response getting and setting all the attributes needed from the client,
     * and setting the level and the color of the card, and the card to replace
     * @param client this is the client changed
     * @param card this is the card to replace from market
     * @param level this is the level of the card bought
     * @param color this is the color of the card bought
     */
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
     * @param view
     */
    @Override
    public void updateClient(View view) {



        //ThinPlayer toChange = getPlayerToChange(client, nickname6);
        view.updateStrongbox(strongbox6, nickname6);
        view.updateWarehouse(warehouse6, nickname6);
        view.updateProductionPower(productionPower, nickname6);
        view.updateCard(level6, color6, card6);

        //toChange.setStrongbox(strongbox6);
        //toChange.setWarehouse(warehouse6);
        //toChange.setProductionPower(productionPower);
        //client.getGame().setCard(level6, color6, card6);
        view.showCli();

        super.updateClient(view);
    }
}
