package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;

public class EndTurnSingleGameResponse extends ResponseToClient{

    private final DevelopmentCard[][] cardsMarket5;
    private final SoloToken token5;
    private final ThinTrack track5;

    public EndTurnSingleGameResponse(ClientHandler client){
        cardsMarket5 = client.getGame().getSetOfCard().show();
        token5 = client.getGame().getSoloTokens().get(
                client.getGame().getSoloTokens().size() - 1
        );
        track5 = new ThinTrack(client.getGame().getLorenzoIlMagnifico());
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

        client.setCardsMarket(cardsMarket5);
        client.setSoloToken(token5);
        client.getOpponents().get(0).setTrack(track5);
        client.show();

        super.updateClient(client);
    }
}
