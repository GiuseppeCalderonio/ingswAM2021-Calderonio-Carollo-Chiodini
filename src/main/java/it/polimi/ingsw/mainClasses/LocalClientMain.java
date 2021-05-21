package it.polimi.ingsw.mainClasses;

import it.polimi.ingsw.view.Client;

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

        String view = args[0];

        new Client(null, 0, view);
    }
}
