package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.Client;

/**
 * this class represent the main
 */
public class ClientMain3 {

    public static void main(String[] args) {

        new Client(args[1], Integer.parseInt(args[0]));
    }
}