package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.network.Client;

public class WinnerResponse extends ResponseToClient{

    private final String winner;

    private final int victoryPoints;

    public WinnerResponse(String winner, int victoryPoints){
        this.winner = winner;
        this.victoryPoints = victoryPoints;
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
        System.out.println("the winner is " + winner + ", you gained: " + victoryPoints);
    }
}
