package it.polimi.ingsw.network;

import java.io.IOException;

/**
 * this interface represent a network user.
 * in particular, every class that want to exchange messages, have to
 * implement this interface, specifying the type of messages
 * to send and to receive, that will be classes parsed in json
 * @param <ToSend> this is the class used to send messages
 * @param <ToReceive> this is the class used to receive messages
 */
public interface NetworkUser<ToSend, ToReceive> {

    /**
     * this method send a message to the network
     * @param networkMessage this is the message to send
     */
    void send(ToSend networkMessage);

    /**
     * this method receive a message from the network
     * @return the message received
     * @throws IOException if a network error occurs
     */
    ToReceive receiveMessage() throws IOException;
}
