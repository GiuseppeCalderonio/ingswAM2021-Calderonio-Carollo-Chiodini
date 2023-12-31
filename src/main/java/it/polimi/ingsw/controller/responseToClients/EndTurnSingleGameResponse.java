package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.model.DevelopmentCards.DevelopmentCard;
import it.polimi.ingsw.model.SingleGame.SoloToken;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;

import java.util.Map;

/**
 * this class represent the end turn response.
 * in particular, this response is sent to the client if and only if
 * a single game is running and the turn finish; once that lorenzo do
 * his action with the token the model state may change, so this
 * response send to the client the changes
 */
public class EndTurnSingleGameResponse extends ResponseToClient{

    /**
     * this attribute represent the new cards market after the lorenzo action
     */
    private final DevelopmentCard[][] cardsMarket5;

    /**
     * this attribute represent the new token
     */
    private final SoloToken token5;

    /**
     * this attribute represent the new track of lorenzo
     */
    private final Map<String, ThinTrack> track5;

    /**
     * this constructor create the response starting from the client,
     * getting from him all the data needed and setting all of them
     * @param client this is the client that send the response
     */
    public EndTurnSingleGameResponse(ClientHandler client){
        cardsMarket5 = client.getGame().getSetOfCard().show();
        token5 = client.getGame().getSoloTokens().get(
                client.getGame().getSoloTokens().size() - 1
        );

        track5 = loadTrack(client);
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


        view.getModel().updateCardsMarket(cardsMarket5);
        view.getModel().updateToken(token5);
        view.getModel().updateTrack(track5);
        view.showCli();
        view.updateTurn(view.getModel().getGame().getMyself().getNickname());

        //super.updateClient(view);
    }
}
