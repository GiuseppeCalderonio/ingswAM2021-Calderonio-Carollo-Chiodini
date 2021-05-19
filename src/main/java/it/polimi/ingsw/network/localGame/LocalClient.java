package it.polimi.ingsw.network.localGame;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.controller.responseToClients.ResponseToClient;
import it.polimi.ingsw.network.ClientHandler;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.newView.ClientNetwork;

import java.io.IOException;

public class LocalClient extends ClientNetwork {

    private final ClientHandler controller;

    public LocalClient(View view) throws IOException {

        this.controller = new LocalClientHandler(view);
    }


    /**
     * this method receive a message from the network
     *
     * @return the message received
     * @throws IOException if a network error occurs
     */
    @Override
    public ResponseToClient receiveMessage() throws IOException {
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
