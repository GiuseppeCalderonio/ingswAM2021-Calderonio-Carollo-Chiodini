package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Client;

/**
 * this class represent the main
 */
public class ClientMain4 {

    /**
     * this method start the game, calling the constructor of the client with the
     * IP address of the server and the port
     * @param args these are the arguments of the program.
     *             in particular, the fist argument contain the port of the server,
     *             and the second argument contain the IP address of the server
     */
    public static void main(String[] args) {

        new Client(args[1], Integer.parseInt(args[0]));
    }
}