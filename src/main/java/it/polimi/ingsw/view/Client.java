package it.polimi.ingsw.view;

import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;

/**
 * this class represent the client.
 * it is used to select if start a client in cli or in gui
 */
public class Client {

    /**
     * this method handle the creation of the client, in particular,
     * it choose if start a cli or a gui
     * @param hostname this is the host name of the server in which connect, if is a local game it is null
     * @param port his is the port of the server in which connect, if is a local game it is 0
     * @param view this is a string representing the type of view to use
     */
    public Client(String hostname, int port, String view) {


        if (view.equals("-GUI")){ // create a gui
            new Gui().startGui(hostname, port);
        }

        else // create a cli
            new Cli(hostname, port);
    }
}
