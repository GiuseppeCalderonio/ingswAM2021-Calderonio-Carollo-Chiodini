package it.polimi.ingsw.controller.client;

import it.polimi.ingsw.controller.Client;

/**
 * this class represent the main
 */
public class ClientMain3 {

    public static void main(String[] args) {

        new Client(args[1], Integer.parseInt(args[0]));
    }
}