package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.controller.commands.PongCommand;
import it.polimi.ingsw.network.Client;

public class PingResponse extends ResponseToClient{

    public PingResponse(){
        super("ping");
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
        client.send(new PongCommand());
    }
}
