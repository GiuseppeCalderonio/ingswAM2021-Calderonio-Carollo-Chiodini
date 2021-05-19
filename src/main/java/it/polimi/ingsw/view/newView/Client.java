package it.polimi.ingsw.view.newView;

import it.polimi.ingsw.view.Cli;
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
