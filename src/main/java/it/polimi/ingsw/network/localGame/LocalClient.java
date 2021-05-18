package it.polimi.ingsw.network.localGame;

import it.polimi.ingsw.controller.commands.Command;
import it.polimi.ingsw.network.Client;
import it.polimi.ingsw.network.ClientHandler;

import java.io.IOException;

/**
 * this class represent the local client.
 * in particular, it extend the class client to handle
 * local matches. the main changes are :
 * (1) there is a reference to the controller in order to
 * simulate the exchange of messages as invocation of methods
 * (2) the method run is overridden and do nothing, and
 * the method send is overridden and invoke the method readMessage of the
 * controller
 */
public class LocalClient extends Client {

    /**
     * this attribute represent the controller
     */
    private final ClientHandler controller;

    /**
     * this constructor create the client using the super constructor.
     * in particular, it does not connect to any socket and create an
     * object ClientHandler of dynamic type LocalClientHandler
     * @see ClientHandler
     * @see LocalClientHandler
     */
    public LocalClient(boolean cli) {
        super(null, 0, cli, null);

        this.controller = new LocalClientHandler(this);



        try {
            start();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * this method is not useful for the local client
     */
    @Override
    public void run() { }

    /**
     * this method send a message to the server, the message
     * have to be in the format class Command
     *
     * @param command this is the socket to send
     */
    @Override
    public void send(Command command) {
        controller.readMessage(command);
    }
}
