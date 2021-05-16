package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.network.Client;

import java.util.ArrayList;
import java.util.Collections;

/**
 * this class represent the response to send if and only if
 * a player own two leader cards of type marbles conversion, and
 * the player selected a row/column containing more than 1 white marble.
 * the class task is to simply store the number of white marbles
 */
public class TwoLeaderWhiteMarblesResponse extends ResponseToClient{

    /**
     * this attribute represent the number of white marbles selected from a choose_marbles action
     */
    private final int whiteMarbles;

    /**
     * this constructor create the response setting the default message:
     * "you own 2 white marble leader cards , choose for each white marble how to convert it",
     * and the singleton list of possible commands [choose_leaderCards], last but not least, set
     * the number of white marbles selected from the row/column chosen
     * @param whiteMarbles this is the number of white marbles chosen
     */
    public TwoLeaderWhiteMarblesResponse(int whiteMarbles){
        super(Status.ACCEPTED);
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
