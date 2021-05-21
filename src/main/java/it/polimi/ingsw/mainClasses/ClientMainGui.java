package it.polimi.ingsw.mainClasses;

import it.polimi.ingsw.view.Client;

/**
 * this class is used just for starting the gui in an easier way
 */
public class ClientMainGui {


    /**
     * this method start the game, calling the constructor of the client with the
     * IP address of the server, the port and the view configuration (CLI/GUI)
     * @param args these are the arguments of the program.
     *             in particular, the fist argument contain the port of the server,
     *             and the second argument contain the IP address of the server
     */
    public static void main(String[] args) {

        int portNumber = Integer.parseInt(args[1]);

        String hostname = args[2];

        try {
            new Client(hostname, portNumber, "-GUI");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
