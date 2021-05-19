package it.polimi.ingsw.network.client;

import it.polimi.ingsw.view.newView.Client;

/**
 * this class represent the main
 */
public class ClientMainRemote {

    /**
     * this method start the game, calling the constructor of the client with the
     * IP address of the server and the port
     * @param args these are the arguments of the program.
     *             in particular, the fist argument contain the port of the server,
     *             and the second argument contain the IP address of the server
     */
    public static void main(String[] args) {

        String view = args[0];

        int portNumber = Integer.parseInt(args[1]);

        String hostname = args[2];

        new Client(hostname, portNumber, view);
    }
}