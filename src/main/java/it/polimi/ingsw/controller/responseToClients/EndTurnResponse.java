package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.view.View;

public class EndTurnResponse extends ResponseToClient{

    private String ownerTurnNickname;

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
