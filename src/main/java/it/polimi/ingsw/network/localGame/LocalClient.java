package it.polimi.ingsw.network.localGame;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.network.ClientNetwork;
import it.polimi.ingsw.view.View;

/**
 * this class represent the local client.
 * in particular, it is used only when there is a local game,
 * and it override some methods, jumping over the network layer and
 * calling directly the methods of the controller
 */
public class LocalClient extends ClientNetwork {

    /**
     * this attribute represent the controller that have to handle the match
     */
    private final ClientHandler controller;

    /**
     * this constructor create the object starting from the view.
     * in particular, it create a local client handler that needs the view to
     * be created, and set it as attribute
     * @param view this is the view to set in the constructor of the local client handler
     * @see LocalClientHandler
     */
    public LocalClient(View view){

        this.controller = new LocalClientHandler(view);
    }


    /**
     * this method receive a message from the network
     *
     * @return the message received
     */
    @Override
    public ResponseToClient receiveMessage() {
        return null;
    }

    /**
     * this method send a message to the network
     *
     * @param networkMessage this is the message to send
     */
    @Override
    public void send(Command networkMessage) {
        controller.readMessage(networkMessage);
    }

    /**
     * When an object implementing interface {@code Runnable} is used
     * to create a thread, starting the thread causes the object's
     * {@code run} method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method {@code run} is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {

    }
}
