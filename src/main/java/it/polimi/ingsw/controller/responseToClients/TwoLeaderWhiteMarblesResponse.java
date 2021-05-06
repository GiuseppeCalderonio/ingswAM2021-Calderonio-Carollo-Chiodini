package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.network.Client;

import java.util.ArrayList;
import java.util.Collections;

public class TwoLeaderWhiteMarblesResponse extends ResponseToClient{

    private final int whiteMarbles;

    public TwoLeaderWhiteMarblesResponse(int whiteMarbles){
        super("you own 2 white marble leader cards , choose for each white marble how to convert it",
                new ArrayList<>(Collections.singletonList("choose_leaderCards")));
        this.whiteMarbles = whiteMarbles;
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

        client.setMarbles(whiteMarbles);

        super.updateClient(client);
    }
}
