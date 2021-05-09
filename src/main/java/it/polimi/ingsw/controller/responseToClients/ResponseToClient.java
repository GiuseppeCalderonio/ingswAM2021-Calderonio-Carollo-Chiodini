package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.network.Client;
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
    private String message;

    /**
     * this attribute represent the possible commands referred to a specific phase of the game
     */
    private List<String> possibleCommands;

    /**
     * this attribute indicates if the message have to ignore the possible commands
     */
    private boolean ignorePossibleCommands = false;

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
    public ResponseToClient(String message){
        this.message = message;
        ignorePossibleCommands = true;
    }

    /**
     * this constructor create a response to send to the client.
     * in particular, it set the message and the attribute ignorePossibleCommands = true
     * @param message this is the message to set
     * @param possibleCommands this is the list of possible commands that the destination client
     *                         can do
     */
    public ResponseToClient(String message, List<String> possibleCommands){
        this.message = message;
        this.possibleCommands = new ArrayList<>(possibleCommands);
    }

    /**
     * this method update the state of a client.
     * in particular, it shows the message sent from the server and,
     * if the command is not of dynamic type ResponseToClient,
     * set the values that have to change after a model's change
     * @param client this is the client to update
     */
    public void updateClient(Client client){
        // if the server didn't send any message
        if (message != null){
            // if the message does not contain any list of possible commands
            if (ignorePossibleCommands)
                // show the message
                System.out.println(message);

            else{

                //add default commands to the list
                possibleCommands.add("quit");
                possibleCommands.add("show");
                // set the new possible commands for the client
                client.setPossibleCommands(possibleCommands);
                // print the message with the possible commands
                System.out.println(message + ", Possible commands:" + possibleCommands);
            }
        }
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
        List<ThinPlayer> players = new ArrayList<>(client.getOpponents());
        players.add(client.getMyself());

        try {
            // get the thin player with the nickname desired
            return players.stream().
                    filter(thinPlayer -> thinPlayer.getNickName().equals(nickname)).
                    collect(Collectors.toList()).get(0);
        } catch (NullPointerException e){
            return null;
        }

    }

    /**
     * this method get the possible commands referred to a specific phase of the game
     * @return the possible commands referred to a specific phase of the game
     */
    public List<String> getPossibleCommands() {
        return possibleCommands;
    }

    /**
     * this method get the string representing the message
     * @return the string representing message
     */
    public String getMessage() {
        return message;
    }

}
