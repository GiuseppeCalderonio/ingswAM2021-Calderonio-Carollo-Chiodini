package it.polimi.ingsw.view;

import it.polimi.ingsw.view.cli.Cli;
import it.polimi.ingsw.view.gui.Gui;

public class Client {

    public Client(String hostname, int port, String view) {


        if (view.equals("-GUI")){
            new Gui().startGui(hostname, port);
        }

        else
            new Cli(hostname, port);
    }
}
