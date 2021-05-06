package it.polimi.ingsw.controller.responseToClients;

import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.view.thinModelComponents.ThinPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ResponseToClient {

    private String message;
    private List<String> possibleCommands;
    private boolean ignorePossibleCommands = false;
    private int marbles;

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
        this.possibleCommands = possibleCommands;
    }

    /**
     * this method update the state of a client.
     * in particular, it shows the message sent from the server and,
     * if the command is not of dynamic type ResponseToClient,
     * set the values that have to change after a model's change
     * @param client this is the client to update
     */
    public void updateClient(Client client){

        if (message != null){

            if (ignorePossibleCommands)
                System.out.println(message);
            else{

                //possibleCommands = new ArrayList<>(response.getPossibleCommands());
                possibleCommands.add("quit");
                possibleCommands.add("show");
                client.setPossibleCommands(possibleCommands);

                System.out.println(message + ", Possible commands:" + possibleCommands);
            }

            if (marbles != 0)
                client.setMarbles(marbles);
        }
    }

    protected ThinPlayer getPlayerToChange(Client client, String nickname){
        List<ThinPlayer> players = new ArrayList<>(client.getOpponents());
        players.add(client.getMyself());
        return players.stream().
                filter(thinPlayer -> thinPlayer.getNickName().equals(nickname)).
                collect(Collectors.toList()).get(0);
    }

    public List<String> getPossibleCommands() {
        return possibleCommands;
    }

    public void setPossibleCommands(List<String> possibleCommands) {
        this.possibleCommands = possibleCommands;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getMarbles() {
        return marbles;
    }

    public void setMarbles(int marbles) {
        this.marbles = marbles;
    }

}
