package it.polimi.ingsw.network.client;

import it.polimi.ingsw.network.LocalClient;

/**
 * this class represent the local client main
 */
public class LocalClientMain {

    /**
     * this method start the local game, creating a new local client
     * @param args these are the arguments of the program, not used
     *             in this specific case
     */
    public static void main(String[] args) {

        boolean cli = Integer.parseInt(args[0]) == 0;

        new LocalClient(cli);
    }
}
