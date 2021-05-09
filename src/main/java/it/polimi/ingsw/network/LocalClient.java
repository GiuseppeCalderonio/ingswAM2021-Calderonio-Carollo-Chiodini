package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.ClientHandler;
import it.polimi.ingsw.controller.LocalClientHandler;
import it.polimi.ingsw.controller.commands.Command;

import java.io.IOException;

public class LocalClient extends Client {

    private ClientHandler controller;


    public LocalClient() {
        super(null, 0);

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
