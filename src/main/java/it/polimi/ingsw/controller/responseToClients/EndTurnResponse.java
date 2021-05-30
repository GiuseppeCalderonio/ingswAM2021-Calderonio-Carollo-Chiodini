package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.view.View;

/**
 * this class represent the end turn response.
 * in particular, it extends the response to client class, and
 * it is used to notify the new turn owner
 */
public class EndTurnResponse extends ResponseToClient{

    /**
     * this attribute represent the new turn owner of the game
     */
    private final String ownerTurnNickname;

    /**
     * this constructor create the object starting from the nickname of the
     * new turn owner
     * @param ownerTurnNickname this is the nickname of the new turn owner
     */
    public EndTurnResponse(String ownerTurnNickname){
        super(Status.ACCEPTED);
        this.ownerTurnNickname = ownerTurnNickname;
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

        view.updateTurn(ownerTurnNickname);

    }
}
