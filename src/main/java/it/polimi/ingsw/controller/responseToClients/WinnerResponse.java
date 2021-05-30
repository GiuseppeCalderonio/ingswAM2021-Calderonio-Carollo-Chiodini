package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.view.View;

/**
 * this class represent the winner response.
 * in particular, it is used to notify the nickname of the winner, and the
 * victory points gained from the receiver of the message
 */
public class WinnerResponse extends ResponseToClient{

    /**
     * this attribute represent the nickname of the winner
     */
    private final String winner;

    /**
     * this attribute represent the victory points gained of the
     * message receiver
     */
    private final int victoryPoints;

    /**
     * this constructor create the object starting from the string representing the winner
     * of the game, and the victory points gained from the receiver of the message
     * @param winner this is the nickname of the winne rof the game
     * @param victoryPoints these are the victory points of the receiver of this message
     */
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
     * @param view this is the view to update
     */
    @Override
    public void updateClient(View view) {
        view.showWinner(winner, victoryPoints);

    }
}
