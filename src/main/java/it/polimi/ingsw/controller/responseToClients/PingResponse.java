package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.PingException;

/**
 * this class represent the ping response
 */
public class PingResponse extends ResponseToClient{

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
            throw new PingException();
    }
}
