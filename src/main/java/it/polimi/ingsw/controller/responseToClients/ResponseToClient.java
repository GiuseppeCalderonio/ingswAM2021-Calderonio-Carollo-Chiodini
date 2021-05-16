package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinLeaderCard;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * this class represent the response to client.
 * in particular, when the server have to notify a client
 * for any reason (sending a message of error, update the game state, say the confirm of an action)
 * create an object of this static type (not everytime dynamic) to send the notification
 */
public class ResponseToClient {

    /**
     * this attribute represent the message to send to the client, it can be null if
     * there is anything to say (for example, the game state changed)
     */
    private Status message;

    /**
     * this is the default constructor, that initialise every attribute with his default value
     */
    public ResponseToClient(){

    }

    /**
     * this constructor create a response to send to the client.
     * in particular, it set the message and the attribute ignorePossibleCommands = true
     * @param message this is the message to set
     */
    public ResponseToClient(Status message){
        this.message = message;
    }

    /**
     * this method update the state of a client.
     * in particular, it shows the message sent from the server and,
     * if the command is not of dynamic type ResponseToClient,
     * set the values that have to change after a model's change
     * @param client this is the client to update
     */
    public void updateClient(Client client){
        // show to the client the context action
        client.showContextAction(message);

    }

    /**
     * this method get the thin player from his nickname.
     * in particular, it create a list containing all the thin players
     * of the client (himself + opponents) and search for that one with the
     * nickname passed in input
     * @param client this is the client from which get the thin players
     * @param nickname this is the nickname of the thin player to return
     * @return the thin player with the nickname passed as input, null if not exist any
     *         player with that nickname (this case should never happen)
     */
    protected ThinPlayer getPlayerToChange(Client client, String nickname){
        // create a list containing all the thin players of the game
        List<ThinPlayer> players = new ArrayList<>(client.getGame().getOpponents());
        players.add(client.getGame().getMyself());

        try {
            // get the thin player with the nickname desired
            return players.stream().
                    filter(thinPlayer -> thinPlayer.getNickname().equals(nickname)).
                    collect(Collectors.toList()).get(0);
        } catch (NullPointerException e){
            return null;
        }

    }

    /**
     * this method get the string representing the message
     * @return the string representing message
     */
    public Status getMessage() {
        return message;
    }

    /**
     * this method hide the leader cards of a thin player, in fact the cards
     * of a player different from the owner, when another player did not activate
     * a leader card, should not be visible
     * @param players these are the player with the leader cards to hide
     */
    protected void hideLeaderCards(List<ThinPlayer> players){
        players.stream().
                flatMap(player -> player.getThinLeaderCards().stream()).
                filter(leaderCard -> !leaderCard.isActive()).
                forEach(ThinLeaderCard::hide);
    }

}
