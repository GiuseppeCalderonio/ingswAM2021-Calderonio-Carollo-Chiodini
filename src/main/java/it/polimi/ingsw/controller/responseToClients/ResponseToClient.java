package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.model.PlayerAndComponents.Player;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.thinModelComponents.ThinLeaderCard;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;
import it.polimi.ingsw.view.thinModelComponents.ThinTrack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     * @param view this is the view to update
     */
    public void updateClient(View view) {
        // show to the client the context action
        view.showContextAction(message);

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

    protected Map<String, ThinTrack> loadTrack(ClientHandler client){
        Map<String, ThinTrack> tracks = new HashMap<>();

        client.getGame().getPlayers().
                forEach(player1 -> tracks.put(player1.getNickname(), new ThinTrack(player1)));

        try{ // if the game is a single game
            Player lorenzo = client.getGame().getLorenzoIlMagnifico();
            tracks.put(lorenzo.getNickname(), new ThinTrack(lorenzo));
        }catch (NullPointerException ignored){ }

        return tracks;

    }

}
